package ca.bradj.eurekacraft.core.network.msg;

import ca.bradj.eurekacraft.client.ClientAccess;
import ca.bradj.eurekacraft.entity.board.EntityRefBoard;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class PlayerChargeUpdate {

    private final int charge;
    private final UUID uuid;

    public PlayerChargeUpdate(
            UUID uuid,
            int charge
    ) {
        this.uuid = uuid;
        this.charge = charge;
    }

    public static void encode(
            PlayerChargeUpdate msg,
            FriendlyByteBuf buffer
    ) {
        buffer.writeUUID(msg.uuid);
        buffer.writeInt(msg.charge);
    }

    public static PlayerChargeUpdate decode(FriendlyByteBuf buffer) {
        return new PlayerChargeUpdate(buffer.readUUID(), buffer.readInt());
    }


    public boolean handle(
            Supplier<NetworkEvent.Context> ctx
    ) {

        final AtomicBoolean success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(
                    Dist.CLIENT, () -> () -> {
                        ClientAccess.updateChargeLevel(uuid, charge);
                        success.set(true);
                    }
            );
        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }
}
