package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.interfaces.IBoardStatsFactory;
import ca.bradj.eurekacraft.interfaces.IBoardStatsFactoryProvider;
import ca.bradj.eurekacraft.interfaces.ITechAffected;
import ca.bradj.eurekacraft.vehicles.RefBoardStats;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class BlueprintItem extends Item implements IBoardStatsFactoryProvider, ITechAffected {

    private static final String NBT_KEY_BOARD_STATS = "board_stats";
    private static final IBoardStatsFactory FACTORY_INSTANCE = new BoardStatsFactory();

    public static final String ITEM_ID = "blueprint";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public BlueprintItem() {
        super(PROPS);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }

    @Override
    public IBoardStatsFactory getBoardStatsFactory() {
        return FACTORY_INSTANCE;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        if (stack.getTag() == null || !stack.getTag().contains(NBT_KEY_BOARD_STATS)) {
            tooltip.add(new TextComponent("Speed: ???")); // TODO: Translate
            tooltip.add(new TextComponent("Agility: ???")); // TODO: Translate
            tooltip.add(new TextComponent("Lift: ???")); // TODO: Translate
        } else {
            RefBoardStats stats = RefBoardStats.deserializeNBT(stack.getTag().getCompound(NBT_KEY_BOARD_STATS));
            tooltip.add(new TextComponent("Speed: " + (int) (stats.speed() * 100))); // TODO: Translate
            tooltip.add(new TextComponent("Agility: " + (int) (stats.agility() * 100))); // TODO: Translate
            tooltip.add(new TextComponent("Lift: " + (int) (stats.lift() * 100))); // TODO: Translate
        }
    }

    @Override
    public void applyTechItem(Collection<ItemStack> inputs, ItemStack blueprint, ItemStack target, Random random) {
        // TODO: Update this function so we can use the best blueprints (or an average?) as the basis for randomization

        if (!(blueprint.getItem() instanceof BlueprintItem)) {
            return;
        }
        if (target.getTag() == null) {
            target.setTag(new CompoundTag());
        }

        // TODO: Consider making blueprint stats "relative" so they affect different boards differently
        RefBoardStats reference = RefBoardStats.StandardBoard;

        ArrayList<RefBoardStats> inputStats = new ArrayList<>();
        for (ItemStack item : inputs) {
            if (item.getItem() instanceof BlueprintItem) { // TODO: Maybe take an interface?
                inputStats.add(FACTORY_INSTANCE.getBoardStatsFromNBTOrCreate(item, reference, random));
            }
        }

        RefBoardStats existingStats = RefBoardStats.Average("blueprint", inputStats);
        RefBoardStats newStats = RefBoardStats.FromReferenceWithRandomOffsets(existingStats, random);
        target.getTag().put(NBT_KEY_BOARD_STATS, RefBoardStats.serializeNBT(newStats));
    }

    public static class BoardStatsFactory implements IBoardStatsFactory {
        @Override
        public RefBoardStats getBoardStatsFromNBTOrCreate(
                ItemStack itemStack, RefBoardStats creationReference, Random rand
        ) {
            if (itemStack.getTag() == null) {
                itemStack.setTag(new CompoundTag());
            }
            if (itemStack.getTag().contains(NBT_KEY_BOARD_STATS)) {
                return RefBoardStats.deserializeNBT(itemStack.getTag().getCompound(NBT_KEY_BOARD_STATS));
            }
            RefBoardStats s = RefBoardStats.FromReferenceWithRandomOffsets(creationReference, rand);
            itemStack.getTag().put(NBT_KEY_BOARD_STATS, RefBoardStats.serializeNBT(s));
            return s;
        }
    }
}
