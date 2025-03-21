package ca.bradj.eurekacraft.vehicles;

import ca.bradj.eurekacraft.core.init.items.ItemsInit;
import ca.bradj.eurekacraft.integration.mc.Compat;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.util.Collection;

import static ca.bradj.eurekacraft.integration.mc.Compat.GRAY;
import static ca.bradj.eurekacraft.materials.Blueprints.NBT_KEY_BOARD_STATS;
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

    @Override
    protected Collection<Component> getSubtitles() {
        Collection<Component> subtitles = super.getSubtitles();
        subtitles.add(Compat.translatableStyled("item.eurekacraft.ref_boards.subtitle", GRAY)
        );
        return subtitles;
    }

    public static ItemStack getWithRandomStats(Compat.RandomSrc rand) {
        ItemStack i = ItemsInit.STANDARD_REF_BOARD.get().getDefaultInstance();
        RefBoardStats newStats = RefBoardStats.FromReferenceWithRandomOffsets(RefBoardStats.StandardBoard, rand);
        storeStatsOnStack(i, newStats);
        i.getOrCreateTag().put(NBT_KEY_BOARD_STATS, RefBoardStats.serializeNBT(newStats));
        return i;
    }

    public static ItemStack getWithRandomBadStats(Compat.RandomSrc rand) {
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
