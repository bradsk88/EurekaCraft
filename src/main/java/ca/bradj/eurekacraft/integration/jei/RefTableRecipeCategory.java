package ca.bradj.eurekacraft.integration.jei;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.container.RefTableContainer;
import ca.bradj.eurekacraft.container.SandingMachineContainer;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.core.init.ItemsInit;
import ca.bradj.eurekacraft.data.recipes.GlideBoardRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class RefTableRecipeCategory implements IRecipeCategory<GlideBoardRecipe> {

    public final static ResourceLocation ID = new ResourceLocation(EurekaCraft.MODID, "ref_table_recipe_category");

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
        return ID;
    }

    @Override
    public Class<? extends GlideBoardRecipe> getRecipeClass() {
        return GlideBoardRecipe.class;
    }

    @Override
    public String getTitle() {
        return BlocksInit.REF_TABLE_BLOCK.get().getName().getString();
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
    public void setIngredients(GlideBoardRecipe recipe, IIngredients ingredients) {
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
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem()); // TODO: Show secondary
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, GlideBoardRecipe recipe, IIngredients ingredients) {
        int leftEdge = RefTableContainer.inventoryLeftX + RefTableContainer.boxWidth - 1;
        int topEdge = RefTableContainer.titleBarHeight + RefTableContainer.margin - 1;
        int boxSize = RefTableContainer.boxWidth;
        recipeLayout.getItemStacks().init(0, true, leftEdge, topEdge);
        recipeLayout.getItemStacks().init(1, true, leftEdge + boxSize, topEdge);
        recipeLayout.getItemStacks().init(2, true, leftEdge, topEdge + boxSize);
        recipeLayout.getItemStacks().init(3, true, leftEdge + boxSize, topEdge + boxSize);
        recipeLayout.getItemStacks().init(4, true, leftEdge, topEdge + (2 * boxSize));
        recipeLayout.getItemStacks().init(5, true, leftEdge + boxSize, topEdge + (2 * boxSize));
        recipeLayout.getItemStacks().init(6, true, leftEdge + (3 * boxSize), topEdge + boxSize);
        recipeLayout.getItemStacks().init(7, true, leftEdge + (4 * boxSize), topEdge + boxSize);
        recipeLayout.getItemStacks().init(8, false, leftEdge + (6 * boxSize), topEdge + boxSize);
        recipeLayout.getItemStacks().set(ingredients);
        ;
    }
}
