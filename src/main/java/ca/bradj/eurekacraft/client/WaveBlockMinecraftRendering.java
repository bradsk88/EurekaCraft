package ca.bradj.eurekacraft.client;

import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class WaveBlockMinecraftRendering {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
    }

}
