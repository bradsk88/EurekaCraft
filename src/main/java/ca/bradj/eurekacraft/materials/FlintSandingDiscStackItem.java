package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

import java.util.Optional;

public class FlintSandingDiscStackItem extends Item implements NoisyCraftingItem {
    public static final String ITEM_ID = "flint_sanding_disc_stack";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP).
            durability(3 * 6).
            setNoRepair();

    public FlintSandingDiscStackItem() {
        super(PROPS);
    }

    @Override
    public Optional<SoundEvent> getCraftingSound() {
        return Optional.of(SoundEvents.GRAVEL_STEP);
    }

}
