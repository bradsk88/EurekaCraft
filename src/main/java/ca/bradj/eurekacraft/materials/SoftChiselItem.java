package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.interfaces.IBoardStatsModifier;
import ca.bradj.eurekacraft.vehicles.RefBoardStats;
import ca.bradj.eurekacraft.world.NoisyItem;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvents;

import java.util.Optional;
import java.util.Random;

public class SoftChiselItem extends Item implements NoisyCraftingItem, IBoardStatsModifier {
    private static final double MAX_STAT_BOOST = 0.1;
    private static final Optional<NoisyItem> CRAFTING_SOUND = Optional.of(
            new NoisyItem(16, SoundEvents.VILLAGER_WORK_TOOLSMITH)
    );

    public static final String ITEM_ID = "soft_chisel";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP).
            durability(3 * 6).
            setNoRepair();

    public SoftChiselItem() {
        super(PROPS);
    }

    @Override
    public Optional<NoisyItem> getCraftingSound() {
        return CRAFTING_SOUND;
    }

    @Override
    public RefBoardStats modifyBoardStats(RefBoardStats stats) { // TODO: Input random
        Random random = new Random();
        return stats.
                WithWeight(stats.weight() - (random.nextDouble() * MAX_STAT_BOOST)).
                WithAgility(stats.agility() + (random.nextDouble() * MAX_STAT_BOOST));
    }
}
