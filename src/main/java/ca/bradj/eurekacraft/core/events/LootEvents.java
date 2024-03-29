package ca.bradj.eurekacraft.core.events;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.world.loot.BlueprintsAdditionModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LootEvents {

    @SubscribeEvent
    public static void registerModifierSerializers(
            final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event
    ) {
        event.getRegistry().registerAll(
                new BlueprintsAdditionModifier.Serializer().setRegistryName(new ResourceLocation(EurekaCraft.MODID, "blueprint_from_village_plains_chest")),
                new BlueprintsAdditionModifier.Serializer().setRegistryName(new ResourceLocation(EurekaCraft.MODID, "blueprint_from_village_savanna_chest")),
                new BlueprintsAdditionModifier.Serializer().setRegistryName(new ResourceLocation(EurekaCraft.MODID, "blueprint_from_village_desert_chest")),
                new BlueprintsAdditionModifier.Serializer().setRegistryName(new ResourceLocation(EurekaCraft.MODID, "blueprint_from_village_snowy_chest")),
                new BlueprintsAdditionModifier.Serializer().setRegistryName(new ResourceLocation(EurekaCraft.MODID, "blueprint_from_village_cartographer")),
                new BlueprintsAdditionModifier.Serializer().setRegistryName(new ResourceLocation(EurekaCraft.MODID, "blueprint_from_village_taiga_chest")),
                new BlueprintsAdditionModifier.Serializer().setRegistryName(new ResourceLocation(EurekaCraft.MODID, "blueprint_from_village_toolsmith_chest")),
                new BlueprintsAdditionModifier.Serializer().setRegistryName(new ResourceLocation(EurekaCraft.MODID, "blueprint_from_village_weaponsmith_chest"))
        );
    }

}
