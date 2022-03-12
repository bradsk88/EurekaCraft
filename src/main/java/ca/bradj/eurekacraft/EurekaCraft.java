package ca.bradj.eurekacraft;

import ca.bradj.eurekacraft.client.BoardItemRendering;
import ca.bradj.eurekacraft.client.TraparStormRendering;
import ca.bradj.eurekacraft.client.entity_rendering.JudgeRenderer;
import ca.bradj.eurekacraft.core.config.EurekaConfig;
import ca.bradj.eurekacraft.core.init.*;
import ca.bradj.eurekacraft.core.network.EurekaCraftNetwork;
import ca.bradj.eurekacraft.render.TraparWaveHandler;
import ca.bradj.eurekacraft.vehicles.deployment.DeploymentCapability;
import ca.bradj.eurekacraft.world.structure.ModStructures;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EurekaCraft.MODID)
public class EurekaCraft {
    public static final String MODID = "eurekacraft";

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public EurekaCraft() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        TilesInit.TILES.register(bus);
        BlocksInit.BLOCKS.register(bus);
        ItemsInit.register(bus);
        EntitiesInit.ENTITIES.register(bus);
        FeaturesInit.PLACEMENTS.register(bus);
        FeaturesInit.FEATURES.register(bus);
        ContainerTypesInit.TYPES.register(bus);
        RecipesInit.register(bus);
        ModStructures.STRUCTURES.register(bus);
        AdvancementsInit.register();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, EurekaConfig.SPEC, EurekaConfig.FILENAME);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModStructures::setupStructures);
        event.enqueueWork(DeploymentCapability::register);
        event.enqueueWork(EurekaCraftNetwork::init);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        BlocksInit.RegisterTextures();
        RenderingRegistry.registerEntityRenderingHandler(EntitiesInit.JUDGE.get(), JudgeRenderer::new);
        event.enqueueWork(
                ModelsInit::registerModels
        );
        event.enqueueWork(
                () -> ClientRegistry.bindTileEntityRenderer(TilesInit.TRAPAR_WAVE.get(), TraparWaveHandler::new)
        );
        event.enqueueWork(
                BoardItemRendering::initItemProperties
        );
        event.enqueueWork(
                TraparStormRendering::init
        );
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerAboutToStartEvent event) {
        if (EurekaConfig.crash_if_flight_disabled.get()) {
            if (!event.getServer().isFlightAllowed()) {
                throw new IllegalStateException(
                        "EurekaCraft is configured to crash the server if " +
                                "flight is disabled. You can fix this by " +
                                "updating server.properties to enable flight, " +
                                "or by updating world/serverconfig/" + EurekaConfig.FILENAME);
            }
        }
    }

}
