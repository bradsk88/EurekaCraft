package ca.bradj.eurekacraft.blocks.machines;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.core.init.TilesInit;
import ca.bradj.eurekacraft.wrappers.EntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RefTableBlock extends EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty ANCIENT = BooleanProperty.create("ancient");
    public static final IntegerProperty SPAWNED_WITH_RECIPE = IntegerProperty.create(
            RefTableConsts.NBT_SPAWNED_WITH_RECIPE, 0, RefTableConsts.spawnRecipes.size()
    );

    private Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    public static final String ITEM_ID = "ref_table_block";
    public static final Item.Properties ITEM_PROPS = new Item.Properties().
            tab(ModItemGroup.EUREKACRAFT_GROUP);
    private RefTableTileEntity entity;

    public RefTableBlock() {
        super(
                BlockBehaviour.Properties.
                        of(Material.WOOD).
                        noOcclusion().
                        strength(1f)
        );
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        ItemStack itemInHand = ctx.getItemInHand();
        boolean ancient = false;
        CompoundTag tag = itemInHand.getOrCreateTag();
        if (tag.contains("ancient")) {
            ancient = tag.getBoolean("ancient");
        }
        int spawnRecipeIndex = 0;
        if (tag.contains(RefTableConsts.NBT_SPAWNED_WITH_RECIPE)) {
            spawnRecipeIndex = tag.getInt(RefTableConsts.NBT_SPAWNED_WITH_RECIPE);
        }
        return this.defaultBlockState().
                setValue(FACING, ctx.getHorizontalDirection().getOpposite()).
                setValue(ANCIENT, ancient).
                setValue(SPAWNED_WITH_RECIPE, spawnRecipeIndex);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(ANCIENT).add(SPAWNED_WITH_RECIPE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        this.entity = TilesInit.REF_TABLE.get().create(pos, state);
        return this.entity;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(
                type, TilesInit.REF_TABLE.get(), RefTableTileEntity::tick
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
        if (!(te instanceof RefTableTileEntity)) {
            this.logger.debug("not RefTableTileEntity " + te);
            return;
        }

        NetworkHooks.openGui((ServerPlayer) player, (MenuProvider) te, blockpos);
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_60537_, LootContext.Builder p_60538_) {
        return this.entity.getItemsStacksForDrop(p_60538_.getLevel().getRandom());
    }
}
