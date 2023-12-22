package ca.bradj.eurekacraft.crop;

import ca.bradj.eurekacraft.core.init.AdvancementsInit;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.core.init.items.ItemsInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

public class HardenedFreshSeedsCrop extends CropBlock {

    public static final String BLOCK_ID = "hardened_fresh_seeds_crop";
    public static final Double FULL_HEIGHT = 16.0D;

    public static final IntegerProperty AGE = BlockStateProperties.AGE_5;
    public static final int MAX_AGE = 5;


    private static final VoxelShape SHAPE = Block.box(
            0.0D, 0.0D, 0.0D, 16.0D, FULL_HEIGHT, 16.0D
    );

    public HardenedFreshSeedsCrop() {
        super(Properties.copy(Blocks.WHEAT).
                lightLevel((BlockState bs) -> 10));
    }

    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    public int getMaxAge() {
        return MAX_AGE;
    }

    protected ItemLike getBaseSeedId() {
        return ItemsInit.FRESH_SEEDS_ITEM.get();
    }

    @Override
    protected boolean mayPlaceOn(BlockState p_200014_1_, BlockGetter p_200014_2_, BlockPos p_200014_3_) {
        return false; // Cannot be placed
    }

    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_,
                               CollisionContext p_220053_4_) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(AGE);
    }

    @Override
    public InteractionResult use(
            BlockState blockState, Level world, BlockPos blockPos,
            Player player, InteractionHand hand, BlockHitResult rtr
    ) {
        if (world.isClientSide()) {
            return InteractionResult.CONSUME;
        }
        if (isMaxAge(blockState)) {
            world.destroyBlock(blockPos, true);
            AdvancementsInit.FRESH_CROPS_HARVEST_TRIGGER.trigger((ServerPlayer) player);
            world.setBlock(blockPos, this.getStateForAge(0), 2);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean isRandomlyTicking(BlockState p_52288_) {
        return true;
    }

    @Override
    public void randomTick(BlockState p_52292_, ServerLevel level, BlockPos blockPos, RandomSource  rand) {
        super.randomTick(p_52292_, level, blockPos, rand);
        if (isMaxAge(p_52292_)) {
            tryChangingToSapling(level, blockPos, rand);
        }
    }

    private void tryChangingToSapling(
            ServerLevel level, BlockPos blockPos, RandomSource  rand
    ) {
        for (Direction d : Direction.Plane.HORIZONTAL) {
            BlockState blockState = level.getBlockState(blockPos.relative(d));
            if (!blockState.is(BlocksInit.FRESH_SEEDS_CROP.get())) {
                return; // Can only harden if all surrounding blocks are also crops
            }
            if (blockState.getValue(FreshSeedsCrop.AGE) != FreshSeedsCrop.MAX_AGE) {
                return;
            }
        }
        if (rand.nextInt(100) == 0) {
            level.setBlock(blockPos, BlocksInit.TRAPAR_SAPLING.get().defaultBlockState(), 4);
        }
    }
}
