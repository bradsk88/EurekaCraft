package ca.bradj.eurekacraft.world.waves;

import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID)
public class ChunkEvents {

    @SubscribeEvent
    public static void chunkLoaded(ChunkEvent evt) {
        LevelAccessor world = evt.getLevel();
        if (world == null) {
            return;
        }
        if (world.isClientSide()) {
            return;
        }
        ChunkWavesData waves = ChunkWavesDataManager.get(world).getData(evt.getChunk(), world.getRandom());
        ChunkWavesDataManager.get(world).initChunkEntity(
                world.getServer().overworld(), evt.getChunk(), waves.getWaves()
        );
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.LevelTickEvent evt) {
        if (evt.level.isClientSide() || evt.phase == TickEvent.Phase.START) {
            return;
        }
        ChunkWavesDataManager.tick((ServerLevel) evt.level);
    }

}
