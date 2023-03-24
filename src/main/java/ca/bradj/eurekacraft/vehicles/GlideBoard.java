package ca.bradj.eurekacraft.vehicles;

import net.minecraft.world.item.ItemStack;

import java.util.Random;

public class GlideBoard extends RefBoardItem {

    public static BoardType ID = new BoardType("glide_board");

    public GlideBoard() {
        super(RefBoardStats.GlideBoard, ID);
    }

    @Override
    public RefBoardStats getStatsForStack(
            ItemStack stack,
            Random rand
    ) {
        return baseStats;
    }
}
