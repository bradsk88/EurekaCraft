package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.blocks.TraparWaveBlock;
import ca.bradj.eurekacraft.blocks.machines.RefTableBlock;
import ca.bradj.eurekacraft.blocks.machines.ReflectionFilmScraper;
import ca.bradj.eurekacraft.blocks.machines.ReflectionFilmScraperInit;
import ca.bradj.eurekacraft.crop.FreshSeeds;
import ca.bradj.eurekacraft.materials.BrokenRefBoardBlock;
import ca.bradj.eurekacraft.materials.ReflectionFilm;
import ca.bradj.eurekacraft.materials.ReflectionFilmDust;
import ca.bradj.eurekacraft.vehicles.BrokenRefBoard;
import ca.bradj.eurekacraft.vehicles.GlideBoard;
import ca.bradj.eurekacraft.vehicles.RefBoardItem;
import ca.bradj.eurekacraft.vehicles.StandardRefBoard;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemsInit {

	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EurekaCraft.MODID);

	public static final RegistryObject<Item> FRESH_SEEDS_ITEM = ITEMS.register(
			FreshSeeds.ITEM_ID, FreshSeeds::new
	);

	public static final RegistryObject<Item> REFLECTION_SCRAPER_BLOCK = ITEMS.register(
			ReflectionFilmScraper.ITEM_ID,
			() -> new BlockItem(
					ReflectionFilmScraperInit.REFLECTION_FILM_BLOCK.get(),
					ReflectionFilmScraper.ITEM_PROPS
			)
	);

	public static final RegistryObject<Item> BROKEN_REF_BOARD_BLOCK = ITEMS.register(
			BrokenRefBoardBlock.ITEM_ID,
			() -> new BlockItem(
					BlocksInit.BROKEN_REF_BOARD.get(),
					BrokenRefBoardBlock.ITEM_PROPS
			)
	);
	public static final RegistryObject<Item> TRAPAR_WAVE_BLOCK = ITEMS.register(
			TraparWaveBlock.ITEM_ID,
			() -> new BlockItem(
					BlocksInit.TRAPAR_WAVE_BLOCK.get(),
					TraparWaveBlock.ITEM_PROPS
			)
	);
	public static final RegistryObject<Item> REF_TABLE_BLOCK = ITEMS.register(
			RefTableBlock.ITEM_ID,
			() -> new BlockItem(
					BlocksInit.REF_TABLE_BLOCK.get(),
					RefTableBlock.ITEM_PROPS
			)
	);

	public static final RegistryObject<Item> GLIDE_BOARD = ITEMS.register(
			RefBoardItem.ItemIDs.GLIDE_BOARD, GlideBoard::new
	);

	public static final RegistryObject<Item> STANDARD_BOARD = ITEMS.register(
			RefBoardItem.ItemIDs.REF_BOARD, StandardRefBoard::new
	);

	public static final RegistryObject<Item> BROKEN_BOARD = ITEMS.register(
			BrokenRefBoard.ITEM_ID, BrokenRefBoard::new
	);


	public static final RegistryObject<Item> REFLECTION_FILM = ITEMS.register(
			ReflectionFilm.ITEM_ID, ReflectionFilm::new
	);


	public static final RegistryObject<Item> REFLECTION_FILM_DUST = ITEMS.register(
			ReflectionFilmDust.ITEM_ID, ReflectionFilmDust::new
	);


	public static void register(IEventBus bus) {
		ITEMS.register(bus);
	}
}
