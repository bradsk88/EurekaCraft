package ca.bradj.eurekacraft.core.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class TraparStormMessage {

    private final boolean storming;

    public TraparStormMessage() {
        this.storming = false;
    }

    public TraparStormMessage(boolean storming) {
        this.storming = storming;
    }

    public static void encode(TraparStormMessage msg, PacketBuffer buffer) {
        buffer.writeBoolean(msg.storming);
    }

    public static TraparStormMessage decode(PacketBuffer buffer) {
        boolean storming = buffer.readBoolean();
        return new TraparStormMessage(storming);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final AtomicBoolean success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                    () -> () -> {

                    }
            );
        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }

    public boolean isStorming() {
        return this.storming;
    }
}
