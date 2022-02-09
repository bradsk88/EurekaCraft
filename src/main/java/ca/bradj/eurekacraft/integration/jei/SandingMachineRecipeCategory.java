package ca.bradj.eurekacraft.integration.jei;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.container.SandingMachineContainer;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.core.init.ItemsInit;
import ca.bradj.eurekacraft.data.recipes.SandingMachineRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class SandingMachineRecipeCategory implements IRecipeCategory<SandingMachineRecipe> {

    public final static ResourceLocation ID = new ResourceLocation(EurekaCraft.MODID, "sanding_machine_recipe_category");

    public final static ResourceLocation TEXTURE = new ResourceLocation(EurekaCraft.MODID, "textures/screens/sanding_machine_screen.png");

    private final IDrawable background;
    private final IDrawable icon;

    public SandingMachineRecipeCategory(
            IGuiHelper helper
    ) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, SandingMachineContainer.titleBarHeight + (4 * SandingMachineContainer.boxHeight));
        this.icon = helper.createDrawableIngredient(new ItemStack(ItemsInit.SANDING_MACHINE_BLOCK.get()));
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends SandingMachineRecipe> getRecipeClass() {
        return SandingMachineRecipe.class;
    }

    @Override
    public String getTitle() {

        return BlocksInit.SANDING_MACHINE.get().getName().getString();
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
    public void setIngredients(SandingMachineRecipe recipe, IIngredients ingredients) {
        NonNullList<Ingredient> i = NonNullList.withSize(2, Ingredient.EMPTY);
        i.set(0, recipe.getIngredients().get(0));
        i.set(1, Ingredient.of(ItemsInit.FLINT_SANDING_DISC.get()));
        ingredients.setInputIngredients(i);
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem()); // TODO: Show secondary
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SandingMachineRecipe recipe, IIngredients ingredients) {
        int leftEdge = SandingMachineContainer.inventoryLeftX + SandingMachineContainer.boxWidth - 1;
        int topEdge = SandingMachineContainer.titleBarHeight + SandingMachineContainer.margin - 1;
        int boxSize = SandingMachineContainer.boxWidth;
        recipeLayout.getItemStacks().init(0, true, leftEdge + boxSize, topEdge + boxSize);
        recipeLayout.getItemStacks().init(1, true, leftEdge + (4 * boxSize), topEdge + boxSize);
        recipeLayout.getItemStacks().init(2, false, leftEdge + (6 * boxSize), topEdge + boxSize);
        recipeLayout.getItemStacks().set(ingredients);
        ;
    }
}
