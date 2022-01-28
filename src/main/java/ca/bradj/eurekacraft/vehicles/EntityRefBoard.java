package ca.bradj.eurekacraft.vehicles;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.core.init.EntitiesInit;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Stack;

// TODO: Destroy ref board on player disconnect

public class EntityRefBoard extends Entity {
    public static final String ENTITY_ID = "ref_board_entity";

    private static final float runSpeed = 0.13f;
    private static final float runEquivalent = 0.25f;

    private float initialSpeed;
    private PlayerEntity playerOrNull;
    private Hand handHeld;
    private final RefBoardStats stats = RefBoardStats.StandardBoard;

    Logger logger = LogManager.getLogger(EurekaCraft.MODID);
    private Vector3d lastDirection = new Vector3d(0, 0, 0);
    private double lastLift = 0;
    private double lastSpeed;
    private Vector3d lastPlayerPosition = new Vector3d(0, 0, 0);

    public EntityRefBoard(EntityType<? extends Entity> entity, World world) {
        super(entity, world);
    }

    public EntityRefBoard(PlayerEntity player, World world, Hand hand) {
        super(EntitiesInit.REF_BOARD.get(), world);
        this.handHeld = hand;
        this.playerOrNull = player;
        this.initialSpeed = runEquivalent * (player.getSpeed() / runSpeed);
        this.lastSpeed = initialSpeed;
        this.logger.debug("Ref board created with " + player + " and " + hand + " at speed " + initialSpeed);
    }

    public Hand getHandHeld() {
        return this.handHeld;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT p_70037_1_) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT p_213281_1_) {

    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return new SSpawnObjectPacket(this);
    }

    @Override
    public void tick() {

        super.tick();
        if (this.playerOrNull == null) {
            logger.debug("player is null");
            return;
        }

        // TODO: Embed on board itself (Probably Min 0.25, Max 1.0)
        double boardWeight = this.stats.weight();
        double boardSpeed = this.stats.speed();
        double turnSpeed = this.stats.agility();
        double liftFactor = this.stats.lift();

        // Calculated base physics
        double defaultFall = -0.05 * boardWeight;
        double defaultAccel = 0.01 * boardWeight;
        double defaultLand = -2 * Math.sqrt(boardWeight);
        double defaultLandAccel = 2 * defaultAccel;
        double defaultMaxSpeed = boardSpeed * runEquivalent;

        double liftOrFall = this.lastLift;
        double flightSpeed = Math.max(this.lastSpeed + defaultAccel, defaultMaxSpeed);

        Block block = this.level.getBlockState(playerOrNull.blockPosition()).getBlock();

        if (block.is(BlocksInit.TRAPAR_WAVE_BLOCK.get())) {
            // Apply lift
            this.logger.debug("Block" + block);
            // TODO: Get "lift factor" from block
            double blockLift = 0.5;
            liftOrFall = blockLift * liftFactor;
        } else {
            liftOrFall = Math.max(liftOrFall + defaultFall, defaultFall);
        }

        if (this.playerOrNull.isShiftKeyDown()) {
            liftOrFall = defaultLand;
            flightSpeed = Math.max(this.lastSpeed + defaultLandAccel, defaultMaxSpeed);
            turnSpeed = 0.5 * turnSpeed;
        }

        if (this.playerOrNull.isOnGround() || this.playerOrNull.isInWater()) {
            this.kill();
        }

        // TODO: Reset board speed (to slow) after a collision (e.g. tree)
//        Vector3d posDiff = this.lastPlayerPosition.subtract(this.playerOrNull.position());
//        logger.debug("PosDiff " + posDiff);
//
//        if (Math.max(Math.abs(posDiff.x), Math.abs(posDiff.z)) < 0.005) {
//            logger.debug("collision");
//            flightSpeed = 0.1; // TODO: Constant?
//        }

        // TODO: Get vector on xy plane - disregard up/down (which reduces xy vectors)
        Vector3d look3D = this.playerOrNull.getViewVector(0);
        Vector3d look2D = new Vector3d(look3D.x, 0, look3D.z).normalize();

        Vector3d add = look2D.multiply(turnSpeed, 1.0, turnSpeed);
        Vector3d nextRaw = this.lastDirection.add(add);
        Vector3d nextDir = nextRaw.normalize();

//        this.logger.debug("look2D" + look2D);
//        this.logger.debug("add" + add);
//        this.logger.debug("nextRaw" + nextRaw);

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

//        logger.debug("Speed: " + flightSpeed);
        if (Math.abs(flightSpeed) > 0) {
            this.lastSpeed = flightSpeed;
        }

        this.lastPlayerPosition = this.playerOrNull.position();

        Vector3d go = nextDir.multiply(flightSpeed, 1.0, flightSpeed);

//        this.logger.debug("add" + add + "go" + go);

        this.playerOrNull.setDeltaMovement(go.x, liftOrFall, go.z);
        this.playerOrNull.hurtMarked = true;
        this.playerOrNull.fallDistance = 0; // To not die!

        this.moveTo(this.playerOrNull.position());
    }

}
