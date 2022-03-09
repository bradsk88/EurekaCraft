package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.world.NoisyItem;

import java.util.Optional;

public interface NoisyCraftingItem {

    Optional<NoisyItem> getCraftingSound();

}
