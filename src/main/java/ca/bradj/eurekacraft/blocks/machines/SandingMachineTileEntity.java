package ca.bradj.eurekacraft.blocks.machines;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.container.SandingMachineContainer;
import ca.bradj.eurekacraft.core.init.RecipesInit;
import ca.bradj.eurekacraft.core.init.TagsInit;
import ca.bradj.eurekacraft.core.init.TilesInit;
import ca.bradj.eurekacraft.data.recipes.SandingMachineRecipe;
import ca.bradj.eurekacraft.materials.NoisyCraftingItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class SandingMachineTileEntity extends BlockEntity implements MenuProvider {
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

    public SandingMachineTileEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(TilesInit.SANDING_MACHINE.get(), p_155229_, p_155230_);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container." + EurekaCraft.MODID + ".sanding_machine");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player, Player Player) {
        return new SandingMachineContainer(id, player, this);
    }

    @Override
    public void load(CompoundTag nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
        this.sandPercent = nbt.getInt("cooked");
        super.load(nbt);
    }

    private CompoundTag save(CompoundTag tag) {
        tag.put("inv", itemHandler.serializeNBT());
        tag.putInt("cooked", this.sandPercent);
        return tag;
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(this.save(tag));
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
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

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, SandingMachineTileEntity entity) {
        if (level.isClientSide) {
            throw new IllegalStateException("Ticker should not be instantiated on client side");
        }

        Optional<SandingMachineRecipe> activeRecipe = entity.getActiveRecipe();
        entity.updateCookingStatus(activeRecipe);
        if (entity.sanding) {
            entity.doCook(activeRecipe);
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
        Ingredient.TagValue tags = new Ingredient.TagValue(TagsInit.Items.SANDING_DISCS);
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
                        null, this.getBlockPos(), s.event, SoundSource.BLOCKS, volume, pitch
                );
                this.noiseCooldown = s.noiseCooldown;
            });
        });

    }

    private void useExtraIngredient() {
        ItemStack stackInSlot = itemHandler.getStackInSlot(abrasiveSlot);
        stackInSlot.hurt(1, new Random(), null);
        if (stackInSlot.getDamageValue() > stackInSlot.getMaxDamage()) {
            level.playSound(null, this.getBlockPos(), SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
            this.itemHandler.extractItem(abrasiveSlot, 1, false);
        }
    }

    private Optional<SandingMachineRecipe> getActiveRecipe() {
        // Shaped
        Container inv = new SimpleContainer(inputSlots);
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
        inv = new SimpleContainer(shapeless.size());
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
