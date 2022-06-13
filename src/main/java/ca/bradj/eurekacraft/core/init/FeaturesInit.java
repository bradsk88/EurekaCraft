package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.world.gen.features.waves.GroundWavesFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FeaturesInit {

    // TODO: Reimplement
//    public static final DeferredRegister<Placement<?>> PLACEMENTS = DeferredRegister.create(
//            ForgeRegistries.DECORATORS,
//            EurekaCraft.MODID
//    );
//
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(
            ForgeRegistries.FEATURES,
            EurekaCraft.MODID
    );
//
//    public static final RegistryObject<TraparWavesPlacement> TRAPAR_WAVES_PLACEMENT = PLACEMENTS.register(
//            TraparWavesPlacement.PLACEMENT_ID, TraparWavesPlacement::new
//    );
//
//    public static final RegistryObject<TraparWavesFeature> TRAPAR_WAVES = FEATURES.register(
//            TraparWavesFeature.FEATURE_ID, TraparWavesFeature::new
//    );

    public static final RegistryObject<GroundWavesFeature> GROUND_WAVES_FEATURE = FEATURES.register(
            "trapar_waves_ground_feature", GroundWavesFeature::new
    );

}
