package ca.bradj.eurekacraft.blocks;

import ca.bradj.eurekacraft.core.init.BlocksInit;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class TraparWaveBlock extends Block {

    // TODO Stop casting shadows
    public static final Properties PROPS = AbstractBlock.Properties.
            copy(Blocks.GLASS).
            noCollission().
            requiresCorrectToolForDrops().
            strength(2.0F).
            isSuffocating(BlocksInit::never);

    public static final String ITEM_ID = "trapar_wave_block";
    public static final Item.Properties ITEM_PROPS = new Item.Properties().
            tab(ItemGroup.TAB_MISC);
    public TraparWaveBlock() {
        super(PROPS);
    }
}
