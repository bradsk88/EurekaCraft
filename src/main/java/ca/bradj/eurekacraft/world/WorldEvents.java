package ca.bradj.eurekacraft.world;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.world.gen.TraparWavesGeneration;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID)
public class WorldEvents {

    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event) {
        TraparWavesGeneration.generateTraparWaves(event);
    }

    @SubscribeEvent
    public static void addDimensionalSpacing(final WorldEvent.Load event) {
        // TODO: Reimplement
//        if (event.getWorld() instanceof ServerLevel) {
//            ServerLevel sw = (ServerLevel) event.getWorld();
//            if (sw.getChunkSource().generator instanceof FlatChunkGenerator && sw.dimension().equals(World.OVERWORLD)) {
//                return;
//            }
//
//            Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(
//                    sw.getChunkSource().generator.getSettings().structureConfig()
//            );
//            tempMap.putIfAbsent(ModStructures.EMPTY_SHACK.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructures.EMPTY_SHACK.get()));
//            tempMap.putIfAbsent(ModStructures.TALL_SHACK.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructures.TALL_SHACK.get()));
//            sw.getChunkSource().generator.getSettings().structureConfig = tempMap;
//        }
    }

}
