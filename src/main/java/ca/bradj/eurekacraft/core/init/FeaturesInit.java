package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.features.TraparWavesFeature;
import ca.bradj.eurekacraft.features.TraparWavesPlacement;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FeaturesInit {

    public static final DeferredRegister<Placement<?>> PLACEMENTS = DeferredRegister.create(
            ForgeRegistries.DECORATORS,
            EurekaCraft.MODID
    );

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(
            ForgeRegistries.FEATURES,
            EurekaCraft.MODID
    );

    public static final RegistryObject<TraparWavesPlacement> TRAPAR_WAVES_PLACEMENT = PLACEMENTS.register(
            TraparWavesPlacement.PLACEMENT_ID, TraparWavesPlacement::new
    );

    public static final RegistryObject<TraparWavesFeature> TRAPAR_WAVES = FEATURES.register(
            TraparWavesFeature.FEATURE_ID, TraparWavesFeature::new
    );

}
