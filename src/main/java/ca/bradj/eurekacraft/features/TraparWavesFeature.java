package ca.bradj.eurekacraft.features;

import ca.bradj.eurekacraft.core.init.BlocksInit;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class TraparWavesFeature extends Feature<NoFeatureConfig> {

    public static final String FEATURE_ID = "trapar_waves_feature";

    public TraparWavesFeature() {
        super(NoFeatureConfig.CODEC);
    }

    @Override
    public boolean place(
            ISeedReader seedReader, ChunkGenerator chunkGenerator, Random random,
            BlockPos blockPos, NoFeatureConfig featureConfig
    ) {
        if (!seedReader.isEmptyBlock(blockPos)) {
            return false;
        }

        if (!seedReader.getBlockState(blockPos).isAir()) {
            return false;
        }

        this.doPlace(seedReader, blockPos);

        return true;
    }

    private void doPlace(ISeedReader seedReader, BlockPos blockPos) {
        if (!seedReader.isEmptyBlock(blockPos)) {
            return;
        }

        if (!seedReader.getBlockState(blockPos).isAir()) {
            return;
        }

        seedReader.setBlock(blockPos, BlocksInit.TRAPAR_WAVE_BLOCK.get().defaultBlockState(), 1);
    }

}
