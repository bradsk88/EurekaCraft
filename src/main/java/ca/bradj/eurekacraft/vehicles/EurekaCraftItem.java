package ca.bradj.eurekacraft.vehicles;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.world.item.Item;

public class EurekaCraftItem extends Item {
    public EurekaCraftItem() {
        super(new Item.Properties().tab(ModItemGroup.EUREKACRAFT_GROUP));
    }
}
