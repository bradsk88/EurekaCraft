package ca.bradj.eurekacraft.integration.mc;

import ca.bradj.eurekacraft.entity.board.EntityRefBoard;
import net.minecraft.server.level.ServerPlayer;

public class ECCompat {
    public static EntityRefBoard newRefBoardEntityForConnectionRecovery(ServerPlayer sp) {
        //noinspection deprecation (This is the only valid use of the deprecated method)
        return new EntityRefBoard(sp, sp.level);
    }
}
