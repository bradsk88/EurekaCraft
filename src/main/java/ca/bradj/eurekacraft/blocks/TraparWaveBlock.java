package ca.bradj.eurekacraft.blocks;


import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.TilesInit;
import ca.bradj.eurekacraft.render.TraparWaveShapes;
import ca.bradj.eurekacraft.vehicles.EntityRefBoard;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class TraparWaveBlock extends Block {

    public static final Properties PROPS = AbstractBlock.Properties.
            copy(Blocks.AIR);

    public static final String ITEM_ID = "trapar_wave_block";
    public static final Item.Properties ITEM_PROPS = new Item.Properties().
            tab(ItemGroup.TAB_MISC);

    public TraparWaveBlock() {
        super(PROPS);
    }

    @Override
    public BlockRenderType getRenderShape(BlockState p_149645_1_) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public net.minecraft.tileentity.TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TilesInit.TRAPAR_WAVE.get().create();
    }

    public static class TileEntity extends net.minecraft.tileentity.TileEntity implements ITickableTileEntity {
        public static final String ID = "trapar_wave_tile_entity";
        Logger logger = LogManager.getLogger(EurekaCraft.MODID);
        private TraparWaveShapes shape;

        public TileEntity(TileEntityType<?> typeIn) {
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
            for (PlayerEntity p : this.level.players()) {
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
    }
}
