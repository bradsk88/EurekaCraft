package ca.bradj.eurekacraft.world.storm;

import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.world.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID)
public class StormSavedDataHandler {

    private static Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    @SubscribeEvent
    public static void chunkLoaded(ChunkEvent.Load evt) {
        if (evt.getWorld() == null) {
            return;
        }
        if (evt.getWorld().isClientSide()) {
            return;
        }
        ServerWorld sw = (ServerWorld) evt.getWorld();
        StormSavedData.initChunk(sw.getSeed(), evt.getChunk().getPos());
    }

    @SubscribeEvent
    public static void chunkUnloaded(ChunkEvent.Unload evt) {
        if (evt.getWorld().isClientSide()) {
            return;
        }
        StormSavedData.removeChunk(evt.getChunk().getPos());
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent evt) {
        if (evt.world.isClientSide()) {
            return;
        }
        if (!evt.world.dimension().location().equals(DimensionType.OVERWORLD_LOCATION.location())) {
            return;
        }
        StormSavedData.tick(evt.world);
    }


}