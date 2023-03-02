package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.blocks.ResinBlock;
import ca.bradj.eurekacraft.blocks.TraparWaveChildBlock;
import ca.bradj.eurekacraft.blocks.machines.RefTableBlock;
import ca.bradj.eurekacraft.blocks.machines.SandingMachineBlock;
import ca.bradj.eurekacraft.crop.FreshSeedsCrop;
import ca.bradj.eurekacraft.crop.HardenedFreshSeedsCrop;
import ca.bradj.eurekacraft.crop.TraparLeavesBlock;
import ca.bradj.eurekacraft.materials.BrokenRefBoardBlock;
import ca.bradj.eurekacraft.world.gen.features.trees.TraparTreeGrower;
import ca.bradj.eurekacraft.world.trees.EurekaWoodBlock;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlocksInit {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			EurekaCraft.MODID);

	public static final RegistryObject<Block> FRESH_SEEDS_CROP = BLOCKS.register(
			FreshSeedsCrop.BLOCK_ID, FreshSeedsCrop::new
	);
	public static final RegistryObject<Block> FRESH_SEEDS_CROP_HARDENED = BLOCKS.register(
			HardenedFreshSeedsCrop.BLOCK_ID, HardenedFreshSeedsCrop::new
	);
	public static final RegistryObject<Block> RESIN = BLOCKS.register(
			ResinBlock.ITEM_ID, ResinBlock::new
	);
	public static final RegistryObject<Block> BROKEN_REF_BOARD = BLOCKS.register(
			BrokenRefBoardBlock.ITEM_ID, BrokenRefBoardBlock::new
	);

	public static final RegistryObject<Block> TRAPAR_LOG_BLOCK = BLOCKS.register(
			"trapar_log", () -> new EurekaWoodBlock(
					BlockBehaviour.Properties.copy(Blocks.OAK_LOG)
			)
	);

	public static final RegistryObject<Block> TRAPAR_WOOD_BLOCK = BLOCKS.register(
			"trapar_wood", () -> new EurekaWoodBlock(
					BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)
			)
	);

	public static final RegistryObject<Block> TRAPAR_LEAVES_BLOCK = BLOCKS.register(
			"trapar_leaves", TraparLeavesBlock::new
	);

	public static final RegistryObject<Block> TRAPAR_SAPLING = BLOCKS.register(
			"fresh_sapling", () -> new SaplingBlock(
					new TraparTreeGrower(),
					BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)
			)
	);

	public static final RegistryObject<Block> TRAPAR_WAVE_CHILD_BLOCK = BLOCKS.register(
			TraparWaveChildBlock.ITEM_ID, TraparWaveChildBlock::new
	);
	public static final RegistryObject<Block> REF_TABLE_BLOCK = BLOCKS.register(
			RefTableBlock.ITEM_ID, RefTableBlock::new
	);
	public static final RegistryObject<Block> SANDING_MACHINE = BLOCKS.register(
			SandingMachineBlock.ITEM_ID, SandingMachineBlock::new
	);

	public static void RegisterTextures() {
		ItemBlockRenderTypes.setRenderLayer(FRESH_SEEDS_CROP.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(FRESH_SEEDS_CROP_HARDENED.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(TRAPAR_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BROKEN_REF_BOARD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(TRAPAR_WAVE_CHILD_BLOCK.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TRAPAR_LEAVES_BLOCK.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(TRAPAR_SAPLING.get(), RenderType.cutout());
	}

	public static boolean never(BlockState p_235436_0_, BlockGetter p_235436_1_, BlockPos p_235436_2_) {
		return false;
	}

}