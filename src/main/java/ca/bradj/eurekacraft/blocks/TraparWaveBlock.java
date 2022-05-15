package ca.bradj.eurekacraft.blocks;


import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.TilesInit;
import ca.bradj.eurekacraft.render.TraparWaveShapes;
import ca.bradj.eurekacraft.entity.board.EntityRefBoard;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class TraparWaveBlock extends Block {

    public static final Properties PROPS = BlockBehaviour.Properties.
            copy(Blocks.AIR);

    public static final String ITEM_ID = "trapar_wave_block";
    public static final Item.Properties ITEM_PROPS = new Item.Properties().
            tab(CreativeModeTab.TAB_MISC);

    public TraparWaveBlock() {
        super(PROPS);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_149645_1_) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        return TilesInit.TRAPAR_WAVE.get().create();
    }

    public static class TileEntity extends BlockEntity {
        public static final String ID = "trapar_wave_tile_entity";
        Logger logger = LogManager.getLogger(EurekaCraft.MODID);
        private TraparWaveShapes shape;

        public TileEntity(BlockEntityType<?> typeIn) {
            super(typeIn);
        }

        public TileEntity() {
            this(TilesInit.TRAPAR_WAVE.get());
        }

        @Override
        public void tick() {
            if (this.level.isClientSide()) {
                return;
            }
            for (Player p : this.level.players()) {
                if (this.shape.isInAffectedRange(p.blockPosition())) {
                    EntityRefBoard.boostPlayer(this.level, p.getId());
                }
            }
        }

        @Override
        public void onLoad() {
            super.onLoad();
            Direction dir = Direction.EAST; // TODO: Random direction
            this.shape = TraparWaveShapes.SHAPE_1.WithCenterAndDirection(this.getBlockPos(), dir);
        }

        public TraparWaveShapes getShape() {
            return this.shape;
        }

        @Override
        public BlockPos getPos() {
            return null;
        }
    }
}
