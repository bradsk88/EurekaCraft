package ca.bradj.eurekacraft.world.gen.features.trees;

import ca.bradj.eurekacraft.core.init.BlocksInit;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ConfiguredFeatures {

    public static final Holder<ConfiguredFeature<TreeConfiguration, ?>> TRAPAR_TREE = FeatureUtils.register(
            "trapar", Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(BlocksInit.TRAPAR_LOG_BLOCK.get()),
                    new StraightTrunkPlacer(5, 6, 3),
                    BlockStateProvider.simple(BlocksInit.TRAPAR_LEAVES_BLOCK.get()),
                    new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.ZERO, 4),
                    new TwoLayersFeatureSize(1, 0, 2)
            ).build());

    public static final Holder<PlacedFeature> TRAPAR_CHECKED = PlacementUtils.register(
            "trapar_checked", TRAPAR_TREE,
            PlacementUtils.filteredByBlockSurvival(BlocksInit.TRAPAR_SAPLING.get())
    );



}
