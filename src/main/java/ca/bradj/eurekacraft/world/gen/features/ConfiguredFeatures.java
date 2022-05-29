package ca.bradj.eurekacraft.world.gen.features;

import ca.bradj.eurekacraft.core.init.BlocksInit;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

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

    private static RuleTest AIR_REPLACEABLES = new BlockMatchTest(Blocks.AIR);

    public static final List<OreConfiguration.TargetBlockState> OVERWORLD_TRAPAR_WAVES = List.of(
            OreConfiguration.target(AIR_REPLACEABLES, BlocksInit.TRAPAR_WAVE_CHILD_BLOCK.get().defaultBlockState())
    );

    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> TRAPAR_WAVES = FeatureUtils.register(
            "trapar_wave", Feature.ORE, new OreConfiguration(
                    OVERWORLD_TRAPAR_WAVES,
                    16 // Max blocks per glob
            )
    );

}
