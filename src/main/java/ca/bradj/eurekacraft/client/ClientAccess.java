package ca.bradj.eurekacraft.client;

import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoard;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoardProvider;
import net.minecraft.client.Minecraft;

public class ClientAccess {

    public static boolean updatePlayerDeployedBoard(int playerId, PlayerDeployedBoard.ColoredBoard bt) {
        Minecraft.getInstance().level.players().stream().
                filter((p) -> p.getId() == playerId).
                forEach(p -> PlayerDeployedBoardProvider.setBoardTypeFor(p, bt.boardType, bt.color, false));
        return true;
    }

}
