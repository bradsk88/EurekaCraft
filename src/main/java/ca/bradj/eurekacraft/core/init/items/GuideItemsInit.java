package ca.bradj.eurekacraft.core.init.items;

import ca.bradj.eurekacraft.guides.AeroBookItem;
import ca.bradj.eurekacraft.vehicles.EurekaCraftItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class GuideItemsInit {

    public static RegistryObject<EurekaCraftItem> AERO_BOOK_ITEM;

    public static void register(DeferredRegister<Item> items) {
        AERO_BOOK_ITEM = items.register(AeroBookItem.ITEM_ID, AeroBookItem::new);
    }

}
