package ca.bradj.eurekacraft.vehicles;

import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.util.ResourceLocation;

public class StandardRefBoard extends RefBoardItem {

    public static final ResourceLocation ID = new ResourceLocation(EurekaCraft.MODID, "standard_ref_board");

    public StandardRefBoard() {
        super(RefBoardStats.StandardBoard, ID); // TODO: Should models be singletons?
    }
}
