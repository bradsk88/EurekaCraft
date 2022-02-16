package ca.bradj.eurekacraft.container;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.blocks.machines.RefTableConsts;
import ca.bradj.eurekacraft.blocks.machines.RefTableTileEntity;
import ca.bradj.eurekacraft.core.init.AdvancementsInit;
import ca.bradj.eurekacraft.core.init.ContainerTypesInit;
import ca.bradj.eurekacraft.core.util.FunctionalIntReferenceHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IntReferenceHolder;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class RefTableContainer extends MachineContainer {
    private final RefTableTileEntity tileEntity;
    private IntReferenceHolder fireTotalSlot;
    private IntReferenceHolder cookProgressSlot;
    private IntReferenceHolder fireRemainderSlot;

    private Logger logger = LogManager.getLogger(EurekaCraft.MODID);
    public static final int boxHeight = 18, boxWidth = 18;
    public static final int inventoryLeftX = 8;
    public static final int titleBarHeight = 12;
    public static final int margin = 4;
    public static final int topOfInputs = titleBarHeight + margin;
    public static final int leftOfInputs = inventoryLeftX;
    public static final int topOfFuel = titleBarHeight + margin + boxHeight;
    public static final int leftOfFuel = inventoryLeftX + (boxWidth * 2) + boxWidth;
    public static final int topOfTech = titleBarHeight + margin + boxHeight;
    public static final int leftOfTech = leftOfFuel + (boxWidth * 2);
    public static final int topOfOutput = topOfTech - (boxHeight / 2);
    public static final int leftOfOutput = leftOfTech + (int) (boxWidth * 2.5);
    public static final int topOfSecondary = topOfTech + boxHeight + margin - 1;
    public static final int leftOfSecondary = leftOfTech + (boxWidth * 3);

    public RefTableContainer(int windowId, PlayerInventory playerInventory, RefTableTileEntity refTableTileEntity) {
        super(ContainerTypesInit.REF_TABLE.get(), windowId, playerInventory);
        this.tileEntity = refTableTileEntity;
        layoutPlayerInventorySlots(86);

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {

                // Inputs
                int cols = 2, rows = 3;
                addRectangleOfBoxes(h, 0, leftOfInputs, topOfInputs, cols, rows);

                // Fuel
                addSlot(new SlotItemHandler(h, RefTableConsts.fuelSlot, leftOfFuel, topOfFuel));

                // Tech
                addSlot(new SlotItemHandler(h, RefTableConsts.techSlot, leftOfTech, topOfTech));

                // Output
                addSlot(new SlotItemHandler(h, RefTableConsts.outputSlot, leftOfOutput, topOfOutput) {
                    @Override
                    public ItemStack onTake(PlayerEntity player, ItemStack stack) {
                        ItemStack itemStack = super.onTake(player, stack);

                        if (player instanceof ServerPlayerEntity) {
                            AdvancementsInit.REF_TABLE_TRIGGER.trigger((ServerPlayerEntity) player, stack);
                        }
                        return itemStack;
                    }
                });

                addSlot(new SlotItemHandler(h, RefTableConsts.secondaryOutputSlot, leftOfSecondary, topOfSecondary));
            });

            this.addDataSlot(this.cookProgressSlot = new FunctionalIntReferenceHolder(this.tileEntity::getCookingProgress, this.tileEntity::setCookingProgress));
            this.addDataSlot(this.fireRemainderSlot = new FunctionalIntReferenceHolder(this.tileEntity::getFireRemaining, this.tileEntity::setFireRemaining));
            this.addDataSlot(this.fireTotalSlot = new FunctionalIntReferenceHolder(this.tileEntity::getFireTotal, this.tileEntity::setFireTotal));
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

    public int getCraftedPercent() {
        return this.cookProgressSlot.get();
    }

    public int getFirePercent() {
        return (int) (100 * this.fireRemainderSlot.get() / (float) this.fireTotalSlot.get());
    }

    @Override
    protected int getInventorySlotCount() {
        return tileEntity.getTotalSlotCount();
    }

}
