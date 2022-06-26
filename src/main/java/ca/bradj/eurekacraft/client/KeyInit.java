package ca.bradj.eurekacraft.client;

import ca.bradj.eurekacraft.EurekaCraft;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;

public final class KeyInit {

    public static KeyMapping brakeFlightMapping;
    public static KeyMapping accelerateFlightMapping;

    public static void init() {
        brakeFlightMapping = registerKey("flight.brake", "key.eurekacraft.flight", InputConstants.KEY_S);
        accelerateFlightMapping = registerKey("flight.accelerate", "key.eurekacraft.flight", InputConstants.KEY_W);
    }

    private static KeyMapping registerKey(
            String name, String category, int keycode
    ) {
        final var key = new KeyMapping("key." + EurekaCraft.MODID + "." + name, keycode, category);
        ClientRegistry.registerKeyBinding(key);
        return key;
    }
}
