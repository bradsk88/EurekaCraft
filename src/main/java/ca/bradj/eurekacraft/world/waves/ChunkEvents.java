package ca.bradj.eurekacraft.world.waves;

import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID)
public class ChunkEvents {

    @SubscribeEvent
    public static void chunkLoaded(ChunkEvent evt) {
        LevelAccessor world = evt.getWorld();
        if (world == null) {
            return;
        }
        if (world.isClientSide()) {
            return;
        }
        ChunkWavesDataManager.get((Level) evt.getWorld()).initData(evt.getChunk(), evt.getWorld().getRandom());
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent evt) {
        if (evt.world.isClientSide() || evt.phase == TickEvent.Phase.START) {
            return;
        }
        ChunkWavesDataManager.tick((ServerLevel) evt.world);
    }

}
