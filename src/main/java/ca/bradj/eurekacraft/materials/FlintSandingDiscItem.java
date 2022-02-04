package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.client.audio.Sound;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.data.SoundDefinition;

import java.util.Optional;

public class FlintSandingDiscItem extends Item implements NoisyCraftingItem {
    public static final String ITEM_ID = "flint_sanding_disc";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP).
            durability(2).
            setNoRepair();

    public FlintSandingDiscItem() {
        super(PROPS);
    }

    @Override
    public Optional<SoundEvent> getCraftingSound() {
        return Optional.of(SoundEvents.GRAVEL_STEP);
    }

}
