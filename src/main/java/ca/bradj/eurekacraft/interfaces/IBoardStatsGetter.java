package ca.bradj.eurekacraft.interfaces;

import ca.bradj.eurekacraft.vehicles.RefBoardStats;
import net.minecraft.world.item.ItemStack;

public interface IBoardStatsGetter {
    RefBoardStats getBoardStats(ItemStack stack);
}
