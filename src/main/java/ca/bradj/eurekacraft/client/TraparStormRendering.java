package ca.bradj.eurekacraft.client;

import ca.bradj.eurekacraft.render.TraparStormRenderHandler;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;

import java.util.OptionalLong;

public class TraparStormRendering {

    public static void init() {
        DimensionRenderInfo dimensionRenderInfo = DimensionRenderInfo.forType(DTypeAccessor.getOverworldType());
        dimensionRenderInfo.setWeatherRenderHandler(
                new TraparStormRenderHandler()
        );
    }

    private static class DTypeAccessor extends DimensionType {

        protected DTypeAccessor(OptionalLong p_i241972_1_, boolean p_i241972_2_, boolean p_i241972_3_, boolean p_i241972_4_, boolean p_i241972_5_, double p_i241972_6_, boolean p_i241972_8_, boolean p_i241972_9_, boolean p_i241972_10_, boolean p_i241972_11_, int p_i241972_12_, ResourceLocation p_i241972_13_, ResourceLocation p_i241972_14_, float p_i241972_15_) {
            super(p_i241972_1_, p_i241972_2_, p_i241972_3_, p_i241972_4_, p_i241972_5_, p_i241972_6_, p_i241972_8_, p_i241972_9_, p_i241972_10_, p_i241972_11_, p_i241972_12_, p_i241972_13_, p_i241972_14_, p_i241972_15_);
        }

        public static DimensionType getOverworldType() {
            return DimensionType.DEFAULT_OVERWORLD;
        }
    }
}
