package ca.bradj.eurekacraft.blocks.machines;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.core.init.TilesInit;
import ca.bradj.eurekacraft.wrappers.EntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SandingMachineBlock extends EntityBlock {

    private Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    public static final String ITEM_ID = "sanding_machine_block";
    public static final Item.Properties ITEM_PROPS = new Item.Properties().
            tab(ModItemGroup.EUREKACRAFT_GROUP);
    private SandingMachineTileEntity entity;

    public SandingMachineBlock() {
        super(
                BlockBehaviour.Properties.
                        of(Material.WOOD).
                        strength(1f)
        );
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        this.entity = TilesInit.SANDING_MACHINE.get().create(p_153215_, p_153216_);
        return this.entity;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(
                type, TilesInit.SANDING_MACHINE.get(), SandingMachineTileEntity::tick
        );
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
        if (!(te instanceof SandingMachineTileEntity)) {
            this.logger.debug("not SandingMachineTileEntity " + te);
            return;
        }

        NetworkHooks.openScreen((ServerPlayer) player, (MenuProvider) te, blockpos);
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_60537_, LootContext.Builder p_60538_) {
        return this.entity.getItemsStacksForDrop();
    }
}
