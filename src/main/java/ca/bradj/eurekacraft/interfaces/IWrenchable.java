package ca.bradj.eurekacraft.interfaces;

import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.Optional;

public interface IWrenchable {
    boolean canApplyWrench(Collection<ItemStack> inputs, ItemStack target);
    Optional<ItemStack> applyWrench(Collection<ItemStack> inputs, ItemStack target);
}
