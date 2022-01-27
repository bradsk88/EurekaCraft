package ca.bradj.eurekacraft.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class TraparWaveBlock extends Block {

    public static final Properties PROPS = AbstractBlock.Properties.
            of(Material.WEB).
            noCollission().
            requiresCorrectToolForDrops().
            strength(2.0F);
    public static final String ITEM_ID = "trapar_wave_block";
    public static final Item.Properties ITEM_PROPS = new Item.Properties().
            tab(ItemGroup.TAB_MISC);
    public TraparWaveBlock() {
        super(PROPS);
    }
}
