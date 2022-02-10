package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.render.RefBoardCoreModel;
import ca.bradj.eurekacraft.vehicles.RefBoardItem;
import ca.bradj.eurekacraft.vehicles.RefBoardStats;
import net.minecraft.util.ResourceLocation;

public class RefBoardCoreItem extends RefBoardItem {

    public static final String ITEM_ID = "ref_board_core";
    public static final ResourceLocation ID = new ResourceLocation(EurekaCraft.MODID, "ref_board_core");

    public RefBoardCoreItem() {
        super(RefBoardStats.HeavyBoard, ID);
        this.canFly = false;
    }

}
