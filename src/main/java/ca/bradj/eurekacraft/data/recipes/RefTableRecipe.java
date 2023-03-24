package ca.bradj.eurekacraft.data.recipes;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.blocks.machines.RefTableConsts;
import ca.bradj.eurekacraft.core.init.RecipesInit;
import ca.bradj.eurekacraft.interfaces.IInitializable;
import ca.bradj.eurekacraft.vehicles.RefBoardStats;
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

import javax.annotation.Nullable;
import java.util.ArrayList;

public class RefTableRecipe implements IGlideBoardRecipe {

    public enum ConstructStats {
        INVALID(""),
        NEW("new"),
        BOOST_AVG("boost_avg");

        private final String val;

        ConstructStats(String val) {
            this.val = val;
        }

        public String toJSONString() {
            return this.val;
        }
    }

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    private final boolean cook;
    private final Secondary secondaryOutput;
    private final int outputQuantity;
    private final ConstructStats constructStats;
    private ExtraInput extraIngredient;

    public RefTableRecipe(
            ResourceLocation id,
            ItemStack output,
            NonNullList<Ingredient> recipeItems,
            boolean cook,
            ExtraInput extraIngredient,
            Secondary secondary,
            int outputQuantity,
            ConstructStats constructStats
    ) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
        this.cook = cook;
        this.extraIngredient = extraIngredient;
        this.secondaryOutput = secondary;
        this.outputQuantity = outputQuantity;
        this.constructStats = constructStats;
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
        ItemStack copy = this.output.copy();
        copy.setCount(this.outputQuantity);
        return copy;
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

    @Override
    public int getOutputQuantity() {
        return outputQuantity;
    }

    @Override
    public ConstructStats getOutputConstructStatsPolicy() {
        return this.constructStats;
    }

    public static class Serializer implements RecipeSerializer<RefTableRecipe> {

        @Override
        public RefTableRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            JsonObject outputJSON = GsonHelper.getAsJsonObject(json, "output");
            ItemStack output = ShapedRecipe.itemFromJson(outputJSON).getDefaultInstance();
            int outputQty = 1;
            boolean initializeMain = false;
            if (outputJSON.has("quantity")) {
                outputQty = outputJSON.get("quantity").getAsInt();
            }
            ConstructStats outputStats = ConstructStats.INVALID;
            if (outputJSON.has("stats")) {
                String outputStatsStr = outputJSON.get("stats").getAsString();
                if ("new".equals(outputStatsStr)) {
                    outputStats = ConstructStats.NEW;
                } else if ("boost_avg".equals(outputStatsStr)) {
                    outputStats = ConstructStats.BOOST_AVG;
                } else {
                    throw new IllegalArgumentException("Unexpected value for \"stats\": " + outputStatsStr);
                }
            }
            Secondary secondary;
            if (json.has("secondary")) {
                JsonObject j = json.getAsJsonObject("secondary");
                ItemStack secondaryOutput = ShapedRecipe.itemFromJson(j.getAsJsonObject("output")).getDefaultInstance();
                double secondaryChance = 0.0;
                if (j.has("chance")) {
                    secondaryChance = j.get("chance").getAsDouble();
                }
                boolean initialize = false;
                if (j.has("initialize")) {
                    initialize = j.get("initialize").getAsBoolean();
                }
                secondary = RefTableRecipe.Secondary.of(secondaryOutput, secondaryChance, initialize);
            } else {
                secondary = RefTableRecipe.Secondary.none();
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


            return new RefTableRecipe(recipeId, output, inputs, cook, extra, secondary, outputQty, outputStats);
        }

        @Nullable
        @Override
        public RefTableRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
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

            int outputQuantity = buffer.readInt();
            String outputStatsStr = buffer.readUtf();

            ItemStack secondaryItem = buffer.readItem();
            double secondaryChance = buffer.readDouble();
            boolean initialize = buffer.readBoolean();

            Secondary secondary = Secondary.of(secondaryItem, secondaryChance, initialize);

            RefTableRecipe.ConstructStats outputStats;
            if ("new".equals(outputStatsStr)) {
                outputStats = ConstructStats.NEW;
            } else if ("boost_avg".equals(outputStatsStr)) {
                outputStats = ConstructStats.BOOST_AVG;
            } else {
                throw new IllegalArgumentException("Unexpected value for \"stats\": " + outputStatsStr);
            }

            return new RefTableRecipe(recipeId, output, inputs, cook, extra, secondary, outputQuantity, outputStats);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, RefTableRecipe recipe) {
            buffer.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buffer);
            }
            recipe.getExtraIngredient().ingredient.toNetwork(buffer);
            buffer.writeBoolean(recipe.getExtraIngredient().consumeOnUse);
            buffer.writeBoolean(recipe.requiresCooking());
            buffer.writeItem(recipe.getResultItem());
            buffer.writeInt(recipe.getOutputQuantity());
            buffer.writeUtf(recipe.constructStats.toJSONString());
            buffer.writeItem(recipe.getSecondaryResultItem().output);
            buffer.writeDouble(recipe.getSecondaryResultItem().chance);
            buffer.writeBoolean(recipe.getSecondaryResultItem().initialize);
        }
    }

    public static class Type implements RecipeType<RefTableRecipe> {
        public static final Type INSTANCE = new Type();
        public static final ResourceLocation ID = new ResourceLocation(EurekaCraft.MODID, "glide_board");
    }

    public static class Secondary {
        public final ItemStack output;
        public final double chance;
        public boolean initialize;

        public static Secondary none() {
            return new Secondary(ItemStack.EMPTY, 0, false);
        }

        public static Secondary of(
                ItemStack secondaryOutput, double secondaryChance, boolean initialize
        ) {
            return new Secondary(secondaryOutput, secondaryChance, initialize);
        }

        private Secondary(ItemStack secondaryOutput, double secondaryChance, boolean initialize) {
            this.output = secondaryOutput;
            this.chance = secondaryChance;
            this.initialize = initialize;
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
