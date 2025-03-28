package ca.bradj.eurekacraft.client;

import ca.bradj.eurekacraft.core.init.items.ItemsInit;
import ca.bradj.eurekacraft.integration.mc.Compat;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoard;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoardProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class ClientAccess {

    public static boolean updatePlayerDeployedBoard(
            int playerId,
            PlayerDeployedBoard.DeployedBoard bt
    ) {
        Minecraft.getInstance().level.players().stream().filter((p) -> p.getId() == playerId)
                                     .forEach(p -> PlayerDeployedBoardProvider.setBoardTypeFor(
                                             p,
                                             bt.boardType,
                                             bt.color,
                                             bt.wheel,
                                             false
                                     ));
        return true;
    }

    public static void showBoardHint() {
        Component tc = Compat.translatable("message.board.clicked_on_ground");
        Minecraft.getInstance().gui.setOverlayMessage(tc, false);
    }

    public static void updateChargeLevel(UUID sp, int chargeAmount) {
        LocalPlayer clientPlayer = Minecraft.getInstance().player;
        UUID uuid = clientPlayer.getUUID();
        if (!uuid.equals(sp)) {
            return;
        }

        for (ItemStack item : clientPlayer.getInventory().items) {
            if (!item.is(ItemsInit.COMPAC_DRIVE.get())) {
                continue;
            }
            // TODO: Set maximum based on level of held drive
            int val = Math.min(500, chargeAmount);
            val = (int) ((val / 500f) * 28);
            item.getOrCreateTag().putInt("charge", val);
        }
    }
}
