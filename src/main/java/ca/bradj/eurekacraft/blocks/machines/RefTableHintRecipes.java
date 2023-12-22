package ca.bradj.eurekacraft.blocks.machines;

import ca.bradj.eurekacraft.core.init.items.ItemsInit;
import ca.bradj.eurekacraft.core.init.items.WheelItemsInit;
import ca.bradj.eurekacraft.data.recipes.RefTableRecipe;
import ca.bradj.eurekacraft.materials.Blueprints;
import ca.bradj.eurekacraft.vehicles.RefBoardStats;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.NonNullList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Random;

public class RefTableHintRecipes {
    public static final ImmutableList<RecipeProvider> spawnRecipes = ImmutableList.copyOf(new RecipeProvider[]{
            // Bearing mold
            (random) -> {
                ItemStack bp = ItemsInit.BLUEPRINT.get().getDefaultInstance().copy();
                Blueprints.FACTORY_INSTANCE.WithFallback(RefBoardStats.BadBoard)
                        .getBoardStatsFromNBTOrCreate(bp, RefBoardStats.BadBoard, random);
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
                        new RefTableRecipe.ExtraInput(Ingredient.of(new ItemStack(
                                WheelItemsInit.SOCKET_WRENCH.get(),
                                1
                        )), false),
                        RefTableRecipe.Secondary.none(),
                        1,
                        RefTableRecipe.ConstructStats.NEW
                );
            },
            // Board wheel
            (random) -> {
                ItemStack bp = ItemsInit.BLUEPRINT.get().getDefaultInstance().copy();
                Blueprints.FACTORY_INSTANCE.WithFallback(RefBoardStats.TerribleBoard)
                        .getBoardStatsFromNBTOrCreate(bp, RefBoardStats.TerribleBoard, random);
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
                        false,
                        new RefTableRecipe.ExtraInput(Ingredient.of(bp), true),
                        RefTableRecipe.Secondary.none(),
                        1,
                        RefTableRecipe.ConstructStats.NEW
                );
            },
            // Board destroy
            (random) -> {
                NonNullList<Ingredient> inputs = NonNullList.createWithCapacity(1);
                inputs.addAll(ImmutableList.of(Ingredient.of(new ItemStack(ItemsInit.BROKEN_BOARD.get()))));
                return new RefTableRecipe(
                        null,
                        Items.COBWEB.getDefaultInstance(),
                        inputs,
                        false,
                        new RefTableRecipe.ExtraInput(Ingredient.of(Items.WOODEN_AXE), true),
                        RefTableRecipe.Secondary.none(),
                        1,
                        RefTableRecipe.ConstructStats.NEW
                );
            },
            // Board shaping
            (random) -> {
                NonNullList<Ingredient> inputs = NonNullList.createWithCapacity(0);
                return new RefTableRecipe(
                        null,
                        ItemsInit.BROKEN_BOARD.get().getDefaultInstance(),
                        inputs,
                        false,
                        new RefTableRecipe.ExtraInput(Ingredient.of(ItemsInit.SOFT_CHISEL.get()), true),
                        RefTableRecipe.Secondary.of(ItemsInit.RESINOUS_DUST.get().getDefaultInstance(), 1, false),
                        1,
                        RefTableRecipe.ConstructStats.NEW
                );
            },
            // Precision wood stick
            (random) -> {
                NonNullList<Ingredient> inputs = NonNullList.createWithCapacity(2);
                inputs.addAll(ImmutableList.of(
                        Ingredient.of(new ItemStack(ItemsInit.PRECISION_WOOD.get())),
                        Ingredient.of(new ItemStack(ItemsInit.PRECISION_WOOD.get()))
                ));
                return new RefTableRecipe(
                        null,
                        Items.COBWEB.getDefaultInstance(),
                        inputs,
                        false,
                        RefTableRecipe.ExtraInput.EMPTY,
                        RefTableRecipe.Secondary.none(),
                        1,
                        RefTableRecipe.ConstructStats.NEW
                );
            },
            // Sanding disc
            (random) -> {
                NonNullList<Ingredient> inputs = NonNullList.createWithCapacity(2);
                inputs.addAll(ImmutableList.of(
                        Ingredient.of(new ItemStack(ItemsInit.FLINT_STICKY_DISC.get()))
                ));
                return new RefTableRecipe(
                        null,
                        Items.COBWEB.getDefaultInstance(),
                        inputs,
                        true,
                        RefTableRecipe.ExtraInput.EMPTY,
                        RefTableRecipe.Secondary.none(),
                        1,
                        RefTableRecipe.ConstructStats.NEW
                );
            },
            // Sanding disc (2)
            (random) -> {
                NonNullList<Ingredient> inputs = NonNullList.createWithCapacity(2);
                inputs.addAll(ImmutableList.of(
                        Ingredient.of(new ItemStack(ItemsInit.FLINT_STICKY_DISC.get())),
                        Ingredient.of(new ItemStack(ItemsInit.FLINT_STICKY_DISC.get()))
                ));
                return new RefTableRecipe(
                        null,
                        Items.COBWEB.getDefaultInstance(),
                        inputs,
                        true,
                        RefTableRecipe.ExtraInput.EMPTY,
                        RefTableRecipe.Secondary.none(),
                        1,
                        RefTableRecipe.ConstructStats.NEW
                );
            },
            // Scub goggles
            (random) -> {
                ItemStack bp = ItemsInit.BLUEPRINT.get().getDefaultInstance().copy();
                Blueprints.FACTORY_INSTANCE.WithFallback(RefBoardStats.TerribleBoard)
                        .getBoardStatsFromNBTOrCreate(bp, RefBoardStats.TerribleBoard, random);
                NonNullList<Ingredient> inputs = NonNullList.createWithCapacity(6);
                inputs.addAll(ImmutableList.of(
                        Ingredient.of(ItemsInit.SCUB_GLASS_LENS.get().getDefaultInstance()),
                        Ingredient.of(Items.COBWEB.getDefaultInstance()),
                        Ingredient.of(Items.STRING.getDefaultInstance()),
                        Ingredient.of(Items.STRING.getDefaultInstance()),
                        Ingredient.of(Items.STRING.getDefaultInstance()),
                        Ingredient.of(Items.STRING.getDefaultInstance())
                ));
                return new RefTableRecipe(
                        null,
                        ItemStack.EMPTY,
                        inputs,
                        true,
                        new RefTableRecipe.ExtraInput(Ingredient.of(bp), true),
                        RefTableRecipe.Secondary.none(),
                        1,
                        RefTableRecipe.ConstructStats.NEW
                );
            },
            // Soft chisel
            (random) -> {
                NonNullList<Ingredient> inputs = NonNullList.createWithCapacity(2);
                inputs.addAll(ImmutableList.of(
                        Ingredient.of(Items.IRON_INGOT.getDefaultInstance()),
                        Ingredient.of(ItemsInit.PRECISION_WOOD_STICK.get().getDefaultInstance())
                ));
                return new RefTableRecipe(
                        null,
                        Items.COBWEB.getDefaultInstance(),
                        inputs,
                        false,
                        RefTableRecipe.ExtraInput.EMPTY,
                        RefTableRecipe.Secondary.none(),
                        1,
                        RefTableRecipe.ConstructStats.NEW
                );
            }
    });


    public static final String NBT_SPAWNED_WITH_RECIPE = "spawned_with_recipe";

    public interface RecipeProvider {
        RefTableRecipe get(RandomSource random);
    }
}
