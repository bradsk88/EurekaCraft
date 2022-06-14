package ca.bradj.eurekacraft;

import ca.bradj.eurekacraft.client.BoardItemRendering;
import ca.bradj.eurekacraft.core.config.EurekaConfig;
import ca.bradj.eurekacraft.core.init.*;
import ca.bradj.eurekacraft.core.network.EurekaCraftNetwork;
import ca.bradj.eurekacraft.entity.board.EntityRefBoard;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EurekaCraft.MODID)
public class EurekaCraft {
    public static final String MODID = "eurekacraft";

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

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
        AdvancementsInit.registerIconItems(bus);
        EntitiesInit.ENTITIES.register(bus);
        // TODO: Reimplement
//        FeaturesInit.PLACEMENTS.register(bus);
        FeaturesInit.FEATURES.register(bus);
        ContainerTypesInit.TYPES.register(bus);
        RecipesInit.register(bus);
//        ModStructures.STRUCTURES.register(bus);
        AdvancementsInit.register();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, EurekaConfig.SPEC, EurekaConfig.FILENAME);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(EurekaCraftNetwork::init);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        BlocksInit.RegisterTextures();
        event.enqueueWork(
                ModelsInit::registerModels
        );
        event.enqueueWork(() -> {
            EntityRenderers.register(EntitiesInit.REF_BOARD.get(), EntityRefBoard.Renderer::new);
        });
        event.enqueueWork(BoardItemRendering::initItemProperties);

        // TODO: Reimplement
//        event.enqueueWork(
//                TraparStormRendering::init
//        );
    }

    @SubscribeEvent
    public void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        // TODO: Reimplement
//        event.<JudgeEntity>registerEntityRenderer (
//                EntitiesInit.JUDGE.get(), JudgeRenderer::new
//        );
    }

    @SubscribeEvent
    public void onServerStarting(ServerAboutToStartEvent event) {
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
