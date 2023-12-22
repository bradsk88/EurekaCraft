package ca.bradj.eurekacraft.crop;

import ca.bradj.eurekacraft.core.config.EurekaConfig;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.core.init.items.ItemsInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
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
    public void randomTick(BlockState p_54451_, ServerLevel level, BlockPos pos, RandomSource  r) {
        Integer distanceToLog = p_54451_.getValue(DISTANCE);
        if (distanceToLog >= 7) {
            int mustBeThreeToReplaceAllBlocks = 3;
            level.setBlock(
                    pos, BlocksInit.TRAPAR_WAVE_CHILD_BLOCK.get().defaultBlockState(),
                    mustBeThreeToReplaceAllBlocks
            );

            ItemStack awardStack = new ItemStack(ItemsInit.TRAPAR_SAPLING_BLOCK::get, 1);
            ItemEntity dropEntity = new ItemEntity(
                    level, pos.getX(), pos.getY(), pos.getZ(), awardStack
            );
            if (r.nextInt(EurekaConfig.fresh_sapling_drop_rarity.get()) == 0) {
                level.addFreshEntity(dropEntity);
            }
        }
    }

}
