package ca.bradj.eurekacraft.world.gen.features;

import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.core.init.FeaturesInit;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

public class ConfiguredFeatures {

    public static final Holder<ConfiguredFeature<TreeConfiguration, ?>> TRAPAR_TREE = FeatureUtils.register(
            "trapar", Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(BlocksInit.TRAPAR_LOG_BLOCK.get()),
                    new StraightTrunkPlacer(5, 6, 3),
                    BlockStateProvider.simple(BlocksInit.TRAPAR_LEAVES_BLOCK.get()),
                    new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.ZERO, 4),
                    new TwoLayersFeatureSize(1, 0, 2)
            ).build());

    public static final Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> TRAPAR_WAVES_GROUND = FeatureUtils.register(
            "trapar_waves_ground", FeaturesInit.GROUND_WAVES_FEATURE.get(), NoneFeatureConfiguration.INSTANCE
    );

    public static final Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> TRAPAR_WAVES_MED = FeatureUtils.register(
            "trapar_waves_med", FeaturesInit.MED_WAVES_FEATURE.get(), NoneFeatureConfiguration.INSTANCE
    );

}
