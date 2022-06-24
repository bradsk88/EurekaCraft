package ca.bradj.eurekacraft.render.refboard;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class RefBoardColoredModel implements BakedModel {

    public static final ModelResourceLocation modelResourceLocation
            = new ModelResourceLocation("eurekacraft:ref_board_registry_name", "inventory");

    private final BakedModel parentModel;
    private final Color color;

    public RefBoardColoredModel(
            BakedModel parent, Color color
    ) {
        this.parentModel = parent;
        this.color = color;
    }

    @Override
    public List<BakedQuad> getQuads(
            @Nullable BlockState state,
            @Nullable Direction dir, Random rand
    ) {
        return colorize(parentModel, color, state, dir, rand);
    }

    private List<BakedQuad> colorize(
            BakedModel parentModel, Color color,
            BlockState state, Direction dir, Random rand
    ) {
        return parentModel.getQuads(state, dir, rand);
    }

//    private BakedQuad colorizeQuad(Direction dir, Color color, BakedQuad q) {
//        boolean shade = true;
//        int[] coloredVertexData = q.getVertices().
//        int [] vertexDataAll = Ints.concat(vertexData1, vertexData2, vertexData3, vertexData4);
//        int itemRenderLayer = 0; // TODO: Might need to change if layering parts
//        return new BakedQuad(vertexDataAll, itemRenderLayer, dir, q.getSprite(), shade);
//        return null;
//    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return null;
    }

    @Override
    public ItemOverrides getOverrides() {
        return null;
    }
}
