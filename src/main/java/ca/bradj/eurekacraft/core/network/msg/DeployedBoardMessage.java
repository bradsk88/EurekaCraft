package ca.bradj.eurekacraft.core.network.msg;

import ca.bradj.eurekacraft.client.ClientAccess;
import ca.bradj.eurekacraft.vehicles.BoardType;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoard;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class DeployedBoardMessage {

    private static final int BOARD_ID_MAX_LENGTH = 128;

    private final PlayerDeployedBoard.ColoredBoard boardType;
    private final int playerId;

    public DeployedBoardMessage() {
        this.playerId = -1;
        this.boardType = PlayerDeployedBoard.ColoredBoard.NONE;
    }

    public DeployedBoardMessage(int playerId, PlayerDeployedBoard.ColoredBoard boardType) {
        this.playerId = playerId;
        this.boardType = boardType;
    }

    public static void encode(DeployedBoardMessage msg, FriendlyByteBuf buffer) {
        buffer.writeUtf(msg.boardType.boardType.getPath(), BOARD_ID_MAX_LENGTH);
        buffer.writeInt(msg.playerId);
        Color c = msg.boardType.getColor();
        buffer.writeFloat(c.getRed()/255f);
        buffer.writeFloat(c.getGreen()/255f);
        buffer.writeFloat(c.getBlue()/255f);
    }

    public static DeployedBoardMessage decode(FriendlyByteBuf buffer) {
        String bts = buffer.readUtf(BOARD_ID_MAX_LENGTH);
        BoardType bt = BoardType.fromNBT(bts);
        int playerId = buffer.readInt();
        float r = buffer.readFloat();
        float g = buffer.readFloat();
        float b = buffer.readFloat();
        return new DeployedBoardMessage(playerId, new PlayerDeployedBoard.ColoredBoard(bt, new Color(r, g, b)));
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
