package ca.bradj.eurekacraft.world.gen;

import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class EurekaPlacements {

    public static List<PlacementModifier> wavesPlacement(
    ) {
        return List.of(
                CountPlacement.of(8), // Veins per chunk
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.BOTTOM, VerticalAnchor.TOP),
                BiomeFilter.biome()
        );
    }

}
