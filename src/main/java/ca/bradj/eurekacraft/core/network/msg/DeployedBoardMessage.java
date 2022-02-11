package ca.bradj.eurekacraft.core.network.msg;

import ca.bradj.eurekacraft.client.ClientAccess;
import ca.bradj.eurekacraft.vehicles.BoardType;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class DeployedBoardMessage {

    private static final int BOARD_ID_MAX_LENGTH = 128;

    private final BoardType boardType;
    private final int playerId;

    public DeployedBoardMessage() {
        this.playerId = -1;
        this.boardType = BoardType.NONE;
    }

    public DeployedBoardMessage(int playerId, BoardType boardType) {
        this.playerId = playerId;
        this.boardType = boardType;
    }

    public static void encode(DeployedBoardMessage msg, PacketBuffer buffer) {
        buffer.writeUtf(msg.boardType.getPath(), BOARD_ID_MAX_LENGTH);
        buffer.writeInt(msg.playerId);
    }

    public static DeployedBoardMessage decode(PacketBuffer buffer) {
        String bt = buffer.readUtf(BOARD_ID_MAX_LENGTH);
        int playerId = buffer.readInt();
        return new DeployedBoardMessage(playerId, new BoardType(bt));
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
