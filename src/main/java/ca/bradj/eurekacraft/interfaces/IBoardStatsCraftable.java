package ca.bradj.eurekacraft.interfaces;

import ca.bradj.eurekacraft.integration.mc.Compat;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public interface IBoardStatsCraftable {

    void generateNewBoardStats(
            ItemStack target,
            Collection<ItemStack> context,
            Compat.RandomSrc random
    );

}
