package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.world.item.Item;

public class ReflectionFilmMoldItem extends Item {

    public static final String ITEM_ID = "reflection_film_mold";
    private static final Properties PROPS = new Properties().
            tab(ModItemGroup.EUREKACRAFT_GROUP).
            durability(3);

    public ReflectionFilmMoldItem() {
        super(PROPS);
    }
}
