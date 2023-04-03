package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.interfaces.IBoardStatsFactory;
import ca.bradj.eurekacraft.vehicles.RefBoardStats;
import ca.bradj.eurekacraft.vehicles.RefBoardStatsUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class Blueprints {

    public static final String NBT_KEY_BOARD_STATS = "board_stats";

    public static final BoardStatsFactory FACTORY_INSTANCE = new BoardStatsFactory();

    public static Collection<Component> getTooltips(
            Optional<RefBoardStats> stats,
            RefBoardStats defaults
    ) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(new TranslatableComponent("item.eurekacraft.blueprints.subtitle_1").withStyle(ChatFormatting.GRAY));
        tooltip.add(new TranslatableComponent("item.eurekacraft.blueprints.subtitle_2").withStyle(ChatFormatting.GRAY));
        tooltip.addAll(RefBoardStatsUtils.getTooltips(stats, defaults));
        return tooltip;
    }

    public static class BoardStatsFactory implements IBoardStatsFactory {
        private final RefBoardStats stats;

        public BoardStatsFactory() {
            this(RefBoardStats.StandardBoard);
        }

        private BoardStatsFactory(RefBoardStats stats) {
            this.stats = stats;
        }

        @Override
        public RefBoardStats getBoardStatsFromNBTOrCreate(
                ItemStack itemStack, RefBoardStats creationReference, Random rand
        ) {
            if (itemStack.getTag() == null) {
                itemStack.setTag(new CompoundTag());
            }
            if (itemStack.getTag().contains(NBT_KEY_BOARD_STATS)) {
                return RefBoardStats.deserializeNBT(
                        itemStack.getTag().getCompound(NBT_KEY_BOARD_STATS)
                ).orElse(this.stats);
            }
            RefBoardStats s = RefBoardStats.FromReferenceWithRandomOffsets(creationReference, rand);
            itemStack.getTag().put(NBT_KEY_BOARD_STATS, RefBoardStats.serializeNBT(s));
            return s;
        }

        public BoardStatsFactory WithFallback(RefBoardStats stats) {
            return new BoardStatsFactory(stats);
        }
    }
}
