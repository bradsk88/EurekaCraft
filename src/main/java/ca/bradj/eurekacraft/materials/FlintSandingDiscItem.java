package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.item.Item;

public class FlintSandingDiscItem extends Item {
    public static final String ITEM_ID = "flint_sanding_disc";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public FlintSandingDiscItem() {
        super(PROPS);
    }
}
