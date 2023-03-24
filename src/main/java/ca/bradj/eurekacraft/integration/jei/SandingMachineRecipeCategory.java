package ca.bradj.eurekacraft.integration.jei;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.container.SandingMachineContainer;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.core.init.items.ItemsInit;
import ca.bradj.eurekacraft.data.recipes.SandingMachineRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import static ca.bradj.eurekacraft.core.init.TagsInit.Items.SANDING_DISCS;

public class SandingMachineRecipeCategory implements IRecipeCategory<SandingMachineRecipe> {

    public final static ResourceLocation TEXTURE = new ResourceLocation(EurekaCraft.MODID, "textures/screens/sanding_machine_screen.png");

    private final IDrawable background;
    private final IDrawable icon;

    public SandingMachineRecipeCategory(
            IGuiHelper helper
    ) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, SandingMachineContainer.titleBarHeight + (4 * SandingMachineContainer.boxHeight));
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ItemsInit.SANDING_MACHINE_BLOCK.get()));
    }

    @Override
    public RecipeType<SandingMachineRecipe> getRecipeType() {
        return EurekaCraftJei.SANDING_MACHINE_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal(BlocksInit.SANDING_MACHINE.get().getName().getString());
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
    public void setRecipe(IRecipeLayoutBuilder recipeLayout, SandingMachineRecipe recipe, IFocusGroup focuses) {
        int leftEdge = SandingMachineContainer.inventoryLeftX + SandingMachineContainer.boxWidth - 1;
        int topEdge = SandingMachineContainer.titleBarHeight + SandingMachineContainer.margin - 1;
        int boxSize = SandingMachineContainer.boxWidth;

        recipeLayout.addSlot(RecipeIngredientRole.INPUT, leftEdge + boxSize, topEdge + boxSize).addIngredients(recipe.getIngredients().get(0));
        Ingredient abSlot = recipe.getSpecialInput().orElse(Ingredient.of(SANDING_DISCS));
        recipeLayout.addSlot(RecipeIngredientRole.INPUT, leftEdge + (4 * boxSize), topEdge + boxSize).addIngredients(abSlot);
        recipeLayout.addSlot(RecipeIngredientRole.OUTPUT, leftEdge + (6 * boxSize), topEdge + boxSize).addItemStack(recipe.getResultItem());
        ;
    }
}
