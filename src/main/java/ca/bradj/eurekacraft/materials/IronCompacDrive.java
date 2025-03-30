package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.config.EurekaConfig;
import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.world.item.Item;

public class IronCompacDrive extends Item implements TraparCapacityGiven {

    public static final String ITEM_ID = "compac_drive";

    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public IronCompacDrive() {
        super(PROPS);
    }

    @Override
    public int getTraparCapacity() {
        return (int) (0.4 * EurekaConfig.max_trapar_storage.get());
    }
}
