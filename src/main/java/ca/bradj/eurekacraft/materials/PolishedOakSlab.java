package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.world.item.Item;

public class PolishedOakSlab extends Item {
    public static final String ITEM_ID = "polished_oak_slab";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public PolishedOakSlab() {
        super(PROPS);
    }
}
