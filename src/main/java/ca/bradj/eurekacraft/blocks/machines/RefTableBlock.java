package ca.bradj.eurekacraft.blocks.machines;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.core.init.TilesInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class RefTableBlock extends HorizontalDirectionalBlock {

    private Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    public static final String ITEM_ID = "ref_table_block";
    public static final Item.Properties ITEM_PROPS = new Item.Properties().
            tab(ModItemGroup.EUREKACRAFT_GROUP);

    public RefTableBlock() {
        super(
                BlockBehaviour.Properties.
                        of(Material.WOOD).
                        noOcclusion().
                        strength(3.5f)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public InteractionResult use(
            BlockState blockState, Level world, BlockPos blockpos, Player player,
            InteractionHand hand, BlockHitResult rayTraceResult
    ) {
        this.showUI(world, blockpos, player);
        return InteractionResult.CONSUME;
    }

    private void showUI(Level world, BlockPos blockpos, Player player) {
        if (world.isClientSide()) {
            return;
        }

        BlockEntity te = world.getBlockEntity(blockpos);
        if (!(te instanceof RefTableTileEntity)) {
            this.logger.debug("not RefTableTileEntity " + te);
            return;
        }

        NetworkHooks.openGui((ServerPlayer) player, (MenuProvider) te, blockpos);
    }
}
