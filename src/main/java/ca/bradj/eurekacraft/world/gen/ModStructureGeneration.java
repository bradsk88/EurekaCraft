package ca.bradj.eurekacraft.world.gen;

import ca.bradj.eurekacraft.world.structure.ModStructures;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class ModStructureGeneration {

    public static void genStructures(final BiomeLoadingEvent event) {
        // TODO: Reimplement (https://www.youtube.com/watch?v=MuFwv0p3f6g&list=PLKGarocXCE1HfOwpsWkDxPYE_xWLAKX6s&index=2)
//        RegistryKey<Biome> biomeKey = RegistryKey.create(ForgeRegistries.Keys.BIOMES,
//                Objects.requireNonNull(event.getName(),
//                        "Non existing biome detected!"));
//
//        BiomeDictionary.Type[] shackTypes = {
//                BiomeDictionary.Type.PLAINS,
//                BiomeDictionary.Type.FOREST,
//                BiomeDictionary.Type.HILLS,
//        };
//
//        boolean isShackMatch = false;
//        for (BiomeDictionary.Type t : shackTypes) {
//            if (BiomeDictionary.hasType(biomeKey, t)) {
//                isShackMatch = true;
//                break;
//            }
//        }
//
//        if (isShackMatch) {
//            List<Supplier<StructureFeature<?, ?>>> structures = event.getGeneration().getStructures();
//            structures.add(() -> ModStructures.EMPTY_SHACK.get().configured(NoFeatureConfig.INSTANCE));
//            structures.add(() -> ModStructures.TALL_SHACK.get().configured(NoFeatureConfig.INSTANCE));
//        }
    }

}
