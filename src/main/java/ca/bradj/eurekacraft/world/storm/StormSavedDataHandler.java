package ca.bradj.eurekacraft.world.storm;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;

import java.util.Collection;
import java.util.function.Supplier;

public class StormSavedDataHandler {

    public static void chunkLoaded(
            Supplier<ChunkPos> evt,
            Supplier<Long> seed
    ) {
        StormSavedData.initChunk(seed.get(), evt.get());
    }

    public static void chunkUnloaded(Supplier<ChunkPos> evt) {
        StormSavedData.removeChunk(evt.get());
    }

    public static void worldTick(
            boolean isOverworld,
            Collection<? extends Player> players
    ) {
        if (!isOverworld) {
            return;
        }
        StormSavedData.serverTick(players);
    }

}
