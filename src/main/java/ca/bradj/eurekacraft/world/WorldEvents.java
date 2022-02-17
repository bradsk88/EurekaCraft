package ca.bradj.eurekacraft.world;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.world.gen.ModStructureGeneration;
import ca.bradj.eurekacraft.world.structure.ModStructures;
import net.minecraft.world.World;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID)
public class WorldEvents {

    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event) {
        ModStructureGeneration.genStructures(event);
        TraparWavesGeneration.generateTraparWaves(event);
    }

    @SubscribeEvent
    public static void addDimensionalSpacing(final WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld sw = (ServerWorld) event.getWorld();
            if (sw.getChunkSource().generator instanceof FlatChunkGenerator && sw.dimension().equals(World.OVERWORLD)) {
                return;
            }

            Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(
                    sw.getChunkSource().generator.getSettings().structureConfig()
            );
            tempMap.putIfAbsent(ModStructures.EMPTY_SHACK.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructures.EMPTY_SHACK.get()));
            tempMap.putIfAbsent(ModStructures.TALL_SHACK.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructures.TALL_SHACK.get()));
            sw.getChunkSource().generator.getSettings().structureConfig = tempMap;
        }
    }

}
