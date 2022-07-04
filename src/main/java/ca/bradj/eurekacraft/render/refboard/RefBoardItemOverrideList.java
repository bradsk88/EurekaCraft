package ca.bradj.eurekacraft.render.refboard;

import ca.bradj.eurekacraft.interfaces.IColorSource;
import ca.bradj.eurekacraft.vehicles.BoardType;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoardProvider;
import ca.bradj.eurekacraft.vehicles.wheels.BoardWheels;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class RefBoardItemOverrideList extends ItemOverrides {

    private static final Color INVISIBLE = new Color(0, 0, 0, 0);

    // TODO: Finish this
    private static final BakedModel NO_BOARD_MODEL = new SimpleBakedModel(
            ImmutableList.of(new BakedQuad(new int[]{}, 0, Direction.DOWN, null, false)),
            ImmutableMap.of(),
            false, false, false,
            null, ItemTransforms.NO_TRANSFORMS, ItemOverrides.EMPTY
    );

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
            if (BoardType.NONE.equals(deployedBoard)) {
                return NO_BOARD_MODEL;
            }
        }
        Color wColor = BoardWheels.FromStack(stack).map(IColorSource::getColor).orElse(INVISIBLE);
        return new RefBoardColoredModelWithWheel(parent, wColor);
    }
}
