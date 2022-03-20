package ca.bradj.eurekacraft.vehicles;

import net.minecraft.item.ItemStack;

public class StandardRefBoard extends RefBoardItem {

    public static final BoardType ID = new BoardType("standard_ref_board");

    public StandardRefBoard() {
        super(RefBoardStats.StandardBoard, ID);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack defaultInstance = super.getDefaultInstance();
        storeStatsOnStack(defaultInstance, RefBoardStats.StandardBoard);
        return defaultInstance;
    }
}
