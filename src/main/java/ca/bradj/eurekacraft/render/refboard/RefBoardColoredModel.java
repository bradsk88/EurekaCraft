package ca.bradj.eurekacraft.render.refboard;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RefBoardColoredModel implements BakedModel {

    public static final ModelResourceLocation modelResourceLocation
            = new ModelResourceLocation("eurekacraft:ref_board", "inventory");
    private final ItemTransforms transforms;
    private final ItemOverrides overrides;

    private List<BakedQuad> quads;

    public RefBoardColoredModel(
            BakedModel parent
    ) {
        this.quads = enableTinting(parent);
        this.transforms = parent.getTransforms();
        this.overrides = parent.getOverrides();
    }

    private List<BakedQuad> enableTinting(
            BakedModel parentModel
    ) {
        return parentModel.getQuads(null, null, null).stream().
                map(v -> new BakedQuad(v.getVertices(), 0, v.getDirection(), v.getSprite(), v.isShade())).
                collect(Collectors.toList());
    }

    @Override
    public List<BakedQuad> getQuads(
            @Nullable BlockState state,
            @Nullable Direction dir, Random rand
    ) {
        if (dir != null) {
            return List.of();
        }
        return this.quads;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return true;
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
        // TODO: Use this to add wheels
        // https://github.com/TheGreyGhost/MinecraftByExample/tree/working-1-16-4/src/main/java/minecraftbyexample/mbe15_item_dynamic_item_model
        return overrides;
    }

    @Override
    public ItemTransforms getTransforms() {
        return transforms;
    }
}
