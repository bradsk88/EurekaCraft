package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.vehicles.BoardType;
import ca.bradj.eurekacraft.vehicles.RefBoardItem;
import ca.bradj.eurekacraft.vehicles.RefBoardStats;

public class RefBoardCoreItem extends RefBoardItem {

    public static final String ITEM_ID = "ref_board_core";
    public static final BoardType ID = new BoardType("ref_board_core");

    public RefBoardCoreItem() {
        super(RefBoardStats.HeavyBoard, ID);
        this.canFly = false;
    }

}
