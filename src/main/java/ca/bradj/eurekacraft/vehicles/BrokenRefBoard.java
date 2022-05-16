package ca.bradj.eurekacraft.vehicles;

import net.minecraft.world.item.ItemStack;

public class BrokenRefBoard extends RefBoardItem {
    public static final String ITEM_ID = "broken_ref_board";
    public static final BoardType ID = new BoardType("broken_ref_board");

    public BrokenRefBoard() {
        super(RefBoardStats.HeavyBoard.damaged(), ID);
    }

    @Override
    public ItemStack getDefaultInstance() {
        return super.getDefaultInstance();
    }
}
