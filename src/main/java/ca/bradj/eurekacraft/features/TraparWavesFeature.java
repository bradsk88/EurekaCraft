package ca.bradj.eurekacraft.features;

import ca.bradj.eurekacraft.core.init.BlocksInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.NoOpFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Random;

public class TraparWavesFeature extends NoOpFeature {

    public static final String FEATURE_ID = "trapar_waves_feature";

    public TraparWavesFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(NoneFeatureConfiguration cfg, WorldGenLevel seedReader, ChunkGenerator gen, Random random, BlockPos blockPos) {
        if (!seedReader.isEmptyBlock(blockPos)) {
            return false;
        }

        if (!seedReader.getBlockState(blockPos).isAir()) {
            return false;
        }

        this.doPlace(seedReader, blockPos);

        return true;
    }

    private void doPlace(WorldGenLevel seedReader, BlockPos blockPos) {
        if (!seedReader.isEmptyBlock(blockPos)) {
            return;
        }

        if (!seedReader.getBlockState(blockPos).isAir()) {
            return;
        }

        seedReader.setBlock(blockPos, BlocksInit.TRAPAR_WAVE_BLOCK.get().defaultBlockState(), 1);
    }

}
