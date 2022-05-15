package ca.bradj.eurekacraft.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class TraparWaveChildBlock extends Block {

    public static final Properties PROPS = Properties.
            copy(Blocks.AIR).noOcclusion().lightLevel((BlockState bs) -> 10);

    public static final String ITEM_ID = "trapar_wave_child_block";

    public TraparWaveChildBlock() {
        super(PROPS);
    }

}
