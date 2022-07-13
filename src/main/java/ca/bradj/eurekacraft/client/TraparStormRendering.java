package ca.bradj.eurekacraft.client;

import ca.bradj.eurekacraft.core.network.msg.TraparStormMessage;
import ca.bradj.eurekacraft.render.TraparStormRenderHandler;
import ca.bradj.eurekacraft.render.TraparStormRenderStarter;
import ca.bradj.eurekacraft.world.storm.StormSavedData;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.client.IWeatherRenderHandler;
import net.minecraftforge.common.MinecraftForge;

public class TraparStormRendering {

    // TODO: Reimplement for 1.18.2
    public static void init() {
        DimensionSpecialEffects dimensionRenderInfo = DimensionSpecialEffects.forType(DimensionType.DEFAULT_OVERWORLD);
        IWeatherRenderHandler defaultRenderer = dimensionRenderInfo.getWeatherRenderHandler();
        IWeatherRenderHandler traparRenderer = new TraparStormRenderHandler();

        TraparStormRenderStarter starter = new TraparStormRenderStarter(dimensionRenderInfo, traparRenderer, defaultRenderer);
        MinecraftForge.EVENT_BUS.register(starter);
    }

    public static void updateFromMessage(TraparStormMessage traparStormMessage) {
        StormSavedData.updateFromMessage(traparStormMessage);
    }
}
