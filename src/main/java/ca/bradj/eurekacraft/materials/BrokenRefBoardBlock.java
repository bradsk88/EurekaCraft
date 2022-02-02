package ca.bradj.eurekacraft.materials;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BrokenRefBoardBlock extends Block {
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

}
