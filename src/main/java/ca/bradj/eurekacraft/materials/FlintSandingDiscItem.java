package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.interfaces.SandingMachineSlotAware;
import ca.bradj.eurekacraft.world.NoisyItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;

import java.util.Collection;
import java.util.Optional;

public class FlintSandingDiscItem extends Item implements NoisyCraftingItem, SandingMachineSlotAware {
    public static final String ITEM_ID = "flint_sanding_disc";
    private static final Optional<NoisyItem> CRAFTING_SOUND = Optional.of(
            new NoisyItem(8, SoundEvents.GRAVEL_STEP)
    );

    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP).
            durability(3).
            setNoRepair();

    public FlintSandingDiscItem() {
        super(PROPS);
    }

    @Override
    public Optional<NoisyItem> getCraftingSound() {
        return CRAFTING_SOUND;
    }

    @Override
    public Optional<Slot> getIdealSlot(
            Collection<Item> currentInputs,
            Item currentGrit
    ) {
        return Optional.of(Slot.GRIT);
    }
}
