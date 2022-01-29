package ca.bradj.eurekacraft.container;

import ca.bradj.eurekacraft.blocks.machines.RefTableTileEntity;
import ca.bradj.eurekacraft.core.init.ContainerTypesInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;

import java.util.Objects;

public class RefTableContainer extends Container {
    private final RefTableTileEntity te;

    public RefTableContainer(int windowId, PlayerInventory playerInventory, RefTableTileEntity refTableTileEntity) {
        super(ContainerTypesInit.REF_TABLE.get(), windowId);
        this.te = refTableTileEntity;
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
}
