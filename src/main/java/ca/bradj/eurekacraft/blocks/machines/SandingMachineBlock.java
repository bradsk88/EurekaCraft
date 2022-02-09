package ca.bradj.eurekacraft.blocks.machines;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.core.init.TilesInit;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
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

public class SandingMachineBlock extends Block {

    private Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    public static final String ITEM_ID = "sanding_machine_block";
    public static final Item.Properties ITEM_PROPS = new Item.Properties().
            tab(ModItemGroup.EUREKACRAFT_GROUP);
    ;

    public SandingMachineBlock() {
        super(
                AbstractBlock.Properties.
                        of(Material.WOOD).
                        harvestLevel(-1).
                        strength(3.5f)
        );
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TilesInit.SANDING_MACHINE.get().create();
    }

    @Override
    public ActionResultType use(
            BlockState blockState, World world, BlockPos blockpos, PlayerEntity player,
            Hand hand, BlockRayTraceResult rayTraceResult
    ) {
        this.showUI(world, blockpos, player);
        return ActionResultType.CONSUME;
    }

    private void showUI(World world, BlockPos blockpos, PlayerEntity player) {
        if (world.isClientSide()) {
            return;
        }

        TileEntity te = world.getBlockEntity(blockpos);
        if (!(te instanceof SandingMachineTileEntity)) {
            this.logger.debug("not SandingMachineTileEntity " + te);
            return;
        }

        NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) te, blockpos);
    }
}
