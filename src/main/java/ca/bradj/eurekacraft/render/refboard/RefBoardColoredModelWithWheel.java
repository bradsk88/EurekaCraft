package ca.bradj.eurekacraft.render.refboard;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class RefBoardColoredModelWithWheel implements BakedModel {

    public static final ModelResourceLocation modelResourceLocation
            = new ModelResourceLocation("eurekacraft:ref_board", "inventory");
    private final BakedModel parentModel;

    public RefBoardColoredModelWithWheel(
            BakedModel parent
    ) {
        this.parentModel = parent;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
        // our chess pieces are only drawn when side is NULL.
        if (side != null) {
            return parentModel.getQuads(state, side, rand);
        }

        List<BakedQuad> combinedQuadsList = new ArrayList(parentModel.getQuads(state, side, rand));
        combinedQuadsList.addAll(getWheelQuads()); // TODO: Take wheel color as input
        return combinedQuadsList;
    }

    private Collection<? extends BakedQuad> getWheelQuads() {
        return ImmutableList.of(); // TODO: Wheel quads
    }

    @Override
    public boolean useAmbientOcclusion() {
        return parentModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return parentModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return parentModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return parentModel.isCustomRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return parentModel.getParticleIcon();
    }

    @Override
    public TextureAtlasSprite getParticleIcon(@NotNull IModelData data) {
        return parentModel.getParticleIcon(data);
    }

    @Override
    public ItemOverrides getOverrides() {
        throw new UnsupportedOperationException("The finalised model does not have an override list.");
    }

    @Override
    public ItemTransforms getTransforms() {
        return parentModel.getTransforms();
    }
}
