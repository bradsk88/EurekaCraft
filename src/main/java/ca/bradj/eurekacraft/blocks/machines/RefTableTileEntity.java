package ca.bradj.eurekacraft.blocks.machines;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.container.RefTableContainer;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.core.init.RecipesInit;
import ca.bradj.eurekacraft.core.init.TilesInit;
import ca.bradj.eurekacraft.data.recipes.GlideBoardRecipe;
import ca.bradj.eurekacraft.interfaces.ITechAffected;
import ca.bradj.eurekacraft.materials.NoisyCraftingItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class RefTableTileEntity extends BlockEntity implements MenuProvider {
    private final Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    public static final String ENTITY_ID = "ref_table_tile_entity";

    private boolean cooking = false;
    private int craftPercent = 0;
    private int fireRemaining = 0;
    private int lastFireAmount = 1;

    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    private int noiseCooldown = 0;

    public RefTableTileEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(TilesInit.REF_TABLE.get(), p_155229_, p_155230_);
    }


//    public RefTableTileEntity() {
//        this(TilesInit.REF_TABLE.get());
//    }


    @Override
    public Component getDisplayName() {
        return new TextComponent("container." + EurekaCraft.MODID + ".ref_table");
    }


    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player, Player p_39956_) {
        return new RefTableContainer(id, player, this);
    }

    @Override
    public void load(CompoundTag nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
        this.craftPercent = nbt.getInt("cooked");
        super.load(nbt);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inv", itemHandler.serializeNBT());
        nbt.putInt("cooked", this.craftPercent);
        super.saveAdditional(nbt);
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
        return new ItemStackHandler(RefTableConsts.totalSlots) {
            @Override
            protected void validateSlotIndex(int slot) {
                super.validateSlotIndex(slot); // TODO: Be more forgiving in case number of slots changes?
            }
        };
    }


    // TODO: Reimplement
//
//    // Crafting
//    @Override
//    public void tick() {
//        if (level.isClientSide) {
//            return;
//        }
//
////        logger.debug("item in tech slot [" + techSlot + "] " + this.itemHandler.getStackInSlot(techSlot));
////        logger.debug("item in fuel slot [" + fuelSlot + "] " + this.itemHandler.getStackInSlot(fuelSlot));
////        logger.debug("item in output slot [" + outputSlot + "] " + this.itemHandler.getStackInSlot(outputSlot));
//
//        if (fireRemaining > 0) {
//            this.fireRemaining--;
//        }
//
//        Optional<GlideBoardRecipe> activeRecipe = this.getActiveRecipe();
//        updateCookingStatus(activeRecipe);
//        if (this.cooking) {
//            this.doCook(activeRecipe, level);
//        }
//    }

    private void updateCookingStatus(Optional<GlideBoardRecipe> active) {
        if (active.isPresent()) {

            ItemStack outSlot = this.itemHandler.getStackInSlot(RefTableConsts.outputSlot);
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
                    this.itemHandler.extractItem(RefTableConsts.fuelSlot, 1, false);
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
        return this.itemHandler.getStackInSlot(RefTableConsts.fuelSlot).
                sameItemStackIgnoreDurability(
                        Items.COAL.getDefaultInstance()
                );
    }

    private void doCook(Optional<GlideBoardRecipe> recipe, Level level) {
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
                ItemStack sOutput = iRecipe.getSecondaryResultItem().output.copy();
                itemHandler.insertItem(RefTableConsts.secondaryOutputSlot, sOutput, false);
            }

            Collection<ItemStack> inputs = new ArrayList<>();
            for (int i = 0; i < RefTableConsts.inputSlots; i++) {
                ItemStack stackInSlot = itemHandler.getStackInSlot(i);
                if (stackInSlot.isEmpty()) {
                    continue;
                }
                inputs.add(stackInSlot);
            }

            for (int i = 0; i < RefTableConsts.inputSlots; i++) {
                itemHandler.extractItem(i, 1, false);
            }

            if (!iRecipe.getExtraIngredient().ingredient.isEmpty()) {
                useExtraIngredient(iRecipe, inputs, output, level);
            }

            itemHandler.insertItem(RefTableConsts.outputSlot, output.copy(), false);

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

            ItemStack stackInSlot = itemHandler.getStackInSlot(RefTableConsts.techSlot);
            Item item = stackInSlot.getItem();
            if (!(item instanceof NoisyCraftingItem)) {
                return;
            }

            ((NoisyCraftingItem) item).getCraftingSound().ifPresent((s) -> {
                float volume = 0.5f;
                float pitch = 0.5f;
                this.level.playSound(
                        null, this.getBlockPos(), s.event, SoundSource.BLOCKS, volume, pitch
                );
                this.noiseCooldown = s.noiseCooldown;
            });
        });

    }

    private void useExtraIngredient(
            GlideBoardRecipe iRecipe, Collection<ItemStack> inputs, ItemStack craftedOutput, Level level
    ) {
        ItemStack techStack = itemHandler.getStackInSlot(RefTableConsts.techSlot);
        techStack.hurt(1, new Random(), null);
        if (iRecipe.getExtraIngredient().consumeOnUse) {
            this.itemHandler.extractItem(RefTableConsts.techSlot, 1, false);
        } else if (techStack.getDamageValue() > techStack.getMaxDamage()) {
            level.playSound(null, this.getBlockPos(), SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
            this.itemHandler.extractItem(RefTableConsts.techSlot, 1, false);
        }

        if (iRecipe.getResultItem().getItem() instanceof ITechAffected) {
            ((ITechAffected) iRecipe.getResultItem().getItem()).applyTechItem(inputs, techStack, craftedOutput, level.getRandom());
        }
    }

    private Optional<GlideBoardRecipe> getActiveRecipe() {
        Optional<GlideBoardRecipe> recipe = getActivePrimaryRecipe();
        if (recipe.isPresent()) {
            GlideBoardRecipe.ExtraInput extra = recipe.get().getExtraIngredient();
            if (!extra.ingredient.isEmpty()) {
                ItemStack techItem = this.itemHandler.getStackInSlot(RefTableConsts.techSlot);
                if (!extra.ingredient.test(techItem)) {
                    return Optional.empty();
                }
            }
        }
        return recipe;
    }

    private Optional<GlideBoardRecipe> getActivePrimaryRecipe() {
        // Shaped
        Container inv = new SimpleContainer(RefTableConsts.inputSlots + 1);
        List<ItemStack> shapeless = new ArrayList<ItemStack>();
        for (int i = 0; i < RefTableConsts.inputSlots; i++) {
            ItemStack stackInSlot = itemHandler.getStackInSlot(i);
            inv.setItem(i, stackInSlot);
            if (!stackInSlot.isEmpty()) {
                shapeless.add(stackInSlot);
            }
        }
        ItemStack techItem = itemHandler.getStackInSlot(RefTableConsts.techSlot);
        inv.setItem(RefTableConsts.inputSlots, techItem);
        shapeless.add(techItem);

        RecipeManager recipeManager = level.getRecipeManager();
        Optional<GlideBoardRecipe> recipe = recipeManager.getRecipeFor(
                RecipesInit.GLIDE_BOARD, inv, level
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
        return RefTableConsts.totalSlots;
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
}
