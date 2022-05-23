package ca.bradj.eurekacraft.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

public class TraparWaveChildBlock extends Block {

    public static final Properties PROPS = Properties.
            copy(Blocks.AIR).noOcclusion().lightLevel((BlockState bs) -> 10).strength(-1);

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
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return -1;
    }

    @Override
    public boolean isBurning(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 100;
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return false;
    }
}
