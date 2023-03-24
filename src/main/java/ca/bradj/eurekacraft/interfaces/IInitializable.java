package ca.bradj.eurekacraft.interfaces;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

public interface IInitializable {
    void initialize(ItemStack target, RandomSource random);
}
