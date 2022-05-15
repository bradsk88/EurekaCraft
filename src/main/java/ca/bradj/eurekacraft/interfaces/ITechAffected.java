package ca.bradj.eurekacraft.interfaces;

import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.Random;

public interface ITechAffected {
    void applyTechItem(Collection<ItemStack> inputs, ItemStack blueprint, ItemStack target, Random random);
}
