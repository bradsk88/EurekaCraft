package ca.bradj.eurekacraft.materials;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ReflectionFilm extends Item {

    public static final String ITEM_ID = "reflection_film";
    private static final Item.Properties PROPS = new Item.Properties().tab(ItemGroup.TAB_MATERIALS);

    public ReflectionFilm() {
        super(PROPS);
    }
}
