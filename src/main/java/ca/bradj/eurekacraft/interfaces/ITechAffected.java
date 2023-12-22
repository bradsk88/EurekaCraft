package ca.bradj.eurekacraft.interfaces;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public interface ITechAffected {
    void applyTechItem(Collection<ItemStack> inputs, ItemStack blueprint, ItemStack target, RandomSource random);
}
