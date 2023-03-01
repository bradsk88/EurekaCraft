package ca.bradj.eurekacraft.villager;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import com.google.common.collect.ImmutableSet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.client.model.pipeline.BlockInfo;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class VillagersInit {

    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(
            ForgeRegistries.POI_TYPES, EurekaCraft.MODID
    );

    public static final DeferredRegister<VillagerProfession> VILLAGER_PROGRESSIONS = DeferredRegister.create(
            ForgeRegistries.PROFESSIONS, EurekaCraft.MODID
    );

    public static final RegistryObject<PoiType> FRESH_LEAVES_POI = POI_TYPES.register(
            "fresh_leaves_poi",
            () -> new PoiType("fresh_leaves_poi", PoiType.getBlockStates(BlocksInit.FRESH_SEEDS_CROP.get()), 1, 1)
    );

    public static final RegistryObject<VillagerProfession> REF_DEALER = VILLAGER_PROGRESSIONS.register(
            "ref_dealer",
            () -> new VillagerProfession(
                    "ref_dealer",
                    FRESH_LEAVES_POI.get(),
                    ImmutableSet.of(),
                    ImmutableSet.of(BlocksInit.FRESH_SEEDS_CROP.get(), BlocksInit.FRESH_SEEDS_CROP_HARDENED.get()),
                    SoundEvents.VILLAGER_WORK_FISHERMAN
            )
    );

    public static void registerPOIs() {
        try {
            Method registerBlockStates = ObfuscationReflectionHelper.findMethod(PoiType.class, "registerBlockStates", PoiType.class);
            registerBlockStates.invoke(null, FRESH_LEAVES_POI.get());
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROGRESSIONS.register(eventBus);
    }
}
