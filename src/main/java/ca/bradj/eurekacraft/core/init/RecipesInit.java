package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.data.recipes.GlideBoardRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipesInit {

    private static final DeferredRegister<IRecipeSerializer<?>> RECIPES = DeferredRegister.create(
            ForgeRegistries.RECIPE_SERIALIZERS, EurekaCraft.MODID
    );

    public static final RegistryObject<GlideBoardRecipe.Serializer> GLIDE_BOARD_SERIALIZER = RECIPES.register(
            "glide_board", GlideBoardRecipe.Serializer::new
    );

    public static IRecipeType<GlideBoardRecipe> GLIDE_BOARD = new GlideBoardRecipe.Type();


    public static void register(IEventBus bus) {
        RECIPES.register(bus);
        Registry.register(Registry.RECIPE_TYPE, GlideBoardRecipe.TYPE_ID, GLIDE_BOARD);
    }

}
