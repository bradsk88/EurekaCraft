package ca.bradj.eurekacraft.interfaces;

import net.minecraft.world.item.ItemStack;

import java.util.Random;

public interface IInitializable {
    // TODO: Consider context (i.e. inputs) during initialization?
    void initialize(ItemStack target, Random random);
}
