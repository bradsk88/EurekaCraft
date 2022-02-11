package ca.bradj.eurekacraft.vehicles;

public class StandardRefBoard extends RefBoardItem {

    public static final BoardType ID = new BoardType("standard_ref_board");

    public StandardRefBoard() {
        super(RefBoardStats.StandardBoard, ID);
    }
}
