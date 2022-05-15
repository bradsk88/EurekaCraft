package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.world.item.Item;

public class ReflectionFilm extends Item {

    public static final String ITEM_ID = "reflection_film";
    private static final Item.Properties PROPS = new Item.Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public ReflectionFilm() {
        super(PROPS);
    }
}
