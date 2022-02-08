package ca.bradj.eurekacraft.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public abstract class MachineContainer extends Container {
    private static final int boxHeight = 18, boxWidth = 18;
    private static final int inventoryLeftX = 8;
    private static final int margin = 4;

    private final IItemHandler playerInventory;

    protected MachineContainer(ContainerType<?> cType, int windowId, PlayerInventory pi) {
        super(cType, windowId);
        this.playerInventory = new InvWrapper(pi);
    }

    protected void layoutPlayerInventorySlots(
            int pixelsFromTop
    ) {
        // Player's inventory
        int rectangleRows = 3;
        addRectangleOfBoxes(playerInventory, 9, inventoryLeftX, pixelsFromTop, 9, rectangleRows);

        // Player's "hot bar" inventory
        pixelsFromTop += (boxHeight * rectangleRows) + margin;
        addLineOfBoxes(playerInventory, 0, inventoryLeftX, pixelsFromTop, 9);
    }

    protected void addRectangleOfBoxes(IItemHandler handler, int inventoryIndex, int leftX, int topY, int xBoxes, int yBoxes) {
        int y = topY;
        int nextInvIndex = inventoryIndex;
        for (int j = 0; j < yBoxes; j++) {
            addLineOfBoxes(handler, nextInvIndex, leftX, y, xBoxes);
            nextInvIndex += xBoxes;
            y += boxHeight;
        }
    }

    protected void addLineOfBoxes(IItemHandler handler, int index, int leftX, int topY, int numBoxes) {
        int x =  leftX;
        int nextInvIndex = index;
        for (int i = 0; i < numBoxes; i++) {
            this.addSlot(new SlotItemHandler(handler, nextInvIndex, x, topY));
            nextInvIndex++;
            x += boxWidth;
        }
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + getInventorySlotCount(), false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + getInventorySlotCount()) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    protected abstract int getInventorySlotCount();
}
