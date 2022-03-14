package ca.bradj.eurekacraft.entity;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.EntitiesInit;
import ca.bradj.eurekacraft.core.init.ItemsInit;
import ca.bradj.eurekacraft.entity.board.EntityRefBoard;
import ca.bradj.eurekacraft.vehicles.BoardType;
import ca.bradj.eurekacraft.vehicles.EliteRefBoard;
import ca.bradj.eurekacraft.vehicles.RefBoardItem;
import ca.bradj.eurekacraft.vehicles.RefBoardStats;
import ca.bradj.eurekacraft.vehicles.deployment.IPlayerEntityBoardDeployed;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoard;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
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
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

// TODO: Persist internal state
public class JudgeEntity extends CreatureEntity {

    private static final Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    public static final ResourceLocation ENTITY_ID = new ResourceLocation(
            EurekaCraft.MODID, "judge_entity"
    );

    private static final RefBoardStats BOARD_STATS = RefBoardStats.StandardBoard.
            WithSpeed(RefBoardStats.MIN_SPEED * 2).
            WithLift(RefBoardStats.MIN_POSITIVE_LIFT).
            WithSurf(RefBoardStats.MAX_SURF_FOREVER);

    private static final IPlayerEntityBoardDeployed deployed = new DeployedBoardCapability();

    private static final LazyOptional<IPlayerEntityBoardDeployed> handler = LazyOptional.of(
            () -> deployed
    );

    private UUID rewardRecipient;
    private BlockPos vanishDestination;
    private boolean hasAward = true;
    private BlockPos awardPos;
    private int vanishWarmup = 100;
    private int timeInWater = 0;
    private int timeOutOfWater = 0;
    private int timeStuck = 0;
    private BlockPos lastBoardPos;

    public JudgeEntity(EntityType<? extends CreatureEntity> entity, World world) {
        super(entity, world);
    }

    public JudgeEntity(ServerPlayerEntity rewardRecipient, World world) {
        this(EntitiesInit.JUDGE.get(), world);
        this.rewardRecipient = rewardRecipient.getUUID();
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes().
                add(Attributes.MAX_HEALTH, 10.0D);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt) {
        Serializer ser = new Serializer(this);
        nbt.put("ec_judge_state", ser.serializeNBT());
        super.addAdditionalSaveData(nbt);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
        Serializer ser = new Serializer(this);
        ser.deserializeNBT(nbt.getCompound("ec_judge_state"));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if (IPlayerEntityBoardDeployed.class.getName().equals(cap.getName())) {
            return handler.cast();
        }
        return super.getCapability(cap);
    }

    public static void spawnToRewardPlayer(ServerPlayerEntity player) {
        JudgeEntity judge = new JudgeEntity(player, player.level);
        Vector3d viewVector = player.getViewVector(1.0f);
        BlockPos spawnPos = new BlockPos(
                player.position().x + viewVector.x,
                player.position().y + viewVector.y,
                player.position().z + viewVector.z
        );
        judge.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
        player.level.addFreshEntity(judge);
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
    protected ActionResultType mobInteract(PlayerEntity player, Hand p_230254_2_) {
        World world = player.level;
        if (world.isClientSide()) {
            return ActionResultType.PASS;
        }
        if (!this.hasAward) {
            return ActionResultType.CONSUME;
        }

        BlockPos ownPos = blockPosition();

        if (this.rewardRecipient != player.getUUID()) {
            this.level.playLocalSound(
                    ownPos.getX(), ownPos.getY(), ownPos.getZ(),
                    SoundEvents.VILLAGER_NO, SoundCategory.NEUTRAL,
                    0.5f, 1.2f, false
            );
            return ActionResultType.CONSUME;
        }

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
            return super.mobInteract(player, p_230254_2_);
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
        this.awardPos = this.blockPosition();

        return ActionResultType.CONSUME;
    }

    @Override
    public void tick() {
        super.tick();

        // TODO: Can this all be server side?

        if (this.isInWater()) {
            this.timeInWater++;
        } else {
            this.timeOutOfWater++;
            if (this.timeOutOfWater > 10) {
                this.timeInWater = 0;
            }
        }

        if (!level.isClientSide() && this.lastBoardPos != null && PlayerDeployedBoard.get(this).isPresent()) {
            BlockPos blockPos = this.blockPosition();
            logger.debug("Yes there is a board deployed at " + blockPos + " [lastPos: " + lastBoardPos + "]");
            if (
                    blockPos.getX() == this.lastBoardPos.getX() &&
                            blockPos.getZ() == this.lastBoardPos.getZ()
            ) {
                timeStuck++;
            }
            this.lastBoardPos = blockPos;
        } else {
            timeStuck = 0;
        }

        if (this.timeStuck > 10) {
            this.timeStuck = 0;
            this.moveTo(
                    this.blockPosition().relative(Direction.getRandom(level.random)),
                    1.0f, 1.0f
            );
            logger.debug("bumping due to stuck");
        }

        if (!this.hasAward) {
            this.vanishWarmup--;
        }

        if (awardPos != null) {

            if (vanishDestination == null || level.random.nextInt(20) == 0) {
                chooseNewDirection();
            }

            if (position().distanceTo(new Vector3d(
                    awardPos.getX(),
                    awardPos.getY(),
                    awardPos.getZ()
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

        this.vanishDestination = newPos;
//        logger.debug("Judge is moving toward " + this.vanishDestination);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new InitialLandGoal(this));
        this.goalSelector.addGoal(0, new FindSafePlaceGoal(this));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, PlayerEntity.class, 10.0F));
        this.goalSelector.addGoal(3, new FlyAwayGoal(this));
        this.goalSelector.addGoal(4, new WalkAwayGoal(this));
    }

    public static class InitialLandGoal extends Goal {

        private final JudgeEntity self;
        private Vector3d landTarget;

        InitialLandGoal(JudgeEntity entity) {
            this.self = entity;
        }

        @Override
        public boolean canUse() {
            // TODO: also use this for initial landing
            return this.self.hasAward && !this.self.isOnGround();
        }

        @Override
        public void tick() {
            super.tick();
            if (landTarget == null) {
                return;
            }
            this.self.lookControl.setLookAt(this.landTarget);

            if (this.landTarget.distanceTo(this.self.position()) < 2) {
                this.stop();
            }
        }

        @Override
        public void start() {
//            logger.debug("Starting goal " + this);
            this.landTarget = RandomPositionGenerator.getLandPos(this.self, 15, 7);

            EntityRefBoard.spawnFromInventory(
                    this.self,
                    (ServerWorld) this.self.level,
                    ItemsInit.BROKEN_BOARD.get().getDefaultInstance(), // Makes it land faster
                    EliteRefBoard.ID
            );
        }
    }

    public static class FindSafePlaceGoal extends Goal {

        private final JudgeEntity self;

        FindSafePlaceGoal(JudgeEntity entity) {
            this.self = entity;
        }

        @Override
        public boolean canUse() {
            return this.self.isInWater();
        }

        @Override
        public void tick() {
            super.tick();
            JudgeEntity judge = (JudgeEntity) this.self;
            logger.debug(String.format(
                    "timeInWater %d timeOutOfWater %d", judge.timeInWater, judge.timeOutOfWater
            ));
            Vector3d oldMov = judge.getDeltaMovement();
            if (judge.isInWater()) {
                judge.setDeltaMovement(
                        oldMov.x, oldMov.y + 1, oldMov.z
                );
            }
            if (judge.timeInWater > 20) {
                judge.moveTo(
                        oldMov.x, oldMov.y + 1, oldMov.z
                );
            }
        }

        @Override
        public void start() {
            super.start();
//            logger.debug("Starting goal " + this);
            Vector3d landTarget = RandomPositionGenerator.getLandPos(this.self, 15, 7);
            if (landTarget == null) {
                landTarget = this.self.position().add(100, 0, 0);
            }
            this.self.navigation.moveTo(landTarget.x, landTarget.y, landTarget.z, 0.5);
        }
    }

    public static class LookAtPlayerGoal extends LookAtGoal {

        private final JudgeEntity self;

        public LookAtPlayerGoal(JudgeEntity p_i1631_1_, Class<? extends LivingEntity> p_i1631_2_, float p_i1631_3_) {
            super(p_i1631_1_, p_i1631_2_, p_i1631_3_);
            this.self = p_i1631_1_;
        }

        @Override
        public boolean canUse() {
            if (this.self.hasAward) {
                return super.canUse();
            }
            return false;
        }

        @Override
        public void start() {
            super.start();
//            logger.debug("Starting goal " + this);
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
            if (this.self.vanishWarmup > 0) {
                return false;
            }
            return self.isOnGround();
        }

        @Override
        public void tick() {
            super.tick();
            BlockPos t = self.vanishDestination;
            if (t == null) {
                return;
            }

            this.self.getLookControl().setLookAt(t.getX(), t.getY(), t.getZ());
            this.self.getMoveControl().setWantedPosition(t.getX(), t.getY(), t.getZ(), 0.5f);
            // TODO: Set off trapar grenades as he flies to generate boost
        }

        @Override
        public void start() {
//            logger.debug("Starting goal " + this);
            if (this.self.level.isClientSide()) {
                return;
            }
            BlockPos ownPos = this.self.blockPosition();


            this.self.setPos(ownPos.getX(), ownPos.getY() + 1, ownPos.getZ());
            EntityRefBoard.spawnFromInventory(
                    this.self,
                    (ServerWorld) this.self.level,
                    new ItemStack(() -> new RefBoardItem(BOARD_STATS, EliteRefBoard.ID) {
                    }),
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
//            logger.debug("Starting goal " + this);
            if (this.self.level.isClientSide()) {
                return;
            }
            if (self.vanishDestination == null) {
                return;
            }

            this.self.navigation.moveTo(
                    self.vanishDestination.getX(),
                    self.vanishDestination.getY(),
                    self.vanishDestination.getZ(),
                    0.5
            );
        }

        @Override
        public void stop() {
            super.stop();
            this.self.navigation.stop();
        }
    }

    private static class Serializer implements INBTSerializable<CompoundNBT> {

        private final JudgeEntity entity;

        private Serializer(JudgeEntity entity) {
            this.entity = entity;
        }

        @Override
        public CompoundNBT serializeNBT() {
            CompoundNBT n = new CompoundNBT();

            if (entity.vanishDestination != null) {
                n.put("vanish_pos", serializePos(entity.vanishDestination));
            }
            if (entity.awardPos != null) {
                n.put("award_pos", serializePos(entity.awardPos));
            }
            if (entity.lastBoardPos != null) {
                n.put("last_pos", serializePos(entity.lastBoardPos));
            }
            n.putBoolean("has_award", entity.hasAward);
            n.putInt("vanish_warmup", entity.vanishWarmup);
            n.putInt("time_in_water", entity.timeInWater);
            n.putInt("time_out_of_water", entity.timeOutOfWater);
            n.putInt("time_stuck", entity.timeStuck);
            n.putString("recipient_uuid", entity.rewardRecipient.toString());

            return n;
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            if (nbt.contains("vanish_pos")) {
                entity.vanishDestination = deserializePos(nbt.getCompound("vanish_pos"));
            }
            if (nbt.contains("award_pos")) {
                entity.awardPos = deserializePos(nbt.getCompound("award_pos"));
            }
            if (nbt.contains("last_pos")) {
                entity.lastBoardPos = deserializePos(nbt.getCompound("last_pos"));
            }
            entity.hasAward = nbt.getBoolean("has_award");
            entity.vanishWarmup = nbt.getInt("vanish_warmup");
            entity.timeInWater = nbt.getInt("time_in_water");
            entity.timeOutOfWater = nbt.getInt("time_out_of_water");
            entity.timeStuck = nbt.getInt("time_stuck");
            entity.rewardRecipient = UUID.fromString(nbt.getString("recipient_uuid"));
        }

        private CompoundNBT serializePos(BlockPos p) {
            CompoundNBT pos = new CompoundNBT();
            pos.putInt("x", p.getX());
            pos.putInt("y", p.getY());
            pos.putInt("z", p.getZ());
            return pos;
        }

        private BlockPos deserializePos(CompoundNBT n) {
            int x = n.getInt("x");
            int y = n.getInt("y");
            int z = n.getInt("z");
            return new BlockPos(x, y, z);
        }

    }

    private static class DeployedBoardCapability implements IPlayerEntityBoardDeployed {

        @Override
        public BoardType getBoardType() {
            return EliteRefBoard.ID;
        }

        @Override
        public void setBoardType(BoardType boardType) {
            return;
        }
    }
}
