package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.core.init.items.ItemsInit;
import ca.bradj.eurekacraft.interfaces.*;
import ca.bradj.eurekacraft.vehicles.RefBoardStats;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;

public class BlueprintItem extends Item implements IBoardStatsFactoryProvider, ITechAffected, IInitializable, IBoardStatsCraftable, IBoardStatsGetter {

    public static boolean debuggerReleaseControl() {
        GLFW.glfwSetInputMode(Minecraft.getInstance().getWindow().getWindow(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        return true;
    }

    public static final String NBT_KEY_BOARD_STATS = "board_stats";
    private static final IBoardStatsFactory FACTORY_INSTANCE = new BoardStatsFactory();

    public static final String ITEM_ID = "blueprint";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public static ItemStack getRandom(RandomSource rand) {
        ItemStack i = ItemsInit.BLUEPRINT.get().getDefaultInstance();
        FACTORY_INSTANCE.getBoardStatsFromNBTOrCreate(i, RefBoardStats.StandardBoard, rand);
        return i;
    }

    public BlueprintItem() {
        super(PROPS);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
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
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        // FIXME: This is causing stats to get overridden every time we add and remove them from an inventory
//        if (!stack.getOrCreateTag().contains(NBT_KEY_BOARD_STATS)) {
//            if (world != null) {
//                FACTORY_INSTANCE.getBoardStatsFromNBTOrCreate(stack, RefBoardStats.StandardBoard, world.getRandom());
//            }
//        }
        RefBoardStats stats = getBoardStats(stack);
        tooltip.add(Component.literal("Speed: " + (int) (stats.speed() * 100))); // TODO: Translate
        tooltip.add(Component.literal("Agility: " + (int) (stats.agility() * 100))); // TODO: Translate
        tooltip.add(Component.literal("Lift: " + (int) (stats.lift() * 100))); // TODO: Translate
    }

    @Override
    public void applyTechItem(Collection<ItemStack> inputs, ItemStack blueprint, ItemStack target, RandomSource random) {
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

    @Override
    public void initialize(
            ItemStack target,
            RandomSource random
    ) {
        RefBoardStats newStats = RefBoardStats.FromReferenceWithRandomOffsets(RefBoardStats.StandardBoard, random);
        target.getOrCreateTag().put(NBT_KEY_BOARD_STATS, RefBoardStats.serializeNBT(newStats));
    }

    @Override
    public void generateNewBoardStats(
            ItemStack target,
            Collection<ItemStack> context,
            RandomSource random
    ) {
        Collection<RefBoardStats> contextStats = context.stream().
                filter(v -> v.getItem() instanceof IBoardStatsGetter).
                map(v -> ((IBoardStatsGetter) v.getItem()).getBoardStats(v)).toList();
        RefBoardStats stats = RefBoardStats.FromReferenceWithRandomOffsets(RefBoardStats.StandardBoard, random);
        if (contextStats.size() != 0) {
            stats = RefBoardStats.Average(
                    "avg",
                    contextStats
            );
        }
        target.getOrCreateTag().put(NBT_KEY_BOARD_STATS, RefBoardStats.serializeNBT(stats));
    }

    public static class BoardStatsFactory implements IBoardStatsFactory {
        @Override
        public RefBoardStats getBoardStatsFromNBTOrCreate(
                ItemStack itemStack, RefBoardStats creationReference, RandomSource rand
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
