package ca.bradj.eurekacraft.interfaces;

import net.minecraft.world.item.Item;

import java.util.Collection;
import java.util.Optional;

public interface SandingMachineSlotAware {

    enum Slot {
        INGREDIENT,
        GRIT,
    }

    Optional<Slot> getIdealSlot(Collection<Item> currentInputs, Item currentGrit);
}
