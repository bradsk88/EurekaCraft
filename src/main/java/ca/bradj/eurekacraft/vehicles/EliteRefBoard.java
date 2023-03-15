package ca.bradj.eurekacraft.vehicles;

import ca.bradj.eurekacraft.core.init.items.ItemsInit;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;

public class EliteRefBoard extends RefBoardItem {

    public static boolean debuggerReleaseControl() {
        GLFW.glfwSetInputMode(Minecraft.getInstance().getWindow().getWindow(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        return true;
    }

    public static final String ITEM_ID = "elite_ref_board";
    public static final BoardType ID = new BoardType(ITEM_ID);

    public EliteRefBoard() {
        super(RefBoardStats.EliteBoard, ID);
    }

    public static ItemStack getWithRandomStats(Random rand) {
        ItemStack i = ItemsInit.ELITE_BOARD.get().getDefaultInstance();
        RefBoardStats newStats = RefBoardStats.FromReferenceWithRandomOffsets(RefBoardStats.EliteBoard, rand);
        storeStatsOnStack(i, newStats);
        return i;
    }

    @Override
    public boolean isFoil(ItemStack p_77636_1_) {
        return true;
    }
}
