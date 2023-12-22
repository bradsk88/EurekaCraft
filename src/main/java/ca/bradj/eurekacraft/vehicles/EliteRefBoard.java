package ca.bradj.eurekacraft.vehicles;

import ca.bradj.eurekacraft.core.init.items.ItemsInit;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.util.Collection;
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

    @Override
    protected Collection<Component> getSubtitles() {
        Collection<Component> subtitles = super.getSubtitles();
        subtitles.add(
                Component.translatable("item.eurekacraft.ref_boards.subtitle").
                        withStyle(ChatFormatting.GRAY)
        );
        return subtitles;
    }

    public static ItemStack getWithRandomStats(RandomSource rand) {
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
