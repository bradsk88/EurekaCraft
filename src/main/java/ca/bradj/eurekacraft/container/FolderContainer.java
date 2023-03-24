package ca.bradj.eurekacraft.container;

import ca.bradj.eurekacraft.core.init.ContainerTypesInit;
import ca.bradj.eurekacraft.core.init.items.ItemsInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.util.Optional;

public class FolderContainer extends Container {

    public static final int boxHeight = 18, boxWidth = 18;
    public static final int inventoryLeftX = 8;
    public static final int titleBarHeight = 12;
    public static final int margin = 4;
    public static final int topOfInputs = titleBarHeight + margin + 1;
    public static final int leftOfInputs = inventoryLeftX;
    public static final int topOfFuel = titleBarHeight + margin + boxHeight;
    public static final int leftOfFuel = inventoryLeftX + (boxWidth * 2) + boxWidth;
    public static final int topOfTech = titleBarHeight + margin + boxHeight;
    public static final int leftOfTech = leftOfFuel + (boxWidth * 2);
    public static final int topOfOutput = topOfTech - (boxHeight / 2);
    public static final int leftOfOutput = leftOfTech + (int) (boxWidth * 2.5);
    public static final int topOfSecondary = topOfTech + boxHeight + margin - 1;
    public static final int leftOfSecondary = leftOfTech + (boxWidth * 3);

    public FolderContainer(
            int windowId,
            Inventory playerInventory,
            FriendlyByteBuf data
    ) {
        super(
                ContainerTypesInit.FOLDER.get(),
                windowId,
                playerInventory
        );
        layoutPlayerInventorySlots(45);

        ItemStack is = playerInventory.player.getMainHandItem();
        if (!ItemsInit.BLUEPRINT_FOLDER.get()
                .equals(is.getItem())) {
            throw new IllegalStateException("Main hand item is not blueprint folder");
        }
        is.getCapability(ForgeCapabilities.ITEM_HANDLER)
                .ifPresent(h -> {
                    // Inputs
                    int cols = 9, rows = 1;
                    addRectangleOfBoxes(
                            h,
                            0,
                            leftOfInputs,
                            topOfInputs,
                            cols,
                            rows
                    );
                });
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    protected int getInventorySlotCount() {
        return 9;
    }

    @Override
    protected int getOutputSlotCount() {
        return 0;
    }

    @Override
    protected Optional<Integer> getFirstIndexForItem(Item item) {
        return Optional.empty();
    }
}
