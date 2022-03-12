package ca.bradj.eurekacraft.entity;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.ItemsInit;
import ca.bradj.eurekacraft.vehicles.EliteRefBoard;
import ca.bradj.eurekacraft.vehicles.RefBoardItem;
import ca.bradj.eurekacraft.vehicles.RefBoardStats;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

// TODO: Persist internal state
public class JudgeEntity extends CreatureEntity {

    private static final Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    public static final ResourceLocation ENTITY_ID = new ResourceLocation(
            EurekaCraft.MODID, "judge_entity"
    );

    private static final RefBoardStats BOARD_STATS = RefBoardStats.StandardBoard.
            WithSpeed(RefBoardStats.MIN_SPEED * 2).
            WithLift(RefBoardStats.MIN_POSITIVE_LIFT);

    private BlockPos targetDestination;
    private boolean hasAward = true;
    private Vector3d awardPos;

    public JudgeEntity(EntityType<? extends CreatureEntity> entity, World world) {
        super(entity, world);

    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes().
                add(Attributes.MAX_HEALTH, 10.0D);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VILLAGER_CELEBRATE;
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
        ILivingEntityData spawn = super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
        setItemSlot(EquipmentSlotType.HEAD, ItemsInit.SCUB_GOGGLES.get().getDefaultInstance());
        // FIXME: Make these render
        return spawn;
    }

    @Override
    protected ActionResultType mobInteract(PlayerEntity p_230254_1_, Hand p_230254_2_) {
        BlockPos ownPos = blockPosition();
        this.level.playLocalSound(
                ownPos.getX(), ownPos.getY(), ownPos.getZ(),
                SoundEvents.VILLAGER_YES, SoundCategory.NEUTRAL,
                0.5f, 1.2f, false
        );
        this.level.playLocalSound(
                ownPos.getX(), ownPos.getY(), ownPos.getZ(),
                SoundEvents.NOTE_BLOCK_CHIME, SoundCategory.NEUTRAL,
                0.5f, 0.2f, false
        );

        if (this.level.isClientSide()) {
            return super.mobInteract(p_230254_1_, p_230254_2_);
        }
        this.level.addFreshEntity(
                new ItemEntity(
                        level,
                        ownPos.getX(), ownPos.getY() + 2, ownPos.getZ(),
                        new ItemStack(
                                ItemsInit.REFLECTION_FILM::get, 1
                        )
                )
                // TODO: Give award instead of film
        );

        this.hasAward = false;
        this.awardPos = this.position();

        return ActionResultType.CONSUME;
    }

    @Override
    public void tick() {
        super.tick();

        if (awardPos != null) {

            if (targetDestination == null || level.random.nextInt(20) == 0) {
                chooseNewDirection();
            }

            if (position().distanceTo(new Vector3d(
                    awardPos.x,
                    awardPos.y,
                    awardPos.z
            )) > 100) {
                // FIXME: This is not adding particles or sound :(
                this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 1.0, 1.0, 1.0);
                this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 1.0, 1.0, 1.0);
                this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 1.0, 1.0, 1.0);
                this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 1.0, 1.0, 1.0);
                this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 1.0, 1.0, 1.0);
                this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 1.0, 1.0, 1.0);
                this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 1.0, 1.0, 1.0);
                this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 1.0, 1.0, 1.0);
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundCategory.AMBIENT, 1.0f, 1.0f, false);
                this.remove();
            }
        }
    }

    void chooseNewDirection() {
        Direction dir = Direction.Plane.HORIZONTAL.getRandomDirection(this.random);
        BlockPos ownPos = this.blockPosition();
        BlockPos newPos = ownPos.offset(0, 20, 0).relative(dir, 200);

        boolean pathClear = true;

        for (BlockPos p : BlockPos.betweenClosed(ownPos, newPos)) {
            if (this.level.isEmptyBlock(p)) {
                continue;
            }
            pathClear = false;
            break;
        }

        if (!pathClear) {
            return;
        }

        this.targetDestination = newPos;
        logger.debug("Judge is moving toward " + this.targetDestination);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new WaitForPlayerGoal(this, 0.2));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, PlayerEntity.class, 10.0F));
        this.goalSelector.addGoal(3, new FlyAwayGoal(this));
        this.goalSelector.addGoal(4, new WalkAwayGoal(this));
    }

    public static class WaitForPlayerGoal extends WaterAvoidingRandomWalkingGoal {

        public WaitForPlayerGoal(JudgeEntity entity, double p_i47301_2_) {
            super(entity, p_i47301_2_);
        }

        @Override
        public boolean canUse() {
            return ((JudgeEntity) this.mob).hasAward;
        }

    }

    public static class LookAtPlayerGoal extends LookAtGoal {

        public LookAtPlayerGoal(MobEntity p_i1631_1_, Class<? extends LivingEntity> p_i1631_2_, float p_i1631_3_) {
            super(p_i1631_1_, p_i1631_2_, p_i1631_3_);
        }

        @Override
        public boolean canUse() {
            if (((JudgeEntity) this.mob).hasAward) {
                return super.canUse();
            }
            return false;
        }

    }

    public static class FlyAwayGoal extends Goal {

        private final JudgeEntity self;

        public FlyAwayGoal(JudgeEntity judgeEntity) {
            this.self = judgeEntity;
        }

        @Override
        public boolean canUse() {
            if (this.self.hasAward) {
                return false;
            }
            return self.isOnGround();
        }

        @Override
        public void tick() {
            super.tick();
            BlockPos t = self.targetDestination;
            if (t == null) {
                return;
            }

            this.self.getLookControl().setLookAt(t.getX(), t.getY(), t.getZ());
            this.self.getMoveControl().setWantedPosition(t.getX(), t.getY(), t.getZ(), 0.5f);
            // TODO: Set off trapar grenades as he flies to generate boost
        }

        @Override
        public void start() {
            if (this.self.level.isClientSide()) {
                return;
            }
            BlockPos ownPos = this.self.blockPosition();


            this.self.setPos(ownPos.getX(), ownPos.getY() + 1, ownPos.getZ());
            EntityRefBoard.spawnNew(
                    this.self,
                    this.self.level,
                    new ItemStack(() -> new RefBoardItem(BOARD_STATS, EliteRefBoard.ID) {}),
                    EliteRefBoard.ID
            );
        }

        @Override
        public void stop() {
            super.stop();
        }
    }

    public static class WalkAwayGoal extends Goal {

        private final JudgeEntity self;

        public WalkAwayGoal(JudgeEntity judgeEntity) {
            this.self = judgeEntity;
        }

        @Override
        public boolean canUse() {
            if (this.self.hasAward) {
                return false;
            }
            return self.isOnGround();
        }

        @Override
        public void start() {
            if (this.self.level.isClientSide()) {
                return;
            }
            if (self.targetDestination == null) {
                return;
            }

            this.self.navigation.moveTo(
                    self.targetDestination.getX(),
                    self.targetDestination.getY(),
                    self.targetDestination.getZ(),
                    0.5
            );
        }

        @Override
        public void stop() {
            super.stop();
            this.self.navigation.stop();
        }
    }
}
