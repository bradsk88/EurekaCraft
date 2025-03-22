package ca.bradj.eurekacraft.villager;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.integration.mc.Compat;
import com.google.common.collect.ImmutableSet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class VillagersInit {

    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(
            ForgeRegistries.POI_TYPES,
            EurekaCraft.MODID
    );

    public static final DeferredRegister<VillagerProfession> VILLAGER_PROGRESSIONS = DeferredRegister.create(
            Compat.VILLAGER_PROFESSIONS,
            EurekaCraft.MODID
    );

    public static final RegistryObject<PoiType> SANDING_MACHINE_POI = POI_TYPES.register(
            "sanding_machine_poi",
            () -> Compat.Poi(BlocksInit.SANDING_MACHINE)
    );

    @SuppressWarnings("DataFlowIssue")
    public static final RegistryObject<VillagerProfession> REF_DEALER = VILLAGER_PROGRESSIONS.register(
            "ref_dealer", () -> new VillagerProfession(
                    "ref_dealer",
                    z -> z.is(SANDING_MACHINE_POI.getKey()),
                    z -> z.is(SANDING_MACHINE_POI.getKey()),
                    ImmutableSet.of(),
                    ImmutableSet.of(
                            BlocksInit.REF_TABLE_BLOCK.get(),
                            BlocksInit.FRESH_SEEDS_CROP.get(),
                            BlocksInit.FRESH_SEEDS_CROP_HARDENED.get()
                    ),
                    SoundEvents.GRAVEL_STEP
            )
    );

    public static void registerPOIs() {
        try {
            Method registerBlockStates = ObfuscationReflectionHelper.findMethod(
                    PoiType.class,
                    "registerBlockStates",
                    PoiType.class
            );
            registerBlockStates.invoke(null, SANDING_MACHINE_POI.get());
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROGRESSIONS.register(eventBus);
    }
}
