package ca.bradj.eurekacraft;

import ca.bradj.eurekacraft.core.init.*;
import ca.bradj.eurekacraft.blocks.machines.ReflectionFilmScraperInit;
import ca.bradj.eurekacraft.vehicles.EntityRefBoard;
import ca.bradj.eurekacraft.world.structure.ModStructures;
import net.minecraft.block.Block;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EurekaCraft.MODID)
public class EurekaCraft {
	public static final String MODID = "eurekacraft";

	// Directly reference a log4j logger.
	private static final Logger LOGGER = LogManager.getLogger();

	public EurekaCraft() {
		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		// Register the enqueueIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		// Register the processIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		// Register the doClientStuff method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);

		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		TilesInit.TILES.register(bus);
		BlocksInit.BLOCKS.register(bus);
		ItemsInit.register(bus);
		EntitiesInit.ENTITIES.register(bus);
		ReflectionFilmScraperInit.BLOCKS.register(bus);
		FeaturesInit.PLACEMENTS.register(bus);
		FeaturesInit.FEATURES.register(bus);
		ContainerTypesInit.TYPES.register(bus);
		RecipesInit.register(bus);
		ModStructures.STRUCTURES.register(bus);
	}

	private void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			ModStructures.setupStructures();
		});
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		// do something that can only be done on the client
		LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
		BlocksInit.RegisterTextures();
		event.enqueueWork(() -> {
			ItemModelsProperties.register(
					ItemsInit.GLIDE_BOARD.get(),
					new ResourceLocation(EurekaCraft.MODID, "deployed"),
					new EntityRefBoard.DeployedPropGetter()
			);
			ItemModelsProperties.register(
					ItemsInit.STANDARD_BOARD.get(),
					new ResourceLocation(EurekaCraft.MODID, "deployed"),
					new EntityRefBoard.DeployedPropGetter()
			);
			ItemModelsProperties.register(
					ItemsInit.BROKEN_BOARD.get(),
					new ResourceLocation(EurekaCraft.MODID, "deployed"),
					new EntityRefBoard.DeployedPropGetter()
			);
			ItemModelsProperties.register(
					ItemsInit.REF_BOARD_CORE.get(),
					new ResourceLocation(EurekaCraft.MODID, "deployed"),
					new EntityRefBoard.DeployedPropGetter()
			);
		});
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
		// some example code to dispatch IMC to another mod
		InterModComms.sendTo("eurekacraft", "helloworld", () -> {
			LOGGER.info("Hello world from the MDK");
			return "Hello world";
		});
	}

	private void processIMC(final InterModProcessEvent event) {
		// some example code to receive and process InterModComms from other mods
		LOGGER.info("Got IMC {}",
				event.getIMCStream().map(m -> m.getMessageSupplier().get()).collect(Collectors.toList()));
	}

	// You can use SubscribeEvent and let the Event Bus discover methods to call
	@SubscribeEvent
	public void onServerStarting(FMLServerStartingEvent event) {
		// do something when the server starts
		LOGGER.info("HELLO from server starting");
	}

	// You can use EventBusSubscriber to automatically subscribe events on the
	// contained class (this is subscribing to the MOD
	// Event bus for receiving Registry Events)
	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
			// register a new block here
			LOGGER.info("HELLO from Register Block");
		}
	}
}
