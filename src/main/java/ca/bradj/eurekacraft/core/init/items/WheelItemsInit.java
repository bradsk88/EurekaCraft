package ca.bradj.eurekacraft.core.init.items;

import ca.bradj.eurekacraft.vehicles.EurekaCraftItem;
import ca.bradj.eurekacraft.vehicles.wheels.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class WheelItemsInit {

    public static RegistryObject<EurekaCraftItem> WHEEL_BEARING_ITEM;
    public static RegistryObject<EurekaCraftItem> WHEEL_PLACEHOLDER_ITEM;
    public static RegistryObject<EurekaCraftItem> WHEEL_BEARING_MOLD_ITEM;
    public static RegistryObject<EurekaCraftItem> SOCKET_WRENCH;
    public static RegistryObject<Wheel> OAK_WOOD_WHEEL_ITEM;
    public static RegistryObject<Wheel> STONE_WHEEL_ITEM;
    public static RegistryObject<Wheel> IRON_WHEEL_ITEM;
    public static RegistryObject<Wheel> GOLD_WHEEL_ITEM;
    public static RegistryObject<Wheel> DIAMOND_WHEEL_ITEM;

    public static void register(DeferredRegister<Item> items) {
        WHEEL_BEARING_ITEM = items.register(WheelBearingItem.ITEM_ID, WheelBearingItem::new);
        WHEEL_PLACEHOLDER_ITEM = items.register(WheelPlaceholderItem.ITEM_ID, WheelPlaceholderItem::new);
        WHEEL_BEARING_MOLD_ITEM = items.register(WheelBearingMoldItem.ITEM_ID, WheelBearingMoldItem::new);
        SOCKET_WRENCH = items.register(SocketWrenchItem.ITEM_ID, SocketWrenchItem::new);
        OAK_WOOD_WHEEL_ITEM = items.register(OakWheelItem.ITEM_ID, OakWheelItem::new);
        STONE_WHEEL_ITEM = items.register(StoneWheelItem.ITEM_ID, StoneWheelItem::new);
        IRON_WHEEL_ITEM = items.register(IronWheelItem.ITEM_ID, IronWheelItem::new);
        GOLD_WHEEL_ITEM = items.register(GoldWheelItem.ITEM_ID, GoldWheelItem::new);
        DIAMOND_WHEEL_ITEM = items.register(DiamondWheelItem.ITEM_ID, DiamondWheelItem::new);
    }
}
