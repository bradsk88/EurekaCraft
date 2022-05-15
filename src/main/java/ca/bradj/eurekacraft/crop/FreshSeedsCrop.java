package ca.bradj.eurekacraft.crop;

import ca.bradj.eurekacraft.core.init.AdvancementsInit;
import ca.bradj.eurekacraft.core.init.ItemsInit;
import net.minecraft.core.BlockPos;
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

public class FreshSeedsCrop extends CropBlock {

    public static final String BLOCK_ID = "fresh_seeds_crop";
    public static final Double FULL_HEIGHT = 16.0D;

    public static final IntegerProperty AGE = BlockStateProperties.AGE_15;
    public static final int MAX_AGE = 15;


    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 3, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 5, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 7, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 9, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 11, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 13, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 15, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, FULL_HEIGHT, 16.0D),
    };

    public FreshSeedsCrop() {
        super(Properties.copy(Blocks.WHEAT).
                lightLevel((BlockState bs) -> {
                    if (bs.getValue(AGE) == MAX_AGE) {
                        return 10;
                    }
                    return 0;
                }));
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
        return super.mayPlaceOn(p_200014_1_, p_200014_2_, p_200014_3_);
    }

    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_,
                               CollisionContext p_220053_4_) {
        return SHAPE_BY_AGE[p_220053_1_.getValue(this.getAgeProperty())];
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
}
