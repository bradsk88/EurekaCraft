package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.render.AbstractBoardModel;
import ca.bradj.eurekacraft.render.GlideBoardModel;
import ca.bradj.eurekacraft.render.RefBoardModel;
import ca.bradj.eurekacraft.vehicles.GlideBoard;
import ca.bradj.eurekacraft.vehicles.StandardRefBoard;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ModelsInit {

    public static final Map<ResourceLocation, AbstractBoardModel> BOARD_MODELS = new HashMap();

    public static void registerModels() {
        BOARD_MODELS.put(StandardRefBoard.ID, new RefBoardModel());
        BOARD_MODELS.put(GlideBoard.ID, new GlideBoardModel());
    }

    public static AbstractBoardModel getModel(ResourceLocation id) {
        return BOARD_MODELS.getOrDefault(id, BOARD_MODELS.get(StandardRefBoard.ID));
    }

}
