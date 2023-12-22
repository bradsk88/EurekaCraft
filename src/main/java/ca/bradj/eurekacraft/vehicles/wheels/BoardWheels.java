package ca.bradj.eurekacraft.vehicles.wheels;

import ca.bradj.eurekacraft.core.init.items.WheelItemsInit;
import ca.bradj.eurekacraft.vehicles.EurekaCraftItem;
import ca.bradj.eurekacraft.vehicles.RefBoardStats;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.*;
import java.util.List;

public class BoardWheels {

    private static Map<String, Wheel> ITEMS = new HashMap<>();

    static {
        ITEMS.put(OakWheelItem.ITEM_ID, WheelItemsInit.OAK_WOOD_WHEEL_ITEM.get());
        ITEMS.put(StoneWheelItem.ITEM_ID, WheelItemsInit.STONE_WHEEL_ITEM.get());
        ITEMS.put(IronWheelItem.ITEM_ID, WheelItemsInit.IRON_WHEEL_ITEM.get());
        ITEMS.put(GoldWheelItem.ITEM_ID, WheelItemsInit.GOLD_WHEEL_ITEM.get());
        ITEMS.put(DiamondWheelItem.ITEM_ID, WheelItemsInit.DIAMOND_WHEEL_ITEM.get());
    }

    private static final String NBT_KEY = "ca.bradj.eurekcraft.board_wheel";
    private static final String TAG_KEY_ITEM_ID = "item_id";

    public static void AddToStack(ItemStack s, EurekaCraftItem c) {
        if (s.getTag() == null) {
            s.setTag(new CompoundTag());
        }
        s.getTag().put(NBT_KEY, BoardWheels.serializeNBT(c));
    }

    public static void RemoveFromStack(ItemStack s) {
        if (s.getTag() == null) {
            s.setTag(new CompoundTag());
        }
        s.getTag().remove(NBT_KEY);
    }

    public static Optional<Wheel> FromStack(ItemStack s) {
        CompoundTag tag = s.getTag();
        if (tag == null) {
            return Optional.empty();
        }
        CompoundTag wheelItem = tag.getCompound(NBT_KEY);
        return BoardWheels.deserializeNBT(wheelItem);
    }

    private static Optional<Wheel> deserializeNBT(CompoundTag tag) {
        if (!tag.contains(TAG_KEY_ITEM_ID)) {
            return Optional.empty();
        }
        String id = tag.getString(TAG_KEY_ITEM_ID);
        return Optional.of(ITEMS.get(id));
    }

    public static CompoundTag serializeNBT(EurekaCraftItem wheel) {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString(TAG_KEY_ITEM_ID, wheel.getItemId());
        return compoundTag;
    }

    public static boolean isWheel(Item item) {
        if (!(item instanceof EurekaCraftItem)) {
            return false;
        }
        return ITEMS.containsKey(((EurekaCraftItem) item).getItemId());
    }

    public static Optional<Wheel> getItem(String wheel) {
        if (ITEMS.containsKey(wheel)) {
            return Optional.of(ITEMS.get(wheel));
        }
        return Optional.empty();
    }

    public static Color GetColor(IWheel wheel) {
        if (!ITEMS.containsKey(wheel.getItemId())) {
            return Color.WHITE;
        }
        return wheel.getColor();
    }

    public static Collection<net.minecraft.network.chat.Component> getTooltips(
            WheelStats stats
    ) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.translatable("item.eurekacraft.wheels.subtitle").withStyle(ChatFormatting.GRAY));
        tooltip.add(prefix("braking", stats.braking));
        tooltip.add(prefix("acceleration", stats.acceleration));
        return tooltip;
    }

    private static Component prefix(String name, double stat) {
        return Component.translatable(
                "item.eurekacraft.wheel_stats." + name + "_prefix",
                (int) (stat)
        );
    }
}
