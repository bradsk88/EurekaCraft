package ca.bradj.eurekacraft.container;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.blocks.machines.SandingMachineTileEntity;
import ca.bradj.eurekacraft.core.init.ContainerTypesInit;
import ca.bradj.eurekacraft.core.util.FunctionalIntReferenceHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IntReferenceHolder;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class SandingMachineContainer extends MachineContainer {
    private final SandingMachineTileEntity tileEntity;
    private IntReferenceHolder cookProgressSlot;

    private Logger logger = LogManager.getLogger(EurekaCraft.MODID);
    public static final int boxHeight = 18, boxWidth = 18;
    public static final int inventoryLeftX = 8;
    public static final int titleBarHeight = 12;
    public static final int margin = 4;

    public SandingMachineContainer(int windowId, PlayerInventory playerInventory, SandingMachineTileEntity te) {
        super(ContainerTypesInit.SANDING_MACHINE.get(), windowId, playerInventory);
        this.tileEntity = te;
        layoutPlayerInventorySlots(86);

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {

                int nextTopY = titleBarHeight + margin + boxHeight;

                // Inputs
                int leftX = inventoryLeftX + (boxWidth * 2);
                int nextIndex = 0;
                addSlot(new SlotItemHandler(h, nextIndex, leftX, nextTopY));

                // Sandpaper
                nextIndex = nextIndex + 1;
                leftX = leftX + (boxWidth * 3);
                addSlot(new SlotItemHandler(h, nextIndex, leftX, nextTopY));

                // Output
                nextIndex = nextIndex + 1;
                int nextLeftX = leftX + (boxWidth * 2);
                addSlot(new SlotItemHandler(h, nextIndex, nextLeftX, nextTopY));
            });

            this.addDataSlot(this.cookProgressSlot = new FunctionalIntReferenceHolder(this.tileEntity::getCookingProgress, this.tileEntity::setCookingProgress));
        }
    }
    public SandingMachineContainer(int windowId, PlayerInventory playerInventory, PacketBuffer data) {
        this(windowId, playerInventory, getTileEntity(playerInventory, data));
    }

    private static SandingMachineTileEntity getTileEntity(final PlayerInventory pi, final PacketBuffer data) {
        Objects.requireNonNull(pi, "PlayerInventory cannot be null");
        Objects.requireNonNull(data, "PacketBuffer cannot be null");
        final TileEntity te = pi.player.level.getBlockEntity(data.readBlockPos());
        if (te instanceof SandingMachineTileEntity) {
            return (SandingMachineTileEntity) te;
        }
        throw new IllegalStateException("Tile Entity is not SandingMachineTileEntity");
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return true; // TODO: Based on distance
    }


    public boolean isCooking() {
        return this.cookProgressSlot.get() > 0;
    }

    @Override
    protected int getInventorySlotCount() {
        return tileEntity.getSlotCount();
    }
}
