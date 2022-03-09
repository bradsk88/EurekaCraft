package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.item.Item;

public class PrecisionWoodStickItem extends Item {

    public static final String ITEM_ID = "precision_wood_stick";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public PrecisionWoodStickItem() {
        super(PROPS);
    }

}
