package ca.bradj.eurekacraft.core.network.msg;

import ca.bradj.eurekacraft.client.ClientAccess;
import ca.bradj.eurekacraft.vehicles.control.Control;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class BoardControlMessage {

    private static final int CONTROL_MAX_LENGTH = 128;

    private final Control control;
    private final int playerId;

    public BoardControlMessage(int playerId, Control control) {
        this.playerId = playerId;
        this.control = control;
    }

    public static void encode(BoardControlMessage msg, FriendlyByteBuf buffer) {
        buffer.writeUtf(msg.control.name(), CONTROL_MAX_LENGTH);
        buffer.writeInt(msg.playerId);
    }

    public static BoardControlMessage decode(FriendlyByteBuf buffer) {
        String control = buffer.readUtf(CONTROL_MAX_LENGTH);
        int playerId = buffer.readInt();
        return new BoardControlMessage(playerId, Control.valueOf(control));
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final AtomicBoolean success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                    () -> () -> success.set(ClientAccess.updatePlayerBoardControl(this.playerId, this.control))
            );
        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }

}
