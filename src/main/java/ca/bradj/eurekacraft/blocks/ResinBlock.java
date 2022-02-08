package ca.bradj.eurekacraft.blocks;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ResinBlock extends Block {

    public static final Properties PROPS = Properties.
            copy(Blocks.SLIME_BLOCK);

    public static final String ITEM_ID = "resin_block";
    public static final Item.Properties ITEM_PROPS = new Item.Properties().
            tab(ModItemGroup.EUREKACRAFT_GROUP);
    public ResinBlock() {
        super(PROPS);
    }

}
