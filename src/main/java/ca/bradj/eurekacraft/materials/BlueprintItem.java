package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.interfaces.IBoardStatsFactory;
import ca.bradj.eurekacraft.interfaces.IBoardStatsFactoryProvider;
import ca.bradj.eurekacraft.interfaces.ITechAffected;
import ca.bradj.eurekacraft.vehicles.RefBoardStats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

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
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.getTag() == null || !stack.getTag().contains(NBT_KEY_BOARD_STATS)) {
            tooltip.add(new StringTextComponent("Speed: ???")); // TODO: Translate
            tooltip.add(new StringTextComponent("Agility: ???")); // TODO: Translate
            tooltip.add(new StringTextComponent("Lift: ???")); // TODO: Translate
        } else {
            RefBoardStats stats = RefBoardStats.deserializeNBT(stack.getTag().getCompound(NBT_KEY_BOARD_STATS));
            tooltip.add(new StringTextComponent("Speed: " + (int) (stats.speed() * 100))); // TODO: Translate
            tooltip.add(new StringTextComponent("Agility: " + (int) (stats.agility() * 100))); // TODO: Translate
            tooltip.add(new StringTextComponent("Lift: " + (int) (stats.lift() * 100))); // TODO: Translate
        }
    }

    @Override
    public void applyTechItem(Collection<ItemStack> inputs, ItemStack blueprint, ItemStack target, Random random) {
        // TODO: Update this function so we can use the best blueprints (or an average?) as the basis for randomization

        if (!(blueprint.getItem() instanceof BlueprintItem)) {
            return;
        }
        if (target.getTag() == null) {
            target.setTag(new CompoundNBT());
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
                itemStack.setTag(new CompoundNBT());
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
