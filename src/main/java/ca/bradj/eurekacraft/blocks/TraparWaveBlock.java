package ca.bradj.eurekacraft.blocks;


import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.core.init.TilesInit;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class TraparWaveBlock extends Block {

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
        Logger logger = LogManager.getLogger(EurekaCraft.MODID);
        public final HashMap<Vector3i, Integer> children = new HashMap<>();

        public TileEntity(TileEntityType<?> typeIn) {
            super(typeIn);
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
                        // TODO: Check for "children" collisions and provide lift
                    }
                }
            }
        }

        @Override
        public void onLoad() {
            super.onLoad();
            Random r = new Random();
            BlockPos p = this.getBlockPos();
            spread(p, new Vector3i(0, 0, 0));
        }

        private void spread(BlockPos p, Vector3i reference) {
            logger.debug("Spreading " + reference);
            if (Math.abs(reference.getY()) > 2) {
                return;
            }

            for (Direction dir : Direction.values()) {
                if (this.children.size() > 4) {
                    return;
                }
                // TODO: Stop doing random gen, just build some predefined shapes and choose those randomly
                Vector3i newReference = reference.relative(dir, 1);
                if (this.level.getBlockState(p.relative(dir)).isAir()) {
                    this.children.put(newReference, 100);
                }
                if (new Random().nextBoolean()) {
                    spread(p.relative(dir), newReference);
                }
            }
        }
    }
}
