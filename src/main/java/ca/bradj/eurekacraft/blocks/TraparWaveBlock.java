package ca.bradj.eurekacraft.blocks;

<<<<<<< Updated upstream
import ca.bradj.eurekacraft.core.init.BlocksInit;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
=======
import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.TilesInit;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
>>>>>>> Stashed changes

public class TraparWaveBlock extends Block {

    public static Map<String, TileEntity> wavesNearPlayers = new HashMap();

    // TODO Stop casting shadows
    public static final Properties PROPS = AbstractBlock.Properties.
            copy(Blocks.GLASS).
            noCollission().
            requiresCorrectToolForDrops().
            strength(2.0F).
            isSuffocating(BlocksInit::never);

    public static final String ITEM_ID = "trapar_wave_block";
    public static final Item.Properties ITEM_PROPS = new Item.Properties().
            tab(ItemGroup.TAB_MISC);

    public TraparWaveBlock() {
        super(PROPS);
    }
<<<<<<< Updated upstream
=======

    // TODO: Make trapar wave a single block with "render properties"
    // So we can add a bunch of single blocks to the world (low cost) and each
    // one can have a tile entity which determines visibility to the player
    // We can even have the shapes change over time or move around


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
        private final String id;
        Logger logger = LogManager.getLogger(EurekaCraft.MODID);

        public TileEntity(TileEntityType<?> typeIn) {
            super(typeIn);
            logger.debug("TileEntity");
            this.id = UUID.randomUUID().toString();
        }

        public TileEntity() {
            this(TilesInit.TRAPAR_WAVE.get());
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public void tick() {
            if (this.level.isClientSide()) {
                for (PlayerEntity p : this.level.players()) {
                    Vector3d playerPos = p.getPosition(0);
                    double distanceTo = playerPos.distanceTo(new Vector3d(
                            this.getBlockPos().getX(),
                            this.getBlockPos().getY(),
                            this.getBlockPos().getZ()
                    ));
                    if (distanceTo < 10) {
//                        logger.debug("Nearby player " + p.getId() + " @ " + playerPos + " " + this);
                        wavesNearPlayers.put(this.id, this);
                    } else {
                        wavesNearPlayers.remove(this.id);
                    }
                }
            }
        }

        @Override
        public void onLoad() {
            super.onLoad();
//            logger.debug("load");
//            Random r = new Random();
//            BlockPos p = this.getBlockPos();
//            for (Direction dir : Direction.values()) {
//                if (this.level.getBlockState(p.relative(dir)).isAir()) {
//                    logger.debug("Filling air at " + p.relative(dir));
////                    this.level.setBlock(p.relative(dir), BlocksInit.TRAPAR_WAVE_CHILD_BLOCK.get().defaultBlockState(), 1);
//                }
//            }
        }
    }
>>>>>>> Stashed changes
}
