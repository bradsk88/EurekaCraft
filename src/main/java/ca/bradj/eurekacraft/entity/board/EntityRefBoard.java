package ca.bradj.eurekacraft.entity.board;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.advancements.BoardTrickTrigger;
import ca.bradj.eurekacraft.core.init.AdvancementsInit;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.core.init.EntitiesInit;
import ca.bradj.eurekacraft.entity.JudgeEntity;
import ca.bradj.eurekacraft.vehicles.BoardType;
import ca.bradj.eurekacraft.vehicles.RefBoardItem;
import ca.bradj.eurekacraft.vehicles.RefBoardStats;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoard;
import ca.bradj.eurekacraft.world.storm.StormSavedData;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// TODO: Destroy ref board on player disconnect

public class EntityRefBoard extends Entity {

    public static final String ENTITY_ID = "ref_board_entity";

    private static final int BOOST_TICKS = 3;
    private static final float INITIAL_YROT = 0.000001f;

    static Map<UUID, EntityRefBoard> deployedBoards = new HashMap();
    private static Map<Integer, Integer> boostedPlayers = new HashMap();
    private static Block[] PASSABLE_BLOCKS = {
            Blocks.AIR, Blocks.CAVE_AIR, Blocks.TALL_GRASS, Blocks.GRASS, Blocks.WATER,
            BlocksInit.TRAPAR_WAVE_BLOCK.get(), BlocksInit.TRAPAR_WAVE_CHILD_BLOCK.get(),
    };
    // Prefer PASSABLE_BLOCKS when possible
    private static final Class<?>[] PASSABLE_BLOCK_CLASSES = new Class[]{
            FlowerBlock.class, TallGrassBlock.class,
    };
    private static final Class<?>[] DESTROYABLE_BLOCK_CLASSES = new Class[]{
            FlowerBlock.class, TallGrassBlock.class, VineBlock.class,
    };

    public static Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    private static final float runSpeed = 0.13f;
    private static final float runEquivalent = 0.25f;
    private static final float maxFlySpeed = 2.0f;
    private static final float minSurfSpeed = runSpeed * 1.5f;
    private static final float surfLift = 0.05f;

    private float initialSpeed;
    private Entity playerOrNull;
    private boolean damaged;
    private boolean canFly;
    private RefBoardStats boardStats;

    private float lastYRot = INITIAL_YROT;
    private Vector3d lastDirection = new Vector3d(1.0, 1.0, 1.0);
    private double lastLift = 0;
    private double lastSpeed;
    private int tickOf100 = 0;

    public EntityRefBoard(EntityType<? extends Entity> entity, World world) {
        super(entity, world);
    }

    EntityRefBoard(Entity player, World world) {
        super(EntitiesInit.REF_BOARD.get(), world);
        this.playerOrNull = player;
    }

    public EntityRefBoard(Entity player, World world, ItemStack boardItem) {
        super(EntitiesInit.REF_BOARD.get(), world);

        if (world.isClientSide()) {
            return;
        }

        ItemStack item = boardItem.getStack();
        if (!(item.getItem() instanceof RefBoardItem)) {
            logger.error("Item in hand was not ref board. Killing entity");
            this.kill();
            return;
        }

        damaged = ((RefBoardItem) item.getItem()).isDamagedBoard();
        canFly = ((RefBoardItem) item.getItem()).canFly();
        boardStats = RefBoardItem.GetStatsFromNBT(item);

        this.playerOrNull = player;

        this.initialSpeed = runEquivalent;
        if (player instanceof PlayerEntity) {
            this.initialSpeed = runEquivalent * (((PlayerEntity) player).getSpeed() / runSpeed);
        }

        this.lastSpeed = initialSpeed;

        if (playerOrNull instanceof ServerPlayerEntity) {
            AdvancementsInit.BOARD_TRICK_TRIGGER.trigger(
                    (ServerPlayerEntity) playerOrNull,
                    BoardTrickTrigger.Trick.FirstRefFlight
            );
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt) {
        nbt.put("ec_ref_board_state", this.serializeNBT());
    }

    @Override
    public CompoundNBT serializeNBT() {
        EntityRefBoard.Serializer ser = new EntityRefBoard.Serializer(this);
        return ser.serializeNBT();
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt) {
        this.deserializeNBT(nbt.getCompound("ec_ref_board_state"));
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        EntityRefBoard.Serializer ser = new EntityRefBoard.Serializer(this);
        ser.deserializeNBT(nbt);
    }

    public static void boostPlayer(World world, int id) {
        if (world.isClientSide()) {
            return;
        }
        boostedPlayers.put(id, BOOST_TICKS);
    }

    public static EntityRefBoard spawnFromInventory(Entity player, ServerWorld level, ItemStack boardItem, BoardType id) {
        if (level.isClientSide()) {
            return null;
        }
        if (deployedBoards.containsKey(player.getUUID())) {
            logger.warn("Tried to spawn new. But there was already a deployed board");
            deployedBoards.remove(player.getUUID()).remove();
            return null;
        }
        EntityRefBoard glider = new EntityRefBoard(player, level, boardItem);
        Vector3d position = player.position();
        glider.setPos(position.x, position.y, position.z);
        spawn(player, level, glider, id);
        return glider;
    }

    static void spawn(Entity player, ServerWorld level, EntityRefBoard board, BoardType id) {
        level.addFreshEntity(board);
        PlayerDeployedBoard.set(player, id);

        if (deployedBoards.containsKey(player.getUUID())) {
            deployedBoards.get(player.getUUID()).remove();
        }
        deployedBoards.put(player.getUUID(), board);

        logger.debug(String.format(
                "Deployed ref board for %s with stats %s", player.getName(), board.boardStats
        ));
    }

    @Override
    public void kill() {
          super.kill();
        if (this.playerOrNull == null) {
            return;
        }
        PlayerDeployedBoard.remove(this.playerOrNull);
        if (!this.level.isClientSide()) {
            deployedBoards.remove(this.playerOrNull.getUUID());
        }
        if (level instanceof ServerWorld) {
            ((ServerWorld) level).getDataStorage().set(
                    EntityRefBoard.Data.NoBoardFor(this.playerOrNull.getUUID())
            );
        }
    }

    @Override
    public void remove() {
        super.remove();
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void onSyncedDataUpdated(DataParameter<?> dataParam) {
        super.onSyncedDataUpdated(dataParam);
    }

    public IPacket<?> getAddEntityPacket() {
        return new SSpawnObjectPacket(this);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level.isClientSide) {
            return;
        }

        if (this.playerOrNull == null || this.playerOrNull.isOnGround()) {
            this.kill();
            return;
        }

        if (!this.isAlive()) {
            return;
        }

        this.tickOf100 = Math.floorMod(this.tickOf100 + 1, 100);

        if (canFly || canSurf(minSurfSpeed)) {
            flyOrSurf();
        }
    }

    private void flyOrSurf() {
        boolean boosted = this.consumeBoost();
        if (this.playerOrNull instanceof JudgeEntity && !damaged) {
            // TODO: Remove. Judge should set off trapar bombs
            boosted = true;
        }

        double boardWeight = boardStats.weight();
        double boardSpeed = boardStats.speed();
        double turnSpeed = boardStats.agility() * 10;
        double liftFactor = boardStats.lift();
        double surf = boardStats.surf();

        // Calculated base physics
        double defaultFall = -0.02 * boardWeight;
        double defaultAccel = 0.01 * (boardWeight + boardSpeed);
        double defaultLand = -1 * Math.sqrt(boardWeight);
        double defaultLandAccel = 2 * defaultAccel;
        double defaultWaterDecel = 1.01 + (0.10 - (0.10 * surf));
        double defaultMaxSpeed = boardSpeed * maxFlySpeed;

        double liftOrFall = this.lastLift;
        double flightSpeed = Math.min(this.lastSpeed + defaultAccel, defaultMaxSpeed);

        if (boosted) {
            // Apply lift
            // TODO: Get "lift factor" from block
            double blockLift = 0.5;
            liftOrFall = blockLift * liftFactor;
        } else {
            liftOrFall = Math.max(liftOrFall + defaultFall, defaultFall);
        }

        boolean applyDamagedEffect = false;
        if (damaged && random.nextBoolean() && random.nextBoolean()) {
            applyDamagedEffect = true;
        }

        if (this.playerOrNull.isShiftKeyDown() || applyDamagedEffect) {
            liftOrFall = defaultLand * (1 - boardStats.landResist());
            flightSpeed = Math.min(this.lastSpeed + defaultLandAccel, defaultMaxSpeed);
            turnSpeed = 0.5 * turnSpeed;
        }

        float nextYRot = calculateYRot((float) turnSpeed);
        Vector3d nextDir = calculateDirection(nextYRot, applyDamagedEffect);

        if (canSurf(flightSpeed)) {
            liftOrFall = surfLift;

            flightSpeed = Math.max(0, this.lastSpeed / defaultWaterDecel);
            animateSurf();
        } else if (playerOrNull.isInWater()) {
            if (!(playerOrNull instanceof JudgeEntity)) { // TODO: More generic check
                this.kill();
                return;
            }
        }

        flightSpeed = reduceSpeedIfCrashed(flightSpeed);
        destroyBlocks();
        storeForNextTick(liftOrFall, flightSpeed, nextDir);

        Vector3d go = nextDir.multiply(flightSpeed, 1.0, flightSpeed);
//        logger.debug("[Flightspeed " + flightSpeed + "] Go: " + go);

        this.playerOrNull.setDeltaMovement(go.x, liftOrFall, go.z);
        this.playerOrNull.hurtMarked = true;
        this.playerOrNull.fallDistance = 0; // To not die!
        this.moveTo(this.playerOrNull.position());
    }

    private void animateSurf() {
        level.addParticle(ParticleTypes.SPLASH, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
        level.addParticle(ParticleTypes.SPLASH, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
        level.addParticle(ParticleTypes.SPLASH, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
        level.addParticle(ParticleTypes.SPLASH, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
        if (Math.floorMod(tickOf100, 4) == 0) {
            level.playSound(null, this.blockPosition(), SoundEvents.GENERIC_SPLASH, SoundCategory.BLOCKS, 0.5f, 1.0f);
        }
    }

    private float calculateYRot(float turnSpeed) { // TODO: Damaged effect
        float lookYRot = this.playerOrNull.getViewYRot(1.0F);
        if (playerOrNull instanceof JudgeEntity) {
            this.lastYRot = lookYRot;
            return lookYRot;
        }

        if (this.lastYRot > 0 && this.lastYRot <= INITIAL_YROT) {
            this.lastYRot = lookYRot;
            return lookYRot;
        }
        float diff = MathHelper.degreesDifference(this.lastYRot, lookYRot);
//        logger.debug("lookrot " + lookYRot + " lastrot " + lastYRot + "lastYRot " + lastYRot + " diff " + diff);
        if (diff > 5) {
            this.lastYRot += turnSpeed;
        } else if (diff < -5 ) {
            this.lastYRot -= turnSpeed;
        }
        return this.lastYRot;
    }

    private Vector3d calculateDirection(float nextYRot, boolean applyDamagedEffect) {
        nextYRot = nextYRot + 90;
        double x = Math.cos(Math.PI * (nextYRot / 180));
        double z = Math.sin(Math.PI * (nextYRot / 180));
        Vector3d nextRaw = new Vector3d(x, 0, z);

        if (applyDamagedEffect && random.nextBoolean()) {
            if (random.nextBoolean()) {
                nextRaw = new Vector3d(0, this.lastDirection.y, this.lastDirection.z);
            } else {
                nextRaw = new Vector3d(this.lastDirection.x, 0, this.lastDirection.z);
            }
        }

        Vector3d nextDir = nextRaw.normalize();

//        this.logger.debug("look2D" + look2D);
//        this.logger.debug("add" + add);

        if (nextDir.x == 0 && nextDir.z == 0) {
//            this.logger.debug("look is " + nextDir);
//            this.logger.debug("using " + this.lastDirection);
            nextDir = this.lastDirection;
        }
        return nextDir;
    }

    private void storeForNextTick(double liftOrFall, double flightSpeed, Vector3d nextDir) {
        if (Math.abs(nextDir.x) > 0 || Math.abs(nextDir.z) > 0) {
            this.lastDirection = nextDir;
        }

//        logger.debug("Lift: " + liftOrFall);
        if (Math.abs(liftOrFall) > 0) {
            this.lastLift = liftOrFall;
        }

//        logger.debug("Speed: " + flightSpeed);
        if (Math.abs(flightSpeed) > 0) {
            this.lastSpeed = flightSpeed;
        }
    }

    private boolean canSurf(double flightSpeed) {
        Direction faceDir = this.playerOrNull.getDirection();
        BlockPos inFront = new BlockPos(this.playerOrNull.position()).relative(faceDir);
        BlockState blockInFront = this.level.getBlockState(inFront);
        if (playerOrNull instanceof JudgeEntity) {
            // TODO More generic check
            return blockInFront.is(Blocks.WATER);
        }

        if (flightSpeed < minSurfSpeed) {
            return false;
        }
        BlockState blockAboveFront = this.level.getBlockState(inFront.above(1));
        boolean underWater = blockAboveFront.is(Blocks.WATER);
        if (underWater) {
            this.kill();
        }
        return blockInFront.is(Blocks.WATER) && isPassable(blockAboveFront) && !underWater;
    }

    private void destroyBlocks() {
        Direction faceDir = this.playerOrNull.getDirection();
        BlockPos inFront = new BlockPos(this.playerOrNull.position()).relative(faceDir);
        BlockState blockInFront = this.level.getBlockState(inFront);
        for (Class<?> c : DESTROYABLE_BLOCK_CLASSES) {
            if (blockInFront.getBlock().getClass() == c) {
                this.level.destroyBlock(inFront, true);
            }
        }
    }

    private double reduceSpeedIfCrashed(double flightSpeed) {
        Direction faceDir = this.playerOrNull.getDirection();
        BlockPos inFront = new BlockPos(this.playerOrNull.position()).relative(faceDir);
        BlockState blockInFront = this.level.getBlockState(inFront);
        if (isPassable(blockInFront)) {
            return flightSpeed;
        }
        return 0.01;
    }

    private boolean isPassable(BlockState blockInFront) {
        for (Block b : PASSABLE_BLOCKS) {
            if (blockInFront.is(b)) {
                return true;
            }
        }
        for (Class<?> c : PASSABLE_BLOCK_CLASSES) {
            if (blockInFront.getBlock().getClass() == c) {
                return true;
            }
        }
        return false;
    }

    private boolean consumeBoost() {
        if (StormSavedData.forBlockPosition(this.blockPosition()).storming) {
            boostedPlayers.put(playerOrNull.getId(), BOOST_TICKS);
        }

        boolean boosted = false;
        int playerId = this.playerOrNull.getId();
        int boost = boostedPlayers.getOrDefault(playerId, 0);
        if (boost > 0) {
            boosted = true;
            boostedPlayers.put(playerId, boost - 1);
        }
        return boosted;
    }

    private static class Serializer implements INBTSerializable<CompoundNBT> {

        private final EntityRefBoard board;

        public Serializer(EntityRefBoard board) {
            this.board = board;
        }

        @Override
        public CompoundNBT serializeNBT() {
            CompoundNBT n = new CompoundNBT();
//            private float initialSpeed;
            n.putFloat("initial_speed", board.initialSpeed);
//            private Entity playerOrNull;
//            private boolean damaged;
            n.putBoolean("damaged", board.damaged);
//            private boolean canFly;
            n.putBoolean("can_fly", board.canFly);
//            private RefBoardStats boardStats;
            if (board.boardStats != null) {
                n.put("stats", board.boardStats.serializeNBT());
            }
//
//            private float lastYRot = INITIAL_YROT;
            n.putFloat("last_yrot", board.lastYRot);
//            private Vector3d lastDirection = new Vector3d(1.0, 1.0, 1.0);
            n.put("last_dir", serializeVector(board.lastDirection));
//            private double lastLift = 0;
            n.putDouble("last_lift", board.lastLift);
//            private double lastSpeed;
            n.putDouble("last_speed", board.lastSpeed);
//            private int tickOf100 = 0;
            n.putInt("tick_of_100", board.tickOf100);

            n.put("position", serializeVector(board.position()));

            return n;
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            board.initialSpeed = nbt.getFloat("initial_speed");
            board.damaged = nbt.getBoolean("damaged");
            board.canFly = nbt.getBoolean("can_fly");

            if (board.boardStats == null) {
                board.boardStats = RefBoardStats.StandardBoard;
            }

            board.boardStats.deserializeNBT(nbt.getCompound("stats"));
            board.lastYRot = nbt.getFloat("last_yrot");
            board.lastDirection = deserializePos(nbt.getCompound("last_dir"));
            board.lastLift = nbt.getDouble("last_lift");
            board.lastSpeed = nbt.getDouble("last_speed");
            board.tickOf100 = nbt.getInt("tick_of_100");

            Vector3d pos = deserializePos(nbt.getCompound("position"));
            board.setPos(pos.x, pos.y, pos.z);
        }

        private CompoundNBT serializeVector(Vector3d p) {
            CompoundNBT pos = new CompoundNBT();
            pos.putDouble("x", p.x);
            pos.putDouble("y", p.y);
            pos.putDouble("z", p.z);
            return pos;
        }

        private Vector3d deserializePos(CompoundNBT n) {
            double x = n.getDouble("x");
            double y = n.getDouble("y");
            double z = n.getDouble("z");
            return new Vector3d(x, y, z);
        }
    }

    public static class Data extends WorldSavedData {

        private final EntityRefBoard board;

        static String ID(UUID playerUUID) {
            return String.format("%s_%s_%s", EurekaCraft.MODID, "board_saved_data", playerUUID.toString());
        }

        public Data(UUID playerUUID, @Nullable EntityRefBoard board) {
            super(ID(playerUUID));
            this.board = board;
        }

        public static WorldSavedData NoBoardFor(UUID uuid) {
            Data data = new Data(uuid, null);
            data.setDirty(true);
            return data;
        }

        @Override
        public void load(CompoundNBT worldNBT) {
            if (this.board == null) {
                throw new IllegalStateException("Cannot load from world into null board");
            }
            if (!worldNBT.contains("board_state")) {
                logger.debug("Removed primed board for player because not present in world data: " + getId());
                this.board.remove();
                return;
            }
            CompoundNBT nbt = worldNBT.getCompound("board_state");
            this.board.deserializeNBT(nbt);
        }

        @Override
        public CompoundNBT save(CompoundNBT worldNBT) {
            if (this.board == null) {
                worldNBT.remove("board_state");
                return worldNBT;
            }
            worldNBT.put("board_state", this.board.serializeNBT());
            return worldNBT;
        }
    }

}
