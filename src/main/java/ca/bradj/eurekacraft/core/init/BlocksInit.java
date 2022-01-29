package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.blocks.TraparWaveBlock;
import ca.bradj.eurekacraft.crop.FreshSeedsCrop;
import ca.bradj.eurekacraft.materials.BrokenRefBoard;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlocksInit {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			EurekaCraft.MODID);

	public static final RegistryObject<Block> FRESH_SEEDS_CROP = BLOCKS.register(
			FreshSeedsCrop.BLOCK_ID, FreshSeedsCrop::new
	);
	public static final RegistryObject<Block> BROKEN_REF_BOARD = BLOCKS.register(
			BrokenRefBoard.ITEM_ID, BrokenRefBoard::new
	);
	public static final RegistryObject<Block> TRAPAR_WAVE_BLOCK = BLOCKS.register(
			TraparWaveBlock.ITEM_ID, TraparWaveBlock::new
	);

	public static void RegisterTextures() {
		RenderTypeLookup.setRenderLayer(FRESH_SEEDS_CROP.get(), RenderType.cutout());
	}

	public static boolean never(BlockState p_235436_0_, IBlockReader p_235436_1_, BlockPos p_235436_2_) {
		return false;
	}

}