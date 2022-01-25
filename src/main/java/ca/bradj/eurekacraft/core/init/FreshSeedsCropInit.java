package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.crop.FreshSeedsCrop;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FreshSeedsCropInit {

	public static final String BLOCK_ID = "fresh_seeds_crop";

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			EurekaCraft.MODID);

	public static final RegistryObject<Block> FRESH_SEEDS_CROP = BLOCKS.register(BLOCK_ID, FreshSeedsCrop::new);

	public static void RegisterTextures() {
		RenderTypeLookup.setRenderLayer(FRESH_SEEDS_CROP.get(), RenderType.cutout());
	}
}