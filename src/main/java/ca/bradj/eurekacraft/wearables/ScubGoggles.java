package ca.bradj.eurekacraft.wearables;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.render.wearables.ScubGoggleHelmetModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ScubGoggles extends ArmorItem {

    public static final String ITEM_ID = "scub_goggles";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public ScubGoggles() {
        super(ArmorMaterial.LEATHER, EquipmentSlotType.HEAD, PROPS);
    }

    @Nullable
    @Override
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        if (EquipmentSlotType.HEAD == armorSlot) {
            return (A) new ScubGoggleHelmetModel(1f); // TODO: Probably need to make sided
        }
        return _default;
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return "eurekacraft:textures/models/armor/scub_goggles.png";
    }
}
