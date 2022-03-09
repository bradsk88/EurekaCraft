package ca.bradj.eurekacraft.world;

import net.minecraft.util.SoundEvent;

public class NoisyItem {
    public final int noiseCooldown;
    public final SoundEvent event;

    public NoisyItem(int tickBuffer, SoundEvent event) {
        this.noiseCooldown = tickBuffer;
        this.event = event;
    }
}
