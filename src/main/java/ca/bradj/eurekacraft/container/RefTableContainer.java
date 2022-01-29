package ca.bradj.eurekacraft.container;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.blocks.machines.RefTableTileEntity;
import ca.bradj.eurekacraft.core.init.ContainerTypesInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
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

    private Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    public RefTableContainer(int windowId, PlayerInventory playerInventory, RefTableTileEntity refTableTileEntity) {
        super(ContainerTypesInit.REF_TABLE.get(), windowId);
        this.tileEntity = refTableTileEntity;
        this.playerInventory = new InvWrapper(playerInventory);
        layoutPlayerInventorySlots(8, 86);

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 80, 31));
                addSlot(new SlotItemHandler(h, 1, 82, 53));
            });
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
    public boolean stillValid(PlayerEntity p_75145_1_) {
        return true; // TODO: ???
    }

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }

        return index;
    }

    private int addSlotBo(IItemHandler handler, int index, int x, int y, int horAmt, int dx, int verAmt, int dy) {
        for (int j = 0; j < verAmt; j++) {
            index = addSlotRange(handler, index, x, y, horAmt, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(int pixelsFromLeftEdge, int pixelsFromTop) {
        addSlotBo(playerInventory, 9, pixelsFromLeftEdge, pixelsFromTop, 9, 18, 3, 18);

        pixelsFromTop += 58; // TODO: COnst
        addSlotRange(playerInventory, 0, pixelsFromLeftEdge, pixelsFromTop, 9, 18);
    }

}
