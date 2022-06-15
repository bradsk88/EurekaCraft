package ca.bradj.eurekacraft.core.network.msg;

import ca.bradj.eurekacraft.client.ClientAccess;
import ca.bradj.eurekacraft.vehicles.BoardType;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoard;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class DeployedBoardMessage {

    private static final int BOARD_ID_MAX_LENGTH = 128;

    private final PlayerDeployedBoard.ColoredBoard boardType;
    private final int playerId;

    public DeployedBoardMessage() {
        this.playerId = -1;
        this.boardType = new PlayerDeployedBoard.ColoredBoard(BoardType.NONE, 1, 1,1 );
    }

    public DeployedBoardMessage(int playerId, PlayerDeployedBoard.ColoredBoard boardType) {
        this.playerId = playerId;
        this.boardType = boardType;
    }

    public static void encode(DeployedBoardMessage msg, FriendlyByteBuf buffer) {
        // TODO: encode color
        buffer.writeUtf(msg.boardType.boardType.getPath(), BOARD_ID_MAX_LENGTH);
        buffer.writeInt(msg.playerId);
    }

    public static DeployedBoardMessage decode(FriendlyByteBuf buffer) {
        String bt = buffer.readUtf(BOARD_ID_MAX_LENGTH);
        int playerId = buffer.readInt();
        // TODO: Decode color
        return new DeployedBoardMessage(playerId, new PlayerDeployedBoard.ColoredBoard(BoardType.fromNBT(bt), 1, 1, 1));
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final AtomicBoolean success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                    () -> () -> success.set(ClientAccess.updatePlayerDeployedBoard(this.playerId, this.boardType))
            );
        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }

}
