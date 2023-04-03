package ca.bradj.eurekacraft.blocks.machines;

import ca.bradj.eurekacraft.core.init.items.ItemsInit;
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

    public static final ImmutableList<RecipeProvider> spawnRecipes = ImmutableList.copyOf(new RecipeProvider[]{
            (random) -> {
                ItemStack bp = ItemsInit.BLUEPRINT.get().getDefaultInstance().copy();
                Blueprints.FACTORY_INSTANCE.
                        WithFallback(RefBoardStats.BadBoard).
                        getBoardStatsFromNBTOrCreate(
                                bp, RefBoardStats.BadBoard, random
                        );
                return new RefTableRecipe(
                        null,
                        Items.COBWEB.getDefaultInstance(),
                        NonNullList.of(
                                Ingredient.of(Items.OAK_PLANKS.getDefaultInstance()),
                                Ingredient.of(ItemsInit.RESINOUS_DUST.get().getDefaultInstance()),
                                Ingredient.of(Items.OAK_PLANKS.getDefaultInstance()),
                                Ingredient.of(Items.OAK_PLANKS.getDefaultInstance())
                        ),
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
