package ca.bradj.eurekacraft.blocks;

import ca.bradj.eurekacraft.core.init.TilesInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.eventbus.api.IEventBus;
import org.jetbrains.annotations.Nullable;

public class TraparWaveChildBlock extends Block implements EntityBlock {

    public static final Properties PROPS = Properties.
            copy(Blocks.AIR).
            noOcclusion().
            lightLevel((BlockState bs) -> 10).strength(-1);
//            randomTicks();

    public static final String ITEM_ID = "trapar_wave_child_block";

    // TODO: add to more blocks?
    public static final IntegerProperty BOOST = IntegerProperty.create("ref_board_boost", 0, 100);

    public TraparWaveChildBlock() {
        super(PROPS);
        this.registerDefaultState(this.getStateDefinition().any().setValue(BOOST, 100));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> state) {
        state.add(BOOST);
        super.createBlockStateDefinition(state);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos bp, BlockState bs) {
        return new TraparWaveChildBlock.TileEntity(bp, bs);
    }

    @Override
    public boolean skipRendering(BlockState p_60532_, BlockState p_60533_, Direction p_60534_) {
        return true;
    }

    public static class TileEntity extends BlockEntity {

        public static final String ID = "trapar_wave_tile_entity";

        public TileEntity(BlockPos p_155229_, BlockState p_155230_) {
            super(TilesInit.TRAPAR_WAVE.get(), p_155229_, p_155230_);
        }
    }
}
