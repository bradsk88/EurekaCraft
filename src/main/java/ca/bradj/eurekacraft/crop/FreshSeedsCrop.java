package ca.bradj.eurekacraft.crop;

import ca.bradj.eurekacraft.core.init.ItemsInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class FreshSeedsCrop extends CropsBlock {

    public static final String BLOCK_ID = "fresh_seeds_crop";
    public static final Double FULL_HEIGHT = 16.0D;

    public static final IntegerProperty AGE = BlockStateProperties.AGE_1;
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, FULL_HEIGHT / 2, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, FULL_HEIGHT, 16.0D),
    };

    public FreshSeedsCrop() {
        super(Properties.copy(Blocks.WHEAT).
                lightLevel((BlockState bs) -> 10));
    }

    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    public int getMaxAge() {
        return 1;
    }

    protected IItemProvider getBaseSeedId() {
        return ItemsInit.FRESH_SEEDS_ITEM.get();
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

    @Override
    public void animateTick(BlockState p_180655_1_, World world, BlockPos p_180655_3_, Random p_180655_4_) {
        super.animateTick(p_180655_1_, world, p_180655_3_, p_180655_4_);
        for (PlayerEntity p : world.players()) {
            Vector3d position = p.getPosition(0);
            BlockPos blockPos = new BlockPos(position);
            spawnRandomParticle(world, p_180655_4_, blockPos);
            spawnRandomParticle(world, p_180655_4_, blockPos);
            spawnRandomParticle(world, p_180655_4_, blockPos);
        }
    }

    private void spawnRandomParticle(World world, Random p_180655_4_, BlockPos blockPos) {
        for (int i = -10; i < 10; i++) {
            for (int j = -10; j < 10; j++) {
                for (int k = -10; k < 10; k++) {
                    if (p_180655_4_.nextBoolean()) {
                        BlockPos pp = blockPos.offset(i, j, k);
                        world.addParticle(
                                ParticleTypes.RAIN,
                                pp.getX(), pp.getY(), pp.getZ(),
                                0.0D, -1.0D, 0.0D
                        );
                    }
                }
            }
        }
    }
}
