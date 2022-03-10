package ca.bradj.eurekacraft.core.init;

import ca.bradj.eurekacraft.advancements.FreshCropHarvestTrigger;
import ca.bradj.eurekacraft.advancements.RefTableTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class AdvancementsInit {

    public static RefTableTrigger REF_TABLE_TRIGGER;
    public static FreshCropHarvestTrigger FRESH_CROPS_HARVEST_TRIGGER;

    public static void register() {
        REF_TABLE_TRIGGER = CriteriaTriggers.register(new RefTableTrigger());
        FRESH_CROPS_HARVEST_TRIGGER = CriteriaTriggers.register(new FreshCropHarvestTrigger());
    }

}
