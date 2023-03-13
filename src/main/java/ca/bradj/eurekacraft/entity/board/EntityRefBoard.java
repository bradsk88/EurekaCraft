package ca.bradj.eurekacraft.entity.board;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.advancements.BoardTrickTrigger;
import ca.bradj.eurekacraft.blocks.TraparWaveChildBlock;
import ca.bradj.eurekacraft.core.init.AdvancementsInit;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.core.init.EntitiesInit;
import ca.bradj.eurekacraft.core.init.items.ItemsInit;
import ca.bradj.eurekacraft.entity.JudgeEntity;
import ca.bradj.eurekacraft.vehicles.*;
import ca.bradj.eurekacraft.vehicles.control.Control;
import ca.bradj.eurekacraft.vehicles.control.PlayerBoardControlProvider;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoard;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoardProvider;
import ca.bradj.eurekacraft.vehicles.wheels.BoardWheels;
import ca.bradj.eurekacraft.vehicles.wheels.IWheel;
import ca.bradj.eurekacraft.vehicles.wheels.Wheel;
import ca.bradj.eurekacraft.vehicles.wheels.WheelStats;
import ca.bradj.eurekacraft.world.storm.StormSavedData;
import ca.bradj.eurekacraft.world.waves.ChunkWavesDataManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.NetworkHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

// TODO: Destroy ref board on player disconnect

public class EntityRefBoard extends Entity {

    public static final String NBT_KEY_BOARD_UUID = "board_uuid";

    public static final String ENTITY_ID = "ref_board_entity";

    private static final int BOOST_TICKS = 10;
    private static final float INITIAL_YROT = 0.000001f;

    private static final float BLOCK_LIFT_RESIDUAL = 0.5f;
    private static final float BLOCK_LIFT_STORM_DEFAULT = 0.5f;
    private static final float BLOCK_LIFT_STORM_BRAKING = 0.35f;
    private static final float BLOCK_LIFT_WAVE_BLOCK_DEFAULT = 0.5f;
    private static final float BLOCK_LIFT_WAVE_BLOCK_BRAKING = 0.75f;

    static Map<UUID, EntityRefBoard> deployedBoards = new HashMap();
    private static Map<Integer, Integer> boostedPlayers = new HashMap();
    private static Block[] PASSABLE_BLOCKS = {
            Blocks.AIR, Blocks.CAVE_AIR, Blocks.TALL_GRASS, Blocks.GRASS, Blocks.WATER,
            BlocksInit.TRAPAR_WAVE_CHILD_BLOCK.get(), Blocks.SNOW
    };
    // Prefer PASSABLE_BLOCKS when possible
    private static final Class<?>[] PASSABLE_BLOCK_CLASSES = new Class[]{
            FlowerBlock.class, TallGrassBlock.class,
    };
    private static final Class<?>[] DESTROYABLE_BLOCK_CLASSES = new Class[]{
            FlowerBlock.class, TallGrassBlock.class, VineBlock.class, SnowLayerBlock.class
    };

    public static Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    private static final float runSpeed = 0.13f;
    private static final float runEquivalent = 0.25f;
    private static final float maxFlySpeed = 2.0f;
    private static final float minSurfSpeed = runSpeed * 1.5f;
    private static final float surfLift = 0.05f;
    private ItemStack boardItemStack;

    private float initialSpeed;
    private Entity playerOrNull;
    private boolean damaged;
    private boolean canFly;
    private RefBoardStats boardStats;
    private WheelStats wheelStats = WheelStats.NONE;

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

        this.boardItemStack = boardItem;

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
        wheelStats = WheelStats.GetStatsFromNBT(item);

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

    public static EntityRefBoard spawnFromInventory(
            Entity player, ServerLevel level, ItemStack boardItem, BoardType board
    ) {
        if (level.isClientSide()) {
            return null;
        }
        if (deployedBoards.containsKey(player.getUUID())) {
            EntityRefBoard oldBoard = deployedBoards.remove(player.getUUID());
            if (!oldBoard.isRemoved()) {
                logger.warn("Tried to spawn new. But there was already a deployed board");
                oldBoard.remove(RemovalReason.DISCARDED);
                PlayerDeployedBoard.DeployedBoard.RemoveFromStack(boardItem);
                return null;
            }
        }

        EntityRefBoard.ensureBoardUUID(boardItem);

        Color c = BoardColor.FromStack(boardItem);
        Optional<Wheel> wheel = BoardWheels.FromStack(boardItem);

        EntityRefBoard glider = new EntityRefBoard(player, level, boardItem);
        Vec3 position = player.position();
        glider.setPos(position.x, position.y, position.z);
        spawn(player, level, glider, board, c, wheel);
        PlayerDeployedBoard.DeployedBoard.AddToStack(boardItem);
        return glider;
    }

    private static void ensureBoardUUID(ItemStack boardItem) {
        if (boardItem.getOrCreateTag().hasUUID(NBT_KEY_BOARD_UUID)) {
            return;
        }
        UUID uuid = UUID.randomUUID();
        boardItem.getOrCreateTag().putUUID(NBT_KEY_BOARD_UUID, uuid);
    }

    public static UUID getEntityBoardUUID(EntityRefBoard b) {
        return b.boardItemStack.getOrCreateTag().getUUID(NBT_KEY_BOARD_UUID);
    }

    static void spawn(
            Entity player, ServerLevel level, EntityRefBoard board,
            BoardType bt, Color c, Optional<? extends IWheel> wheelItem
    ) {
        level.addFreshEntity(board);
        PlayerDeployedBoardProvider.setBoardTypeFor(player, bt, c, wheelItem, true);

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
        PlayerDeployedBoardProvider.removeBoardFor(this.playerOrNull);
        if (this.boardItemStack != null) {
            PlayerDeployedBoard.DeployedBoard.RemoveFromStack(this.boardItemStack);
        }
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

    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level.isClientSide) {
            return;
        }


        if (this.playerOrNull == null) {
            this.kill();
            return;
        }

        Control c = PlayerBoardControlProvider.getControl(playerOrNull);
        if (this.playerOrNull.isOnGround()) {
            if (!Control.BRAKE.equals(c)) {
                kill();
                return;
            }
        }

        if (this.boardStats == null) {
            this.kill();
            return;
        }

        if (!this.isAlive()) {
            return;
        }

        this.tickOf100 = Math.floorMod(this.tickOf100 + 1, 100);
        this.doHardPhysics();

        if (canFly || canSurf(minSurfSpeed)) {
            flyOrSurf(c);
        }
    }

    private void doHardPhysics() {
        boolean applyDamagedEffect = false;
        if (damaged && random.nextBoolean()) {
            applyDamagedEffect = true;
        }

        double turnSpeed = boardStats.agility() * 15; // 15 is effectively "turn on a dime"

        float lookYRot = this.playerOrNull.getViewYRot(1.0F);
        initializeRotation(lookYRot);
        int turnDir = calculateTurnDir(lookYRot);
        float nextYRot = calculateYRot(turnDir, (float) turnSpeed);

        this.lastDirection = convertYRotToDirection(nextYRot, applyDamagedEffect);

        if (turnDir != 0 && this.lastSpeed > (maxFlySpeed * 0.5)) {
            this.lastSpeed = Math.max(runSpeed, this.lastSpeed * 0.9);
        }
    }

    private void initializeRotation(float lookYRot) {
        if (this.lastYRot > 0 && this.lastYRot <= INITIAL_YROT) {
            // Initialize lastRot to look direction. This should only run once in the entity's life.
            this.lastYRot = lookYRot;
        }
    }

    private int calculateTurnDir(float lookYRot) {
        if (playerOrNull instanceof JudgeEntity) {
            return 0;
        }

        float diff = Mth.degreesDifference(this.lastYRot, lookYRot);
//        logger.debug("lookrot " + lookYRot + " lastYRot " + lastYRot + " diff " + diff);
        if (diff < -7.5) {
            return -1;
        }
        if (diff > 7.5) {
            return 1;
        }
        return 0;
    }

    private void flyOrSurf(Control c) {
        float blockLift = this.calculateBoost(c);
        if (blockLift > 0) {
            EurekaCraft.LOGGER.trace("Boosted at " + this.blockPosition());
        }
        boolean boosted = this.consumeBoost();
        if (boosted && blockLift == 0) {
            blockLift = BLOCK_LIFT_RESIDUAL;
        }

        if (this.playerOrNull instanceof JudgeEntity && !damaged) {
            // TODO: Remove. Judge should set off trapar bombs
            blockLift = BLOCK_LIFT_STORM_BRAKING;
        }

        double boardWeight = boardStats.weight();
        double boardSpeed = boardStats.speed();
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

        if (blockLift > 0) {
            liftOrFall = applyBoost(blockLift, liftFactor);
        } else {
            liftOrFall = Math.max(liftOrFall + defaultFall, defaultFall);
        }

        double wheelAccel = wheelStats.acceleration != 0 ? boardStats.getLatentAcceleration() + wheelStats.acceleration : 0;
        double accelFactor = 1f + (0.05f * wheelAccel / 100f);
        double wheelBraking = wheelStats.braking != 0 ? boardStats.getLatentBraking() + wheelStats.braking : 0;
        double brakeFactor = 1f - (0.1f * wheelBraking / 100f);

        if (this.playerOrNull.isShiftKeyDown() || (damaged && random.nextBoolean())) {
            liftOrFall = defaultLand * (1 - boardStats.landResist());
            flightSpeed = Math.min(this.lastSpeed + defaultLandAccel, defaultMaxSpeed);
        }

        if (canSurf(flightSpeed)) {
            liftOrFall = surfLift;

            flightSpeed = Math.max(0, flightSpeed / defaultWaterDecel);
            animateSurf();
        } else if (playerOrNull.isInWater()) {
            if (!(playerOrNull instanceof JudgeEntity)) { // TODO: More generic check
                this.kill();
                return;
            }
        }

        switch (c) {
            case ACCELERATE -> flightSpeed = Math.min(flightSpeed * accelFactor, defaultMaxSpeed);
            case BRAKE -> {
                flightSpeed = Math.max(flightSpeed * brakeFactor, 0);
                if (wheelBraking >= 100) {
                    liftOrFall = Math.max(liftOrFall * 0.25, 0);
                }
            }
            case NONE -> {
            }
            default -> throw new IllegalArgumentException("Unexpected control value: " + c);
        }

        flightSpeed = reduceSpeedIfCrashed(flightSpeed);
        destroyBlocks();
        storeForNextTick(liftOrFall, flightSpeed, this.lastDirection);

        Vec3 go = this.lastDirection.multiply(flightSpeed, 1.0, flightSpeed);
//        logger.debug("[Flightspeed " + flightSpeed + "] Go: " + go);

        this.playerOrNull.setDeltaMovement(go.x, liftOrFall, go.z);
        this.playerOrNull.hurtMarked = true;
        this.playerOrNull.fallDistance = 0; // To not die!
        this.playerOrNull.setYBodyRot(this.lastYRot);
        this.moveTo(this.playerOrNull.position());
    }

    private double applyBoost(float blockLift, double liftFactor) {
        double liftOrFall;
        double floorY = level.getSeaLevel();
        double playerY = this.getY();
        double heightAboveFloor = Math.max(1, playerY - floorY);
        int maxHeight = level.getMaxBuildHeight();
        if (heightAboveFloor > 0.5f * maxHeight) {
            blockLift = 0.5f * blockLift;
        }
        double maxHeightAboveFloor = maxHeight - floorY;
        double percentToMaxHeight = Math.max(0.1, (1 - (heightAboveFloor / maxHeightAboveFloor)));
        blockLift *= (float) percentToMaxHeight;
        EurekaCraft.LOGGER.trace("Percent to max height: " + percentToMaxHeight + ", blockLift: " + blockLift);
        liftOrFall = blockLift * liftFactor;
        return liftOrFall;
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

    private float calculateYRot(int turnDir, float turnSpeed) {
        if (turnDir > 0) {
            this.lastYRot += turnSpeed;
        } else if (turnDir < 0) {
            this.lastYRot -= turnSpeed;
        }
        return this.lastYRot;
    }

    private Vec3 convertYRotToDirection(float nextYRot, boolean applyDamagedEffect) {
        nextYRot = nextYRot + 90;
        double x = Math.cos(Math.PI * (nextYRot / 180));
        double z = Math.sin(Math.PI * (nextYRot / 180));
        Vec3 nextRaw = new Vec3(x, 0, z);

        if (applyDamagedEffect) {
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

    private float calculateBoost(Control c) {
        if (StormSavedData.forBlockPosition(this.blockPosition()).storming) {
            boostedPlayers.put(playerOrNull.getId(), BOOST_TICKS);
            if (Control.BRAKE.equals(c)) {
                return BLOCK_LIFT_STORM_BRAKING;
            }
            return BLOCK_LIFT_STORM_DEFAULT;
        }

        ChunkPos cp = new ChunkPos(this.blockPosition());
        if (ChunkWavesDataManager.get(level).getData(
                level.getChunk(cp.x, cp.z), level.getRandom()
        ).isWavePresentAt(this.blockPosition())) {
            boostedPlayers.put(playerOrNull.getId(), BOOST_TICKS);
            if (Control.BRAKE.equals(c)) {
                return BLOCK_LIFT_WAVE_BLOCK_BRAKING;
            }
            return BLOCK_LIFT_WAVE_BLOCK_DEFAULT;
        }

        Direction faceDir = this.playerOrNull.getDirection();
        BlockPos inFront = new BlockPos(this.playerOrNull.position()).relative(faceDir);
        BlockState blockInFront = this.level.getBlockState(inFront);
        if (blockInFront.hasProperty(TraparWaveChildBlock.BOOST)) {
            boostedPlayers.put(playerOrNull.getId(), BOOST_TICKS);
            if (Control.BRAKE.equals(c)) {
                return BLOCK_LIFT_WAVE_BLOCK_BRAKING;
            }
            return BLOCK_LIFT_WAVE_BLOCK_DEFAULT;
        }
        return 0;
    }

    private boolean consumeBoost() {
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
            n.putString("board_type", ((RefBoardItem) board.boardItemStack.getItem()).getBoardType().toNBT());
            n.putUUID(NBT_KEY_BOARD_UUID, board.boardItemStack.getOrCreateTag().getUUID(NBT_KEY_BOARD_UUID));
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
            if (board.boardItemStack == null) {
                // TODO: This item stack is throwaway. Is there another way we could store this UUID?
                StandardRefBoard srb = ItemsInit.STANDARD_REF_BOARD.get();
                board.boardItemStack = new ItemStack(srb);
            }
            board.boardItemStack.getOrCreateTag().putUUID(NBT_KEY_BOARD_UUID, nbt.getUUID(NBT_KEY_BOARD_UUID));
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

        public static SavedData NoBoardFor(UUID uuid) {
            Data data = new Data(uuid, null);
            data.setDirty(true);
            return data;
        }

        public static Data get(Level level, UUID playerUUID, @Nullable EntityRefBoard board) {
            if (level.isClientSide) {
                throw new RuntimeException("EntityRefBoard.Data should only be used on server side");
            }
            DimensionDataStorage storage = ((ServerLevel) level).getDataStorage();
            return storage.computeIfAbsent(
                    (CompoundTag tag) -> new Data(playerUUID, board, tag),
                    () -> new Data(playerUUID, board),
                    "board_saved_data" // TODO: does this ID matter?
            );
        }

        public Data(UUID playerUUID, @Nullable EntityRefBoard board) {
            super();
            this.board = board;
        }

        public Data(UUID playerUUID, @Nullable EntityRefBoard board, CompoundTag worldNBT) {
            this(playerUUID, board);
            if (this.board == null) {
                throw new IllegalStateException("Cannot load from world into null board");
            }
            if (!worldNBT.contains("board_state")) {
                logger.debug("Removed primed board for player because not present in world data: " + playerUUID);
                this.board.remove(RemovalReason.DISCARDED);
                return;
            }
            CompoundTag nbt = worldNBT.getCompound("board_state");
            this.board.deserializeNBT(nbt);
        }

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

    public static class Renderer extends EntityRenderer<EntityRefBoard> {

        public Renderer(EntityRendererProvider.Context ctx) {
            super(ctx);
        }

        @Override
        public ResourceLocation getTextureLocation(EntityRefBoard p_114482_) {
            return null;
        }

        @Override
        public boolean shouldRender(EntityRefBoard p_114491_, Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_) {
            return false;
        }
    }

}
