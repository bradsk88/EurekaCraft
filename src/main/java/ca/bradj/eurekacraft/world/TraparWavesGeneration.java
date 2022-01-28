package ca.bradj.eurekacraft.world;

import ca.bradj.eurekacraft.core.init.FeaturesInit;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.FeatureSpreadConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.List;
import java.util.function.Supplier;

public class TraparWavesGeneration {
    public static void generateTraparWaves(BiomeLoadingEvent event) {
        List<Supplier<ConfiguredFeature<?, ?>>> base = event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION);
        ConfiguredPlacement<FeatureSpreadConfig> cfg = FeaturesInit.TRAPAR_WAVES_PLACEMENT.get().configured(
                new FeatureSpreadConfig(10)
        );
        base.add(() -> FeaturesInit.TRAPAR_WAVES.get().configured(NoFeatureConfig.INSTANCE).decorated(cfg));
    }
}
