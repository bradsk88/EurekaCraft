package ca.bradj.eurekacraft.core.network.msg;

import ca.bradj.eurekacraft.client.ClientAccess;
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

public class OnGroundMessage {

    public OnGroundMessage() {
    }

    public static void encode(OnGroundMessage msg, FriendlyByteBuf buffer) {
    }

    public static OnGroundMessage decode(FriendlyByteBuf buffer) {
        return new OnGroundMessage();
    }


    public boolean handle(
            Supplier<NetworkEvent.Context> ctx
    ) {
        final AtomicBoolean success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                    () -> () -> {
                        ClientAccess.showBoardHint();
                        success.set(true);
                    }
            );
        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }
}
