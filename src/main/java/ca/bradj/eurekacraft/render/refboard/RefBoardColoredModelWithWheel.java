package ca.bradj.eurekacraft.render.refboard;

import ca.bradj.eurekacraft.EurekaCraft;
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

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class RefBoardColoredModelWithWheel implements BakedModel {

    public static final ModelResourceLocation modelResourceLocation
            = new ModelResourceLocation("eurekacraft:ref_board", "inventory");
    private final BakedModel parentModel;
    private final Color wheelColor;

    public RefBoardColoredModelWithWheel(
            BakedModel parent, Color wheelColor
    ) {
        this.parentModel = parent;
        this.wheelColor = wheelColor;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
        // our chess pieces are only drawn when side is NULL.
        if (side != null) {
            return parentModel.getQuads(state, side, rand);
        }

        List<BakedQuad> combinedQuadsList = new ArrayList(parentModel.getQuads(state, side, rand));
        combinedQuadsList.addAll(getWheelQuads(state, side, rand));
        return combinedQuadsList;
    }

    private Collection<? extends BakedQuad> getWheelQuads(
            @Nullable BlockState state, @Nullable Direction side, Random rand
    ) {
        List<BakedQuad> quads = this.parentModel.getQuads(state, side, rand);
        List<BakedQuad> newQuads = new ArrayList<>();
        // TODO: Can we generate the quads, rather than just coloring them?
        // 66
//        TextureAtlasSprite sprite = quads.get(0).getSprite();
//        newQuads.add(makeQuad(
//                0.125f, -0.125f, 0.0625f,
//                0.125f, -0.125f, -0.0625f,
//                0.3125f, -0.125f, -0.0625f,
//                0.3125f, -0.125f, 0.0625f,
//                33024, Direction.DOWN, sprite
//        ));
//        // 67
//        newQuads.add(makeQuad(
//                0.125f, -0.0625f, -0.0625f,
//                0.125f, -0.0625f, 0.0625f,
//                0.3125f, -0.0625f, 0.0625f,
//                0.3125f, -0.0625f, -0.0625f,
//                32512, Direction.UP, sprite
//        ));
//        // 68
//        newQuads.add(makeQuad(
//                0.3125f, -0.0625f, -0.0625f,
//                0.3125f, -0.125f, -0.0625f,
//                0.125f, -0.125f, -0.0625f,
//                0.125f, -0.0625f, -0.0625f,
//                8454144, Direction.NORTH, sprite
//        ));
//        // 69
//        newQuads.add(makeQuad(
//                0.125f, -0.0625f, 0.0625f,
//                0.125f, -0.125f, 0.0625f,
//                0.3125f, -0.125f, 0.0625f,
//                0.3125f, -0.0625f, 0.0625f,
//                8323072, Direction.SOUTH, sprite
//        ));
//        // 70
//        newQuads.add(makeQuad(
//                0.125f, -0.0625f, -0.0625f,
//                0.125f, -0.125f, -0.0625f,
//                0.125f, -0.125f, 0.0625f,
//                0.125f, -0.0625f, 0.0625f,
//                129, Direction.WEST, sprite
//        ));
//        // 71
//        newQuads.add(makeQuad(
//                0.3125f, -0.0625f, 0.0625f,
//                0.3125f, -0.125f, 0.0625f,
//                0.3125f, -0.125f, -0.0625f,
//                0.3125f, -0.0625f, -0.0625f,
//                127, Direction.EAST, sprite
//        ));

        for (int j = 66; j <= 71; j++) {
            BakedQuad wheelQuad = quads.get(j);
            int[] vertices = wheelQuad.getVertices();

            for (int i = 0; i < vertices.length; i += 8) {
                int agbr = 0;
                agbr |= wheelColor.getAlpha() << 24;
                agbr |= wheelColor.getBlue() << 16;
                agbr |= wheelColor.getGreen() << 8;
                agbr |= wheelColor.getRed() << 0;
                vertices[i + 3] = agbr;
            }
            newQuads.add(new BakedQuad(
                    vertices, wheelQuad.getTintIndex(), wheelQuad.getDirection(),
                    wheelQuad.getSprite(), wheelQuad.isShade()
            ));
        }
        return ImmutableList.copyOf(newQuads);
    }

//    private BakedQuad makeQuad(
//            float x, float y, float z,
//            float x2, float y2, float z2,
//            float x3, float y3, float z3,
//            float x4, float y4, float z4,
//            int normal, Direction dir
//    ) {
//        return new BakedQuad(
//                new int[]{
//                        Float.floatToRawIntBits(x),
//                        Float.floatToRawIntBits(y),
//                        Float.floatToRawIntBits(z),
//                        wheelColor.getRGB(),
//                        Float.floatToRawIntBits(0),
//                        Float.floatToRawIntBits(1),
//                        Float.floatToRawIntBits(1f),
//                        normal,
//                        Float.floatToRawIntBits(x2),
//                        Float.floatToRawIntBits(y2),
//                        Float.floatToRawIntBits(z2),
//                        wheelColor.getRGB(),
//                        Float.floatToRawIntBits(0),
//                        Float.floatToRawIntBits(1),
//                        Float.floatToRawIntBits(1f),
//                        normal,
//                        Float.floatToRawIntBits(x3),
//                        Float.floatToRawIntBits(y3),
//                        Float.floatToRawIntBits(z3),
//                        wheelColor.getRGB(),
//                        Float.floatToRawIntBits(0),
//                        Float.floatToRawIntBits(1),
//                        Float.floatToRawIntBits(1f),
//                        normal,
//                        Float.floatToRawIntBits(x4),
//                        Float.floatToRawIntBits(y4),
//                        Float.floatToRawIntBits(z4),
//                        wheelColor.getRGB(),
//                        Float.floatToRawIntBits(0),
//                        Float.floatToRawIntBits(1),
//                        Float.floatToRawIntBits(1f),
//                        normal
//                }, 0, dir, parentModel.getParticleIcon(), true
//        );
//    }

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
