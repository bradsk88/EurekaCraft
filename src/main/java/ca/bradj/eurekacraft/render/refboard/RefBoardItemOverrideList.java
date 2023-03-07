package ca.bradj.eurekacraft.render.refboard;

import ca.bradj.eurekacraft.interfaces.IColorSource;
import ca.bradj.eurekacraft.vehicles.BoardColor;
import ca.bradj.eurekacraft.vehicles.BoardType;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoardProvider;
import ca.bradj.eurekacraft.vehicles.wheels.BoardWheels;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class RefBoardItemOverrideList extends ItemOverrides {

    private static final Color INVISIBLE = new Color(0, 0, 0, 0);

    private static final BakedModel NO_BOARD_MODEL = new BakedModel() {
        @Override
        public @NotNull List<BakedQuad> getQuads(
                @Nullable BlockState state,
                @Nullable Direction side,
                @NotNull RandomSource rand,
                @NotNull ModelData data,
                @Nullable RenderType renderType
        ) {
            return ImmutableList.of(new BakedQuad(new int[]{}, 0, Direction.DOWN, null, false));
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
            return ItemOverrides.EMPTY;
        }
    };

    @Nullable
    @Override
    public BakedModel resolve(
            BakedModel parent, ItemStack stack, @Nullable ClientLevel level,
            @Nullable LivingEntity player, int p_173469_
    ) {
        if (player instanceof Player) {
            BoardType deployedBoard = PlayerDeployedBoardProvider.getBoardTypeFor(player).
                    map(v -> v.boardType).
                    orElse(BoardType.NONE);
            if (!BoardType.NONE.equals(deployedBoard)) {
                return NO_BOARD_MODEL;
            }
        }
        Color fallback = BoardColor.FromStack(stack);
        Color wColor = BoardWheels.FromStack(stack).map(IColorSource::getColor).orElse(fallback);
        return new RefBoardColoredModelWithWheel(parent, wColor);
    }
}
