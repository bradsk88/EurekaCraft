package ca.bradj.eurekacraft.interfaces;

import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public interface IPaintable {
    void applyPaint(Collection<ItemStack> inputs, ItemStack paint, ItemStack target);
}
