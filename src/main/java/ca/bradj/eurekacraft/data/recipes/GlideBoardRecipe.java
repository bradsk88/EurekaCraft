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
public class GlideBoardRecipe implements IGlideBoardRecipe {

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    private static final int recipeSize = 6; // TOO: Confirm
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

    @Override
    public boolean matches(IInventory inv, World p_77569_2_) {
        for (int i = 0; i < recipeItems.size(); i++) {
            if (!recipeItems.get(i).test(inv.getItem(i))) {
                return false;
            }
        }
        if (extraIngredient != null && inv.getContainerSize() > recipeItems.size()) {
            return extraIngredient.ingredient.test(inv.getItem(recipeItems.size()));
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
    public ItemStack assemble(IInventory p_77572_1_) {
        return null;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipesInit.GLIDE_BOARD_SERIALIZER.get();
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<GlideBoardRecipe> {

        @Override
        public GlideBoardRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ItemStack output = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "output"));
            Secondary secondary;
            if (json.has("secondary")) {
                JsonObject j = json.getAsJsonObject("secondary");
                ItemStack secondaryOutput = ShapedRecipe.itemFromJson(j.getAsJsonObject("output"));
                double secondaryChance = j.get("chance").getAsDouble();
                secondary = GlideBoardRecipe.Secondary.of(secondaryOutput, secondaryChance);
            } else {
                secondary = GlideBoardRecipe.Secondary.none();
            }

            JsonArray ingredients = JSONUtils.getAsJsonArray(json, "ingredients");
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
        public GlideBoardRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
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

            ItemStack secondaryItem = buffer.readItem();
            double secondaryChance = buffer.readDouble();
            Secondary secondary = Secondary.of(secondaryItem, secondaryChance);

            return new GlideBoardRecipe(recipeId, output, inputs, cook, extra, secondary);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, GlideBoardRecipe recipe) {
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

    public static class Type implements IRecipeType<GlideBoardRecipe> {
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
