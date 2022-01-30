package ca.bradj.eurekacraft.blocks.machines;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.TilesInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class RefTableBlock extends Block {

    private Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    public static final String ITEM_ID = "ref_table_block";
    public static final Item.Properties ITEM_PROPS = new Item.Properties().
            tab(ItemGroup.TAB_MISC);;

    public RefTableBlock() {
        super(Properties.copy(Blocks.COAL_ORE));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TilesInit.REF_TABLE.get().create();
    }

    @Override
    public ActionResultType use(
            BlockState blockState, World world, BlockPos blockpos, PlayerEntity player,
            Hand hand, BlockRayTraceResult rayTraceResult
    ) {

        this.showUI(world, blockpos, player);
        return super.use(blockState, world, blockpos, player, hand, rayTraceResult);
    }

    private void showUI(World world, BlockPos blockpos, PlayerEntity player) {
        if (world.isClientSide()) {
            return;
        }

        TileEntity te = world.getBlockEntity(blockpos);
        if (!(te instanceof RefTableTileEntity)) {
            return;
        }

        NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) te, blockpos);
    }
}
