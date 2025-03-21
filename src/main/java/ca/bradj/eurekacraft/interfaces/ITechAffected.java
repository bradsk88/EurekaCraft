package ca.bradj.eurekacraft.interfaces;

import ca.bradj.eurekacraft.integration.mc.Compat;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public interface ITechAffected {
    void applyTechItem(
            Collection<ItemStack> inputs,
            ItemStack blueprint,
            ItemStack target,
            Compat.RandomSrc random
    );
}
