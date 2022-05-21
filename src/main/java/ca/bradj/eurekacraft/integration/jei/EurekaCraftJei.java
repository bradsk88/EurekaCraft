package ca.bradj.eurekacraft.integration.jei;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.RecipesInit;
import ca.bradj.eurekacraft.data.recipes.RefTableRecipe;
import ca.bradj.eurekacraft.data.recipes.SandingMachineRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@JeiPlugin
public class EurekaCraftJei implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(EurekaCraft.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new RefTableRecipeCategory(registration.getJeiHelpers().getGuiHelper())
        );
        registration.addRecipeCategories(
                new SandingMachineRecipeCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level.getRecipeManager());
        RecipeType<RefTableRecipe> glideBoardType = RecipeType.create(
                EurekaCraft.MODID, RefTableRecipe.Type.ID.getPath(), RefTableRecipe.class
        );
        RecipeType<SandingMachineRecipe> sandingMachineType = RecipeType.create(
                EurekaCraft.MODID, SandingMachineRecipe.Type.ID.getPath(), SandingMachineRecipe.class
        );
        List<RefTableRecipe> allRecipes = rm.getAllRecipesFor(RecipesInit.REF_TABLE).
                stream().
                filter(Objects::nonNull).
                collect(Collectors.toList());
        registration.addRecipes(glideBoardType, allRecipes);
        List<SandingMachineRecipe> smRecipes = rm.getAllRecipesFor(RecipesInit.SANDING_MACHINE).
                stream().
                filter(Objects::nonNull).
                collect(Collectors.toList());
        registration.addRecipes(sandingMachineType, smRecipes);
    }
}
