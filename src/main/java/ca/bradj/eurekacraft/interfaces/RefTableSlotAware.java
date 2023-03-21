package ca.bradj.eurekacraft.interfaces;

import net.minecraft.world.item.Item;

import java.util.Collection;
import java.util.Optional;

public interface RefTableSlotAware {

    enum Slot {
        INGREDIENT,
        FUEL,
        TECH,
    }

    Optional<Slot> getIdealSlot(Collection<Item> currentInputs, Item currentFuel, Item currentTech);
}
