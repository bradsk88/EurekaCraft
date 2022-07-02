package ca.bradj.eurekacraft.client;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.vehicles.BoardType;
import ca.bradj.eurekacraft.vehicles.control.Control;
import ca.bradj.eurekacraft.vehicles.control.PlayerBoardControlProvider;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoard;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoardProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class KeyEvents {

    @SubscribeEvent
    public static void ClientTick(TickEvent.ClientTickEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        PlayerDeployedBoardProvider.getBoardTypeFor(player).ifPresentOrElse(
                (PlayerDeployedBoard.DeployedBoard bt) -> {
                    if (BoardType.NONE.equals(bt.boardType)) {
                        setControl(player, Control.NONE);
                        return;
                    }
                    if (KeyInit.brakeFlightMapping.isDown()) {
                        setControl(player, Control.BRAKE);
                    } else if (KeyInit.accelerateFlightMapping.isDown()) {
                        setControl(player, Control.ACCELERATE);
                    } else {
                        setControl(player, Control.NONE);
                    }
                },
                () -> {
                    setControl(player, Control.NONE);
                }
        );

    }

    private static void setControl(Player player, Control c) {
        PlayerBoardControlProvider.setControl(player, c, true);
    }
}
