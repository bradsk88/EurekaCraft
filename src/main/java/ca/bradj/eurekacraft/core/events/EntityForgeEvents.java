package ca.bradj.eurekacraft.core.events;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.EntitiesInit;
import ca.bradj.eurekacraft.entity.JudgeEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityForgeEvents {

    @SubscribeEvent
    public static void addEntityAttributes(
            EntityAttributeCreationEvent event
    ) {
        event.put(EntitiesInit.JUDGE.get(), JudgeEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void onRegisterEntities(
            RegistryEvent.Register<EntityType<?>> event
    ) {
        return;
    }
}
