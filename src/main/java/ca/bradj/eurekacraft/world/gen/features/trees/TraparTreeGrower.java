package ca.bradj.eurekacraft.world.gen.features.trees;

import ca.bradj.eurekacraft.world.gen.features.ConfiguredFeatures;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class TraparTreeGrower extends AbstractTreeGrower {

    @Nullable
    @Override
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource rand, boolean p_204308_) {
        return ConfiguredFeatures.TRAPAR_TREE;
    }
}
