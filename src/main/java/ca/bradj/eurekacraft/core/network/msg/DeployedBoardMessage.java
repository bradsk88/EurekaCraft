package ca.bradj.eurekacraft.core.network.msg;

import ca.bradj.eurekacraft.client.ClientAccess;
import ca.bradj.eurekacraft.interfaces.IIDHaver;
import ca.bradj.eurekacraft.vehicles.BoardType;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoard;
import ca.bradj.eurekacraft.vehicles.wheels.BoardWheels;
import ca.bradj.eurekacraft.vehicles.wheels.Wheel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.awt.*;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class DeployedBoardMessage {

    private static final int BUFFER_STRING_LENGTH = 128;

    private final PlayerDeployedBoard.DeployedBoard boardType;
    private final int playerId;

    public DeployedBoardMessage() {
        this.playerId = -1;
        this.boardType = PlayerDeployedBoard.DeployedBoard.NONE;
    }

    public DeployedBoardMessage(int playerId, PlayerDeployedBoard.DeployedBoard boardType) {
        this.playerId = playerId;
        this.boardType = boardType;
    }

    public static void encode(DeployedBoardMessage msg, FriendlyByteBuf buffer) {
        buffer.writeUtf(msg.boardType.boardType.getPath(), BUFFER_STRING_LENGTH);
        buffer.writeInt(msg.playerId);
        Color c = msg.boardType.getColor();
        buffer.writeFloat(c.getRed()/255f);
        buffer.writeFloat(c.getGreen()/255f);
        buffer.writeFloat(c.getBlue()/255f);
        buffer.writeUtf(msg.boardType.wheel.map(IIDHaver::getItemId).orElse("none"));
    }

    public static DeployedBoardMessage decode(FriendlyByteBuf buffer) {
        String bts = buffer.readUtf(BUFFER_STRING_LENGTH);
        BoardType bt = BoardType.fromNBT(bts);
        int playerId = buffer.readInt();
        float r = buffer.readFloat();
        float g = buffer.readFloat();
        float b = buffer.readFloat();
        String wheel = buffer.readUtf(BUFFER_STRING_LENGTH);
        Optional<Wheel> wheelItem = Optional.empty();
        if (!"none".equals(wheel)) {
            wheelItem = BoardWheels.getItem(wheel);
        }
        PlayerDeployedBoard.DeployedBoard db = new PlayerDeployedBoard.DeployedBoard(
                bt, new Color(r, g, b), wheelItem
        );
        return new DeployedBoardMessage(playerId, db);
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
