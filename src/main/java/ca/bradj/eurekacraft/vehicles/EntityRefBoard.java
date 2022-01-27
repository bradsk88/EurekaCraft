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

// TODO: Destroy ref board on player disconnect

public class EntityRefBoard extends Entity {
    public static final String ENTITY_ID = "ref_board_entity";
    private PlayerEntity playerOrNull;
    private Hand handHeld;

    Logger logger = LogManager.getLogger(EurekaCraft.MODID);
    private Vector3d lastDirection = new Vector3d(0, 0, 0);
    private double lastLift = 0;

    public EntityRefBoard(EntityType<? extends Entity> entity, World world) {
        super(entity, world);
    }

    public EntityRefBoard(PlayerEntity player, World world, Hand hand) {
        super(EntitiesInit.REF_BOARD.get(), world);
        this.handHeld = hand;
        this.playerOrNull = player;
        this.logger.debug("Ref board created with " + player + " and " + hand);
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
            return;
        }
        Vector3d oldPos = this.playerOrNull.position();
//        this.playerOrNull.push(10, 5, 10);
//        this.playerOrNull.hurt(DamageSource.OUT_OF_WORLD, 0);
//        this.playerOrNull.push(4, 4, 4);
//        this.logger.debug("Delta: " + this.playerOrNull.getDeltaMovement());

        // TODO: Embed on board itself
        double boardSpeed = 1.0;
        double turnSpeed = 0.5;
        double boardWeight = 0.25;
        double liftFactor = 1.0;
        double defaultFall = -0.05 * boardWeight;
        double defaultLand = -4 * boardWeight;

        Vector3d impactPos = this.playerOrNull.position();
        this.level.addParticle(ParticleTypes.POOF, impactPos.x, impactPos.y, impactPos.z, 0, 1, 0);

        double liftOrFall = this.lastLift;


        Block block = this.level.getBlockState(playerOrNull.blockPosition()).getBlock();

        if (block.is(BlocksInit.TRAPAR_WAVE_BLOCK.get())) {
            // Apply lift
            this.logger.debug("Block" + block);
            // TODO: Get "lift factor" from block
            double blockLift = 0.5;
            liftOrFall = blockLift;
        } else {
            liftOrFall = Math.max(liftOrFall + defaultFall, defaultFall);
        }

        if (this.playerOrNull.isShiftKeyDown()) {
            liftOrFall = defaultLand;
            boardSpeed = 1.5 * boardSpeed;
            turnSpeed = 0.5 * turnSpeed;
        }

        if (this.playerOrNull.isOnGround()) {
            this.kill();
        }

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

        logger.debug("Lift: " + liftOrFall);
        if (Math.abs(liftOrFall) > 0) {
            this.lastLift = liftOrFall;
        }

        Vector3d go = nextDir.multiply(boardSpeed, 1.0, boardSpeed);

//        this.logger.debug("add" + add + "go" + go);

        this.playerOrNull.setDeltaMovement(go.x, liftOrFall, go.z);
        this.playerOrNull.hurtMarked = true;
        this.playerOrNull.fallDistance = 0; // To not die!
    }


}
