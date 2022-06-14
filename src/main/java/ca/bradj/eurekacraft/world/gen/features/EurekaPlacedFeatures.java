package ca.bradj.eurekacraft.world.gen.features;

import ca.bradj.eurekacraft.world.gen.EurekaPlacements;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class EurekaPlacedFeatures {

    public static final Holder<PlacedFeature> TRAPAR_WAVES_GROUND_PLACED = PlacementUtils.register(
            "trapar_waves_ground_placed", ConfiguredFeatures.TRAPAR_WAVES_GROUND,
            EurekaPlacements.wavesPlacement()
    );

    public static final Holder<PlacedFeature> TRAPAR_WAVES_MED_PLACED = PlacementUtils.register(
            "trapar_waves_med_placed", ConfiguredFeatures.TRAPAR_WAVES_MED,
            EurekaPlacements.wavesPlacement()
    );
}
