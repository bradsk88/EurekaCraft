package ca.bradj.eurekacraft.vehicles;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.core.init.EntitiesInit;
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
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
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
            FlowerBlock.class, TallGrassBlock.class,
    };

    private static final float runSpeed = 0.13f;
    private static final float runEquivalent = 0.25f;
    private static final float maxFlySpeed = 1f;
    private static final float skimLift = 0.1f;

    private float initialSpeed;
    private PlayerEntity playerOrNull;
    private Hand handHeld;
    private RefBoardItem item;

    public static Logger logger = LogManager.getLogger(EurekaCraft.MODID);
    private Vector3d lastDirection = new Vector3d(0, 0, 0);
    private double lastLift = 0;
    private double lastSpeed;
    private int skimTime = 20; // TODO: Board stats?

    public EntityRefBoard(EntityType<? extends Entity> entity, World world) {
        super(entity, world);
    }

    public EntityRefBoard(PlayerEntity player, World world, Hand hand) {
        super(EntitiesInit.REF_BOARD.get(), world);

        if (world.isClientSide()) {
            return;
        }

        ItemStack itemInHand = player.getItemInHand(hand);
        if (!(itemInHand.getItem() instanceof RefBoardItem)) {
            logger.error("Item in hand was not ref board. Killing entity");
            this.kill();
            return;
        }

        this.item = (RefBoardItem) itemInHand.getItem();

        if (deployedBoards.containsKey(player.getId())) {
            deployedBoards.get(player.getId()).kill();
        }

        deployedBoards.put(player.getId(), this);

        this.handHeld = hand;
        this.playerOrNull = player;
        this.initialSpeed = runEquivalent * (player.getSpeed() / runSpeed);
        this.lastSpeed = initialSpeed;
    }

    public static void boostPlayer(World world, int id) {
        if (world.isClientSide()) {
            return;
        }
        boostedPlayers.put(id, BOOST_TICKS);
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

    public Hand getHandHeld() {
        return this.handHeld;
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

        if (this.playerOrNull == null || this.playerOrNull.isOnGround() || this.skimTime <= 0) {
            this.kill();
            return;
        }

        if (!this.isAlive()) {
            return;
        }

        if (this.item.canFly()) {
            fly();
        }

        this.moveTo(this.playerOrNull.position());
    }

    private void fly() {
        boolean boosted = this.consumeBoost();

        if (StormSavedData.forBlockPosition(this.blockPosition()).storming) {
            boosted = true;
        }

        double boardWeight = this.item.getStats().weight();
        double boardSpeed = this.item.getStats().speed();
        double turnSpeed = this.item.getStats().agility();
        double liftFactor = this.item.getStats().lift();

        // Calculated base physics
        double defaultFall = -0.02 * boardWeight;
        double defaultAccel = 0.02 * boardWeight;
        double defaultLand = -1 * Math.sqrt(boardWeight);
        double defaultLandAccel = 2 * defaultAccel;
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
        if (this.item.isDamagedBoard() && random.nextBoolean() && random.nextBoolean()) {
            applyDamagedEffect = true;
        }

        if (this.playerOrNull.isShiftKeyDown() || applyDamagedEffect) {
            liftOrFall = defaultLand * (1 - this.item.getStats().landResist());
            flightSpeed = Math.min(this.lastSpeed + defaultLandAccel, defaultMaxSpeed);
            turnSpeed = 0.5 * turnSpeed;
            skimTime--;
        }

        if (this.playerOrNull.isInWater()) {
            // TODO: Minimum speed for skim
            this.skimTime--;
            liftOrFall = skimLift;
            flightSpeed = Math.max(0, flightSpeed / 1.1);
            level.addParticle(ParticleTypes.SPLASH, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
            level.addParticle(ParticleTypes.SPLASH, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
            level.addParticle(ParticleTypes.SPLASH, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
            level.addParticle(ParticleTypes.SPLASH, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
            level.playSound(null, this.blockPosition(), SoundEvents.PLAYER_SPLASH, SoundCategory.BLOCKS, 1.0f, 1.0f);
        }

        Vector3d look3D = this.playerOrNull.getViewVector(0);
        Vector3d look2D = new Vector3d(look3D.x, 0, look3D.z).normalize();
        Vector3d add = look2D.multiply(turnSpeed, 1.0, turnSpeed);

        Vector3d nextRaw = this.lastDirection.add(add);

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

        if (Math.abs(nextDir.x) > 0 || Math.abs(nextDir.z) > 0) {
            this.lastDirection = nextDir;
        }

//        logger.debug("Lift: " + liftOrFall);
        if (Math.abs(liftOrFall) > 0) {
            this.lastLift = liftOrFall;
        }

        destroyBlocks();
        flightSpeed = reduceSpeedIfCrashed(flightSpeed);

//        logger.debug("Speed: " + flightSpeed);
        if (Math.abs(flightSpeed) > 0) {
            this.lastSpeed = flightSpeed;
        }

        Vector3d go = nextDir.multiply(flightSpeed, 1.0, flightSpeed);

//        this.logger.debug("add" + add + "go" + go);

        this.playerOrNull.setDeltaMovement(go.x, liftOrFall, go.z);
        this.playerOrNull.hurtMarked = true;
        this.playerOrNull.fallDistance = 0; // To not die!
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
        for (Block b : PASSABLE_BLOCKS) {
            if (blockInFront.is(b)) {
                return flightSpeed;
            }
        }
        for (Class<?> c : PASSABLE_BLOCK_CLASSES) {
            if (blockInFront.getBlock().getClass() == c) {
                return flightSpeed;
            }
        }
        logger.debug("Crashed into " + blockInFront);
        return 0.01;
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
