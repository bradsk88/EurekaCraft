package ca.bradj.eurekacraft.materials;

import net.minecraft.block.*;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BrokenRefBoardBlock extends HorizontalBlock {
    public static final String ITEM_ID = "broken_ref_board_block";
    public static final Item.Properties ITEM_PROPS = new Item.Properties().
            tab(ItemGroup.TAB_MISC);
    private static final Properties PROPS = Properties.
            copy(Blocks.IRON_BLOCK).
            sound(SoundType.WOOD).
            noOcclusion();

    public BrokenRefBoardBlock() {
        super(PROPS);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> b) {
        b.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    // TODO: Add voxel shape to make this 2 blocks tall
}
