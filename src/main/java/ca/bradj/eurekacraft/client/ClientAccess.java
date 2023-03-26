package ca.bradj.eurekacraft.client;

import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoard;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoardProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;

public class ClientAccess {

    public static boolean updatePlayerDeployedBoard(int playerId, PlayerDeployedBoard.DeployedBoard bt) {
        Minecraft.getInstance().level.players().stream().
                filter((p) -> p.getId() == playerId).
                forEach(p -> PlayerDeployedBoardProvider.setBoardTypeFor(p, bt.boardType, bt.color, bt.wheel, false));
        return true;
    }

    public static void showBoardHint() {
        TranslatableComponent tc = new TranslatableComponent(
                "message.board.clicked_on_ground"
        );
        Minecraft.getInstance().gui.setOverlayMessage(tc, false);
    }

}
