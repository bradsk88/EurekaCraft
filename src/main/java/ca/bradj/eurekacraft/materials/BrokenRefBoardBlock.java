package ca.bradj.eurekacraft.materials;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class BrokenRefBoardBlock extends Block {
    public static final String ITEM_ID = "broken_ref_board_block";
    public static final Item.Properties ITEM_PROPS = new Item.Properties().
            tab(ItemGroup.TAB_MISC);
    private static final Properties PROPS = Properties.copy(Blocks.OAK_WOOD);

    public BrokenRefBoardBlock() {
        super(PROPS);
    }
}
