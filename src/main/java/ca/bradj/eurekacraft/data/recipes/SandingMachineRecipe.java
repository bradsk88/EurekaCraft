package ca.bradj.eurekacraft.data.recipes;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.RecipesInit;
import ca.bradj.eurekacraft.core.init.TagsInit;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;

public class SandingMachineRecipe implements ISandingMachineRecipe {

    private final ResourceLocation id;
    private final Optional<Ingredient> special;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    private static final int recipeSize = 1;

    public SandingMachineRecipe(
            ResourceLocation id, Optional<Ingredient> special, ItemStack output, NonNullList<Ingredient> recipeItems
    ) {
        this.id = id;
        this.special = special;
        this.output = output;
        this.recipeItems = recipeItems;
    }

    @Override
    public boolean matches(Container inv, Level p_77569_2_) {
        Ingredient input = recipeItems.get(0);
        if (!input.test(inv.getItem(0))) {
            return false;
        }
        if (special.isPresent()) {
            if (special.get().test(inv.getItem(1))) {
                return true;
            }
            return false;
        }

        return Ingredient.of(TagsInit.Items.SANDING_DISCS).test(inv.getItem(1));
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

    public static class Serializer implements RecipeSerializer<SandingMachineRecipe> {

        Logger logger = LogManager.getLogger(EurekaCraft.MODID + "/SandingMachine");

        @Override
        public SandingMachineRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ItemStack output = ShapedRecipe.itemFromJson(GsonHelper.getAsJsonObject(json, "result")).getDefaultInstance();

            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(ingredients.size(), Ingredient.EMPTY);

            for (int i = 0; i < ingredients.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            Optional<Ingredient> special = Optional.empty();
            if (json.has("special")) {
                special = Optional.of(Ingredient.fromJson(json.get("special")));
            }

            return new SandingMachineRecipe(recipeId, special, output, inputs);
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

            Optional<Ingredient> special = Optional.of(Ingredient.of(buffer.readItem()));

            return new SandingMachineRecipe(recipeId, special, output, inputs);
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
            buffer.writeItem(recipe.getSpecialInput().map(v -> v.getItems()[0]).orElse(ItemStack.EMPTY));
        }
    }

    public Optional<Ingredient> getSpecialInput() {
        return special;
    }

    public static class Type implements RecipeType<SandingMachineRecipe> {

        public static final Type INSTANCE = new Type();
        public static final ResourceLocation ID = new ResourceLocation(EurekaCraft.MODID, "sanding_machine");

    }

}
