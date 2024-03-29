package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.data.recipes.RefTableRecipe;
import ca.bradj.eurekacraft.data.recipes.SandingMachineRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipesInit {

    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(
            ForgeRegistries.RECIPE_SERIALIZERS, EurekaCraft.MODID
    );

    public static final RegistryObject<RefTableRecipe.Serializer> GLIDE_BOARD_SERIALIZER = SERIALIZERS.register(
            "glide_board", RefTableRecipe.Serializer::new
    );
    public static RecipeType<RefTableRecipe> REF_TABLE = new RefTableRecipe.Type();

    public static final RegistryObject<SandingMachineRecipe.Serializer> SANDING_MACHINE_SERIALIZER = SERIALIZERS.register(
            "sanding_machine", SandingMachineRecipe.Serializer::new
    );
    public static RecipeType<SandingMachineRecipe> SANDING_MACHINE = new SandingMachineRecipe.Type();


    public static void register(IEventBus bus) {
        SERIALIZERS.register(bus);
    }

    public static void registerTypes(final RegistryEvent.Register<RecipeSerializer<?>> event) {
        Registry.register(Registry.RECIPE_TYPE, RefTableRecipe.Type.ID, RefTableRecipe.Type.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, SandingMachineRecipe.Type.ID, SANDING_MACHINE);
    }

}
