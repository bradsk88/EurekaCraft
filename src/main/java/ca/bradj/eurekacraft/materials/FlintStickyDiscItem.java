package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.item.Item;

public class FlintStickyDiscItem extends Item {
    public static final String ITEM_ID = "flint_sticky_disc";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public FlintStickyDiscItem() {
        super(PROPS);
    }
}
