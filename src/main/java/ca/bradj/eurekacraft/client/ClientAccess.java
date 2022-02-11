package ca.bradj.eurekacraft.client;

import ca.bradj.eurekacraft.vehicles.BoardType;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoard;
import net.minecraft.client.Minecraft;

public class ClientAccess {

    public static boolean updatePlayerDeployedBoard(int playerId, BoardType bt) {
        Minecraft.getInstance().level.players().stream().filter((p) -> p.getId() == playerId).forEach(p -> {
            PlayerDeployedBoard.set(p, bt);
        });
        return true;
    }

}
