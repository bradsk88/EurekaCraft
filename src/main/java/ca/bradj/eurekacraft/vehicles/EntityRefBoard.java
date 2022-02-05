package ca.bradj.eurekacraft.vehicles;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.core.init.EntitiesInit;
import ca.bradj.eurekacraft.render.AbstractBoardModel;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

// TODO: Destroy ref board on player disconnect

public class EntityRefBoard extends Entity {
    public static final String ENTITY_ID = "ref_board_entity";

    private static Map<Integer, EntityRefBoard> deployedBoards = new HashMap();

    public static boolean isDeployedFor(Integer playerId) {
        return deployedBoards.containsKey(playerId);
    }

    private static final float runSpeed = 0.13f;
    private static final float runEquivalent = 0.25f;

    private float initialSpeed;
    private PlayerEntity playerOrNull;
    private Hand handHeld;
    private RefBoardItem item;

    public static Logger logger = LogManager.getLogger(EurekaCraft.MODID);
    private Vector3d lastDirection = new Vector3d(0, 0, 0);
    private double lastLift = 0;
    private double lastSpeed;

    public EntityRefBoard(EntityType<? extends Entity> entity, World world) {
        super(entity, world);
    }

    public EntityRefBoard(PlayerEntity player, World world, Hand hand) {
        super(EntitiesInit.REF_BOARD.get(), world);

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

    public static AbstractBoardModel getModelFor(int id) {
        return deployedBoards.get(id).item.getModel();
    }

    @Override
    public void kill() {
        super.kill();
        if (this.playerOrNull != null && !this.level.isClientSide) {
            deployedBoards.remove(this.playerOrNull.getId());
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

        if (this.playerOrNull == null || this.playerOrNull.isOnGround() || this.playerOrNull.isInWater()) {
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
        // TODO: Embed on board itself (Probably Min 0.25, Max 1.0)
        double boardWeight = this.item.getStats().weight();
        double boardSpeed = this.item.getStats().speed();
        double turnSpeed = this.item.getStats().agility();
        double liftFactor = this.item.getStats().lift();

        // Calculated base physics
        double defaultFall = -0.01 * boardWeight;
        double defaultAccel = 0.01 * boardWeight;
        double defaultLand = -1 * Math.sqrt(boardWeight);
        double defaultLandAccel = 2 * defaultAccel;
        double defaultMaxSpeed = boardSpeed * runEquivalent;

        double liftOrFall = this.lastLift;
        double flightSpeed = Math.max(this.lastSpeed + defaultAccel, defaultMaxSpeed);

        Block block = this.level.getBlockState(playerOrNull.blockPosition()).getBlock();

        if (block.is(BlocksInit.TRAPAR_WAVE_BLOCK.get())) {
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
            flightSpeed = Math.max(this.lastSpeed + defaultLandAccel, defaultMaxSpeed);
            turnSpeed = 0.5 * turnSpeed;
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

        Direction faceDir = this.playerOrNull.getDirection();
        BlockPos inFront = new BlockPos(this.playerOrNull.getPosition(0)).relative(faceDir);
        if (
                !(this.level.getBlockState(inFront).is(Blocks.AIR) || this.level.getBlockState(inFront).is(BlocksInit.TRAPAR_WAVE_BLOCK.get()))
        ) {
            flightSpeed = 0.01;
        }

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

    public static class DeployedPropGetter implements IItemPropertyGetter {
        @Override
        public float call(ItemStack item, @Nullable ClientWorld world, @Nullable LivingEntity entity) {
            if (entity == null) {
                return 0.0F;
            }
            if (!(item.getItem() instanceof RefBoardItem)) {
                return 0.0F;
            }
            if (!EntityRefBoard.isDeployedFor(entity.getId())) {
                return 0.0F;
            }
            return 1.0F;
        }
    }
}
