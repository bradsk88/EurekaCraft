package ca.bradj.eurekacraft.wrappers;

import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class EntityBlock extends BaseEntityBlock {

    protected EntityBlock(Properties props) {
        super(props);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState bs) {
        return RenderShape.MODEL;
    }
}
