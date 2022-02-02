package ca.bradj.eurekacraft.vehicles;

import ca.bradj.eurekacraft.render.BrokenRefBoardModel;
import net.minecraft.item.ItemStack;

public class BrokenRefBoard extends RefBoardItem {
    public static final String ITEM_ID = "broken_ref_board";

    public BrokenRefBoard() {
        super(RefBoardStats.HeavyBoard.damaged(), new BrokenRefBoardModel());
    }

    @Override
    public ItemStack getDefaultInstance() {
        return super.getDefaultInstance();
    }
}
