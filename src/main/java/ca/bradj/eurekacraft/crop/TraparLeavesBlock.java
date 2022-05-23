package ca.bradj.eurekacraft.crop;

import ca.bradj.eurekacraft.core.init.BlocksInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class TraparLeavesBlock extends LeavesBlock {

    public TraparLeavesBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES));
    }

    @Override
    public void tick(BlockState p_54426_, ServerLevel p_54427_, BlockPos p_54428_, Random p_54429_) {
        super.tick(p_54426_, p_54427_, p_54428_, p_54429_);
    }

    @Override
    public void randomTick(BlockState p_54451_, ServerLevel level, BlockPos pos, Random r) {
        Boolean persist = p_54451_.getValue(PERSISTENT);
        Integer distanceToLog = p_54451_.getValue(DISTANCE);
        if (!persist && distanceToLog == 7) {
            level.setBlock(pos, BlocksInit.TRAPAR_WAVE_CHILD_BLOCK.get().defaultBlockState(), 4);
        }
    }

}
