package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.item.Item;

public class ClayStickyDiscItem extends Item {
    public static final String ITEM_ID = "clay_sticky_disc";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public ClayStickyDiscItem() {
        super(PROPS);
    }
}
