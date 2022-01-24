package ca.bradj.eurekacraft.core.init;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

public class FreshSeeds extends BlockNamedItem implements IPlantable {

	public static final Item.Properties FRESH_SEEDS_PROPS = new Item.Properties().tab(ItemGroup.TAB_MISC);
	
	public FreshSeeds() {
		super(
				FreshSeedsCropInit.FRESH_SEEDS_CROP.get(), FRESH_SEEDS_PROPS
		);
	}

	@Override
	public PlantType getPlantType(IBlockReader world, BlockPos pos) {
		return PlantType.CROP;
	}

	@Override
	public BlockState getPlant(IBlockReader world, BlockPos pos) {
		return FreshSeedsCropInit.FRESH_SEEDS_CROP.get().defaultBlockState();
	}

}
