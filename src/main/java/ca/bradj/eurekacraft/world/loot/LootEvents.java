package ca.bradj.eurekacraft.world.loot;

import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LootEvents {

    @SubscribeEvent
    public static void registerModifierSerializers(
            RegistryEvent.Register<GlobalLootModifierSerializer<?>> event
    ) {
        String[] lootTables = {
                "blueprint_in_abandoned_mineshaft",
                "blueprint_in_village_plains_house",
                "blueprint_in_simple_dungeon",
                "blueprint_in_village_savanna_house",
                "blueprint_in_village_taiga_house",
                "blueprint_in_village_desert_house"
        };

        for (String id : lootTables) {
            event.getRegistry().registerAll(
                    new EurekaAdditionModifier.Serializer().setRegistryName(
                            new ResourceLocation(EurekaCraft.MODID, id)
                    )
            );
        }
    }

}
