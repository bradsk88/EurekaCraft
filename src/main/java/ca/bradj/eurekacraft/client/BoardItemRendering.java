package ca.bradj.eurekacraft.client;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.items.ItemsInit;
import ca.bradj.eurekacraft.render.refboard.RefBoardColoredModel;
import ca.bradj.eurekacraft.vehicles.BoardColor;
import ca.bradj.eurekacraft.vehicles.BoardType;
import ca.bradj.eurekacraft.vehicles.RefBoardItem;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoard;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoardProvider;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class BoardItemRendering {

    public static void registerItemModel(
            Function<ResourceLocation, BakedModel> get,
            BiConsumer<ResourceLocation, BakedModel> put
    ) {
        EurekaCraft.LOGGER.debug("Registering item model");
        ModelResourceLocation itemModelResourceLocation = RefBoardColoredModel.modelResourceLocation;
        BakedModel existingModel = get.apply(itemModelResourceLocation);
        if (existingModel == null) {
            EurekaCraft.LOGGER.warn("Did not find the expected vanilla baked model in registry: " + itemModelResourceLocation);
        } else if (existingModel instanceof RefBoardColoredModel) {
            EurekaCraft.LOGGER.warn("Tried to replace ChessboardModel twice");
        } else {
            RefBoardColoredModel customModel = new RefBoardColoredModel(existingModel);
            put.accept(itemModelResourceLocation, customModel);
        }
    }

    public static void initItemProperties() {
        ItemProperties.register(
                ItemsInit.GLIDE_BOARD.get(),
                new ResourceLocation(EurekaCraft.MODID, "deployed"),
                new DeployedPropGetter()
        );
        ItemProperties.register(
                ItemsInit.STANDARD_REF_BOARD.get(),
                new ResourceLocation(EurekaCraft.MODID, "deployed"),
                new DeployedPropGetter()
        );
        ItemProperties.register(
                ItemsInit.STANDARD_REF_BOARD.get(),
                new ResourceLocation(EurekaCraft.MODID, "color"),
                new ColorPropGetter()
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

    public static int itemColor(
            ItemStack itemStack,
            int i
    ) {
        Color c = BoardColor.FromStack(itemStack);
        return c.getRGB();
    }


    public static class DeployedPropGetter implements ItemPropertyFunction {

        public DeployedPropGetter() {
        }

        @Override
        public float call(
                ItemStack item,
                @Nullable ClientLevel world,
                @Nullable LivingEntity entity,
                int unused
        ) {
            if (entity == null) {
                return 0.0F;
            }
            if (!(item.getItem() instanceof RefBoardItem)) {
                return 0.0F;
            }
            if (!(entity instanceof Player)) {
                return 0.0F;
            }
            Optional<PlayerDeployedBoard.DeployedBoard> boardType = PlayerDeployedBoardProvider.getBoardTypeFor(entity);
            if (!boardType.isPresent()) {
                return 0.0F;
            }
            if (BoardType.NONE.equals(boardType.get().boardType)) {
                return 0.0F;
            }
            return 1.0F;
        }
    }

    public static class ColorPropGetter implements ItemPropertyFunction {

        public ColorPropGetter() {
        }

        @Override
        public float call(
                ItemStack item,
                @Nullable ClientLevel world,
                @Nullable LivingEntity entity,
                int unused
        ) {
            if (entity == null) {
                return 0;
            }
            if (!(entity instanceof Player)) {
                return 0;
            }
            Optional<PlayerDeployedBoard.DeployedBoard> boardType = PlayerDeployedBoardProvider.getBoardTypeFor(entity);
            if (!boardType.isPresent()) {
                return 0;
            }
            if (boardType.get().getColor().equals(ItemsInit.PAINT_BUCKET_BLACK.get().getColor())) {
                return 1;
            }
            return 0;
        }
    }
}
