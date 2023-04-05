package ca.bradj.eurekacraft.world;

import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.interfaces.RefTableSlotAware;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.Collection;
import java.util.Optional;

public class PosterBlockItem extends BlockItem implements RefTableSlotAware {

    public PosterBlockItem() {
        super(
                BlocksInit.POSTER_SPAWN_BLOCK.get(),
                new Item.Properties().tab(ModItemGroup.EUREKACRAFT_GROUP)
        );
    }

    @Override
    public Optional<Slot> getIdealSlot(
            Collection<Item> currentInputs,
            Item currentFuel,
            Item currentTech
    ) {
        return Optional.of(Slot.TECH);
    }
}
