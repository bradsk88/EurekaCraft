package ca.bradj.eurekacraft.client;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.ItemsInit;
import ca.bradj.eurekacraft.vehicles.BoardType;
import ca.bradj.eurekacraft.vehicles.RefBoardItem;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoard;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoardProvider;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BoardItemRendering {

    @SubscribeEvent
    public static void registerColors(ColorHandlerEvent.Item event) {
        event.getItemColors().register(new ItemColor() {
            @Override
            public int getColor(ItemStack p_92672_, int p_92673_) {
                return 0;
            }
        }, ItemsInit.STANDARD_REF_BOARD.get());
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
        // FIXME: make this (or some other way of coloring the item) work
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
        public float call(ItemStack item, @Nullable ClientLevel world, @Nullable LivingEntity entity, int unused) {
            if (entity == null) {
                return 0;
            }
            if (!(entity instanceof Player)) {
                return 0;
            }
            Optional<PlayerDeployedBoard.ColoredBoard> boardType = PlayerDeployedBoardProvider.getBoardTypeFor(entity);
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
