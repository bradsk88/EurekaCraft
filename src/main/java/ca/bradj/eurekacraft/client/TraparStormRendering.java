package ca.bradj.eurekacraft.client;

import ca.bradj.eurekacraft.core.network.msg.TraparStormMessage;
import ca.bradj.eurekacraft.world.storm.StormSavedData;

public class TraparStormRendering {

    public static void init() {
        // FIXME: Bringe back storm rendering
//        DimensionSpecialEffects dimensionRenderInfo = DimensionSpecialEffects.forType(DimensionType.DEFAULT_OVERWORLD);
//        IWeatherRenderHandler defaultRenderer = dimensionRenderInfo.getWeatherRenderHandler();
//        IWeatherRenderHandler traparRenderer = new TraparStormRenderHandler();
//
//        TraparStormRenderStarter starter = new TraparStormRenderStarter(dimensionRenderInfo, traparRenderer, defaultRenderer);
//        MinecraftForge.EVENT_BUS.register(starter);
    }

    public static void updateFromMessage(TraparStormMessage traparStormMessage) {
        StormSavedData.updateFromMessage(traparStormMessage);
    }
}
