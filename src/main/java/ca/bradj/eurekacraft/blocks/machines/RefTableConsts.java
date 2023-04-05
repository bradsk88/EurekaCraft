package ca.bradj.eurekacraft.blocks.machines;

import ca.bradj.eurekacraft.core.init.items.ItemsInit;
import ca.bradj.eurekacraft.core.init.items.WheelItemsInit;
import ca.bradj.eurekacraft.data.recipes.RefTableRecipe;
import ca.bradj.eurekacraft.materials.Blueprints;
import ca.bradj.eurekacraft.vehicles.RefBoardStats;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Random;

public class RefTableConsts {
    public static final int inputSlotIndex = 0;
    public static final int inputSlots = 6;
    public static final int fuelSlot = inputSlots;
    public static final int techSlot = fuelSlot + 1;
    public static final int outputSlot = techSlot + 1;
    public static final int secondaryOutputSlot = outputSlot + 1;
    public static final int totalSlots = secondaryOutputSlot + 1;
    public static final String NBT_SPAWNED_WITH_RECIPE = "spawned_with_recipe";

    public interface RecipeProvider {
        RefTableRecipe get(Random random);
    }

    // TODO: Add all ref table recipes
    public static final ImmutableList<RecipeProvider> spawnRecipes = ImmutableList.copyOf(new RecipeProvider[]{
            // Bearing mold
            (random) -> {
                ItemStack bp = ItemsInit.BLUEPRINT.get().getDefaultInstance().copy();
                Blueprints.FACTORY_INSTANCE.
                        WithFallback(RefBoardStats.BadBoard).
                        getBoardStatsFromNBTOrCreate(
                                bp, RefBoardStats.BadBoard, random
                        );
                NonNullList<Ingredient> inputs = NonNullList.createWithCapacity(2);
                inputs.addAll(ImmutableList.of(
                        Ingredient.of(new ItemStack(ItemsInit.BROKEN_BOARD.get(), 1)),
                        Ingredient.of(new ItemStack(WheelItemsInit.OAK_WOOD_WHEEL_ITEM.get(), 1))
                ));
                return new RefTableRecipe(
                        null,
                        ItemStack.EMPTY,
                        inputs,
                        true,
                        new RefTableRecipe.ExtraInput(
                                Ingredient.of(new ItemStack(WheelItemsInit.SOCKET_WRENCH.get(), 1)), false
                        ),
                        RefTableRecipe.Secondary.none(),
                        1,
                        RefTableRecipe.ConstructStats.NEW
                );
            },
            // Board wheel
            (random) -> {
                ItemStack bp = ItemsInit.BLUEPRINT.get().getDefaultInstance().copy();
                Blueprints.FACTORY_INSTANCE.
                        WithFallback(RefBoardStats.BadBoard).
                        getBoardStatsFromNBTOrCreate(
                                bp, RefBoardStats.BadBoard, random
                        );
                NonNullList<Ingredient> inputs = NonNullList.createWithCapacity(4);
                inputs.addAll(ImmutableList.of(
                        Ingredient.of(new ItemStack(Items.OAK_PLANKS, 1)),
                        Ingredient.of(new ItemStack(ItemsInit.RESINOUS_DUST.get())),
                        Ingredient.of(new ItemStack(Items.OAK_PLANKS, 1)),
                        Ingredient.of(new ItemStack(Items.OAK_PLANKS, 1))
                ));
                return new RefTableRecipe(
                        null,
                        Items.COBWEB.getDefaultInstance(),
                        inputs,
                        true,
                        new RefTableRecipe.ExtraInput(
                                Ingredient.of(bp), true
                        ),
                        RefTableRecipe.Secondary.none(),
                        1,
                        RefTableRecipe.ConstructStats.NEW
                );
            }
    });
}
