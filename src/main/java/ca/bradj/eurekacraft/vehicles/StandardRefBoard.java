package ca.bradj.eurekacraft.vehicles;

import ca.bradj.eurekacraft.core.init.items.ItemsInit;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

import static ca.bradj.eurekacraft.materials.BlueprintItem.NBT_KEY_BOARD_STATS;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;

public class StandardRefBoard extends RefBoardItem {

    public static boolean debuggerReleaseControl() {
        GLFW.glfwSetInputMode(Minecraft.getInstance().getWindow().getWindow(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        return true;
    }

    public static final BoardType ID = new BoardType("standard_ref_board");

    public StandardRefBoard() {
        super(RefBoardStats.StandardBoard, ID);
    }

    public static ItemStack getWithRandomStats(Random rand) {
        ItemStack i = ItemsInit.STANDARD_REF_BOARD.get().getDefaultInstance();
        RefBoardStats newStats = RefBoardStats.FromReferenceWithRandomOffsets(RefBoardStats.StandardBoard, rand);
        storeStatsOnStack(i, newStats);
        i.getOrCreateTag().put(NBT_KEY_BOARD_STATS, RefBoardStats.serializeNBT(newStats));
        return i;
    }

    public static ItemStack getWithRandomBadStats(Random rand) {
        ItemStack i = ItemsInit.STANDARD_REF_BOARD.get().getDefaultInstance();
        RefBoardStats newStats = RefBoardStats.FromReferenceWithRandomOffsets(RefBoardStats.BadBoard, rand);
        storeStatsOnStack(i, newStats);
        i.getOrCreateTag().put(NBT_KEY_BOARD_STATS, RefBoardStats.serializeNBT(newStats));
        return i;
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack defaultInstance = super.getDefaultInstance();
        storeStatsOnStack(defaultInstance, RefBoardStats.StandardBoard);
        return defaultInstance;
    }
}
