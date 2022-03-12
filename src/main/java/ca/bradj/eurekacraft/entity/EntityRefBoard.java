package ca.bradj.eurekacraft.entity;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.core.init.EntitiesInit;
import ca.bradj.eurekacraft.vehicles.BoardType;
import ca.bradj.eurekacraft.vehicles.RefBoardItem;
import ca.bradj.eurekacraft.vehicles.RefBoardStats;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoard;
import ca.bradj.eurekacraft.world.storm.StormSavedData;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

// TODO: Destroy ref board on player disconnect

public class EntityRefBoard extends Entity {
    public static final String ENTITY_ID = "ref_board_entity";

    private static final int BOOST_TICKS = 3;
    private static final float INITIAL_YROT = 0.000001f;

    private static Map<Integer, EntityRefBoard> deployedBoards = new HashMap();
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

    public static Logger logger = LogManager.getLogger(EurekaCraft.MODID);
    private float lastYRot = INITIAL_YROT;
    private Vector3d lastDirection = new Vector3d(1.0, 1.0, 1.0);
    private double lastLift = 0;
    private double lastSpeed;
    private int tickOf100 = 0;

    public EntityRefBoard(EntityType<? extends Entity> entity, World world) {
        super(entity, world);
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
        logger.debug("Deployed ref board with stats" + boardStats);

        if (deployedBoards.containsKey(player.getId())) {
            deployedBoards.get(player.getId()).kill();
        }

        deployedBoards.put(player.getId(), this);

        this.playerOrNull = player;

        this.initialSpeed = runEquivalent;
        if (player instanceof PlayerEntity) {
            this.initialSpeed = runEquivalent * (((PlayerEntity) player).getSpeed() / runSpeed);
        }

        this.lastSpeed = initialSpeed;
    }

    public static void boostPlayer(World world, int id) {
        if (world.isClientSide()) {
            return;
        }
        boostedPlayers.put(id, BOOST_TICKS);
    }

    public static EntityRefBoard spawnNew(Entity player, World level, ItemStack boardItem, BoardType id) {
        if (level.isClientSide()) {
            return null;
        }
        EntityRefBoard glider = new EntityRefBoard(player, level, boardItem);
        Vector3d position = player.position();
        glider.setPos(position.x, position.y, position.z);
        level.addFreshEntity(glider);
        PlayerDeployedBoard.set(player, id);
        return glider;
    }

    @Override
    public void kill() {
        super.kill();
        if (this.playerOrNull != null) {
            PlayerDeployedBoard.remove(this.playerOrNull);
            if (!this.level.isClientSide()) {
                deployedBoards.remove(this.playerOrNull.getId());
            }
        }
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void onSyncedDataUpdated(DataParameter<?> dataParam) {
        super.onSyncedDataUpdated(dataParam);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT p_70037_1_) {
        // TODO: Implement?
        if (this.level.isClientSide) {
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT p_213281_1_) {
        // TODO: Implement?
        if (this.level.isClientSide) {
        }
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
        if (this.playerOrNull instanceof JudgeEntity) {
            // TODO: Remove. Judge should set off trapar bombs
            boosted = true;
        }

        if (StormSavedData.forBlockPosition(this.blockPosition()).storming) {
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
        if (playerOrNull instanceof  JudgeEntity) {
            this.lastYRot = lookYRot;
            return lookYRot;
        }

        if (this.lastYRot > 0 && this.lastYRot <= INITIAL_YROT) {
            this.lastYRot = lookYRot;
            return lookYRot;
        }
        float diff = MathHelper.degreesDifference(this.lastYRot, lookYRot);
        logger.debug("lookrot " + lookYRot + " lastrot " + lastYRot + "lastYRot " + lastYRot + " diff " + diff);
        if (diff > 0) {
            this.lastYRot += turnSpeed;
        } else {
            this.lastYRot -= turnSpeed;
        }
        return this.lastYRot;
    }

    private Vector3d calculateDirection(float nextYRot, boolean applyDamagedEffect) {
        nextYRot = nextYRot + 90;
        double x = Math.cos(Math.PI * (nextYRot/180));
        double z = Math.sin(Math.PI * (nextYRot/180));
        Vector3d nextRaw = new Vector3d(x, 0 , z);

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
        if (playerOrNull instanceof JudgeEntity) {
            // TODO More generic check
            return false;
        }

        if (flightSpeed < minSurfSpeed) {
            return false;
        }
        Direction faceDir = this.playerOrNull.getDirection();
        BlockPos inFront = new BlockPos(this.playerOrNull.position()).relative(faceDir);
        BlockState blockInFront = this.level.getBlockState(inFront);
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
        boolean boosted = false;
        int playerId = this.playerOrNull.getId();
        int boost = boostedPlayers.getOrDefault(playerId, 0);
        if (boost > 0) {
            boosted = true;
            boostedPlayers.put(playerId, boost - 1);
        }
        return boosted;
    }

}
