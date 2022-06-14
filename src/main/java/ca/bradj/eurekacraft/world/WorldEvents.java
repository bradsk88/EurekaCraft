package ca.bradj.eurekacraft.world;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.world.gen.TraparWavesGeneration;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID)
public class WorldEvents {

    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event) {
        TraparWavesGeneration.generateTraparWaves(event);
    }

}
