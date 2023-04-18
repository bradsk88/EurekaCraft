package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nullable;

public class BrokenRefBoardBlock extends HorizontalDirectionalBlock {
    public static final String ITEM_ID = "broken_ref_board_block";
    public static final Item.Properties ITEM_PROPS = new Item.Properties().
            tab(ModItemGroup.EUREKACRAFT_GROUP);
    private static final Properties PROPS =
            BlockBehaviour.Properties.
                    of(Material.WOOD).
                    noOcclusion().
                    strength(2f);

    public BrokenRefBoardBlock() {
        super(PROPS);
    }

    @Override
    public void animateTick(
            BlockState p_220827_,
            Level p_220828_,
            BlockPos p_220829_,
            RandomSource p_220830_
    ) {
        super.animateTick(p_220827_, p_220828_, p_220829_, p_220830_);
        double d3 = ((double)p_220830_.nextFloat() - 0.5D) * 0.5D;
        double d4 = ((double)p_220830_.nextFloat() - 0.5D) * 0.5D;
        int j = p_220830_.nextInt(2) * 2 - 1;
        double d5 = (double)(p_220830_.nextFloat() * 2.0F * (float)j);
        p_220828_.addParticle(ParticleTypes.PORTAL, p_220829_.getX(), p_220829_.getY(), p_220829_.getZ(), d3, d4, d5);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) {
        b.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    // TODO: Add voxel shape to make this 2 blocks tall
}
