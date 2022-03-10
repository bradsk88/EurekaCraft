package ca.bradj.eurekacraft.crop;

import ca.bradj.eurekacraft.core.init.ItemsInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class FreshSeedsCrop extends CropsBlock {

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

    protected IItemProvider getBaseSeedId() {
        return ItemsInit.FRESH_SEEDS_ITEM.get();
    }

    @Override
    protected boolean mayPlaceOn(BlockState p_200014_1_, IBlockReader p_200014_2_, BlockPos p_200014_3_) {
        return super.mayPlaceOn(p_200014_1_, p_200014_2_, p_200014_3_);
    }

    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_,
                               ISelectionContext p_220053_4_) {
        return SHAPE_BY_AGE[p_220053_1_.getValue(this.getAgeProperty())];
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(AGE);
    }

    @Override
    public ActionResultType use(
            BlockState blockState, World world, BlockPos blockPos,
            PlayerEntity player, Hand hand, BlockRayTraceResult rtr
    ) {
        if (isMaxAge(blockState)) {
            world.destroyBlock(blockPos, true);
            world.setBlock(blockPos, this.getStateForAge(0), 2);
        }
        return super.use(blockState, world, blockPos, player, hand, rtr);
    }
}
