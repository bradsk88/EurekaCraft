package ca.bradj.eurekacraft.render.refboard;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RefBoardColoredModel implements BakedModel {

    public static final ModelResourceLocation modelResourceLocation
            = new ModelResourceLocation(
            "eurekacraft:ref_board",
            "inventory"
    );
    private final ItemTransforms transforms;
    private final ItemOverrides overrides;

    private List<BakedQuad> quads;

    public RefBoardColoredModel(
            BakedModel parent
    ) {
        this.quads = enableTinting(parent);
        this.transforms = parent.getTransforms();
        this.overrides = new RefBoardItemOverrideList();
    }

    private List<BakedQuad> enableTinting(
            BakedModel parentModel
    ) {
        return parentModel.getQuads(
                        null,
                        null,
                        null
                )
                .stream().
                map(v -> new BakedQuad(
                        v.getVertices(),
                        0,
                        v.getDirection(),
                        v.getSprite(),
                        v.isShade()
                )).
                collect(Collectors.toList());
    }

    @Override
    public List<BakedQuad> getQuads(
            @Nullable BlockState p_235039_,
            @Nullable Direction p_235040_,
            RandomSource p_235041_
    ) {
        return this.getQuads(p_235039_, p_235040_, p_235041_, ModelData.EMPTY, null);
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(
            @Nullable BlockState state,
            @Nullable Direction side,
            @NotNull RandomSource rand,
            @NotNull ModelData data,
            @Nullable RenderType renderType
    ) {
        if (side != null) {
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
        return overrides;
    }

    @Override
    public ItemTransforms getTransforms() {
        return transforms;
    }
}
