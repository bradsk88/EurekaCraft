package ca.bradj.eurekacraft.vehicles;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.interfaces.IIDHaver;
import net.minecraft.world.item.Item;

public abstract class EurekaCraftItem extends Item implements IIDHaver {

    protected static Properties BASE_PROPS() {
        return new Item.Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);
    }

    String itemId;

    public EurekaCraftItem(String itemId) {
        this(itemId, BASE_PROPS());
    }

    protected EurekaCraftItem(
            String itemId, Properties props
    ) {
        super(props);
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }
}
