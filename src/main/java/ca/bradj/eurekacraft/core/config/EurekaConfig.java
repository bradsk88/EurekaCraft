package ca.bradj.eurekacraft.core.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class EurekaConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final String KEY_CRASH_IF_NOFLIGHT = "Crash if flight disabled";

    public static final ForgeConfigSpec.BooleanValue crash_if_flight_disabled;
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

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
