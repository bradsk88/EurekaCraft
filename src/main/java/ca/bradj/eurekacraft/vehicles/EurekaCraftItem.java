package ca.bradj.eurekacraft.vehicles;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.world.item.Item;

public class EurekaCraftItem extends Item {

    protected static Properties BASE_PROPS() {
        return new Item.Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);
    }

    public EurekaCraftItem() {
        this(BASE_PROPS());
    }

    protected EurekaCraftItem(
            Properties props
    ) {
        super(props);
    }
}
