package ca.bradj.eurekacraft.blocks.machines;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.container.RefTableContainer;
import ca.bradj.eurekacraft.core.init.RecipesInit;
import ca.bradj.eurekacraft.core.init.TilesInit;
import ca.bradj.eurekacraft.core.init.items.ItemsInit;
import ca.bradj.eurekacraft.core.init.items.WheelItemsInit;
import ca.bradj.eurekacraft.data.recipes.RefTableRecipe;
import ca.bradj.eurekacraft.interfaces.IPaintable;
import ca.bradj.eurekacraft.interfaces.ITechAffected;
import ca.bradj.eurekacraft.interfaces.IWrenchable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;

public class RefTableTileEntity extends EurekaCraftMachineEntity implements MenuProvider {
    private final Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    public static final String ENTITY_ID = "ref_table_tile_entity";

    private boolean cooking = false;
    private int craftPercent = 0;
    private int fireRemaining = 0;
    private int lastFireAmount = 1;


    public RefTableTileEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(TilesInit.REF_TABLE.get(), p_155229_, p_155230_, RefTableConsts.totalSlots);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container." + EurekaCraft.MODID + ".ref_table");
    }


    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player, Player p_39956_) {
        return new RefTableContainer(id, player, this);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.craftPercent = nbt.getInt("cooked");
    }

    @Override
    protected ItemStack getSelfAsItemStack() {
        return ItemsInit.REF_TABLE_BLOCK.get().getDefaultInstance();
    }

    protected CompoundTag store(CompoundTag tag) {
        tag = super.store(tag);
        tag.putInt("cooked", this.craftPercent);
        return tag;
    }

    // Crafting
    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, RefTableTileEntity entity) {
        if (level.isClientSide) {
            throw new IllegalStateException("Ticker should not be instantiated on client side");
        }

//        logger.debug("item in tech slot [" + techSlot + "] " + this.itemHandler.getStackInSlot(techSlot));
//        logger.debug("item in fuel slot [" + fuelSlot + "] " + this.itemHandler.getStackInSlot(fuelSlot));
//        logger.debug("item in output slot [" + outputSlot + "] " + this.itemHandler.getStackInSlot(outputSlot));

        if (entity.fireRemaining > 0) {
            entity.fireRemaining--;
        }

        Optional<RefTableRecipe> activeRecipe = entity.getActiveRecipe();
        entity.updateCookingStatus(activeRecipe);
        if (entity.cooking) {
            entity.doCook(activeRecipe, level);
        }
    }

    private void updateCookingStatus(Optional<RefTableRecipe> active) {
        if (active.isPresent()) {

            ItemStack outSlot = getStackInSlot(RefTableConsts.outputSlot);
            if (!outSlot.isEmpty()) {
                if (!outSlot.getItem().getDefaultInstance().sameItemStackIgnoreDurability(active.get().getResultItem())) {
                    return;
                }
                if (!active.get().getResultItem().isStackable()) {
                    return;
                }
            }

            if (active.get().requiresCooking()) {
                if (!this.hasFuel()) {
                    if (!this.hasCoal()) {
                        this.cooking = false;
                        this.craftPercent = 0;
                        return;
                    }
                    extractItem(RefTableConsts.fuelSlot, 1);
                    this.fireRemaining = Items.COAL.getBurnTime(Items.COAL.getDefaultInstance(), RecipeType.SMELTING);
                    if (this.fireRemaining < 0) {
                        this.fireRemaining = 500;
                    }
                    this.lastFireAmount = this.fireRemaining;
                    this.cooking = true;
                    return;
                }
            }
            if (this.cooking) {
                return;
            }
            this.cooking = true;
            this.craftPercent = 0;
        } else {
            this.cooking = false;
            this.craftPercent = 0;
        }
    }

    private boolean hasFuel() {
        return this.fireRemaining > 0;
    }

    private boolean hasCoal() {
        return this.getStackInSlot(RefTableConsts.fuelSlot).
                sameItemStackIgnoreDurability(
                        Items.COAL.getDefaultInstance()
                );
    }

    private void doCook(Optional<RefTableRecipe> recipe, Level level) {
        if (craftPercent < 100) {
            this.craftPercent++;
            this.makeCraftingNoise(recipe);
            return;
        }
        this.cooking = false;
        this.craftPercent = 0;

        recipe.ifPresent(iRecipe -> {
            ItemStack output = iRecipe.getResultItem();

            if (new Random().nextFloat() < iRecipe.getSecondaryResultItem().chance) {
                ItemStack sOutput = iRecipe.getSecondaryResultItem().output.copy();
                if (sOutput.sameItemStackIgnoreDurability(WheelItemsInit.WHEEL_PLACEHOLDER_ITEM.get().getDefaultInstance())) {
                    EurekaCraft.LOGGER.debug("Not outputting placeholder secondary");
                } else {
                    insertItem(RefTableConsts.secondaryOutputSlot, sOutput);
                }
            }

            Collection<ItemStack> inputs = new ArrayList<>();
            for (int i = 0; i < RefTableConsts.inputSlots; i++) {
                ItemStack stackInSlot = getStackInSlot(i);
                if (stackInSlot.isEmpty()) {
                    continue;
                }
                inputs.add(stackInSlot);
            }

            for (int i = 0; i < RefTableConsts.inputSlots; i++) {
                extractItem(i, 1);
            }

            if (!iRecipe.getExtraIngredient().ingredient.isEmpty()) {
                useExtraIngredient(iRecipe, inputs, output, level);
            }

            insertItem(RefTableConsts.outputSlot, output.copy());

            setChanged();
        });
    }

    private void useExtraIngredient(
            RefTableRecipe iRecipe, Collection<ItemStack> inputs, ItemStack craftedOutput, Level level
    ) {
        ItemStack techStack = getStackInSlot(RefTableConsts.techSlot);
        techStack.hurt(1, new Random(), null);
        if (iRecipe.getExtraIngredient().consumeOnUse) {
            extractItem(RefTableConsts.techSlot, 1);
        } else if (techStack.getDamageValue() > techStack.getMaxDamage()) {
            level.playSound(null, this.getBlockPos(), SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
            extractItem(RefTableConsts.techSlot, 1);
        }

        if (iRecipe.getResultItem().getItem() instanceof ITechAffected) {
            ((ITechAffected) iRecipe.getResultItem().getItem()).applyTechItem(inputs, techStack, craftedOutput, level.getRandom());
        }

        if (iRecipe.getResultItem().getItem() instanceof IPaintable) {
            ((IPaintable) iRecipe.getResultItem().getItem()).applyPaint(inputs, techStack, craftedOutput);
        }

        if (getStackInSlot(RefTableConsts.techSlot).sameItem(WheelItemsInit.SOCKET_WRENCH.get().getDefaultInstance())) {
            if (iRecipe.getResultItem().getItem() instanceof IWrenchable) {
                Optional<ItemStack> removedPart = ((IWrenchable) iRecipe.getResultItem().getItem()).applyWrench(inputs, craftedOutput);
                if (removedPart.isPresent()) {
                    if (!getStackInSlot(RefTableConsts.secondaryOutputSlot).isEmpty()) {
                        throw new IllegalStateException("Expected output slot to be empty for part removal recipe");
                    }
                    insertItem(RefTableConsts.secondaryOutputSlot, removedPart.get());
                }
            }
        }
    }

    private Optional<RefTableRecipe> getActiveRecipe() {
        Optional<RefTableRecipe> recipe = getActivePrimaryRecipe();
        if (recipe.isPresent()) {
            RefTableRecipe.ExtraInput extra = recipe.get().getExtraIngredient();
            RefTableRecipe.Secondary secondary = recipe.get().getSecondaryResultItem();
            if (!extra.ingredient.isEmpty()) {
                ItemStack techItem = getStackInSlot(RefTableConsts.techSlot);
                if (!extra.ingredient.test(techItem)) {
                    return Optional.empty();
                }
                if (invalidSocketRecipe(techItem)) {
                    return Optional.empty();
                }
            }
            if (!secondary.output.isEmpty()) {
                ItemStack secondarySlotStack = getStackInSlot(RefTableConsts.secondaryOutputSlot);
                boolean isSameSecondary = secondarySlotStack.sameItemStackIgnoreDurability(secondary.output);
                boolean canFitSecondary = secondarySlotStack.getCount() < secondary.output.getMaxStackSize();
                boolean canDeposit = secondarySlotStack.isEmpty() || (isSameSecondary && canFitSecondary);
                if (!canDeposit) {
                    return Optional.empty();
                }
            }
        }
        return recipe;
    }

    private boolean invalidSocketRecipe(ItemStack techItem) {
        Collection<ItemStack> inputs = new ArrayList<>();
        for (int i = 0; i < RefTableConsts.inputSlots; i++) {
            ItemStack stackInSlot = getStackInSlot(i);
            if (stackInSlot.isEmpty()) {
                continue;
            }
            inputs.add(stackInSlot);
        }

        if (!techItem.sameItemStackIgnoreDurability(WheelItemsInit.SOCKET_WRENCH.get().getDefaultInstance())) {
            return false;
        }
        for (Item i : inputs.stream().map(ItemStack::getItem).toList()) {
            if (i instanceof IWrenchable) {
                if (((IWrenchable) i).canApplyWrench(inputs, techItem)) {
                    return false;
                }
            }
        }
        return true;
    }

    private Optional<RefTableRecipe> getActivePrimaryRecipe() {
        // Shaped
        Container inv = new SimpleContainer(RefTableConsts.inputSlots + 1);
        List<ItemStack> shapeless = new ArrayList<ItemStack>();
        for (int i = 0; i < RefTableConsts.inputSlots; i++) {
            ItemStack stackInSlot = getStackInSlot(i);
            inv.setItem(i, stackInSlot);
            if (!stackInSlot.isEmpty()) {
                shapeless.add(stackInSlot);
            }
        }
        ItemStack techItem = getStackInSlot(RefTableConsts.techSlot);
        inv.setItem(RefTableConsts.inputSlots, techItem);
        shapeless.add(techItem);

        RecipeManager recipeManager = level.getRecipeManager();
        Optional<RefTableRecipe> recipe = recipeManager.getRecipeFor(
                RecipesInit.REF_TABLE, inv, level
        );

        if (recipe.isPresent()) {
            return recipe;
        }

        // Shapeless
        // TODO: Reduce duplication with above
        inv = new SimpleContainer(shapeless.size());
        for (int i = 0; i < shapeless.size(); i++) {
            ItemStack stackInSlot = shapeless.get(i);
            inv.setItem(i, stackInSlot);
        }

        recipe = recipeManager.getRecipeFor(
                RecipesInit.REF_TABLE, inv, level
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

    public int getFireRemaining() {
        return this.fireRemaining;
    }

    public void setFireRemaining(int i) {
        this.fireRemaining = i;
    }

    public int getFireTotal() {
        return this.lastFireAmount;
    }

    public void setFireTotal(int i) {
        this.lastFireAmount = i;
    }

    @Override
    protected ItemStack getItemForCraftingNoise() {
        return getStackInSlot(RefTableConsts.techSlot);
    }
}
