package ca.bradj.eurekacraft.blocks;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class TraparWaveChildBlock extends Block {

    public static final Properties PROPS = Properties.
            copy(Blocks.AIR).
            noOcclusion().
            lightLevel((BlockState bs) -> 10).strength(-1).
            randomTicks();

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

    @Override
    public boolean skipRendering(BlockState p_60532_, BlockState p_60533_, Direction p_60534_) {
        return true;
    }
}
