package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ReflectionFilmInit {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EurekaCraft.MODID);

    public static final RegistryObject<Item> REFLECTION_FILM = ITEMS.register(
            ReflectionFilm.ITEM_ID, ReflectionFilm::new
    );

}
