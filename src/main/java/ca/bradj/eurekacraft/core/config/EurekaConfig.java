package ca.bradj.eurekacraft.core.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class EurekaConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final String KEY_CRASH_IF_NOFLIGHT = "Crash if flight disabled";
    public static final String KEY_TRAPAR_SAPLING_GROWTH_RARITY = "Fresh sapling growth rarity";
    public static final String KEY_TRAPAR_SAPLING_DROP_RARITY = "Fresh sapling drop rarity";

    public static final ForgeConfigSpec.BooleanValue crash_if_flight_disabled;
    public static final ForgeConfigSpec.IntValue fresh_sapling_growth_rarity;
    public static final ForgeConfigSpec.IntValue fresh_sapling_drop_rarity;
    public static final String FILENAME = "eurekacraft-common.toml";

    static {
        BUILDER.push("Config for EurekaCraft");

        crash_if_flight_disabled = BUILDER.comment(
                "By default, this mod will cause servers to crash if flight is " +
                        "disabled.\nThis is because this mod serves virtually no " +
                        "purpose if you cannot fly"
        ).define(
                KEY_CRASH_IF_NOFLIGHT, true
        );


        fresh_sapling_growth_rarity = BUILDER.comment(
                "Controls the rarity of naturally-grown trapar saplings. 0 is " +
                        "guaranteed/instant. Higher is more rare"
        ).defineInRange(
                KEY_TRAPAR_SAPLING_GROWTH_RARITY, 1000, 0, Integer.MAX_VALUE
        );

        fresh_sapling_drop_rarity = BUILDER.comment(
                "Controls the rarity of trapar saplings dropped from a converted trapar tree"
        ).defineInRange(
                KEY_TRAPAR_SAPLING_DROP_RARITY, 50, 0, Integer.MAX_VALUE
        );

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
