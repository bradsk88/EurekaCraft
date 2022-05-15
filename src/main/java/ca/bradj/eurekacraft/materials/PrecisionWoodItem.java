package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.world.item.Item;

public class PrecisionWoodItem extends Item {
    public static final String ITEM_ID = "precision_wood";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public PrecisionWoodItem() {
        super(PROPS);
    }
}
