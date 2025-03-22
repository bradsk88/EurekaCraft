package ca.bradj.eurekacraft.world.waves;

import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID)
public class ChunkEvents {

    @SubscribeEvent
    public static void worldTick(TickEvent.LevelTickEvent evt) {
        if (evt.level.isClientSide() || evt.phase == TickEvent.Phase.START) {
            return;
        }
        ChunkWavesDataManager.tick((ServerLevel) evt.level);
    }

}
