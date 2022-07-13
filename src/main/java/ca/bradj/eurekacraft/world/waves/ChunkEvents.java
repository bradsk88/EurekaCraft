package ca.bradj.eurekacraft.world.waves;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.world.storm.StormSavedData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID)
public class ChunkEvents {

    @SubscribeEvent
    public static void chunkLoaded(ChunkEvent.Load evt) {
        if (evt.getWorld() == null) {
            return;
        }
        if (evt.getWorld().isClientSide()) {
            return;
        }
        ServerLevel sw = (ServerLevel) evt.getWorld();
        ChunkWavesDataManager.get((Level) evt.getWorld()).initData(evt.getWorld().getRandom(), evt.getChunk().getPos());
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent evt) {
        if (evt.world.isClientSide() || evt.phase == TickEvent.Phase.START) {
            return;
        }
        ChunkWavesDataManager.tick(evt.world);
    }

}
