package ca.bradj.eurekacraft.world.gen;

import ca.bradj.eurekacraft.world.gen.features.EurekaPlacedFeatures;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.List;

public class TraparWavesGeneration {
    public static void generateTraparWaves(BiomeLoadingEvent event) {
        List<Holder<PlacedFeature>> base = event.getGeneration().getFeatures(
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION
        );
        base.add(EurekaPlacedFeatures.TRAPAR_WAVES_PLACED);

        // TODO: Reimplement
//        List<Supplier<ConfiguredFeature<?, ?>>> base = event.getGeneration().getFeatures(GenerationStage.Decoration.RAW_GENERATION);
//        ConfiguredPlacement<FeatureSpreadConfig> cfg = FeaturesInit.TRAPAR_WAVES_PLACEMENT.get().configured(
//                new FeatureSpreadConfig(10)
//        );
//        base.add(() -> FeaturesInit.TRAPAR_WAVES.get().configured(NoFeatureConfig.INSTANCE).decorated(cfg));
    }
}
