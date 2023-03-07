package ca.bradj.eurekacraft.integration.jei;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.container.RefTableContainer;
import ca.bradj.eurekacraft.container.SandingMachineContainer;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.core.init.items.ItemsInit;
import ca.bradj.eurekacraft.data.recipes.RefTableRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.NonNullList;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import static ca.bradj.eurekacraft.core.init.TagsInit.Items.ITEMS_THAT_BURN;

public class RefTableRecipeCategory implements IRecipeCategory<RefTableRecipe> {

    public final static ResourceLocation TEXTURE = new ResourceLocation(
            EurekaCraft.MODID,
            "textures/screens/ref_table_screen.png"
    );

    private final IDrawable background;
    private final IDrawable icon;

    public RefTableRecipeCategory(
            IGuiHelper helper
    ) {
        this.background = helper.createDrawable(
                TEXTURE,
                0,
                0,
                176,
                SandingMachineContainer.titleBarHeight + (4 * SandingMachineContainer.boxHeight)
        );
        this.icon = helper.createDrawableIngredient(
                VanillaTypes.ITEM_STACK,
                new ItemStack(ItemsInit.REF_TABLE_BLOCK.get())
        );
    }

    @Override
    public RecipeType<RefTableRecipe> getRecipeType() {
        return EurekaCraftJei.REF_TABLE_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal(BlocksInit.REF_TABLE_BLOCK.get()
                .getName()
                .getString());
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }


    // FIXME: Needed after migration?
//    @Override
//    public void setIngredients(RefTableRecipe recipe, IIngredients ingredients) {
//        NonNullList<Ingredient> i = NonNullList.withSize(8, Ingredient.EMPTY);
//        for (int j = 0; j < 8; j++) {
//            if (j < recipe.getIngredients().size()) {
//                Ingredient p_set_2_ = recipe.getIngredients().get(j);
//                if (p_set_2_.isEmpty() || p_set_2_.getItems()[0].sameItemStackIgnoreDurability(Items.AIR.getDefaultInstance())) {
//                    i.set(j, Ingredient.of(Items.AIR.getDefaultInstance()));
//                } else {
//                    i.set(j, p_set_2_);
//                }
//            } else {
//                i.set(j, Ingredient.of(Items.AIR.getDefaultInstance()));
//            }
//        }
//        if (recipe.requiresCooking()) {
//            i.set(6, Ingredient.of(ITEMS_THAT_BURN));
//        } else {
//            i.set(6, Ingredient.of(Items.AIR.getDefaultInstance()));
//        }
//        i.set(7, recipe.getExtraIngredient().ingredient);
//        ingredients.setInputIngredients(i);
//        ImmutableList<ItemStack> outputs = ImmutableList.of(
//                recipe.getResultItem()
//        );
//        if (!recipe.getSecondaryResultItem().output.isEmpty()) {
//            outputs = ImmutableList.of(
//                    recipe.getResultItem(),
//                    recipe.getSecondaryResultItem().output
//            );
//        }
//        ingredients.setOutputs(VanillaTypes.ITEM, outputs);
//    }


    // FIXME: Finish migrating
    @Override
    public void setRecipe(
            IRecipeLayoutBuilder recipeLayout,
            RefTableRecipe recipe,
            IFocusGroup focuses
    ) {
        int leftEdge = RefTableContainer.inventoryLeftX;
        int topEdge = RefTableContainer.topOfInputs;
        int boxSize = RefTableContainer.boxWidth;
        this.init(
                recipeLayout,
                recipe,
                0,
                true,
                leftEdge,
                topEdge
        );
        this.init(
                recipeLayout,
                recipe,
                1,
                true,
                leftEdge + boxSize,
                topEdge
        );
        this.init(
                recipeLayout,
                recipe,
                2,
                true,
                leftEdge,
                topEdge + boxSize
        );
        this.init(
                recipeLayout,
                recipe,
                3,
                true,
                leftEdge + boxSize,
                topEdge + boxSize
        );
        this.init(
                recipeLayout,
                recipe,
                4,
                true,
                leftEdge,
                topEdge + (2 * boxSize)
        );
        this.init(
                recipeLayout,
                recipe,
                5,
                true,
                leftEdge + boxSize,
                topEdge + (2 * boxSize)
        );
        if (recipe.requiresCooking()) {
            recipeLayout.addSlot(
                            RecipeIngredientRole.INPUT,
                            RefTableContainer.leftOfFuel,
                            RefTableContainer.topOfFuel
                    ).
                    addIngredients(Ingredient.of(ITEMS_THAT_BURN));
        }
        recipeLayout.addSlot(
                        RecipeIngredientRole.INPUT,
                        RefTableContainer.leftOfTech,
                        RefTableContainer.topOfTech
                ).
                addIngredients(recipe.getExtraIngredient().ingredient);
        recipeLayout.addSlot(
                        RecipeIngredientRole.OUTPUT,
                        RefTableContainer.leftOfOutput,
                        RefTableContainer.topOfOutput
                ).
                addItemStack(recipe.getResultItem());
        // TODO: Render secondary chance
        recipeLayout.addSlot(
                        RecipeIngredientRole.OUTPUT,
                        RefTableContainer.leftOfSecondary,
                        RefTableContainer.topOfSecondary
                ).
                addItemStack(recipe.getSecondaryResultItem().output);
    }

    private void init(
            IRecipeLayoutBuilder recipeLayout,
            RefTableRecipe recipe,
            int idx,
            boolean isInput,
            int leftEdge,
            int topEdge
    ) {
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        if (ingredients.size() <= idx) {
            return;
        }

        RecipeIngredientRole role = RecipeIngredientRole.OUTPUT;
        if (isInput) {
            role = RecipeIngredientRole.INPUT;
        }
        recipeLayout.addSlot(
                        role,
                        leftEdge,
                        topEdge
                )
                .addIngredients(ingredients.get(idx));
    }
}
