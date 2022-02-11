package ca.bradj.eurekacraft.client;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.ItemsInit;
import ca.bradj.eurekacraft.vehicles.BoardType;
import ca.bradj.eurekacraft.vehicles.RefBoardItem;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoard;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;

public class BoardItemRendering {
    public static Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    public static void initItemProperties() {
        logger.debug("initItemProperties");
        ItemModelsProperties.register(
                ItemsInit.GLIDE_BOARD.get(),
                new ResourceLocation(EurekaCraft.MODID, "deployed"),
                new DeployedPropGetter()
        );
        ItemModelsProperties.register(
                ItemsInit.STANDARD_BOARD.get(),
                new ResourceLocation(EurekaCraft.MODID, "deployed"),
                new DeployedPropGetter()
        );
        ItemModelsProperties.register(
                ItemsInit.BROKEN_BOARD.get(),
                new ResourceLocation(EurekaCraft.MODID, "deployed"),
                new DeployedPropGetter()
        );
        ItemModelsProperties.register(
                ItemsInit.REF_BOARD_CORE.get(),
                new ResourceLocation(EurekaCraft.MODID, "deployed"),
                new DeployedPropGetter()
        );
    }


    public static class DeployedPropGetter implements IItemPropertyGetter {

        @Override
        public float call(ItemStack item, @Nullable ClientWorld world, @Nullable LivingEntity entity) {
            if (entity == null) {
                logger.debug("Not deployed because entity is null");
                return 0.0F;
            }
            if (!(item.getItem() instanceof RefBoardItem)) {
                logger.debug("Not deployed because item is " + item.getItem());
                return 0.0F;
            }
            if (!(entity instanceof PlayerEntity)) {
                logger.debug("Not deployed because entity is " + entity);
                return 0.0F;
            }
            Optional<BoardType> boardType = PlayerDeployedBoard.get((PlayerEntity) entity);
            if (!boardType.isPresent()) {
                logger.debug("Not deployed because board is not present");
                return 0.0F;
            }
            if (BoardType.NONE.equals(boardType.get())) {
                return 0.0F;
            }
            return 1.0F;
        }
    }
}
