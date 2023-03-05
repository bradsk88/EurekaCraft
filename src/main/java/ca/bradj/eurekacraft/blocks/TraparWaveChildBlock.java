package ca.bradj.eurekacraft.blocks;

import ca.bradj.eurekacraft.core.init.TilesInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class TraparWaveChildBlock extends Block implements EntityBlock {

    public static final Properties PROPS = BlockBehaviour.Properties.of(Material.AIR).noOcclusion().noCollission();

    public static final String ITEM_ID = "trapar_wave_child_block";

    // TODO: add to more blocks?
    public static final IntegerProperty BOOST = IntegerProperty.create("ref_board_boost", 0, 100);

    public TraparWaveChildBlock() {
        super(PROPS);
        this.registerDefaultState(this.getStateDefinition().any().setValue(BOOST, 100));
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return Shapes.empty();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> state) {
        state.add(BOOST);
        super.createBlockStateDefinition(state);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos bp, BlockState bs) {
        return TilesInit.TRAPAR_WAVE.get().create(bp, bs);
    }

    @Override
    public boolean skipRendering(BlockState p_53972_, BlockState p_53973_, Direction p_53974_) {
        return true;
    }

    public static class TileEntity extends BlockEntity {

        public static final String ID = "trapar_wave_tile_entity";

        public TileEntity(BlockPos p_155229_, BlockState p_155230_) {
            super(TilesInit.TRAPAR_WAVE.get(), p_155229_, p_155230_);
        }
    }
}
