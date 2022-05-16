package ca.bradj.eurekacraft.vehicles;

import net.minecraft.world.item.ItemStack;

public class EliteRefBoard extends RefBoardItem {

    public static final String ITEM_ID = "elite_ref_board";
    public static final BoardType ID = new BoardType(ITEM_ID);

    public EliteRefBoard() {
        super(RefBoardStats.EliteBoard, ID);
    }

    @Override
    public boolean isFoil(ItemStack p_77636_1_) {
        return true;
    }
}
