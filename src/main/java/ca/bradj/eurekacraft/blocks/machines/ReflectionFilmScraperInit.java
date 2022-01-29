package ca.bradj.eurekacraft.blocks.machines;

import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ReflectionFilmScraperInit {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
            ForgeRegistries.BLOCKS, EurekaCraft.MODID
    );

    public static final RegistryObject<ReflectionFilmScraper> REFLECTION_FILM_BLOCK = BLOCKS.register(
            "reflection_film_scraper", ReflectionFilmScraper::new
    );

}
