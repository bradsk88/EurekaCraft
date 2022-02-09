package ca.bradj.eurekacraft.blocks.machines;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.container.RefTableContainer;
import ca.bradj.eurekacraft.core.init.RecipesInit;
import ca.bradj.eurekacraft.core.init.TilesInit;
import ca.bradj.eurekacraft.data.recipes.GlideBoardRecipe;
import ca.bradj.eurekacraft.materials.NoisyCraftingItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RefTableTileEntity extends TileEntity implements INamedContainerProvider, ITickableTileEntity {
    private final Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    public static final String ENTITY_ID = "ref_table_tile_entity";

    private boolean cooking = false;
    private int craftPercent = 0;

    private static int inputSlots = 6;
    private static int fuelSlot = inputSlots;
    private static int techSlot = fuelSlot + 1;
    private static int outputSlot = techSlot + 1;
    private static int totalSlots = outputSlot + 1;
    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    private int noiseCooldown = 0;

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
        this.craftPercent = nbt.getInt("cooked");
        super.load(blockState, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.put("inv", itemHandler.serializeNBT());
        nbt.putInt("cooked", this.craftPercent);
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

//        logger.debug("item in tech slot [" + techSlot + "] " + this.itemHandler.getStackInSlot(techSlot));
//        logger.debug("item in fuel slot [" + fuelSlot + "] " + this.itemHandler.getStackInSlot(fuelSlot));
//        logger.debug("item in output slot [" + outputSlot + "] " + this.itemHandler.getStackInSlot(outputSlot));


        Optional<GlideBoardRecipe> activeRecipe = this.getActiveRecipe();
        updateCookingStatus(activeRecipe);
        if (this.cooking) {
            logger.debug("Cook % " + this.craftPercent); // TODO: Show in UI
            this.doCook(activeRecipe);
        }
    }

    private void updateCookingStatus(Optional<GlideBoardRecipe> active) {
        if (active.isPresent()) {

            ItemStack outSlot = this.itemHandler.getStackInSlot(outputSlot);
            if (!outSlot.isEmpty()) {
                if (!outSlot.getItem().getDefaultInstance().sameItemStackIgnoreDurability(active.get().getResultItem())) {
                    return;
                }
            }

            if (active.get().requiresCooking()) {
                if (!this.hasCoal()) {
                    this.cooking = false;
                    this.craftPercent = 0;
                    return;
                }
            }
            if (this.cooking) {
                return;
            }
            this.cooking = true;
            this.craftPercent = 0;
            if (active.get().requiresCooking()) {
                // FIXME: Add "heat reserve" to table otherwise player must put at least 2 coal in table.
                this.itemHandler.extractItem(fuelSlot, 1, false);
            }
        } else {
            this.cooking = false;
            this.craftPercent = 0;
        }
    }

    private boolean hasCoal() {
        return this.itemHandler.getStackInSlot(fuelSlot).
                sameItemStackIgnoreDurability(
                        Items.COAL.getDefaultInstance()
                );
    }

    private void doCook(Optional<GlideBoardRecipe> recipe) {
        if (craftPercent < 100) {
            this.craftPercent++;
            this.makeCraftingNoise(recipe);
            return;
        }
        this.cooking = false;
        this.craftPercent = 0;

        recipe.ifPresent(iRecipe -> {
//            logger.debug("recipe match!");
            ItemStack output = iRecipe.getResultItem();

            if (new Random().nextFloat() < iRecipe.getSecondaryResultItem().chance) {
                // TODO Implement slot
                logger.debug("Would have produced an extra item if there was a slot for it: " + iRecipe.getSecondaryResultItem().output);
            }

            for (int i = 0; i < inputSlots; i++) {
                itemHandler.extractItem(i, 1, false);
            }

            if (!iRecipe.getExtraIngredient().ingredient.isEmpty()) {
                useExtraIngredient(iRecipe);
            }

            itemHandler.insertItem(outputSlot, output, false);

            setChanged();
        });
    }

    private void makeCraftingNoise(Optional<GlideBoardRecipe> recipe) {
        assert this.level != null;

        if (this.noiseCooldown > 0) {
            this.noiseCooldown--;
            return;
        }

        recipe.ifPresent((r) -> {
            if (r.getExtraIngredient().ingredient.isEmpty()) {
                return;
            }

            ItemStack stackInSlot = itemHandler.getStackInSlot(techSlot);
            Item item = stackInSlot.getItem();
            if (!(item instanceof NoisyCraftingItem)) {
                return;
            }

            ((NoisyCraftingItem) item).getCraftingSound().ifPresent((s) -> {
                float volume = 0.5f;
                float pitch = 0.5f;
                this.level.playSound(
                        null, this.getBlockPos(), s, SoundCategory.BLOCKS, volume, pitch
                );
                this.noiseCooldown = 8; // TODO: maybe add to the NoisyCraftingItem so each item can decide cooldown?
            });
        });

    }

    private void useExtraIngredient(GlideBoardRecipe iRecipe) {
        ItemStack stackInSlot = itemHandler.getStackInSlot(techSlot);
        stackInSlot.hurt(1, new Random(), null);
        if (iRecipe.getExtraIngredient().consumeOnUse) {
            this.itemHandler.extractItem(techSlot, 1, false);
        } else if (stackInSlot.getDamageValue() > stackInSlot.getMaxDamage()) {
            level.playSound(null, this.getBlockPos(), SoundEvents.ITEM_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
            this.itemHandler.extractItem(techSlot, 1, false);
        }
    }

    private Optional<GlideBoardRecipe> getActiveRecipe() {
        Optional<GlideBoardRecipe> recipe = getActivePrimaryRecipe();
        if (recipe.isPresent()) {
            GlideBoardRecipe.ExtraInput extra = recipe.get().getExtraIngredient();
            if (!extra.ingredient.isEmpty()) {
                ItemStack techItem = this.itemHandler.getStackInSlot(techSlot);
                if (!extra.ingredient.test(techItem)) {
                    return Optional.empty();
                }
            }
        }
        return recipe;
    }

    private Optional<GlideBoardRecipe> getActivePrimaryRecipe() {
        // Shaped
        Inventory inv = new Inventory(inputSlots);
        List<ItemStack> shapeless = new ArrayList<ItemStack>();
        for (int i = 0; i < inputSlots; i++) {
            ItemStack stackInSlot = itemHandler.getStackInSlot(i);
            inv.setItem(i, stackInSlot);
            if (!stackInSlot.isEmpty()) {
                shapeless.add(stackInSlot);
            }
        }

        RecipeManager recipeManager = level.getRecipeManager();
        Optional<GlideBoardRecipe> recipe = recipeManager.getRecipeFor(
                RecipesInit.GLIDE_BOARD, inv, level
        );

        if (recipe.isPresent()) {
            return recipe;
        }

        // Shapeless
        // TODO: Reduce duplication with above
        inv = new Inventory(shapeless.size());
        for (int i = 0; i < shapeless.size(); i++) {
            ItemStack stackInSlot = shapeless.get(i);
            inv.setItem(i, stackInSlot);
        }

        recipe = recipeManager.getRecipeFor(
                RecipesInit.GLIDE_BOARD, inv, level
        );

        return recipe;
    }

    public int getCookingProgress() {
        return craftPercent;
    }

    public void setCookingProgress(int v) {
        // TODO: Needed?
        this.craftPercent = v;
    }

    public int getTotalSlotCount() {
        return totalSlots;
    }
}
