package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.advancements.BoardTrickTrigger;
import ca.bradj.eurekacraft.advancements.FreshCropHarvestTrigger;
import ca.bradj.eurekacraft.advancements.RefTableTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AdvancementsInit {

    public static final DeferredRegister<Item> ICON_ITEMS = DeferredRegister.create(
            ForgeRegistries.ITEMS, EurekaCraft.MODID
    );

    public static final class IDs {
        public static final String FirstFlight = "trick_first_flight";
    }

    static {
        ICON_ITEMS.<Item>register(IDs.FirstFlight, AdvancementItem.FirstFlight::new);
    }

    public static RefTableTrigger REF_TABLE_TRIGGER;
    public static FreshCropHarvestTrigger FRESH_CROPS_HARVEST_TRIGGER;
    public static BoardTrickTrigger BOARD_TRICK_TRIGGER;

    public static void register() {
        REF_TABLE_TRIGGER = CriteriaTriggers.register(new RefTableTrigger());
        FRESH_CROPS_HARVEST_TRIGGER = CriteriaTriggers.register(new FreshCropHarvestTrigger());
        BOARD_TRICK_TRIGGER = CriteriaTriggers.register(new BoardTrickTrigger());
    }

    public static abstract class AdvancementItem extends Item {
        public static class FirstFlight extends AdvancementItem {}

        public AdvancementItem() {
            super(new Properties());
        }
    }

    public static void registerIconItems(IEventBus bus) {
        ICON_ITEMS.register(bus);
    }
}
