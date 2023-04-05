package ca.bradj.eurekacraft.blocks.machines;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.container.RefTableContainer;
import ca.bradj.eurekacraft.core.init.RecipesInit;
import ca.bradj.eurekacraft.core.init.TilesInit;
import ca.bradj.eurekacraft.core.init.items.ItemsInit;
import ca.bradj.eurekacraft.core.init.items.WheelItemsInit;
import ca.bradj.eurekacraft.data.recipes.RefTableRecipe;
import ca.bradj.eurekacraft.interfaces.*;
import ca.bradj.eurekacraft.vehicles.RefBoardStats;
import ca.bradj.eurekacraft.vehicles.RefBoardStatsUtils;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;
import java.util.*;

import static ca.bradj.eurekacraft.materials.Blueprints.NBT_KEY_BOARD_STATS;

public class RefTableTileEntity extends EurekaCraftMachineEntity implements MenuProvider {

    public static final String ENTITY_ID = "ref_table_tile_entity";
    private int spawnRecipeIndex = -1;

    private boolean cooking = false;
    private int craftPercent = 0;
    private int fireRemaining = 0;
    private int lastFireAmount = 1;
    private boolean ancient;
    private boolean spent;


    public RefTableTileEntity(
            BlockPos p_155229_,
            BlockState p_155230_
    ) {
        super(
                TilesInit.REF_TABLE.get(),
                p_155229_,
                p_155230_,
                RefTableConsts.totalSlots
        );
        if (p_155230_.hasProperty(RefTableBlock.ANCIENT)) {
            this.ancient = p_155230_.getValue(RefTableBlock.ANCIENT);
        }
        this.spawnRecipeIndex = determineSpawnIndex(p_155230_);
    }

    private static int determineSpawnIndex(BlockState bs) {
        if (bs.hasProperty(RefTableBlock.SPAWNED_WITH_RECIPE)) {
            int spawnRecipeNum = bs.getValue(RefTableBlock.SPAWNED_WITH_RECIPE);
            int i = spawnRecipeNum - 1;
            if (spawnRecipeNum > RefTableConsts.spawnRecipes.size()) {
                EurekaCraft.LOGGER.error(String.format("Out of bounds spawn recipe index %d", i));
                return -1;
            }
            return i;
        }
        return -1;
    }

    private void initializeSpawnRecipe(Random random, RefTableConsts.RecipeProvider recipeProvider) {
        RefTableRecipe recipe = recipeProvider.get(random);
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        for (int i = 0; i < ingredients.size(); i++) {
            if (ingredients.get(0).isEmpty()) {
                continue;
            }
            this.insertItem(RefTableConsts.inputSlotIndex + i, ingredients.get(i).getItems()[0]);
        }
        if (!recipe.getExtraIngredient().isEmpty) {
            this.insertItem(RefTableConsts.techSlot, recipe.getExtraIngredient().ingredient.getItems()[0]);
        }
        if (recipe.requiresCooking()) {
            // TODO: Randomize fuel?
            this.insertItem(RefTableConsts.fuelSlot, Items.COAL.getDefaultInstance().copy());
        }
        if (!recipe.getResultItem().isEmpty()) {
            this.insertItem(RefTableConsts.outputSlot, recipe.getResultItem());
        }
        RefTableRecipe.Secondary secondary = recipe.getSecondaryResultItem();
        if (!secondary.output.isEmpty()) {
            ItemStack output = secondary.output;
            if (secondary.initialize && output.getItem() instanceof IInitializable) {
                ((IInitializable) output.getItem()).initialize(output, random);
            }
            this.insertItem(RefTableConsts.secondaryOutputSlot, output);
        }
    }

    @Override
    public Component getDisplayName() {
        if (this.ancient) {
            return new TranslatableComponent("container." + EurekaCraft.MODID + ".ref_table.ancient");
        }
        return new TranslatableComponent("container." + EurekaCraft.MODID + ".ref_table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(
            int id,
            Inventory player,
            Player p_39956_
    ) {
        return new RefTableContainer(
                id,
                player,
                this
        );
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.craftPercent = nbt.getInt("cooked");
        this.ancient = nbt.getBoolean("ancient");
        this.spent = nbt.getBoolean("spent");
        this.spawnRecipeIndex = nbt.getInt(RefTableConsts.NBT_SPAWNED_WITH_RECIPE) - 1;
    }

    @Override
    protected Collection<ItemStack> getSelfAsItemStacks(Random rand) {
        if (this.ancient) {
            ArrayList<ItemStack> ingredients = new ArrayList<>();
            // Top
            boolean gotLuckyWood = rand.nextBoolean();
            if (gotLuckyWood) {
                ingredients.add(new ItemStack(ItemsInit.PRECISION_WOOD.get(), rand.nextInt(3)));
            } else {
                ingredients.add(new ItemStack(ItemsInit.POLISHED_OAK_SLAB.get(), rand.nextInt(3)));
            }

            // Resin
            ingredients.add(new ItemStack(ItemsInit.RESINOUS_DUST.get(), rand.nextInt(3)));

            // Base
            boolean gotLuckyBase = rand.nextBoolean();
            if (gotLuckyBase) {
                ingredients.add(new ItemStack(Items.IRON_NUGGET, rand.nextInt(32)));
            } else {
                ingredients.add(new ItemStack(Items.IRON_INGOT, rand.nextInt(8)));
            }
            return ingredients;
        }

        return ImmutableList.of(
                ItemsInit.REF_TABLE_BLOCK.get().getDefaultInstance()
        );
    }

    protected CompoundTag store(CompoundTag tag) {
        tag = super.store(tag);
        tag.putInt(
                "cooked",
                this.craftPercent
        );
        tag.putBoolean("ancient", this.ancient);
        tag.putBoolean("spent", this.spent);
        tag.putInt(RefTableConsts.NBT_SPAWNED_WITH_RECIPE, this.spawnRecipeIndex + 1);
        return tag;
    }

    // Crafting
    public static <T extends BlockEntity> void tick(
            Level level,
            BlockPos pos,
            BlockState state,
            RefTableTileEntity entity
    ) {
        if (level.isClientSide) {
            throw new IllegalStateException("Ticker should not be instantiated on client side");
        }

        tryInitializeSpawnRecipe(level, entity);

//        logger.debug("item in tech slot [" + techSlot + "] " + this.itemHandler.getStackInSlot(techSlot));
//        logger.debug("item in fuel slot [" + fuelSlot + "] " + this.itemHandler.getStackInSlot(fuelSlot));
//        logger.debug("item in output slot [" + outputSlot + "] " + this.itemHandler.getStackInSlot(outputSlot));

        if (entity.fireRemaining > 0) {
            entity.fireRemaining--;
        }

        if (entity.spent) {
            return;
        }

        Optional<RefTableRecipe> activeRecipe = entity.getActiveRecipe();
        entity.updateCookingStatus(activeRecipe);
        if (entity.cooking) {
            entity.doCook(
                    activeRecipe,
                    level
            );
        }
    }

    private static void tryInitializeSpawnRecipe(
            Level level,
            RefTableTileEntity entity
    ) {
        for (Player p : level.players()) {
            if (p.isCreative()) {
                return;
            }
        }
        if (ItemsInit.POSTER_SPAWN_BLACK.get().equals(entity.getTechItem())) {
            entity.spawnRecipeIndex = level.getRandom().nextInt(RefTableConsts.spawnRecipes.size());
            entity.extractItem(RefTableConsts.techSlot, 1);
        }
        if (entity.spawnRecipeIndex >= 0) {
            RefTableConsts.RecipeProvider recipe = RefTableConsts.spawnRecipes.get(entity.spawnRecipeIndex);
            entity.initializeSpawnRecipe(level.getRandom(), recipe);
            entity.spawnRecipeIndex = -1;
        }
    }

    private void updateCookingStatus(Optional<RefTableRecipe> active) {
        if (active.isPresent()) {

            ItemStack outSlot = getStackInSlot(RefTableConsts.outputSlot);
            if (!outSlot.isEmpty()) {
                if (!outSlot.getItem()
                        .getDefaultInstance()
                        .sameItemStackIgnoreDurability(active.get()
                                .getResultItem())) {
                    return;
                }
                if (!active.get()
                        .getResultItem()
                        .isStackable()) {
                    return;
                }
            }

            if (active.get()
                    .requiresCooking()) {
                if (!this.hasFuel()) {
                    if (!this.hasCoal()) {
                        this.cooking = false;
                        this.craftPercent = 0;
                        return;
                    }
                    ItemStack item = extractItem(
                            RefTableConsts.fuelSlot,
                            1
                    );
                    this.fireRemaining = item.getBurnTime(RecipeType.SMELTING);
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
        ItemStack stackInSlot = this.getStackInSlot(RefTableConsts.fuelSlot);
        int burnTime = ForgeHooks.getBurnTime(
                stackInSlot,
                RecipeType.SMELTING
        );
        return burnTime > 0;
    }

    private void doCook(
            Optional<RefTableRecipe> recipe,
            Level level
    ) {
        if (craftPercent < 100) {
            this.craftPercent++;
            this.makeCraftingNoise(recipe);
            return;
        }
        this.cooking = false;
        this.craftPercent = 0;

        recipe.ifPresent(iRecipe -> {
            // FIXME: This causes us to lose the NBT from the input item
            ItemStack output = iRecipe.getResultItem()
                    .copy();

            Collection<ItemStack> inputs = new ArrayList<>();
            for (int i = 0; i < RefTableConsts.inputSlots; i++) {
                ItemStack stackInSlot = getStackInSlot(i);
                if (stackInSlot.isEmpty()) {
                    continue;
                }
                inputs.add(stackInSlot);
            }

            if (output.getItem() instanceof IBoardStatsCraftable) {
                ((IBoardStatsCraftable) output.getItem()).generateNewBoardStats(
                        output,
                        inputs,
                        level.getRandom()
                );
            }

            if (level.getRandom()
                    .nextFloat() < iRecipe.getSecondaryResultItem().chance) {
                ItemStack sOutput = iRecipe.getSecondaryResultItem().output.copy();
                if (sOutput.sameItemStackIgnoreDurability(WheelItemsInit.WHEEL_PLACEHOLDER_ITEM.get()
                        .getDefaultInstance())) {
                    EurekaCraft.LOGGER.debug("Not outputting placeholder secondary");
                } else {

                    if (iRecipe.getSecondaryResultItem().initialize) {
                        if (!(sOutput.getItem() instanceof IInitializable)) {
                            EurekaCraft.LOGGER.error("Recipe calls for init but item does not support it:" + sOutput.getItem());
                        }
                        ((IInitializable) sOutput.getItem()).initialize(
                                sOutput,
                                level.getRandom()
                        );
                    }

                    insertItem(
                            RefTableConsts.secondaryOutputSlot,
                            sOutput
                    );
                }
            }

            for (int i = 0; i < RefTableConsts.inputSlots; i++) {
                extractItem(
                        i,
                        1
                );
            }

            if (!iRecipe.getExtraIngredient().ingredient.isEmpty()) {
                useExtraIngredient(
                        iRecipe,
                        inputs,
                        output,
                        level
                );
            }

            switch (iRecipe.getOutputConstructStatsPolicy()) {
                case NEW -> {
                    if (!(iRecipe.getResultItem()
                            .getItem() instanceof IInitializable)) {
                        EurekaCraft.LOGGER.error(
                                "Recipe calls for init but item does not support it:" + iRecipe.getResultItem()
                                        .getItem()
                        );
                    }
                    ((IInitializable) iRecipe.getResultItem()
                            .getItem()).initialize(
                            iRecipe.getResultItem(),
                            level.getRandom()
                    );
                }
                case BOOST_AVG -> {
                    Collection<RefBoardStats> contextStats = inputs.stream().
                            filter(v -> v.getItem() instanceof IBoardStatsGetter).
                            map(v -> ((IBoardStatsGetter) v.getItem()).getBoardStats(v))
                            .toList();
                    RefBoardStats stats = RefBoardStatsUtils.BoostAvg(
                            contextStats,
                            level.getRandom(),
                            1.1f,
                            1.25f
                    );
                    output.getOrCreateTag()
                            .put(
                                    NBT_KEY_BOARD_STATS,
                                    RefBoardStats.serializeNBT(stats)
                            );
                }
                case INVALID -> {
                    // TODO: Is this ok?
                }
            }

            insertItem(
                    RefTableConsts.outputSlot,
                    output
            );

            if (this.ancient) {
                getTileData().putBoolean("spent", true);
            }

            setChanged();
        });
    }

    private void useExtraIngredient(
            RefTableRecipe iRecipe,
            Collection<ItemStack> inputs,
            ItemStack craftedOutput,
            Level level
    ) {
        ItemStack techStack = getStackInSlot(RefTableConsts.techSlot);
        techStack.hurt(
                1,
                level.getRandom(),
                null
        );
        if (iRecipe.getExtraIngredient().consumeOnUse) {
            extractItem(
                    RefTableConsts.techSlot,
                    1
            );
        } else if (techStack.getDamageValue() > techStack.getMaxDamage()) {
            level.playSound(
                    null,
                    this.getBlockPos(),
                    SoundEvents.ITEM_BREAK,
                    SoundSource.BLOCKS,
                    1.0f,
                    1.0f
            );
            extractItem(
                    RefTableConsts.techSlot,
                    1
            );
        }

        if (iRecipe.getResultItem()
                .getItem() instanceof ITechAffected) {
            ((ITechAffected) iRecipe.getResultItem()
                    .getItem()).applyTechItem(
                    inputs,
                    techStack,
                    craftedOutput,
                    level.getRandom()
            );
        }

        if (iRecipe.getResultItem()
                .getItem() instanceof IPaintable) {
            ((IPaintable) iRecipe.getResultItem()
                    .getItem()).applyPaint(
                    inputs,
                    techStack,
                    craftedOutput
            );
        }

        if (getStackInSlot(RefTableConsts.techSlot).sameItem(WheelItemsInit.SOCKET_WRENCH.get()
                .getDefaultInstance())) {
            if (iRecipe.getResultItem()
                    .getItem() instanceof IWrenchable) {
                Optional<ItemStack> removedPart = ((IWrenchable) iRecipe.getResultItem()
                        .getItem()).applyWrench(
                        inputs,
                        craftedOutput
                );
                if (removedPart.isPresent()) {
                    if (!getStackInSlot(RefTableConsts.secondaryOutputSlot).isEmpty()) {
                        throw new IllegalStateException("Expected output slot to be empty for part removal recipe");
                    }
                    insertItem(
                            RefTableConsts.secondaryOutputSlot,
                            removedPart.get()
                    );
                }
            }
        }
    }

    private Optional<RefTableRecipe> getActiveRecipe() {
        Optional<RefTableRecipe> recipe = getActivePrimaryRecipe();
        if (recipe.isPresent()) {
            RefTableRecipe.ExtraInput extra = recipe.get()
                    .getExtraIngredient();
            RefTableRecipe.Secondary secondary = recipe.get()
                    .getSecondaryResultItem();
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

        if (!techItem.sameItemStackIgnoreDurability(WheelItemsInit.SOCKET_WRENCH.get()
                .getDefaultInstance())) {
            return false;
        }
        for (Item i : inputs.stream()
                .map(ItemStack::getItem)
                .toList()) {
            if (i instanceof IWrenchable) {
                if (((IWrenchable) i).canApplyWrench(
                        inputs,
                        techItem
                )) {
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
            inv.setItem(
                    i,
                    stackInSlot
            );
            if (!stackInSlot.isEmpty()) {
                shapeless.add(stackInSlot);
            }
        }
        ItemStack techItem = getStackInSlot(RefTableConsts.techSlot);
        inv.setItem(
                RefTableConsts.inputSlots,
                techItem
        );
        shapeless.add(techItem);

        RecipeManager recipeManager = level.getRecipeManager();
        Optional<RefTableRecipe> recipe = recipeManager.getRecipeFor(
                RecipesInit.REF_TABLE,
                inv,
                level
        );

        if (recipe.isPresent()) {
            return recipe;
        }

        // Shapeless
        // TODO: Reduce duplication with above
        inv = new SimpleContainer(shapeless.size());
        for (int i = 0; i < shapeless.size(); i++) {
            ItemStack stackInSlot = shapeless.get(i);
            inv.setItem(
                    i,
                    stackInSlot
            );
        }

        recipe = recipeManager.getRecipeFor(
                RecipesInit.REF_TABLE,
                inv,
                level
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
    protected Optional<ItemStack> getItemForCraftingNoise() {
        Optional<RefTableRecipe> recipe = getActiveRecipe();
        if (recipe.isEmpty()) {
            return Optional.empty();
        }

        if (recipe.get().getExtraIngredient().ingredient.test(getStackInSlot(RefTableConsts.techSlot))) {
            return Optional.of(getStackInSlot(RefTableConsts.techSlot));
        }
        return Optional.empty();
    }

    public Collection<Item> getInputItems() {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < RefTableConsts.inputSlots; i++) {
            items.add(getStackInSlot(i).getItem());
        }
        return ImmutableList.copyOf(items);
    }

    public Item getFuelItem() {
        return getStackInSlot(RefTableConsts.fuelSlot).getItem();
    }

    public Item getTechItem() {
        return getStackInSlot(RefTableConsts.techSlot).getItem();
    }

    public int getInputsSlotIndex() {
        return 0;
    }

    public int getFuelSlotIndex() {
        return RefTableConsts.fuelSlot;
    }

    public int getTechSlotIndex() {
        return RefTableConsts.techSlot;
    }
}
