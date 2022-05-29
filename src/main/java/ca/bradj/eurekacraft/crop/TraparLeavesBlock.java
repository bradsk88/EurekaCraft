package ca.bradj.eurekacraft.crop;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class TraparLeavesBlock extends LeavesBlock {

    public static final Logger LOGGER = LogManager.getLogger();

    public TraparLeavesBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES));
    }

    public boolean isRandomlyTicking(BlockState p_54449_) {
        return true;
    }

    @Override
    public void randomTick(BlockState p_54451_, ServerLevel level, BlockPos pos, Random r) {
        Integer distanceToLog = p_54451_.getValue(DISTANCE);
        if (distanceToLog >= 7) {
            int mustBeThreeToReplaceAllBlocks = 3;
            level.setBlock(
                    pos, BlocksInit.TRAPAR_WAVE_CHILD_BLOCK.get().defaultBlockState(),
                    mustBeThreeToReplaceAllBlocks
            );
        }
    }

}
