package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.client.gui.SandingMachineScreen;
import ca.bradj.eurekacraft.data.recipes.GlideBoardRecipe;
import ca.bradj.eurekacraft.data.recipes.SandingMachineRecipe;
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

    public static final RegistryObject<SandingMachineRecipe.Serializer> SANDING_MACHINE_SERIALIZER = RECIPES.register(
            "sanding_machine", SandingMachineRecipe.Serializer::new
    );
    public static IRecipeType<SandingMachineRecipe> SANDING_MACHINE = new SandingMachineRecipe.Type();


    public static void register(IEventBus bus) {
        RECIPES.register(bus);
        Registry.register(Registry.RECIPE_TYPE, GlideBoardRecipe.TYPE_ID, GLIDE_BOARD);
        Registry.register(Registry.RECIPE_TYPE, SandingMachineRecipe.TYPE_ID, SANDING_MACHINE);
    }

}
