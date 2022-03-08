package ca.bradj.eurekacraft.interfaces;

import net.minecraft.item.ItemStack;

import java.util.Random;

public interface ITechAffected {
    void applyTechItem(ItemStack blueprint, ItemStack target, Random random);
}
