package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.crop.FreshSeeds;
import ca.bradj.eurekacraft.machines.ReflectionFilmScraper;
import ca.bradj.eurekacraft.machines.ReflectionFilmScraperInit;
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
	
}
