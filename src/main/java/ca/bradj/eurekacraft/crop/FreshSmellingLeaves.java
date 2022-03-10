package ca.bradj.eurekacraft.crop;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.item.Item;

public class FreshSmellingLeaves extends Item {

    public static final String ITEM_ID = "fresh_smelling_leaves";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public FreshSmellingLeaves() {
        super(PROPS);
    }
}
