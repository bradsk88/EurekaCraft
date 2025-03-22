package ca.bradj.eurekacraft.core.events;

import com.google.common.collect.ImmutableList;

public class LootAdditions {

    public static final ImmutableList<String> ALL;

    static {
        ImmutableList.Builder<String> b = ImmutableList.builder();
        b.add("blueprint_from_village_savanna_chest");
        b.add("blueprint_from_village_desert_chest");
        b.add("blueprint_from_village_snowy_chest");
        b.add("blueprint_from_village_plains_chest");
        b.add("blueprint_from_village_cartographer");
        b.add("blueprint_from_village_taiga_chest");
        b.add("blueprint_from_village_toolsmith_chest");
        b.add("blueprint_from_village_weaponsmith_chest");
        b.add("photo_from_village_plains_chest");
        b.add("photo_from_village_savanna_chest");
        b.add("photo_from_village_desert_chest");
        b.add("photo_from_village_snowy_chest");
        b.add("photo_from_village_cartographer");
        b.add("photo_from_village_taiga_chest");
        b.add("photo_from_village_toolsmith_chest");
        b.add("photo_from_village_weaponsmith_chest");

        ALL = b.build();
    }

}
