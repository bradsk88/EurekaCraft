package ca.bradj.eurekacraft.features;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.FeatureSpreadConfig;
import net.minecraft.world.gen.placement.SimplePlacement;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TraparWavesPlacement extends SimplePlacement<FeatureSpreadConfig> {
    public static final String PLACEMENT_ID = "trapar_waves_placement";

    public TraparWavesPlacement() {
        super(FeatureSpreadConfig.CODEC);
    }

    @Override
    protected Stream<BlockPos> place(Random p_212852_1_, FeatureSpreadConfig p_212852_2_, BlockPos p_212852_3_) {
        return IntStream.range(0, p_212852_1_.nextInt(p_212852_1_.nextInt(p_212852_2_.count().sample(p_212852_1_)) + 1)).mapToObj((p_242916_2_) -> {
            int i = p_212852_1_.nextInt(16) + p_212852_3_.getX();
            int j = p_212852_1_.nextInt(16) + p_212852_3_.getZ();
            int k = p_212852_1_.nextInt(120) + 4;
            return new BlockPos(i, k, j);
        });
    }
}
