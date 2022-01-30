package ca.bradj.eurekacraft.container;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.blocks.machines.RefTableTileEntity;
import ca.bradj.eurekacraft.core.init.ContainerTypesInit;
import ca.bradj.eurekacraft.core.util.FunctionalIntReferenceHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IntReferenceHolder;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class RefTableContainer extends Container {
    private final RefTableTileEntity tileEntity;
    private final IItemHandler playerInventory;
    private IntReferenceHolder cookProgressSlot;

    private Logger logger = LogManager.getLogger(EurekaCraft.MODID);
    private final int boxHeight = 18, boxWidth = 18;
    private final int inventoryLeftX = 8;
    private final int titleBarHeight = 12;
    private final int margin = 4;

    public RefTableContainer(int windowId, PlayerInventory playerInventory, RefTableTileEntity refTableTileEntity) {
        super(ContainerTypesInit.REF_TABLE.get(), windowId);
        this.tileEntity = refTableTileEntity;
        this.playerInventory = new InvWrapper(playerInventory);
        layoutPlayerInventorySlots(86);

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {

                // Inputs
                int leftX = inventoryLeftX + boxWidth;
                int nextIndex = 0;
                int cols = 2, rows = 3;
                addRectangleOfBoxes(h, nextIndex, leftX, titleBarHeight + margin, cols, rows);

                // Fuel + Upgrades
                nextIndex = nextIndex + (cols * rows);
                leftX = leftX + (boxWidth * cols) + boxWidth;
                cols = 2; rows = 1;
                int nextTopY = titleBarHeight + margin + boxHeight;
                addRectangleOfBoxes(h, nextIndex, leftX, nextTopY, cols, rows);

                // Output
                nextIndex = nextIndex + (cols * rows);
                int nextLeftX = leftX + (boxWidth * cols) + boxWidth;
                addSlot(new SlotItemHandler(h, nextIndex, nextLeftX, nextTopY));
            });

            this.addDataSlot(this.cookProgressSlot = new FunctionalIntReferenceHolder(this.tileEntity::getCookingProgress, this.tileEntity::setCookingProgress));
        }
    }
    public RefTableContainer(int windowId, PlayerInventory playerInventory, PacketBuffer data) {
        this(windowId, playerInventory, getTileEntity(playerInventory, data));
    }

    private static RefTableTileEntity getTileEntity(final PlayerInventory pi, final PacketBuffer data) {
        Objects.requireNonNull(pi, "PlayerInventory cannot be null");
        Objects.requireNonNull(data, "PacketBuffer cannot be null");
        final TileEntity te = pi.player.level.getBlockEntity(data.readBlockPos());
        if (te instanceof RefTableTileEntity) {
            return (RefTableTileEntity) te;
        }
        throw new IllegalStateException("Tile Entity is not RefTableTileEntity");
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return true; // TODO: Based on distance
    }

    private void layoutPlayerInventorySlots(int pixelsFromTop) {
        // Player's inventory
        int rectangleRows = 3;
        addRectangleOfBoxes(playerInventory, 9, inventoryLeftX, pixelsFromTop, 9, rectangleRows);

        // Player's "hot bar" inventory
        pixelsFromTop += (boxHeight * rectangleRows) + margin;
        addLineOfBoxes(playerInventory, 0, inventoryLeftX, pixelsFromTop, 9);
    }

    private void addRectangleOfBoxes(IItemHandler handler, int inventoryIndex, int leftX, int topY, int xBoxes, int yBoxes) {
        int y = topY;
        int nextInvIndex = inventoryIndex;
        for (int j = 0; j < yBoxes; j++) {
            addLineOfBoxes(handler, nextInvIndex, leftX, y, xBoxes);
            nextInvIndex += xBoxes;
            y += boxHeight;
        }
    }

    private void addLineOfBoxes(IItemHandler handler, int index, int leftX, int topY, int numBoxes) {
        int x =  leftX;
        int nextInvIndex = index;
        for (int i = 0; i < numBoxes; i++) {
            addSlot(new SlotItemHandler(handler, nextInvIndex, x, topY));
            nextInvIndex++;
            x += boxWidth;
        }
    }

    public boolean isCooking() {
        return this.cookProgressSlot.get() > 0;
    }
}
