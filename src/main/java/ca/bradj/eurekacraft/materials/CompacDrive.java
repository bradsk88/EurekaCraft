package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.world.item.Item;

public class CompacDrive extends Item {

    public static final String ITEM_ID = "compac_drive";

    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public CompacDrive() {
        super(PROPS);
    }

}
