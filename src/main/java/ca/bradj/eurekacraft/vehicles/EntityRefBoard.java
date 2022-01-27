package ca.bradj.eurekacraft.vehicles;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.EntitiesInit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.DamageSource;
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

        this.logger.debug("Ticking ref board: ");

        super.tick();
        if (this.playerOrNull == null) {
            return;
        }
        Vector3d oldPos = this.playerOrNull.position();
//        this.playerOrNull.push(10, 5, 10);
//        this.playerOrNull.hurt(DamageSource.OUT_OF_WORLD, 0);
//        this.playerOrNull.push(4, 4, 4);
        this.logger.debug("Delta: " + this.playerOrNull.getDeltaMovement());

        // TODO: Only ascend on trapar blocks
        double y = 0.1;
        if (this.playerOrNull.isShiftKeyDown()) {
            y = -0.1;
        }

        double boardSpeed = 0.4;

        this.logger.debug("xRot", this.playerOrNull.getViewVector(0).xRot(0));
        this.logger.debug("zRot", this.playerOrNull.getViewVector(0).zRot(0));

        // TODO: Get vector on xy plane - disregard up/down (which reduces xy vectors)
        Vector3d go = this.playerOrNull.getViewVector(0).multiply(boardSpeed, 1.0, boardSpeed);

        this.playerOrNull.setDeltaMovement(go.x, y, go.z);
        this.playerOrNull.hurtMarked = true;
    }


}
