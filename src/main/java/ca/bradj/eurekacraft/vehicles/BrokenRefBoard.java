package ca.bradj.eurekacraft.vehicles;

import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class BrokenRefBoard extends RefBoardItem {
    public static final String ITEM_ID = "broken_ref_board";
    public static final ResourceLocation ID = new ResourceLocation(EurekaCraft.MODID, "broken_ref_board");

    public BrokenRefBoard() {
        super(RefBoardStats.HeavyBoard.damaged(), ID);
    }

    @Override
    public ItemStack getDefaultInstance() {
        return super.getDefaultInstance();
    }
}
