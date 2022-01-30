package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.blocks.TraparWaveBlock;
import ca.bradj.eurekacraft.blocks.machines.RefTableBlock;
import ca.bradj.eurekacraft.crop.FreshSeeds;
import ca.bradj.eurekacraft.blocks.machines.ReflectionFilmScraper;
import ca.bradj.eurekacraft.blocks.machines.ReflectionFilmScraperInit;
import ca.bradj.eurekacraft.materials.BrokenRefBoard;
import ca.bradj.eurekacraft.vehicles.RefBoard;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
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
			BrokenRefBoard.ITEM_ID,
			() -> new BlockItem(
					BlocksInit.BROKEN_REF_BOARD.get(),
					BrokenRefBoard.ITEM_PROPS
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

	public static final RegistryObject<Item> REF_BOARD = ITEMS.register(
			RefBoard.ITEM_ID, RefBoard::new
	);


}
