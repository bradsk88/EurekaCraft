package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.materials.RefBoardCoreItem;
import ca.bradj.eurekacraft.render.*;
import ca.bradj.eurekacraft.vehicles.BrokenRefBoard;
import ca.bradj.eurekacraft.vehicles.EliteRefBoard;
import ca.bradj.eurekacraft.vehicles.GlideBoard;
import ca.bradj.eurekacraft.vehicles.StandardRefBoard;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ModelsInit {

    public static final Map<ResourceLocation, AbstractBoardModel> BOARD_MODELS = new HashMap();

    public static void registerModels() {
        BOARD_MODELS.put(StandardRefBoard.ID, new RefBoardModel());
        BOARD_MODELS.put(EliteRefBoard.ID, new EliteRefBoardModel());
        BOARD_MODELS.put(GlideBoard.ID, new GlideBoardModel());
        BOARD_MODELS.put(BrokenRefBoard.ID, new BrokenRefBoardModel());
        BOARD_MODELS.put(RefBoardCoreItem.ID, new RefBoardCoreModel());
    }

    public static AbstractBoardModel getModel(
            ResourceLocation id, float r, float g, float b
    ) {
        return BOARD_MODELS.getOrDefault(id, BOARD_MODELS.get(StandardRefBoard.ID)).withColor(r, g, b);
    }

}
