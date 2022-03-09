package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.world.NoisyItem;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvents;

import java.util.Optional;

public class FlintSandingDiscItem extends Item implements NoisyCraftingItem {
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

}
