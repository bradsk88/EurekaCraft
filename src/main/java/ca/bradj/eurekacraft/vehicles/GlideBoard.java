package ca.bradj.eurekacraft.vehicles;

public class GlideBoard extends RefBoardItem {

    public static BoardType ID = new BoardType("glide_board");

    public GlideBoard() {
        super(RefBoardStats.GlideBoard, ID);
    }
}
