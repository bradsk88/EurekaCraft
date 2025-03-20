package ca.bradj.eurekacraft.world.storm;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.integration.mc.Compat;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID)
public class StormSavedDataHandler {

    @SubscribeEvent
    public static void chunkLoaded(ChunkEvent.Load evt) {
        if (Compat.getWorld(evt) == null) {
            return;
        }
        if (Compat.getWorld(evt).isClientSide()) {
            return;
        }
        ServerLevel sw = (ServerLevel) Compat.getWorld(evt);
        StormSavedData.initChunk(sw.getSeed(), evt.getChunk().getPos());
    }

    @SubscribeEvent
    public static void chunkUnloaded(ChunkEvent.Unload evt) {
        if (Compat.getWorld(evt).isClientSide()) {
            return;
        }
        StormSavedData.removeChunk(evt.getChunk().getPos());
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.LevelTickEvent evt) {
        if (getWorld(evt).isClientSide()) {
            return;
        }
        if (!getWorld(evt).dimension().location().equals(Compat.OVERWORLD)) {
            return;
        }
        StormSavedData.tick(getWorld(evt));
    }

    private static Level getWorld(TickEvent.LevelTickEvent evt) {
        return evt.level;
    }


}
