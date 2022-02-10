package ca.bradj.eurekacraft.vehicles;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.render.GlideBoardModel;
import net.minecraft.util.ResourceLocation;

public class GlideBoard extends RefBoardItem {

    public static ResourceLocation ID = new ResourceLocation(EurekaCraft.MODID, "glide_board");

    public GlideBoard() {
        super(RefBoardStats.GlideBoard, ID);
    }
}
