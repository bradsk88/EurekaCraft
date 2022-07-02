package ca.bradj.eurekacraft.wearables;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.render.wearables.ScubGoggleHelmetModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemRenderProperties;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ScubGoggles extends ArmorItem {

    public static final String ITEM_ID = "scub_goggles";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public ScubGoggles() {
        super(ArmorMaterials.LEATHER, EquipmentSlot.HEAD, PROPS);
    }
    // TODO: Implement rendering

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            @Nullable
            @Override
            public HumanoidModel<?> getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
                if (EquipmentSlot.HEAD == armorSlot) {
                    return new ScubGoggleHelmetModel();
                }
                return null;
            }
        });
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return "eurekacraft:textures/models/armor/scub_goggles.png";
    }
}
