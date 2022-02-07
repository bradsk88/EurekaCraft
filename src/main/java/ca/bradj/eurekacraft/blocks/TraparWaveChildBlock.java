package ca.bradj.eurekacraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

public class TraparWaveChildBlock extends Block {

    // TODO Stop casting shadows
    public static final Properties PROPS = Properties.
            copy(Blocks.AIR).noOcclusion().lightLevel((BlockState bs) -> 10);

    public static final String ITEM_ID = "trapar_wave_child_block";

    public TraparWaveChildBlock() {
        super(PROPS);
    }

}
