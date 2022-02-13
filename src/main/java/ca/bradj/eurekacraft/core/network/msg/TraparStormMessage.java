package ca.bradj.eurekacraft.core.network.msg;

import ca.bradj.eurekacraft.client.TraparStormRendering;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class TraparStormMessage {

    public final boolean storming;
    public final ChunkPos chunkPos;

    public TraparStormMessage() {
        this.storming = false;
        this.chunkPos = new ChunkPos(0);
    }

    public TraparStormMessage(ChunkPos cp, boolean storming) {
        this.chunkPos = cp;
        this.storming = storming;
    }

    public static void encode(TraparStormMessage msg, PacketBuffer buffer) {
        buffer.writeBoolean(msg.storming);
        buffer.writeInt(msg.chunkPos.x);
        buffer.writeInt(msg.chunkPos.z);
    }

    public static TraparStormMessage decode(PacketBuffer buffer) {
        boolean storming = buffer.readBoolean();
        int x = buffer.readInt();
        int z = buffer.readInt();
        return new TraparStormMessage(new ChunkPos(x, z), storming);
    }


    public boolean handle(
            Supplier<NetworkEvent.Context> ctx
    ) {
        final AtomicBoolean success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                    () -> () -> {
                        TraparStormRendering.updateFromMessage(this);
                        success.set(true);
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
