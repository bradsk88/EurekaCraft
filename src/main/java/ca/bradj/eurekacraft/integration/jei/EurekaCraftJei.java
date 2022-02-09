package ca.bradj.eurekacraft.integration.jei;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.RecipesInit;
import ca.bradj.eurekacraft.data.recipes.GlideBoardRecipe;
import ca.bradj.eurekacraft.data.recipes.SandingMachineRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

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
        List<GlideBoardRecipe> allRecipes = rm.getAllRecipesFor(RecipesInit.GLIDE_BOARD).
                stream().
                filter(Objects::nonNull).
                collect(Collectors.toList());
        registration.addRecipes(allRecipes, RefTableRecipeCategory.ID);
        List<SandingMachineRecipe> smRecipes = rm.getAllRecipesFor(RecipesInit.SANDING_MACHINE).
                stream().
                filter(Objects::nonNull).
                collect(Collectors.toList());
        registration.addRecipes(smRecipes, SandingMachineRecipeCategory.ID);
    }
}
