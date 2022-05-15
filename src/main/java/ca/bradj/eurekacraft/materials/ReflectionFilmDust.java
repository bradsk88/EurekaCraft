package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.world.item.Item;

public class ReflectionFilmDust extends Item {

    public static final String ITEM_ID = "reflection_film_dust";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public ReflectionFilmDust() {
        super(PROPS);
    }
}
