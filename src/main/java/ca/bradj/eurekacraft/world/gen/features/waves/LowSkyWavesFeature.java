package ca.bradj.eurekacraft.world.gen.features.waves;

import ca.bradj.eurekacraft.core.init.BlocksInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Random;

public class LowSkyWavesFeature extends Feature<NoneFeatureConfiguration> {
    public LowSkyWavesFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    private static final int xzSpread = 2;
    private static final int yGrow = 2;

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> p_159473_) {
        BlockPos blockpos = p_159473_.origin();
        Random random = p_159473_.random();
        WorldGenLevel worldgenlevel = p_159473_.level();
        if (blockpos.getY() < worldgenlevel.getMinBuildHeight() + 5) {
            return false;
        } else {
            int i = random.nextInt(xzSpread);
            int j = random.nextInt(xzSpread);

            for (BlockPos blockpos1 : BlockPos.betweenClosed(blockpos.offset(-i, 0, -j), blockpos.offset(i, 1, j))) {
                this.tryPlaceBlock(worldgenlevel, blockpos1, random);

            }

            return true;
        }
    }

    private void tryPlaceBlock(LevelAccessor p_65268_, BlockPos p_65269_, Random p_65270_) {
        if (p_65268_.isEmptyBlock(p_65269_)) {
            p_65268_.setBlock(p_65269_, BlocksInit.TRAPAR_WAVE_CHILD_BLOCK.get().defaultBlockState(), 4);
            for (int i = 1; i < yGrow; i++) {
                if (p_65268_.isEmptyBlock(p_65269_)) {
                    BlockPos bp = p_65269_.atY(p_65269_.getY() + i);
                    p_65268_.setBlock(bp, BlocksInit.TRAPAR_WAVE_CHILD_BLOCK.get().defaultBlockState(), 4);
                }
            }
        }

    }
}
