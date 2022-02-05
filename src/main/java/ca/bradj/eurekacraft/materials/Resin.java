package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.item.Item;

public class Resin extends Item {

    public static final String ITEM_ID = "resin";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public Resin() {
        super(PROPS);
    }
}
