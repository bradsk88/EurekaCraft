package ca.bradj.eurekacraft.crop;

import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

public class FreshSeeds extends ItemNameBlockItem implements IPlantable {

	public static final String ITEM_ID = "fresh_seeds";

	private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);
	
	public FreshSeeds() {
		super(
				BlocksInit.FRESH_SEEDS_CROP.get(), PROPS
		);
	}

	@Override
	public PlantType getPlantType(BlockGetter world, BlockPos pos) {
		return PlantType.CROP;
	}

	@Override
	public BlockState getPlant(BlockGetter world, BlockPos pos) {
		return BlocksInit.FRESH_SEEDS_CROP.get().defaultBlockState();
	}

}
