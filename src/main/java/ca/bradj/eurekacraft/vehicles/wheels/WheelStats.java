package ca.bradj.eurekacraft.vehicles.wheels;

import ca.bradj.eurekacraft.vehicles.EurekaCraftItem;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WheelStats {

    public static final WheelStats NONE = new WheelStats(0, 0);
    private static final Map<String, WheelStats> STATS;

    static {
        Map<String, WheelStats> map = new HashMap<>();
        map.put(OakWheelItem.ITEM_ID, new WheelStats(10, 10));
        map.put(StoneWheelItem.ITEM_ID, new WheelStats(15, 20));
        map.put(IronWheelItem.ITEM_ID, new WheelStats(25, 50));
        map.put(GoldWheelItem.ITEM_ID, new WheelStats(50, 100));
        map.put(DiamondWheelItem.ITEM_ID, new WheelStats(100, 100));
        STATS = ImmutableMap.copyOf(map);
    }

    public final int acceleration;
    public final int braking;

    private WheelStats(int acceleration, int braking) {
        this.acceleration = acceleration;
        this.braking = braking;
    }

    public static WheelStats GetStatsFromNBT(ItemStack item) {
        Optional<Wheel> wheel = BoardWheels.FromStack(item);
        if (wheel.isEmpty()) {
            return NONE;
        }
        EurekaCraftItem wheelItem = wheel.get();
        return STATS.get(wheelItem.getItemId());
    }
}
