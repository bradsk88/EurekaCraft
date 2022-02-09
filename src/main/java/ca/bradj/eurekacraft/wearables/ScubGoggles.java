package ca.bradj.eurekacraft.wearables;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;

public class ScubGoggles extends ArmorItem {

    public static final String ITEM_ID = "scub_goggles";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public ScubGoggles() {
        super(ArmorMaterial.LEATHER, EquipmentSlotType.HEAD, PROPS);
    }
}
