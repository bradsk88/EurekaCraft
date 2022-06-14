package ca.bradj.eurekacraft.core.events;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.world.loot.BlueprintsAdditionModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LootEvents {

    @SubscribeEvent
    public static void registerModifierSerializers(
            final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event
    ) {

        ArrayList<String> files = new ArrayList<>();
        files.add("blueprint_from_village_plains_chest");
//        files.add("blueprint_from_village_savanna_chest");
//        files.add("blueprint_from_village_desert_chest");
//        files.add("blueprint_from_village_snowy_chest");
//        files.add("blueprint_from_village_cartographer");
//        files.add("blueprint_from_village_taiga_chest");
//        files.add("blueprint_from_village_toolsmith_chest");
//        files.add("blueprint_from_village_weaponsmith_chest");

        for (String f : files) {
            event.getRegistry().registerAll(new BlueprintsAdditionModifier.Serializer().setRegistryName(
                    new ResourceLocation(EurekaCraft.MODID, f)
            ));
        }
    }

}
