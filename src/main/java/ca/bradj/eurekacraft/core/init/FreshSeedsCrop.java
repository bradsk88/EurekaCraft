package ca.bradj.eurekacraft.core.init;

import java.util.List;
import java.util.Random;

import ca.bradj.eurekacraft.ExampleMod;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ILootGenerator;
import net.minecraft.loot.LootContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FreshSeedsCrop extends CropsBlock {

	public static final Double FULL_HEIGHT = 16.0D;

	public static final FreshSeedsCrop.Properties PROPS = AbstractBlock.Properties.of(Material.WOOD);

	public static final IntegerProperty AGE = BlockStateProperties.AGE_1;
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] {
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, FULL_HEIGHT / 2, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, FULL_HEIGHT, 16.0D),
	};

	public FreshSeedsCrop() {
		super(Properties.copy(Blocks.WHEAT).lightLevel((BlockState bs) -> 10));
	}

	public IntegerProperty getAgeProperty() {
		return AGE;
	}

	public int getMaxAge() {
		return 1;
	}

	@Override
	public void animateTick(BlockState p_180655_1_, World world, BlockPos pos, Random p_180655_4_) {
		super.animateTick(p_180655_1_, world, pos, p_180655_4_);
		world.addParticle(ParticleTypes.BUBBLE, pos.getX(), pos.getY() + 1.0D , pos.getZ(), 0.0, FULL_HEIGHT, 0.0);
	}

	protected IItemProvider getBaseSeedId() {
		return FreshSeedsInit.FRESH_SEEDS_ITEM.get();
	}

	@Override
	protected boolean mayPlaceOn(BlockState p_200014_1_, IBlockReader p_200014_2_, BlockPos p_200014_3_) {
		return super.mayPlaceOn(p_200014_1_, p_200014_2_, p_200014_3_);
	}

	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_,
							   ISelectionContext p_220053_4_) {
		return SHAPE_BY_AGE[p_220053_1_.getValue(this.getAgeProperty())];
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_) {
		p_206840_1_.add(AGE);
	}
}
