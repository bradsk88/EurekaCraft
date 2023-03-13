package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.core.init.items.ItemsInit;
import ca.bradj.eurekacraft.interfaces.IBoardStatsFactory;
import ca.bradj.eurekacraft.interfaces.IBoardStatsFactoryProvider;
import ca.bradj.eurekacraft.interfaces.ITechAffected;
import ca.bradj.eurekacraft.vehicles.RefBoardStats;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;

public class BlueprintItem extends Item implements IBoardStatsFactoryProvider, ITechAffected {

    public static boolean debuggerReleaseControl() {
        GLFW.glfwSetInputMode(
                Minecraft.getInstance()
                        .getWindow()
                        .getWindow(),
                GLFW_CURSOR,
                GLFW_CURSOR_NORMAL
        );
        return true;
    }


    public static final String ITEM_ID = "blueprint";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public static ItemStack getRandom(Random rand) {
        ItemStack i = ItemsInit.BLUEPRINT.get()
                .getDefaultInstance();
        Blueprints.getBoardStatsFromNBTOrCreate(
                i,
                RefBoardStats.StandardBoard,
                rand
        );
        return i;
    }

    public BlueprintItem() {
        super(PROPS);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }

    @Override
    public IBoardStatsFactory getBoardStatsFactory() {
        return Blueprints.FACTORY_INSTANCE;
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            @Nullable Level world,
            List<Component> tooltip,
            TooltipFlag flagIn
    ) {
        Blueprints.appendHoverText(
                RefBoardStats.StandardBoard,
                stack,
                world,
                tooltip
        );
    }

    @Override
    public void applyTechItem(
            Collection<ItemStack> inputs,
            ItemStack blueprint,
            ItemStack target,
            Random random
    ) {
        // TODO: Consider making blueprint stats "relative" so they affect different boards differently
        RefBoardStats reference = RefBoardStats.StandardBoard;

        Blueprints.applyAsTechItem(
                BlueprintItem.class,
                reference,
                inputs,
                blueprint,
                target,
                random
        );
    }
}
