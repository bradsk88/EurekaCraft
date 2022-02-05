package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.item.Item;

public class ResinousDust extends Item {

    public static final String ITEM_ID = "resinous_dust";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public ResinousDust() {
        super(PROPS);
    }
}
