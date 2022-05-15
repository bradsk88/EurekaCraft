package ca.bradj.eurekacraft.data.recipes;

import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface ISandingMachineRecipe extends Recipe<Container> {

    ResourceLocation TYPE_ID = new ResourceLocation(EurekaCraft.MODID, "sanding_machine");

    @Override
    default RecipeType<?> getType() {
        return Registry.RECIPE_TYPE.getOptional(TYPE_ID).get();
    }

    @Override
    default boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    default boolean isSpecial() {
        return true; // TODO: Confirm this is the right function
    }
}
