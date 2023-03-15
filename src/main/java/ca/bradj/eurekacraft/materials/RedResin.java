package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.world.item.Item;

public class RedResin extends Item {

    public static final String ITEM_ID = "red_resin";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public RedResin() {
        super(PROPS);
    }
}
