package ca.bradj.eurekacraft.interfaces;

import net.minecraft.item.ItemStack;

import java.util.Random;

public interface ITechAffectable { // TODO: More generic name?
    void applyTechItem(ItemStack blueprint, ItemStack target, Random level);
}
