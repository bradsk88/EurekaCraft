package ca.bradj.eurekacraft.vehicles;

import ca.bradj.eurekacraft.render.RefBoardModel;

public class StandardRefBoard extends RefBoard {
    public StandardRefBoard() {
        super(RefBoardStats.StandardBoard, new RefBoardModel()); // TODO: Should models be singletons?
    }
}
