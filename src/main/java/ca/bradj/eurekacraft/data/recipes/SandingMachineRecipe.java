package ca.bradj.eurekacraft.data.recipes;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.RecipesInit;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
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
    public boolean matches(IInventory inv, World p_77569_2_) {
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
    public ItemStack assemble(IInventory p_77572_1_) {
        return null;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipesInit.SANDING_MACHINE_SERIALIZER.get();
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SandingMachineRecipe> {

        Logger logger = LogManager.getLogger(EurekaCraft.MODID + "/SandingMachine");

        @Override
        public SandingMachineRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ItemStack output = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));

            JsonArray ingredients = JSONUtils.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(ingredients.size(), Ingredient.EMPTY);

            for (int i = 0; i < ingredients.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new SandingMachineRecipe(recipeId, output, inputs);
        }

        @Nullable
        @Override
        public SandingMachineRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            int rSize = buffer.readInt();
            NonNullList<Ingredient> inputs = NonNullList.withSize(rSize, Ingredient.EMPTY);
            for (int i = 0; i < rSize; i++) {
                inputs.set(i, Ingredient.fromNetwork(buffer));
            }

            ItemStack output = buffer.readItem();

            return new SandingMachineRecipe(recipeId, output, inputs);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, SandingMachineRecipe recipe) {
            buffer.writeInt(recipe.getIngredients().size());
            int i = 0;
            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buffer);
                i++;
            }
            buffer.writeItem(recipe.getResultItem());
        }
    }

    public static class Type implements IRecipeType<SandingMachineRecipe> {
        @Override
        public String toString() {
            return SandingMachineRecipe.TYPE_ID.toString();
        }
    }

}
