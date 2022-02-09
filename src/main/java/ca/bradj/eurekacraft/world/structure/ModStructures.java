package ca.bradj.eurekacraft.world.structure;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.world.structure.structures.EmptyShack;
import ca.bradj.eurekacraft.world.structure.structures.TallShack;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ModStructures {

    public static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, EurekaCraft.MODID);

    public static final RegistryObject<EmptyShack> EMPTY_SHACK = STRUCTURES.register("empty_shack", EmptyShack::new);
    public static final RegistryObject<TallShack> TALL_SHACK = STRUCTURES.register("tall_shack", TallShack::new);

    public static void setupStructures() {
        Random r = new Random(308374);
        setupMapSpacingAndLand(EMPTY_SHACK.get(), new StructureSeparationSettings(100, 50, r.nextInt(Integer.MAX_VALUE)), true);
        setupMapSpacingAndLand(TALL_SHACK.get(), new StructureSeparationSettings(100, 50, r.nextInt(Integer.MAX_VALUE)), true);
    }

    private static <F extends Structure<?>> void setupMapSpacingAndLand(
            F structure, StructureSeparationSettings settings, boolean transformSurroundingLand
    ) {

        Structure.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

        if (transformSurroundingLand) {
            Structure.NOISE_AFFECTING_FEATURES = ImmutableList.<Structure<?>>builder().
                    addAll(Structure.NOISE_AFFECTING_FEATURES).
                    add(structure).
                    build();
        }

        DimensionStructuresSettings.DEFAULTS = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder().
                putAll(DimensionStructuresSettings.DEFAULTS).
                put(structure, settings).
                build();

        WorldGenRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(s -> {
            Map<Structure<?>, StructureSeparationSettings> structureMap = s.getValue().structureSettings().structureConfig();

            /*
             * Pre-caution in case a mod makes the structure map immutable like datapacks do.
             * I take no chances myself. You never know what another mods does...
             *
             * structureConfig requires AccessTransformer  (See resources/META-INF/accesstransformer.cfg)
             */
            if(structureMap instanceof ImmutableMap){
                Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
                tempMap.put(structure, settings);
                s.getValue().structureSettings().structureConfig = tempMap;
            }
            else{
                structureMap.put(structure, settings);
            }
        });

    }

}
