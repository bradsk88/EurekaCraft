package ca.bradj.eurekacraft.materials;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ReflectionFilmDust extends Item {

    public static final String ITEM_ID = "reflection_film_dust";
    private static final Properties PROPS = new Properties().tab(ItemGroup.TAB_MATERIALS);

    public ReflectionFilmDust() {
        super(PROPS);
    }
}
