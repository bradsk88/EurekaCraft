package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.interfaces.IBoardStatsFactory;
import ca.bradj.eurekacraft.vehicles.RefBoardStats;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class Blueprints {

    public static final IBoardStatsFactory FACTORY_INSTANCE = new BoardStatsFactory();
    public static final String NBT_KEY_BOARD_STATS = "board_stats";

    public static void applyAsTechItem(
            Class<? extends Item> blueprintClass,
            RefBoardStats reference,
            Collection<ItemStack> inputs,
            ItemStack blueprint,
            ItemStack target,
            Random random
    ) {
        // TODO: Update this function so we can use the best blueprints (or an average?) as the basis for randomization

        if (!(blueprint.getItem() instanceof BlueprintPoorItem)) {
            return;
        }
        if (target.getTag() == null) {
            target.setTag(new CompoundTag());
        }

        ArrayList<RefBoardStats> inputStats = new ArrayList<>();
        for (ItemStack item : inputs) {
            if (item.getItem() instanceof BlueprintPoorItem) { // TODO: Maybe take an interface?
                inputStats.add(FACTORY_INSTANCE.getBoardStatsFromNBTOrCreate(
                        item,
                        reference,
                        random
                ));
            }
        }

        RefBoardStats existingStats = RefBoardStats.Average(
                "blueprint",
                inputStats
        );
        RefBoardStats newStats = RefBoardStats.FromReferenceWithRandomOffsets(
                existingStats,
                random
        );
        newStats = RefBoardStats.FromReferenceWithRandomBoost(
                newStats,
                random,
                1.1f
        );
        newStats = newStats.WithWeight(reference.weight()); // TODO: Get from the board itself

        target.getTag()
                .put(
                        NBT_KEY_BOARD_STATS,
                        RefBoardStats.serializeNBT(newStats)
                );
    }

    public static RefBoardStats getBoardStatsFromNBTOrCreate(
            ItemStack itemStack,
            RefBoardStats reference,
            Random rand
    ) {
        return FACTORY_INSTANCE.getBoardStatsFromNBTOrCreate(
                itemStack,
                reference,
                rand
        );
    }

    private static boolean stackHasStats(ItemStack stack) {
        return stack.getOrCreateTag()
                .contains(NBT_KEY_BOARD_STATS);
    }

    private static CompoundTag getStats(ItemStack stack) {
        return stack.getOrCreateTag()
                .getCompound(NBT_KEY_BOARD_STATS);
    }

    public static void appendHoverText(
            RefBoardStats defaultStats,
            ItemStack stack,
            Level world,
            List<Component> tooltip
    ) {
        if (!Blueprints.stackHasStats(stack)) {
            if (world != null) {
                Blueprints.getBoardStatsFromNBTOrCreate(
                        stack,
                        defaultStats,
                        world.getRandom()
                );
            }
        }
        RefBoardStats stats = RefBoardStats.deserializeNBT(Blueprints.getStats(stack));
        tooltip.add(new TextComponent("Speed: " + (int) (stats.speed() * 100))); // TODO: Translate
        tooltip.add(new TextComponent("Agility: " + (int) (stats.agility() * 100))); // TODO: Translate
        tooltip.add(new TextComponent("Lift: " + (int) (stats.lift() * 100))); // TODO: Translate
    }

    public static class BoardStatsFactory implements IBoardStatsFactory {
        @Override
        public RefBoardStats getBoardStatsFromNBTOrCreate(
                ItemStack itemStack,
                RefBoardStats creationReference,
                Random rand
        ) {
            if (itemStack.getTag() == null) {
                itemStack.setTag(new CompoundTag());
            }
            if (itemStack.getTag()
                    .contains(NBT_KEY_BOARD_STATS)) {
                return RefBoardStats.deserializeNBT(itemStack.getTag()
                        .getCompound(NBT_KEY_BOARD_STATS));
            }
            RefBoardStats s = RefBoardStats.FromReferenceWithRandomOffsets(
                    creationReference,
                    rand
            );
            itemStack.getTag()
                    .put(
                            NBT_KEY_BOARD_STATS,
                            RefBoardStats.serializeNBT(s)
                    );
            return s;
        }
    }
}
