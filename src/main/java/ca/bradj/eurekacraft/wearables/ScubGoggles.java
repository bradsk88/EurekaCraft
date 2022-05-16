package ca.bradj.eurekacraft.wearables;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;

public class ScubGoggles extends ArmorItem {

    public static final String ITEM_ID = "scub_goggles";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public ScubGoggles() {
        super(ArmorMaterials.LEATHER, EquipmentSlot.HEAD, PROPS);
    }
    // TODO: Implement rendering
//
//    @OnlyIn(Dist.CLIENT)
//    @Nullable
//    @Override
//    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A _default) {
//        if (EquipmentSlotType.HEAD == armorSlot) {
//            return (A) new ScubGoggleHelmetModel(1f);
//        }
//        return _default;
//    }
//
//    @Nullable
//    @Override
//    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
//        return "eurekacraft:textures/models/armor/scub_goggles.png";
//    }
}
