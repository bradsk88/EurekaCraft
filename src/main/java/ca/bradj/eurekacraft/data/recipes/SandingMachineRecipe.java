package ca.bradj.eurekacraft.data.recipes;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.RecipesInit;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

// FIXME: Rename to RefTableRecipe
public class SandingMachineRecipe implements ISandingMachineRecipe {

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    private static final int recipeSize = 1;

    public SandingMachineRecipe(
            ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems
    ) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
    }

    @Override
    public boolean matches(Container inv, Level p_77569_2_) {
        for (int i = 0; i < recipeItems.size(); i++) {
            if (!recipeItems.get(i).test(inv.getItem(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    @Override
    public ItemStack getResultItem() {
        return this.output.copy();
    }

    @Override
    public ItemStack assemble(Container p_77572_1_) {
        return null;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipesInit.SANDING_MACHINE_SERIALIZER.get();
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<SandingMachineRecipe> {

        Logger logger = LogManager.getLogger(EurekaCraft.MODID + "/SandingMachine");

        @Override
        public SandingMachineRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ItemStack output = ShapedRecipe.itemFromJson(GsonHelper.getAsJsonObject(json, "result")).getDefaultInstance();

            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(ingredients.size(), Ingredient.EMPTY);

            for (int i = 0; i < ingredients.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new SandingMachineRecipe(recipeId, output, inputs);
        }

        @Nullable
        @Override
        public SandingMachineRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            int rSize = buffer.readInt();
            NonNullList<Ingredient> inputs = NonNullList.withSize(rSize, Ingredient.EMPTY);
            for (int i = 0; i < rSize; i++) {
                inputs.set(i, Ingredient.fromNetwork(buffer));
            }

            ItemStack output = buffer.readItem();

            return new SandingMachineRecipe(recipeId, output, inputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, SandingMachineRecipe recipe) {
            buffer.writeInt(recipe.getIngredients().size());
            int i = 0;
            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buffer);
                i++;
            }
            buffer.writeItem(recipe.getResultItem());
        }
    }

    public static class Type implements RecipeType<SandingMachineRecipe> {
        @Override
        public String toString() {
            return SandingMachineRecipe.TYPE_ID.toString();
        }
    }

}
