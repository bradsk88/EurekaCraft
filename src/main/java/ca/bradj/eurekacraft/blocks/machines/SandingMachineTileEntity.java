package ca.bradj.eurekacraft.blocks.machines;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.container.SandingMachineContainer;
import ca.bradj.eurekacraft.core.init.ItemsInit;
import ca.bradj.eurekacraft.core.init.RecipesInit;
import ca.bradj.eurekacraft.core.init.TagsInit;
import ca.bradj.eurekacraft.core.init.TilesInit;
import ca.bradj.eurekacraft.data.recipes.SandingMachineRecipe;
import ca.bradj.eurekacraft.materials.NoisyCraftingItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
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

public class SandingMachineTileEntity extends TileEntity implements INamedContainerProvider, ITickableTileEntity {
    private final Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    public static final String ENTITY_ID = "sanding_machine_tile_entity";

    private boolean sanding = false;
    private int sandPercent = 0;

    private static int inputSlots = 1;
    private static int abrasiveSlot = inputSlots;
    private static int outputSlot = abrasiveSlot + 1;
    private static int totalSlots = outputSlot + 1;
    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    private int noiseCooldown = 0;

    public SandingMachineTileEntity(TileEntityType<?> typeIn) {
        super(typeIn);
    }

    public SandingMachineTileEntity() {
        this(TilesInit.SANDING_MACHINE.get());
    }


    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container." + EurekaCraft.MODID + ".sanding_machine");
    }


    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory player, PlayerEntity playerEntity) {
        return new SandingMachineContainer(id, player, this);
    }

    @Override
    public void load(BlockState blockState, CompoundNBT nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
        this.sandPercent = nbt.getInt("cooked");
        super.load(blockState, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.put("inv", itemHandler.serializeNBT());
        nbt.putInt("cooked", this.sandPercent);
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

        Optional<SandingMachineRecipe> activeRecipe = this.getActiveRecipe();
        updateCookingStatus(activeRecipe);
        if (this.sanding) {
            logger.debug("Cook % " + this.sandPercent); // TODO: Show in UI
            this.doCook(activeRecipe);
        }
    }

    private void updateCookingStatus(Optional<SandingMachineRecipe> active) {
        if (active.isPresent()) {
            ItemStack outSlot = this.itemHandler.getStackInSlot(outputSlot);
            if (!outSlot.isEmpty()) {
                if (!outSlot.getItem().getDefaultInstance().sameItemStackIgnoreDurability(active.get().getResultItem())) {
                    return;
                }
            }

            if (!this.hasSandpaper()) {
                this.sanding = false;
                this.sandPercent = 0;
                return;
            }

            if (this.sanding) {
                return;
            }
            this.sanding = true;
        } else {
            this.sanding = false;
        }
        this.sandPercent = 0;
    }

    private boolean hasSandpaper() {
        Ingredient.TagList tags = new Ingredient.TagList(TagsInit.Items.SANDING_DISCS);
        ItemStack abrasive = this.itemHandler.getStackInSlot(abrasiveSlot);
        return tags.getItems().stream().anyMatch(i -> i.sameItemStackIgnoreDurability(abrasive));

    }

    private void doCook(Optional<SandingMachineRecipe> recipe) {
        if (sandPercent < 100) {
            this.sandPercent++;
            this.makeCraftingNoise(recipe);
            return;
        }
        this.sanding = false;
        this.sandPercent = 0;

        recipe.ifPresent(iRecipe -> {
            ItemStack output = iRecipe.getResultItem();

            for (int i = 0; i < inputSlots; i++) {
                itemHandler.extractItem(i, 1, false);
            }

            useExtraIngredient();

            itemHandler.insertItem(outputSlot, output, false);

            setChanged();
        });
    }

    private void makeCraftingNoise(Optional<SandingMachineRecipe> recipe) {
        assert this.level != null;

        if (this.noiseCooldown > 0) {
            this.noiseCooldown--;
            return;
        }

        recipe.ifPresent((r) -> {
            ItemStack stackInSlot = itemHandler.getStackInSlot(abrasiveSlot);
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

    private void useExtraIngredient() {
        ItemStack stackInSlot = itemHandler.getStackInSlot(abrasiveSlot);
        stackInSlot.hurt(1, new Random(), null);
        if (stackInSlot.getDamageValue() > stackInSlot.getMaxDamage()) {
            level.playSound(null, this.getBlockPos(), SoundEvents.ITEM_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
            this.itemHandler.extractItem(abrasiveSlot, 1, false);
        }
    }

    private Optional<SandingMachineRecipe> getActiveRecipe() {
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
        Optional<SandingMachineRecipe> recipe = recipeManager.getRecipeFor(
                RecipesInit.SANDING_MACHINE, inv, level
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
                RecipesInit.SANDING_MACHINE, inv, level
        );

        return recipe;
    }

    public int getCookingProgress() {
        return sandPercent;
    }

    public void setCookingProgress(int v) {
        // TODO: Needed?
        this.sandPercent = v;
    }

    public int getSlotCount() {
        return totalSlots;
    }
}
