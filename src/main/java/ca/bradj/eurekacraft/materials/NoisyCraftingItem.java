package ca.bradj.eurekacraft.materials;

import net.minecraft.util.SoundEvent;

import java.util.Optional;

public interface NoisyCraftingItem {

    Optional<SoundEvent> getCraftingSound();

}
