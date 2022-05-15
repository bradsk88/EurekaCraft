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
import ca.bradj.eurekacraft.world.storm.StormSavedData;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class JudgeEntity extends PathfinderMob {

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
    private BlockPos lastPos;

    public JudgeEntity(EntityType<? extends PathfinderMob> entity, Level world) {
        super(entity, world);
    }

    public JudgeEntity(ServerPlayer rewardRecipient, Level world) {
        this(EntitiesInit.JUDGE.get(), world);
        this.rewardRecipient = rewardRecipient.getUUID();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().
                add(Attributes.MAX_HEALTH, 10.0D);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        Serializer ser = new Serializer(this);
        nbt.put("ec_judge_state", ser.serializeNBT());
        super.addAdditionalSaveData(nbt);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        Serializer ser = new Serializer(this);
        ser.deserializeNBT(nbt.getCompound("ec_judge_state"));
        if (this.vanishDestination != null && !this.isOnGround()) {
            spawnRefBoard();
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if (IPlayerEntityBoardDeployed.class.getName().equals(cap.getName())) {
            return handler.cast();
        }
        return super.getCapability(cap);
    }

    public static void spawnToRewardPlayer(ServerPlayer player) {
        JudgeEntity judge = new JudgeEntity(player, player.level);
        Vec3 viewVector = player.getViewVector(1.0f);
        Vec3 max = new Vec3(
                player.position().x + (viewVector.x * 20),
                player.position().y + (viewVector.y * 20),
                player.position().z + (viewVector.z * 20)
        );
        Vec3 min = new Vec3(
                player.position().x + (viewVector.x * 10),
                player.position().y + (viewVector.y * 10),
                player.position().z + (viewVector.z * 10)
        );
        judge.setPos(min.x, min.y, min.z);
        Vec3 airPos = AirRandomPos.getPosTowards(judge, 8, 3, -2, max, Math.PI / 10f);
        if (airPos == null) {
            airPos = player.position();
        }
        judge.setPos(airPos.x, airPos.y, airPos.z);
        judge.setYBodyRot(player.yBodyRot);
        player.level.addFreshEntity(judge);
        player.level.playSound(
                null, airPos.x, airPos.y, airPos.z, SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 1.0f, 0.5f
        );
        player.sendMessage(new TextComponent("message.tricks.judge_appeared"), Util.NIL_UUID);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VILLAGER_YES;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public SpawnGroupData finalizeSpawn(
            ServerLevelAccessor acc, DifficultyInstance diff, MobSpawnType spawnType,
            @org.jetbrains.annotations.Nullable SpawnGroupData sgData,
            @org.jetbrains.annotations.Nullable CompoundTag tag
    ) {
        SpawnGroupData spawn = super.finalizeSpawn(acc, diff, spawnType, sgData, tag);
        setItemSlot(EquipmentSlot.HEAD, ItemsInit.SCUB_GOGGLES.get().getDefaultInstance());
        // FIXME: Make these render
        return spawn;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand p_230254_2_) {
        Level world = player.level;
        if (world.isClientSide()) {
            this.hasAward = false;
            return super.mobInteract(player, p_230254_2_);
        }

        BlockPos ownPos = blockPosition();

        if (!this.rewardRecipient.equals(player.getUUID()) || !this.hasAward) {
            logger.debug("RewardRecipient " + rewardRecipient + " player " + player.getUUID() + " [hasAward:" + hasAward + "]");
            this.level.playSound(
                    null, ownPos, SoundEvents.VILLAGER_NO, SoundSource.NEUTRAL, 0.5f, 1.2f
            );
            return InteractionResult.CONSUME;
        }

        giveAwardToPlayer(player);

        return InteractionResult.CONSUME;
    }

    private void giveAwardToPlayer(Player player) {
        BlockPos ownPos = blockPosition();
        this.level.playSound(
                null, ownPos, SoundEvents.VILLAGER_CELEBRATE, SoundSource.NEUTRAL, 0.5f, 1.2f
        );
        this.level.playSound(
                null, ownPos, SoundEvents.NOTE_BLOCK_CHIME, SoundSource.NEUTRAL, 0.5f, 0.2f
        );

        // TODO: Give award instead of film
        ItemStack awardStack = new ItemStack(ItemsInit.REFLECTION_FILM::get, 1);
        ItemEntity awardEntity = new ItemEntity(
                level, ownPos.getX(), ownPos.getY() + 2, ownPos.getZ(), awardStack
        );
        this.level.addFreshEntity(awardEntity);

        this.hasAward = false;
        this.awardPos = this.blockPosition();

        player.sendMessage(new TextComponent("message.tricks.congrats"), Util.NIL_UUID);
    }

    @Override
    public void tick() {
        super.tick();

        if (level.isClientSide()) {
            return;
        }

        trackTimeSpentInWater();
        flyAwayTick();

        this.lastPos = blockPosition();
    }

    private void trackTimeSpentInWater() {
        if (this.isInWater()) {
            this.timeInWater++;
        } else {
            this.timeOutOfWater++;
            if (this.timeOutOfWater > 10) {
                this.timeInWater = 0;
            }
        }
    }

    private void flyAwayTick() {
        if (!this.hasAward) {
            this.vanishWarmup--;
        }

        if (awardPos != null) {
            if (vanishDestination == null || level.random.nextInt(10) == 0) {
                chooseNewDirection();
            }

            if (shouldVanish()) {
                vanish();
            }
        }

        avoidGettingStuck();
    }

    private void avoidGettingStuck() {
        int lastTimeStuck = timeStuck;
        timeStuck = 0;
        if (this.lastPos == null) {
            return;
        }

        BlockPos blockPos = this.blockPosition();
        logger.debug("Checking stuck [Current: " + blockPos + "] [Last: " + this.lastPos + "]");
        boolean sameX = blockPos.getX() == this.lastPos.getX();
        boolean sameZ = blockPos.getZ() == this.lastPos.getZ();
        if (sameX && sameZ) {
            timeStuck = lastTimeStuck + 1;
        }

        if (timeStuck < 50) {
            return;
        }

        Direction randDir = Direction.Plane.HORIZONTAL.getRandomDirection(level.random);
        BlockPos newPosition = this.blockPosition().relative(randDir);
        if (level.getBlockState(newPosition).isAir()) {
            if (level.getBlockState(newPosition.above()).isAir()) {
                if (level.getBlockState(newPosition.below()).isAir()) {
                    this.moveTo(newPosition, 1.0f, 1.0f);
                    logger.debug("bumping due to stuck");
                    timeStuck = 0;
                }
            }
        }
    }

    private boolean shouldVanish() {
        return position().distanceTo(new Vec3(
                awardPos.getX(),
                awardPos.getY(),
                awardPos.getZ()
        )) > 100;
    }

    private void vanish() {
        // FIXME: This is not adding particles
        this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 1.0, 1.0, 1.0);
        this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 1.0, 1.0, 1.0);
        this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 1.0, 1.0, 1.0);
        this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 1.0, 1.0, 1.0);
        this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 1.0, 1.0, 1.0);
        this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 1.0, 1.0, 1.0);
        this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 1.0, 1.0, 1.0);
        this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 1.0, 1.0, 1.0);
        this.level.playSound(null, this.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.AMBIENT, 1.0f, 0.5f);
        StormSavedData.triggerTraparExplosion(this.blockPosition(), 4, 2f);
        this.remove(RemovalReason.DISCARDED);
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
        logger.debug("Judge is moving toward " + this.vanishDestination);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new InitialLandGoal(this));
        this.goalSelector.addGoal(0, new FindSafePlaceGoal(this));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(3, new FlyAwayGoal(this));
        this.goalSelector.addGoal(4, new WalkAwayGoal(this));
    }

    private abstract static class JudgeGoal extends Goal {
        @Override
        public void start() {
            super.start();
            logger.debug("Starting goal " + this);
        }
    }

    public static class InitialLandGoal extends JudgeGoal {

        private final JudgeEntity self;
        private Vec3 landTarget;

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
            this.landTarget = LandRandomPos.getPos(this.self, 15, 7);

            EntityRefBoard.spawnFromInventory(
                    this.self,
                    (ServerLevel) this.self.level,
                    ItemsInit.BROKEN_BOARD.get().getDefaultInstance(), // Makes it land faster
                    EliteRefBoard.ID
            );
        }
    }

    public static class FindSafePlaceGoal extends JudgeGoal {

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
            Vec3 oldMov = judge.getDeltaMovement();
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
            Vec3 landTarget = LandRandomPos.getPos(this.self, 15, 7);
            if (landTarget == null) {
                landTarget = this.self.position().add(100, 0, 0);
            }
            this.self.navigation.moveTo(landTarget.x, landTarget.y, landTarget.z, 0.5);
        }
    }

    public static class LookAtPlayerGoal extends net.minecraft.world.entity.ai.goal.LookAtPlayerGoal {

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

    public static class FlyAwayGoal extends JudgeGoal {

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
            if (this.self.level.isClientSide()) {
                return;
            }
            BlockPos ownPos = this.self.blockPosition();

            this.self.setPos(ownPos.getX(), ownPos.getY() + 1, ownPos.getZ());
            this.self.spawnRefBoard();
        }

        @Override
        public void stop() {
            super.stop();
        }
    }

    private void spawnRefBoard() {
        EntityRefBoard.spawnFromInventory(
                this,
                (ServerLevel) this.level,
                new ItemStack(() -> new RefBoardItem(BOARD_STATS, EliteRefBoard.ID) {
                }),
                EliteRefBoard.ID
        );
    }

    public static class WalkAwayGoal extends JudgeGoal {

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

    private static class Serializer implements INBTSerializable<CompoundTag> {

        private final JudgeEntity entity;

        private Serializer(JudgeEntity entity) {
            this.entity = entity;
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag n = new CompoundTag();

            if (entity.vanishDestination != null) {
                n.put("vanish_pos", serializePos(entity.vanishDestination));
            }
            if (entity.awardPos != null) {
                n.put("award_pos", serializePos(entity.awardPos));
            }
            if (entity.lastPos != null) {
                n.put("last_pos", serializePos(entity.lastPos));
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
        public void deserializeNBT(CompoundTag nbt) {
            if (nbt.contains("vanish_pos")) {
                entity.vanishDestination = deserializePos(nbt.getCompound("vanish_pos"));
            }
            if (nbt.contains("award_pos")) {
                entity.awardPos = deserializePos(nbt.getCompound("award_pos"));
            }
            if (nbt.contains("last_pos")) {
                entity.lastPos = deserializePos(nbt.getCompound("last_pos"));
            }
            entity.hasAward = nbt.getBoolean("has_award");
            entity.vanishWarmup = nbt.getInt("vanish_warmup");
            entity.timeInWater = nbt.getInt("time_in_water");
            entity.timeOutOfWater = nbt.getInt("time_out_of_water");
            entity.timeStuck = nbt.getInt("time_stuck");
            entity.rewardRecipient = UUID.fromString(nbt.getString("recipient_uuid"));
        }

        private CompoundTag serializePos(BlockPos p) {
            CompoundTag pos = new CompoundTag();
            pos.putInt("x", p.getX());
            pos.putInt("y", p.getY());
            pos.putInt("z", p.getZ());
            return pos;
        }

        private BlockPos deserializePos(CompoundTag n) {
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
