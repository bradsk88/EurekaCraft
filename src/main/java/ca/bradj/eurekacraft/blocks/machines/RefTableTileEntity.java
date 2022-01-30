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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class RefTableTileEntity extends TileEntity implements INamedContainerProvider, ITickableTileEntity {
    private final Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    public static final String ENTITY_ID = "ref_table_tile_entity";

    private boolean cooking = false;
    private int cookPercent = 0;

    private static int inputSlots = 6;
    private static int outputSlot = 6;
    private static int totalSlots = 7;
    private final ItemStackHandler itemHandler = createHandler();
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
        this.cookPercent = nbt.getInt("cooked");
        super.load(blockState, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.put("inv", itemHandler.serializeNBT());
        nbt.putInt("cooked", this.cookPercent);
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

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(totalSlots);
    }

    // Crafting
    @Override
    public void tick() {
        if (level.isClientSide) {
            return;
        }
        cookCheckTick();
        if (this.cooking) {
            logger.debug("Cook % " + this.cookPercent); // TODO: Show in UI
            this.doCook();
        }
    }

    private void cookCheckTick() {
        if (this.isRecipeActive() && this.hasCoal() && !this.cooking) {
            this.cooking = true;
            this.cookPercent = 0;
            return;
        }
        if (!this.hasCoal() || !this.isRecipeActive()) {
            this.cooking = false;
            this.cookPercent = 0;
        }
    }

    private boolean hasCoal() { // TODO
        return true;
    }

    private void doCook() {
        if (cookPercent < 100) {
            this.cookPercent++;
            return;
        }
        this.cooking = false;
        this.cookPercent = 0;
        Optional<GlideBoardRecipe> recipe = getActiveRecipe();

        recipe.ifPresent(iRecipe -> {
            logger.debug("recipe match!");
            ItemStack output = iRecipe.getResultItem();

            for (int i = 0; i < inputSlots; i++) {
                itemHandler.extractItem(i, 1, false);
            }

            logger.debug("inserting " + output);
            itemHandler.insertItem(outputSlot, output, false);

            setChanged();
        });
    }

    private boolean isRecipeActive() {
        Optional<GlideBoardRecipe> recipe = getActiveRecipe();
        return recipe.isPresent();
    }

    private Optional<GlideBoardRecipe> getActiveRecipe() {
        Inventory inv = new Inventory(inputSlots);
        for (int i = 0; i < inputSlots; i++) {
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }

        Optional<GlideBoardRecipe> recipe = level.getRecipeManager().getRecipeFor(
                RecipesInit.GLIDE_BOARD, inv, level
        );
        return recipe;
    }
}
