package ca.bradj.eurekacraft.wearables.deployment;

import ca.bradj.eurekacraft.core.init.ItemsInit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class DeployedPlayerGoggles {

    public static boolean areGogglesBeingWorn(@Nullable Entity player) {
        if (player == null) {
            return false;
        }
        for (ItemStack helmet : player.getArmorSlots()) {
            if (helmet.sameItemStackIgnoreDurability(ItemsInit.SCUB_GOGGLES.get().getDefaultInstance())) {
                return true;
            }
        }
        return false;
    }
}
