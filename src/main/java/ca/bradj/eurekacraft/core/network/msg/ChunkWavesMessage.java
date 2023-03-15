package ca.bradj.eurekacraft.core.network.msg;

import ca.bradj.eurekacraft.client.TraparStormRendering;
import ca.bradj.eurekacraft.world.waves.ChunkWavesData;
import ca.bradj.eurekacraft.world.waves.ChunkWavesDataManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ChunkWavesMessage {

    public final Collection<BlockPos> waveBlocks;
    public final ChunkPos chunkPos;

    public ChunkWavesMessage(ChunkPos cp, Collection<BlockPos> waveBlocks) {
        this.chunkPos = cp;
        this.waveBlocks = waveBlocks;
    }

    public static void encode(ChunkWavesMessage msg, FriendlyByteBuf buffer) {
        buffer.writeCollection(msg.waveBlocks, FriendlyByteBuf::writeBlockPos);
        buffer.writeInt(msg.chunkPos.x);
        buffer.writeInt(msg.chunkPos.z);
    }

    public static ChunkWavesMessage decode(FriendlyByteBuf buffer) {
        Collection<BlockPos> waveBlocks = buffer.readCollection(
                ArrayList::new,
                FriendlyByteBuf::readBlockPos
        );
        int x = buffer.readInt();
        int z = buffer.readInt();
        return new ChunkWavesMessage(new ChunkPos(x, z), waveBlocks);
    }


    public boolean handle(
            Supplier<NetworkEvent.Context> ctx
    ) {
        final AtomicBoolean success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                    () -> () -> {
                        ChunkWavesDataManager.updateFromMessage(this);
                        success.set(true);
                    }
            );
        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }
}
