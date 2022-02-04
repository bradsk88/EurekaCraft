package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.item.Item;

public class DiamondReflectionFilm extends Item {

    public static final String ITEM_ID = "diamond_reflection_film";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public DiamondReflectionFilm() {
        super(PROPS);
    }
}
