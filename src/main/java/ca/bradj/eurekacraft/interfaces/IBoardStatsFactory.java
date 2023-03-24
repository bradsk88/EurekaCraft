package ca.bradj.eurekacraft.interfaces;

import ca.bradj.eurekacraft.vehicles.RefBoardStats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

public interface IBoardStatsFactory {
    RefBoardStats getBoardStatsFromNBTOrCreate(
            ItemStack itemStack, RefBoardStats creationReference, RandomSource rand
    );
}
