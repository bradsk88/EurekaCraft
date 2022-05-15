package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.data.recipes.GlideBoardRecipe;
import ca.bradj.eurekacraft.data.recipes.SandingMachineRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipesInit {

    private static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(
            ForgeRegistries.RECIPE_SERIALIZERS, EurekaCraft.MODID
    );

    public static final RegistryObject<GlideBoardRecipe.Serializer> GLIDE_BOARD_SERIALIZER = RECIPES.register(
            "glide_board", GlideBoardRecipe.Serializer::new
    );
    public static RecipeType<GlideBoardRecipe> GLIDE_BOARD = new GlideBoardRecipe.Type();

    public static final RegistryObject<SandingMachineRecipe.Serializer> SANDING_MACHINE_SERIALIZER = RECIPES.register(
            "sanding_machine", SandingMachineRecipe.Serializer::new
    );
    public static RecipeType<SandingMachineRecipe> SANDING_MACHINE = new SandingMachineRecipe.Type();


    public static void register(IEventBus bus) {
        RECIPES.register(bus);
        Registry.register(Registry.RECIPE_TYPE, GlideBoardRecipe.TYPE_ID, GLIDE_BOARD);
        Registry.register(Registry.RECIPE_TYPE, SandingMachineRecipe.TYPE_ID, SANDING_MACHINE);
    }

}
