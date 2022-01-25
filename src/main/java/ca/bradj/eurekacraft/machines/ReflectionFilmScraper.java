package ca.bradj.eurekacraft.machines;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ReflectionFilmScraper extends Block {
    public static final String ITEM_ID = "reflection_film_scraper";
    public static final Item.Properties ITEM_PROPS = new Item.Properties().
            tab(ItemGroup.TAB_MISC);
    private static final Properties PROPS = Properties.copy(Blocks.CRAFTING_TABLE).
            sound(SoundType.WOOD);

    public ReflectionFilmScraper() {
        super(PROPS);
    }
}
