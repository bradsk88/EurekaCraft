package ca.bradj.eurekacraft.integration.jei;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.container.RefTableContainer;
import ca.bradj.eurekacraft.container.SandingMachineContainer;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.core.init.items.ItemsInit;
import ca.bradj.eurekacraft.data.recipes.RefTableRecipe;
import com.google.common.collect.ImmutableList;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class RefTableRecipeCategory implements IRecipeCategory<RefTableRecipe> {

    public final static ResourceLocation TEXTURE = new ResourceLocation(EurekaCraft.MODID, "textures/screens/ref_table_screen.png");

    private final IDrawable background;
    private final IDrawable icon;

    public RefTableRecipeCategory(
            IGuiHelper helper
    ) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, SandingMachineContainer.titleBarHeight + (4 * SandingMachineContainer.boxHeight));
        this.icon = helper.createDrawableIngredient(new ItemStack(ItemsInit.REF_TABLE_BLOCK.get()));
    }

    @Override
    public ResourceLocation getUid() {
        return RefTableRecipe.Type.ID;
    }

    @Override
    public Class<? extends RefTableRecipe> getRecipeClass() {
        return RefTableRecipe.class;
    }

    @Override
    public Component getTitle() {
        return new TextComponent(BlocksInit.REF_TABLE_BLOCK.get().getName().getString());
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(RefTableRecipe recipe, IIngredients ingredients) {
        NonNullList<Ingredient> i = NonNullList.withSize(8, Ingredient.EMPTY);
        for (int j = 0; j < 8; j++) {
            if (j < recipe.getIngredients().size()) {
                Ingredient p_set_2_ = recipe.getIngredients().get(j);
                if (p_set_2_.isEmpty() || p_set_2_.getItems()[0].sameItemStackIgnoreDurability(Items.AIR.getDefaultInstance())) {
                    i.set(j, Ingredient.of(Items.AIR.getDefaultInstance()));
                } else {
                    i.set(j, p_set_2_);
                }
            } else {
                i.set(j, Ingredient.of(Items.AIR.getDefaultInstance()));
            }
        }
        if (recipe.requiresCooking()) {
            i.set(6, Ingredient.of(Items.COAL.getDefaultInstance()));
        } else {
            i.set(6, Ingredient.of(Items.AIR.getDefaultInstance()));
        }
        i.set(7, recipe.getExtraIngredient().ingredient);
        ingredients.setInputIngredients(i);
        ImmutableList<ItemStack> outputs = ImmutableList.of(
                recipe.getResultItem()
        );
        if (!recipe.getSecondaryResultItem().output.isEmpty()) {
            outputs = ImmutableList.of(
                    recipe.getResultItem(),
                    recipe.getSecondaryResultItem().output
            );
        }
        ingredients.setOutputs(VanillaTypes.ITEM, outputs);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, RefTableRecipe recipe, IIngredients ingredients) {
        int leftEdge = RefTableContainer.inventoryLeftX;
        int topEdge = RefTableContainer.topOfInputs;
        int boxSize = RefTableContainer.boxWidth;
        this.init(recipeLayout, 0, true, leftEdge, topEdge);
        this.init(recipeLayout, 1, true, leftEdge + boxSize, topEdge);
        this.init(recipeLayout, 2, true, leftEdge, topEdge + boxSize);
        this.init(recipeLayout, 3, true, leftEdge + boxSize, topEdge + boxSize);
        this.init(recipeLayout, 4, true, leftEdge, topEdge + (2 * boxSize));
        this.init(recipeLayout, 5, true, leftEdge + boxSize, topEdge + (2 * boxSize));
        this.init(recipeLayout, 6, true, RefTableContainer.leftOfFuel, RefTableContainer.topOfFuel);
        this.init(recipeLayout, 7, true, RefTableContainer.leftOfTech, RefTableContainer.topOfTech);
        this.init(recipeLayout, 8, false, RefTableContainer.leftOfOutput, RefTableContainer.topOfOutput);
        // TODO: Render secondary chance
        this.init(recipeLayout, 9, false, RefTableContainer.leftOfSecondary, RefTableContainer.topOfSecondary);
        recipeLayout.getItemStacks().set(ingredients);
    }

    private void init(IRecipeLayout recipeLayout, int idx, boolean isInput, int leftEdge, int topEdge) {
        recipeLayout.getItemStacks().init(idx, isInput, leftEdge - 1, topEdge - 1);
    }
}
