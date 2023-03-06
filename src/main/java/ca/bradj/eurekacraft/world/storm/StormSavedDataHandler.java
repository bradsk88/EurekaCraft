package ca.bradj.eurekacraft.world.storm;

import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID)
public class StormSavedDataHandler {

    @SubscribeEvent
    public static void chunkLoaded(ChunkEvent.Load evt) {
        if (evt.getLevel() == null) {
            return;
        }
        if (evt.getLevel().isClientSide()) {
            return;
        }
        ServerLevel sw = (ServerLevel) evt.getLevel();
        StormSavedData.initChunk(sw.getSeed(), evt.getChunk().getPos());
    }

    @SubscribeEvent
    public static void chunkUnloaded(ChunkEvent.Unload evt) {
        if (evt.getLevel().isClientSide()) {
            return;
        }
        StormSavedData.removeChunk(evt.getChunk().getPos());
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.LevelTickEvent evt) {
        if (evt.level.isClientSide()) {
            return;
        }
        // FIXME: Migrate storm rendering
//        if (!evt.level.dimension().location().equals(DimensionType.OVERWORLD_LOCATION.location())) {
//            return;
//        }
        StormSavedData.tick(evt.level);
    }


}
