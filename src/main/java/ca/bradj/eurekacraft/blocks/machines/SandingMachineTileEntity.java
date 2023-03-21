package ca.bradj.eurekacraft.blocks.machines;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.container.SandingMachineContainer;
import ca.bradj.eurekacraft.core.init.RecipesInit;
import ca.bradj.eurekacraft.core.init.TagsInit;
import ca.bradj.eurekacraft.core.init.TilesInit;
import ca.bradj.eurekacraft.core.init.items.ItemsInit;
import ca.bradj.eurekacraft.data.recipes.SandingMachineRecipe;
import com.google.common.collect.ImmutableList;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.*;

public class SandingMachineTileEntity extends EurekaCraftMachineEntity implements MenuProvider {

    public static final String ENTITY_ID = "sanding_machine_tile_entity";

    private boolean sanding = false;
    private int sandPercent = 0;

    private static int inputSlots = 1;
    private static int abrasiveSlot = inputSlots;
    private static int outputSlot = abrasiveSlot + 1;
    private static int totalSlots = outputSlot + 1;

    public SandingMachineTileEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(TilesInit.SANDING_MACHINE.get(), p_155229_, p_155230_, totalSlots);
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
        super.load(nbt);
        this.sandPercent = nbt.getInt("cooked");
    }

    @Override
    protected ItemStack getSelfAsItemStack() {
        return ItemsInit.SANDING_MACHINE_BLOCK.get().getDefaultInstance();
    }

    protected CompoundTag store(CompoundTag tag) {
        tag.putInt("cooked", this.sandPercent);
        return tag;
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
            ItemStack outSlot = getStackInSlot(outputSlot);
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
        ItemStack abrasive = getStackInSlot(abrasiveSlot);
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
                extractItem(i, 1);
            }

            useExtraIngredient();

            insertItem(outputSlot, output);

            setChanged();
        });
    }

    private void useExtraIngredient() {
        ItemStack stackInSlot = getStackInSlot(abrasiveSlot);
        stackInSlot.hurt(1, new Random(), null);
        if (stackInSlot.getDamageValue() > stackInSlot.getMaxDamage()) {
            level.playSound(null, this.getBlockPos(), SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
            extractItem(abrasiveSlot, 1);
        }
    }

    private Optional<SandingMachineRecipe> getActiveRecipe() {
        // Shaped
        Container inv = new SimpleContainer(inputSlots);
        List<ItemStack> shapeless = new ArrayList<ItemStack>();
        for (int i = 0; i < inputSlots; i++) {
            ItemStack stackInSlot = getStackInSlot(i);
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

    @Override
    protected ItemStack getItemForCraftingNoise() {
        return getStackInSlot(abrasiveSlot);
    }

    public ImmutableList<Item> getInputItems() {
        return ImmutableList.of(getStackInSlot(inputSlots).getItem());
    }

    public Item getAbrasiveItem() {
        return getStackInSlot(abrasiveSlot).getItem();
    }

    public int getInputsSlotIndex() {
        return inputSlots;
    }

    public int getAbrasiveSlotIndex() {
        return abrasiveSlot;
    }
}
