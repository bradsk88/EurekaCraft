package ca.bradj.eurekacraft.interfaces;

import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.Random;

public interface IBoardStatsCraftable {

    void generateNewBoardStats(
            ItemStack target,
            Collection<ItemStack> context,
            Random random
    );

}
