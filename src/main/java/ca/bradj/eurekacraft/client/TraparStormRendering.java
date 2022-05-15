package ca.bradj.eurekacraft.client;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.network.msg.TraparStormMessage;
import ca.bradj.eurekacraft.render.TraparStormRenderHandler;
import ca.bradj.eurekacraft.render.TraparStormRenderStarter;
import ca.bradj.eurekacraft.world.storm.StormSavedData;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.client.IWeatherRenderHandler;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.OptionalLong;

public class TraparStormRendering {
    private static final Logger logger = LogManager.getLogger(EurekaCraft.MODID);
    private static TraparStormRenderStarter starter;

    public static void init() {
        DimensionSpecialEffects dimensionRenderInfo = DimensionSpecialEffects.forType(DTypeAccessor.getOverworldType());
        IWeatherRenderHandler defaultRenderer = dimensionRenderInfo.getWeatherRenderHandler();
        IWeatherRenderHandler traparRenderer = new TraparStormRenderHandler();

        starter = new TraparStormRenderStarter(dimensionRenderInfo, traparRenderer, defaultRenderer);
        MinecraftForge.EVENT_BUS.register(starter);
    }

    public static void updateFromMessage(TraparStormMessage traparStormMessage) {
        StormSavedData.updateFromMessage(traparStormMessage);
    }

    private class DTypeAccessor extends DimensionType {

        public static DimensionType getOverworldType() {
            return DimensionType.DEFAULT_OVERWORLD;
        }
    }
}
