package ca.bradj.eurekacraft.container;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.blocks.machines.SandingMachineTileEntity;
import ca.bradj.eurekacraft.core.init.ContainerTypesInit;
import ca.bradj.eurekacraft.core.util.FunctionalIntReferenceHolder;
import ca.bradj.eurekacraft.interfaces.SandingMachineSlotAware;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.Optional;

public class SandingMachineContainer extends Container {
    private final SandingMachineTileEntity tileEntity;
    private DataSlot cookProgressSlot;

    private Logger logger = LogManager.getLogger(EurekaCraft.MODID);
    public static final int boxHeight = 18, boxWidth = 18;
    public static final int inventoryLeftX = 8;
    public static final int titleBarHeight = 12;
    public static final int margin = 4;

    public SandingMachineContainer(
            int windowId,
            net.minecraft.world.Container playerInventory,
            SandingMachineTileEntity te
    ) {
        super(
                ContainerTypesInit.SANDING_MACHINE.get(),
                windowId,
                playerInventory
        );
        this.tileEntity = te;
        layoutPlayerInventorySlots(86);

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                    .ifPresent(h -> {

                        int nextTopY = titleBarHeight + margin + boxHeight;

                        // Inputs
                        int leftX = inventoryLeftX + (boxWidth * 2);
                        int nextIndex = 0;
                        addSlot(new SlotItemHandler(
                                h,
                                nextIndex,
                                leftX,
                                nextTopY
                        ));

                        // Sandpaper
                        nextIndex = nextIndex + 1;
                        leftX = leftX + (boxWidth * 3);
                        addSlot(new SlotItemHandler(
                                h,
                                nextIndex,
                                leftX,
                                nextTopY
                        ));

                        // Output
                        nextIndex = nextIndex + 1;
                        int nextLeftX = leftX + (boxWidth * 2);
                        addSlot(new SlotItemHandler(
                                h,
                                nextIndex,
                                nextLeftX,
                                nextTopY
                        ));
                    });

            this.addDataSlot(this.cookProgressSlot = new FunctionalIntReferenceHolder(
                    this.tileEntity::getCookingProgress,
                    this.tileEntity::setCookingProgress
            ));
        }
    }

    public SandingMachineContainer(
            int windowId,
            Inventory playerInventory,
            FriendlyByteBuf data
    ) {
        this(
                windowId,
                playerInventory,
                getTileEntity(
                        playerInventory,
                        data
                )
        );
    }

    private static SandingMachineTileEntity getTileEntity(
            final Inventory pi,
            final FriendlyByteBuf data
    ) {
        Objects.requireNonNull(
                pi,
                "PlayerInventory cannot be null"
        );
        Objects.requireNonNull(
                data,
                "PacketBuffer cannot be null"
        );
        final BlockEntity te = pi.player.level.getBlockEntity(data.readBlockPos());
        if (te instanceof SandingMachineTileEntity) {
            return (SandingMachineTileEntity) te;
        }
        throw new IllegalStateException("Tile Entity is not SandingMachineTileEntity");
    }

    @Override
    public boolean stillValid(Player player) {
        return true; // TODO: Based on distance
    }

    public int getCraftedPercent() {
        return this.cookProgressSlot.get();
    }

    @Override
    protected int getInventorySlotCount() {
        return tileEntity.getTotalSlotCount() - 1;
    }

    @Override
    protected int getOutputSlotCount() {
        return tileEntity.getTotalSlotCount() - getInventorySlotCount();
    }

    @Override
    protected Optional<Integer> getFirstIndexForItem(Item item) {
        if (item instanceof SandingMachineSlotAware) {
            Optional<SandingMachineSlotAware.Slot> slot = ((SandingMachineSlotAware) item).getIdealSlot(
                    tileEntity.getInputItems(),
                    tileEntity.getAbrasiveItem()
            );
            if (slot.isPresent()) {
                return switch (slot.get()) {
                    case INGREDIENT -> Optional.of(tileEntity.getInputsSlotIndex());
                    case GRIT -> Optional.of(tileEntity.getAbrasiveSlotIndex());
                };
            }
        }
        return Optional.empty();
    }
}
