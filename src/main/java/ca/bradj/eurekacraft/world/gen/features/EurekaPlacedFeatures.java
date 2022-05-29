package ca.bradj.eurekacraft.world.gen.features;

import ca.bradj.eurekacraft.world.gen.EurekaPlacements;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class EurekaPlacedFeatures {

    public static final Holder<PlacedFeature> TRAPAR_WAVES_PLACED = PlacementUtils.register(
            "trapar_waves_placed", ConfiguredFeatures.TRAPAR_WAVES,
            EurekaPlacements.wavesPlacement()
    );

}
