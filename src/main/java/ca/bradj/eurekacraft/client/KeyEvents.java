package ca.bradj.eurekacraft.client;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.vehicles.BoardType;
import ca.bradj.eurekacraft.vehicles.control.Control;
import ca.bradj.eurekacraft.vehicles.control.PlayerBoardControlProvider;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoard;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoardProvider;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
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
                    if (KeyEvents.brakeFlightMapping.isDown()) {
                        setControl(player, Control.BRAKE);
                    } else if (KeyEvents.accelerateFlightMapping.isDown()) {
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

    public static KeyMapping brakeFlightMapping;
    public static KeyMapping accelerateFlightMapping;

    public static void registerKeyMappings(
            RegisterKeyMappingsEvent evt
    ) {
        brakeFlightMapping = registerKey(
                evt,
                "flight.brake",
                "key.eurekacraft.flight",
                InputConstants.KEY_S
        );
        accelerateFlightMapping = registerKey(
                evt,
                "flight.accelerate",
                "key.eurekacraft.flight",
                InputConstants.KEY_W
        );
    }

    private static KeyMapping registerKey(
            RegisterKeyMappingsEvent evt,
            String name, String category, int keycode
    ) {
        final var key = new KeyMapping("key." + EurekaCraft.MODID + "." + name, keycode, category);
        evt.register(key);
        return key;
    }

    private static void setControl(Player player, Control c) {
        PlayerBoardControlProvider.setControl(player, c, true);
    }
}
