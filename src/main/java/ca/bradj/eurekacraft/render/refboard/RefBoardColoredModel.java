package ca.bradj.eurekacraft.render.refboard;

import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ForgeHooksClient;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RefBoardColoredModel implements BakedModel {

    public static final ModelResourceLocation modelResourceLocation
            = new ModelResourceLocation("eurekacraft:ref_board", "inventory");

    private List<BakedQuad> quads;

    public RefBoardColoredModel(
            BakedModel parent
    ) {
        this.quads = enableTinting(parent);
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
        // TODO: Use this to add wheels
        // https://github.com/TheGreyGhost/MinecraftByExample/tree/working-1-16-4/src/main/java/minecraftbyexample/mbe15_item_dynamic_item_model
        return ItemOverrides.EMPTY;
    }
}
