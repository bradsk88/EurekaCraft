package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.world.loot.BlueprintsAdditionModifier;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LootModifiers {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, EurekaCraft.MODID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_BLUEPRINT =
            LOOT_MODIFIER_SERIALIZERS.register("add_item", BlueprintsAdditionModifier.CODEC);

    public static void register(IEventBus bus) {
        bus.register(LOOT_MODIFIER_SERIALIZERS);
    }

}
