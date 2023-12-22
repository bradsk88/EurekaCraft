package ca.bradj.eurekacraft.interfaces;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public interface IBoardStatsCraftable {

    void generateNewBoardStats(
            ItemStack target,
            Collection<ItemStack> context,
            RandomSource random
    );

}
