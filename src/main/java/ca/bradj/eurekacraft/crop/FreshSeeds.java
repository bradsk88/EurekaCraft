package ca.bradj.eurekacraft.crop;

import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

public class FreshSeeds extends BlockNamedItem implements IPlantable {

	public static final String ITEM_ID = "fresh_seeds";

	private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);
	
	public FreshSeeds() {
		super(
				BlocksInit.FRESH_SEEDS_CROP.get(), PROPS
		);
	}

	@Override
	public PlantType getPlantType(IBlockReader world, BlockPos pos) {
		return PlantType.CROP;
	}

	@Override
	public BlockState getPlant(IBlockReader world, BlockPos pos) {
		return BlocksInit.FRESH_SEEDS_CROP.get().defaultBlockState();
	}

}
