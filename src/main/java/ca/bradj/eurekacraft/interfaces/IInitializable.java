package ca.bradj.eurekacraft.interfaces;

import net.minecraft.world.item.ItemStack;

import java.util.Random;

public interface IInitializable {
    void initialize(ItemStack target, Random random);
}
