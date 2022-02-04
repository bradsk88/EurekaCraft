package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.render.RefBoardCoreModel;
import ca.bradj.eurekacraft.vehicles.RefBoardItem;
import ca.bradj.eurekacraft.vehicles.RefBoardStats;

public class RefBoardCoreItem extends RefBoardItem {

    public static final String ITEM_ID = "ref_board_core";

    public RefBoardCoreItem() {
        super(RefBoardStats.HeavyBoard, new RefBoardCoreModel());
        this.canFly = false;
    }

    // TODO: maybe spawn a board that does nothing? :P

}
