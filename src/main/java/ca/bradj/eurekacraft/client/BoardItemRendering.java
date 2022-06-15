package ca.bradj.eurekacraft.client;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.ItemsInit;
import ca.bradj.eurekacraft.vehicles.BoardType;
import ca.bradj.eurekacraft.vehicles.RefBoardItem;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoard;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoardProvider;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BoardItemRendering {
    public static Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    public static void initItemProperties() {
        ItemProperties.register(
                ItemsInit.GLIDE_BOARD.get(),
                new ResourceLocation(EurekaCraft.MODID, "deployed"),
                new DeployedPropGetter()
        );
        ItemProperties.register(
                ItemsInit.STANDARD_BOARD.get(),
                new ResourceLocation(EurekaCraft.MODID, "deployed"),
                new DeployedPropGetter()
        );
        ItemProperties.register(
                ItemsInit.BROKEN_BOARD.get(),
                new ResourceLocation(EurekaCraft.MODID, "deployed"),
                new DeployedPropGetter()
        );
        ItemProperties.register(
                ItemsInit.REF_BOARD_CORE.get(),
                new ResourceLocation(EurekaCraft.MODID, "deployed"),
                new DeployedPropGetter()
        );
        ItemProperties.register(
                ItemsInit.ELITE_BOARD.get(),
                new ResourceLocation(EurekaCraft.MODID, "deployed"),
                new DeployedPropGetter()
        );
    }

    public static class DeployedPropGetter implements ItemPropertyFunction {

        public DeployedPropGetter() {
        }

        @Override
        public float call(ItemStack item, @Nullable ClientLevel world, @Nullable LivingEntity entity, int unused) {
            if (entity == null) {
                return 0.0F;
            }
            if (!(item.getItem() instanceof RefBoardItem)) {
                return 0.0F;
            }
            if (!(entity instanceof Player)) {
                return 0.0F;
            }
            Optional<PlayerDeployedBoard.ColoredBoard> boardType = PlayerDeployedBoardProvider.getBoardTypeFor(entity);
            if (!boardType.isPresent()) {
                return 0.0F;
            }
            if (BoardType.NONE.equals(boardType.get())) {
                return 0.0F;
            }
            return 1.0F;
        }
    }
}
