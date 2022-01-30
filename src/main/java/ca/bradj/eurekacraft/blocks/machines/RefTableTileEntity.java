package ca.bradj.eurekacraft.blocks.machines;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.container.RefTableContainer;
import ca.bradj.eurekacraft.core.init.RecipesInit;
import ca.bradj.eurekacraft.core.init.TilesInit;
import ca.bradj.eurekacraft.data.recipes.GlideBoardRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Stack;

public class RefTableTileEntity extends TileEntity implements INamedContainerProvider, ITickableTileEntity {
    private static final Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    public static final String ENTITY_ID = "ref_table_tile_entity";

    protected final Stack<ItemStack> toOutput = new Stack<>();

    private static int inputSlots = 6;
    private static int outputSlot = 6;
    private static int totalSlots = 7;
    private final RefTableItemHandler itemHandler = new RefTableItemHandler(this);
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    public RefTableTileEntity(TileEntityType<?> typeIn) {
        super(typeIn);
    }

    public RefTableTileEntity() {
        this(TilesInit.REF_TABLE.get());
    }


    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container." + EurekaCraft.MODID + ".ref_table");
    }


    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory player, PlayerEntity playerEntity) {
        return new RefTableContainer(id, player, this);
    }

    @Override
    public void load(BlockState blockState, CompoundNBT nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
        super.load(blockState, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.put("inv", itemHandler.serializeNBT());
        return super.save(nbt);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return this.handler.cast();
        }
        return super.getCapability(cap, side);
    }

    // Crafting
    @Override
    public void tick() {
        if (level.isClientSide) {
            return;
        }
        if (!this.toOutput.isEmpty()) {
            this.toOutput.pop();
            this.setChanged();
        }
    }

    public static class RefTableItemHandler extends ItemStackHandler {

        private final RefTableTileEntity owner;

        public RefTableItemHandler(RefTableTileEntity owner) {
            super(totalSlots);
            this.owner = owner;
        }


        public void craft() {
            World level = this.owner.getLevel();
            if (level.isClientSide()) {
                return;
            }

            if (!super.getStackInSlot(outputSlot).isEmpty()) {
                return;
            }

            Inventory inv = new Inventory(inputSlots);
            for (int i = 0; i < inputSlots; i++) {
                inv.setItem(i, super.getStackInSlot(i));
            }

            RecipeManager recipeManager = level.getRecipeManager();
            Optional<GlideBoardRecipe> recipe = recipeManager.getRecipeFor(
                    RecipesInit.GLIDE_BOARD, inv, level
            );

            if (recipe.isPresent()) {
                logger.debug("recipe match!");
                ItemStack output = recipe.get().getResultItem();

                super.insertItem(outputSlot, output, false);

                logger.debug("queueing to insert" + output);
                owner.toOutput.add(null);
            } else {
                ItemStack output = super.getStackInSlot(outputSlot);
                super.extractItem(outputSlot, output.getCount(), false);

                logger.debug("queueing to clear");
                owner.toOutput.add(null);
            }

        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            ItemStack output = super.insertItem(slot, stack, simulate);
            craft();
            return output;
        }

        @Nonnull
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot == outputSlot) {
                for (int i = 0; i < inputSlots; i++) {
                    super.extractItem(i, amount, false);
                }
            }

            craft();

            return super.extractItem(slot, amount, simulate);
        }

    }
}
