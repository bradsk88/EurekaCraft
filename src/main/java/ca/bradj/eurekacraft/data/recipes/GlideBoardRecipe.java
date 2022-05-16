package ca.bradj.eurekacraft.data.recipes;

import ca.bradj.eurekacraft.blocks.machines.RefTableConsts;
import ca.bradj.eurekacraft.core.init.RecipesInit;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.JsonUtils;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;

// FIXME: Rename to RefTableRecipe
public class GlideBoardRecipe implements IGlideBoardRecipe {

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    private final boolean cook;
    private final Secondary secondaryOutput;
    private ExtraInput extraIngredient;

    public GlideBoardRecipe(
            ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems,
            boolean cook, ExtraInput extraIngredient,
            Secondary secondary) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
        this.cook = cook;
        this.extraIngredient = extraIngredient;
        this.secondaryOutput = secondary;
    }

    private boolean findMatchAndRemove(
            ArrayList<Ingredient> ingredients, ArrayList<ItemStack> inputs
    ) {
        for (ItemStack i : inputs) {
            for (Ingredient n : ingredients) {
                if (n.test(i)) {
                    ingredients.remove(n);
                    inputs.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean matches(Container inv, Level p_77569_2_) {
        if (inv.isEmpty()) {
            return false;
        }

        ArrayList<ItemStack> itemsInInput = new ArrayList<>();
        for (int i = 0; i < Math.min(inv.getContainerSize(), RefTableConsts.inputSlots); i++) {
            if (inv.getItem(i).isEmpty()) {
                continue;
            }
            itemsInInput.add(inv.getItem(i));
        }
        if (itemsInInput.size() != recipeItems.size()) {
            return false;
        }
        if (recipeItems.size() < RefTableConsts.inputSlots) {
            ArrayList<Ingredient> recipeCopy = new ArrayList<>(recipeItems);
            ArrayList<ItemStack> inputsCopy = new ArrayList<>(itemsInInput);
            boolean foundMatch = true;
            while (foundMatch) {
                foundMatch = findMatchAndRemove(recipeCopy, inputsCopy);
            }
            if (recipeCopy.size() > 0 || inputsCopy.size() > 0) {
                return false;
            }
        } else {
            // Shaped
            for (int i = 0; i < recipeItems.size(); i++) {
                ItemStack item = inv.getItem(i);
                if (item.isDamageableItem() && item.isDamaged()) {
                    return false;
                }
                if (!recipeItems.get(i).test(item)) {
                    return false;
                }
            }
        }
        if (extraIngredient != null && !extraIngredient.ingredient.isEmpty()) {
            if (inv.getContainerSize() == 1) {
                return false;
            }
            if (inv.getContainerSize() > RefTableConsts.inputSlots) {
                ItemStack item = inv.getItem(RefTableConsts.inputSlots);
                return extraIngredient.ingredient.test(item);
            }
        }

        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    public ExtraInput getExtraIngredient() {
        return this.extraIngredient;
    }

    public boolean requiresCooking() {
        return this.cook;
    }

    @Override
    public ItemStack getResultItem() {
        return this.output.copy();
    }

    public Secondary getSecondaryResultItem() {
        return this.secondaryOutput;
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
        return RecipesInit.GLIDE_BOARD_SERIALIZER.get();
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<GlideBoardRecipe> {

        @Override
        public GlideBoardRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ItemStack output = ShapedRecipe.itemFromJson(GsonHelper.getAsJsonObject(json, "output")).getDefaultInstance();
            Secondary secondary;
            if (json.has("secondary")) {
                JsonObject j = json.getAsJsonObject("secondary");
                ItemStack secondaryOutput = ShapedRecipe.itemFromJson(j.getAsJsonObject("output")).getDefaultInstance();
                double secondaryChance = j.get("chance").getAsDouble();
                secondary = GlideBoardRecipe.Secondary.of(secondaryOutput, secondaryChance);
            } else {
                secondary = GlideBoardRecipe.Secondary.none();
            }

            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(ingredients.size(), Ingredient.EMPTY);

            for (int i = 0; i < ingredients.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            ExtraInput extra = ExtraInput.EMPTY;
            if (json.has("extra")) {
                JsonObject x = json.getAsJsonObject("extra");
                Ingredient i = Ingredient.fromJson(x);
                boolean consume = false;
                if (x.has("consume")) {
                    consume = x.get("consume").getAsBoolean();
                }
                extra = new ExtraInput(i, consume);
            }
            boolean cook = json.get("cook").getAsBoolean();


            return new GlideBoardRecipe(recipeId, output, inputs, cook, extra, secondary);
        }

        @Nullable
        @Override
        public GlideBoardRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            int rSize = buffer.readInt();
            NonNullList<Ingredient> inputs = NonNullList.withSize(rSize, Ingredient.EMPTY);
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buffer));
            }
            Ingredient extraIngredient = Ingredient.fromNetwork(buffer);
            boolean consumeExtra = buffer.readBoolean();
            ExtraInput extra = new ExtraInput(extraIngredient, consumeExtra);
            boolean cook = buffer.readBoolean();

            ItemStack output = buffer.readItem();
            output = output.getItem().getDefaultInstance();

            ItemStack secondaryItem = buffer.readItem();
            double secondaryChance = buffer.readDouble();
            Secondary secondary = Secondary.of(secondaryItem, secondaryChance);

            return new GlideBoardRecipe(recipeId, output, inputs, cook, extra, secondary);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, GlideBoardRecipe recipe) {
            buffer.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buffer);
            }
            recipe.getExtraIngredient().ingredient.toNetwork(buffer);
            buffer.writeBoolean(recipe.getExtraIngredient().consumeOnUse);
            buffer.writeBoolean(recipe.requiresCooking());
            buffer.writeItem(recipe.getResultItem());
            buffer.writeItem(recipe.getSecondaryResultItem().output);
            buffer.writeDouble(recipe.getSecondaryResultItem().chance);
        }
    }

    public static class Type implements RecipeType<GlideBoardRecipe> {
        @Override
        public String toString() {
            return GlideBoardRecipe.TYPE_ID.toString();
        }
    }

    public static class Secondary {
        public final ItemStack output;
        public final double chance;

        public static Secondary none() {
            return new Secondary(ItemStack.EMPTY, 0);
        }

        public static Secondary of(
                ItemStack secondaryOutput, double secondaryChance
        ) {
            return new Secondary(secondaryOutput, secondaryChance);
        }

        private Secondary(ItemStack secondaryOutput, double secondaryChance) {
            this.output = secondaryOutput;
            this.chance = secondaryChance;
        }
    }

    public static class ExtraInput {

        public static final ExtraInput EMPTY = new ExtraInput(Ingredient.EMPTY, false);
        public final Ingredient ingredient;
        public final boolean consumeOnUse;

        public ExtraInput(Ingredient ingredient, boolean consumeOnUse) {
            this.ingredient = ingredient;
            this.consumeOnUse = consumeOnUse;
        }
    }
}
