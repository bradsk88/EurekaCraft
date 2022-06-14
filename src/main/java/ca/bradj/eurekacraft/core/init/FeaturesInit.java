package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.world.gen.features.waves.GroundWavesFeature;
import ca.bradj.eurekacraft.world.gen.features.waves.LowSkyWavesFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FeaturesInit {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(
            ForgeRegistries.FEATURES,
            EurekaCraft.MODID
    );

    public static final RegistryObject<GroundWavesFeature> GROUND_WAVES_FEATURE = FEATURES.register(
            "trapar_waves_ground_feature", GroundWavesFeature::new
    );

    public static final RegistryObject<LowSkyWavesFeature> MED_WAVES_FEATURE = FEATURES.register(
            "trapar_waves_med_feature", LowSkyWavesFeature::new
    );

}
