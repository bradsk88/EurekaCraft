package ca.bradj.eurekacraft.world.gen;

import ca.bradj.eurekacraft.world.structure.ModStructures;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public class ModStructureGeneration {

    public static void genStructures(final BiomeLoadingEvent event) {
        RegistryKey<Biome> biomeKey = RegistryKey.create(ForgeRegistries.Keys.BIOMES,
                Objects.requireNonNull(event.getName(),
                        "Non existing biome detected!"));

        BiomeDictionary.Type[] shackTypes = {
                BiomeDictionary.Type.PLAINS,
                BiomeDictionary.Type.FOREST,
                BiomeDictionary.Type.HILLS,
        };

        boolean isShackMatch = false;
        for (BiomeDictionary.Type t : shackTypes) {
            if (BiomeDictionary.hasType(biomeKey, t)) {
                isShackMatch = true;
                break;
            }
        }

        if (isShackMatch) {
            List<Supplier<StructureFeature<?, ?>>> structures = event.getGeneration().getStructures();
            structures.add(() -> ModStructures.EMPTY_SHACK.get().configured(NoFeatureConfig.INSTANCE));
            structures.add(() -> ModStructures.TALL_SHACK.get().configured(NoFeatureConfig.INSTANCE));
        }
    }

}
