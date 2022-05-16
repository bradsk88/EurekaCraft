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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.NetworkHooks;
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
    private Vec3 lastDirection = new Vec3(1.0, 1.0, 1.0);
    private double lastLift = 0;
    private double lastSpeed;
    private int tickOf100 = 0;

    public EntityRefBoard(EntityType<? extends Entity> entity, Level world) {
        super(entity, world);
    }

    EntityRefBoard(Entity player, Level world) {
        super(EntitiesInit.REF_BOARD.get(), world);
        this.playerOrNull = player;
    }

    public EntityRefBoard(Entity player, Level world, ItemStack boardItem) {
        super(EntitiesInit.REF_BOARD.get(), world);

        if (world.isClientSide()) {
            return;
        }

        ItemStack item = boardItem.copy();
        if (!(item.getItem() instanceof RefBoardItem)) {
            logger.error("Item in InteractionHand was not ref board. Killing entity");
            this.kill();
            return;
        }

        damaged = ((RefBoardItem) item.getItem()).isDamagedBoard();
        canFly = ((RefBoardItem) item.getItem()).canFly();
        boardStats = RefBoardItem.GetStatsFromNBT(item);

        this.playerOrNull = player;

        this.initialSpeed = runEquivalent;
        if (player instanceof Player) {
            this.initialSpeed = runEquivalent * (((Player) player).getSpeed() / runSpeed);
        }

        this.lastSpeed = initialSpeed;

        if (playerOrNull instanceof ServerPlayer) {
            AdvancementsInit.BOARD_TRICK_TRIGGER.trigger(
                    (ServerPlayer) playerOrNull,
                    BoardTrickTrigger.Trick.FirstRefFlight
            );
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        nbt.put("ec_ref_board_state", this.serializeNBT());
    }

    @Override
    public CompoundTag serializeNBT() {
        EntityRefBoard.Serializer ser = new EntityRefBoard.Serializer(this);
        return ser.serializeNBT();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        this.deserializeNBT(nbt.getCompound("ec_ref_board_state"));
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        EntityRefBoard.Serializer ser = new EntityRefBoard.Serializer(this);
        ser.deserializeNBT(nbt);
    }

    public static void boostPlayer(Level world, int id) {
        if (world.isClientSide()) {
            return;
        }
        boostedPlayers.put(id, BOOST_TICKS);
    }

    public static EntityRefBoard spawnFromInventory(Entity player, ServerLevel level, ItemStack boardItem, BoardType id) {
        if (level.isClientSide()) {
            return null;
        }
        if (deployedBoards.containsKey(player.getUUID())) {
            logger.warn("Tried to spawn new. But there was already a deployed board");
            deployedBoards.remove(player.getUUID()).remove(RemovalReason.DISCARDED);
            return null;
        }
        EntityRefBoard glider = new EntityRefBoard(player, level, boardItem);
        Vec3 position = player.position();
        glider.setPos(position.x, position.y, position.z);
        spawn(player, level, glider, id);
        return glider;
    }

    static void spawn(Entity player, ServerLevel level, EntityRefBoard board, BoardType id) {
        level.addFreshEntity(board);
        PlayerDeployedBoard.set(player, id);

        if (deployedBoards.containsKey(player.getUUID())) {
            deployedBoards.get(player.getUUID()).remove(RemovalReason.DISCARDED);
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
        if (level instanceof ServerLevel) {
            ((ServerLevel) level).getDataStorage().set(
                    EntityRefBoard.Data.ID(this.playerOrNull.getUUID()),
                    EntityRefBoard.Data.NoBoardFor(this.playerOrNull.getUUID())
            );
        }
    }

    @Override
    public void remove(
            RemovalReason reason
    ) {
        super.remove(reason);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> dataParam) {
        super.onSyncedDataUpdated(dataParam);
    }

    // TODO: Reimplement
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
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
        Vec3 nextDir = calculateDirection(nextYRot, applyDamagedEffect);

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

        Vec3 go = nextDir.multiply(flightSpeed, 1.0, flightSpeed);
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
            level.playSound(null, this.blockPosition(), SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 0.5f, 1.0f);
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
        float diff = Mth.degreesDifference(this.lastYRot, lookYRot);
//        logger.debug("lookrot " + lookYRot + " lastrot " + lastYRot + "lastYRot " + lastYRot + " diff " + diff);
        if (diff > 5) {
            this.lastYRot += turnSpeed;
        } else if (diff < -5 ) {
            this.lastYRot -= turnSpeed;
        }
        return this.lastYRot;
    }

    private Vec3 calculateDirection(float nextYRot, boolean applyDamagedEffect) {
        nextYRot = nextYRot + 90;
        double x = Math.cos(Math.PI * (nextYRot / 180));
        double z = Math.sin(Math.PI * (nextYRot / 180));
        Vec3 nextRaw = new Vec3(x, 0, z);

        if (applyDamagedEffect && random.nextBoolean()) {
            if (random.nextBoolean()) {
                nextRaw = new Vec3(0, this.lastDirection.y, this.lastDirection.z);
            } else {
                nextRaw = new Vec3(this.lastDirection.x, 0, this.lastDirection.z);
            }
        }

        Vec3 nextDir = nextRaw.normalize();

//        this.logger.debug("look2D" + look2D);
//        this.logger.debug("add" + add);

        if (nextDir.x == 0 && nextDir.z == 0) {
//            this.logger.debug("look is " + nextDir);
//            this.logger.debug("using " + this.lastDirection);
            nextDir = this.lastDirection;
        }
        return nextDir;
    }

    private void storeForNextTick(double liftOrFall, double flightSpeed, Vec3 nextDir) {
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

    private static class Serializer implements INBTSerializable<CompoundTag> {

        private final EntityRefBoard board;

        public Serializer(EntityRefBoard board) {
            this.board = board;
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag n = new CompoundTag();
//            private float initialSpeed;
            n.putFloat("initial_speed", board.initialSpeed);
//            private Entity playerOrNull;
//            private boolean damaged;
            n.putBoolean("damaged", board.damaged);
//            private boolean canFly;
            n.putBoolean("can_fly", board.canFly);
//            private RefBoardStats boardStats;
            if (board.boardStats != null) {
                n.put("stats", RefBoardStats.serializeNBT(board.boardStats));
            }
//
//            private float lastYRot = INITIAL_YROT;
            n.putFloat("last_yrot", board.lastYRot);
//            private Vec3 lastDirection = new Vec3(1.0, 1.0, 1.0);
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
        public void deserializeNBT(CompoundTag nbt) {
            board.initialSpeed = nbt.getFloat("initial_speed");
            board.damaged = nbt.getBoolean("damaged");
            board.canFly = nbt.getBoolean("can_fly");

            if (board.boardStats == null) {
                board.boardStats = RefBoardStats.StandardBoard;
            }

            board.boardStats = RefBoardStats.deserializeNBT(nbt.getCompound("stats"));
            board.lastYRot = nbt.getFloat("last_yrot");
            board.lastDirection = deserializePos(nbt.getCompound("last_dir"));
            board.lastLift = nbt.getDouble("last_lift");
            board.lastSpeed = nbt.getDouble("last_speed");
            board.tickOf100 = nbt.getInt("tick_of_100");

            Vec3 pos = deserializePos(nbt.getCompound("position"));
            board.setPos(pos.x, pos.y, pos.z);
        }

        private CompoundTag serializeVector(Vec3 p) {
            CompoundTag pos = new CompoundTag();
            pos.putDouble("x", p.x);
            pos.putDouble("y", p.y);
            pos.putDouble("z", p.z);
            return pos;
        }

        private Vec3 deserializePos(CompoundTag n) {
            double x = n.getDouble("x");
            double y = n.getDouble("y");
            double z = n.getDouble("z");
            return new Vec3(x, y, z);
        }
    }

    public static class Data extends SavedData {

        private final EntityRefBoard board;

        static String ID(UUID playerUUID) {
            return String.format("%s_%s_%s", EurekaCraft.MODID, "board_saved_data", playerUUID.toString());
        }

        public Data(UUID playerUUID, @Nullable EntityRefBoard board) {
            super();
            this.board = board;
        }

        public static SavedData NoBoardFor(UUID uuid) {
            Data data = new Data(uuid, null);
            data.setDirty(true);
            return data;
        }

        // TODO: Reimplement?
//        @Override
//        public void load(CompoundTag worldNBT) {
//            if (this.board == null) {
//                throw new IllegalStateException("Cannot load from world into null board");
//            }
//            if (!worldNBT.contains("board_state")) {
//                logger.debug("Removed primed board for player because not present in world data: " + getId());
//                this.board.remove();
//                return;
//            }
//            CompoundTag nbt = worldNBT.getCompound("board_state");
//            this.board.deserializeNBT(nbt);
//        }

        @Override
        public CompoundTag save(CompoundTag worldNBT) {
            if (this.board == null) {
                worldNBT.remove("board_state");
                return worldNBT;
            }
            worldNBT.put("board_state", this.board.serializeNBT());
            return worldNBT;
        }
    }

}
