package ca.bradj.eurekacraft.interfaces;

import ca.bradj.eurekacraft.integration.mc.Compat;
import net.minecraft.world.item.ItemStack;

public interface IInitializable {
    void initialize(ItemStack target, Compat.RandomSrc random);
}
