package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.core.init.items.ItemsInit;
import ca.bradj.eurekacraft.interfaces.*;
import ca.bradj.eurekacraft.vehicles.RefBoardStats;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static ca.bradj.eurekacraft.materials.BlueprintItem.NBT_KEY_BOARD_STATS;

public class BlueprintAdvancedItem extends Item implements IBoardStatsFactoryProvider, IInitializable, IBoardStatsCraftable, IBoardStatsGetter {

    private static final IBoardStatsFactory FACTORY_INSTANCE = new BoardStatsFactory();

    public static final String ITEM_ID = "blueprint_advanced";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public static ItemStack getRandom(Random rand) {
        ItemStack i = ItemsInit.BLUEPRINT_ADVANCED.get().getDefaultInstance();
        FACTORY_INSTANCE.getBoardStatsFromNBTOrCreate(i, RefBoardStats.EliteBoard, rand);
        return i;
    }

    public BlueprintAdvancedItem() {
        super(PROPS);
    }

    @Override
    public IBoardStatsFactory getBoardStatsFactory() {
        return FACTORY_INSTANCE;
    }

    @Override
    public RefBoardStats getBoardStats(ItemStack stack) {
        return RefBoardStats.deserializeNBT(stack.getOrCreateTag().getCompound(NBT_KEY_BOARD_STATS));
    }

    @Override
    public void initialize(
            ItemStack target,
            Random random
    ) {
        RefBoardStats newStats = RefBoardStats.FromReferenceWithRandomOffsets(RefBoardStats.EliteBoard, random);
        target.getOrCreateTag().put(NBT_KEY_BOARD_STATS, RefBoardStats.serializeNBT(newStats));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        // FIXME: This is causing stats to get overridden every time we add and remove them from an inventory
//        if (!stack.getOrCreateTag().contains(NBT_KEY_BOARD_STATS)) {
//            if (world != null) {
//                FACTORY_INSTANCE.getBoardStatsFromNBTOrCreate(stack, RefBoardStats.StandardBoard, world.getRandom());
//            }
//        }
        RefBoardStats stats = getBoardStats(stack);
        tooltip.add(new TextComponent("Speed: " + (int) (stats.speed() * 100))); // TODO: Translate
        tooltip.add(new TextComponent("Agility: " + (int) (stats.agility() * 100))); // TODO: Translate
        tooltip.add(new TextComponent("Lift: " + (int) (stats.lift() * 100))); // TODO: Translate
    }

    @Override
    public void generateNewBoardStats(
            ItemStack target,
            Collection<ItemStack> context,
            Random random
    ) {
        Collection<RefBoardStats> contextStats = context.stream().
                filter(v -> v.getItem() instanceof IBoardStatsGetter).
                map(v -> ((IBoardStatsGetter) v.getItem()).getBoardStats(v)).toList();
        RefBoardStats stats = RefBoardStats.FromReferenceWithRandomOffsets(RefBoardStats.EliteBoard, random);
        if (contextStats.size() != 0) {
            stats = RefBoardStats.Average(
                    "avg",
                    contextStats
            );
        }
        target.getOrCreateTag().put(NBT_KEY_BOARD_STATS, RefBoardStats.serializeNBT(stats));
    }

    public static class BoardStatsFactory extends BlueprintItem.BoardStatsFactory {
        @Override
        public RefBoardStats getBoardStatsFromNBTOrCreate(ItemStack itemStack, RefBoardStats creationReference, Random rand) {
            RefBoardStats boostedReference = creationReference.WithAllIncreased(0.25);
            if (RefBoardStats.isElite(creationReference)) {
                boostedReference = creationReference;
            }
            return super.getBoardStatsFromNBTOrCreate(itemStack, boostedReference, rand);
        }
    }
}
