package ca.bradj.eurekacraft.container;

import ca.bradj.eurekacraft.blocks.machines.RefTableConsts;
import ca.bradj.eurekacraft.blocks.machines.RefTableTileEntity;
import ca.bradj.eurekacraft.core.init.AdvancementsInit;
import ca.bradj.eurekacraft.core.init.ContainerTypesInit;
import ca.bradj.eurekacraft.core.util.FunctionalIntReferenceHolder;
import ca.bradj.eurekacraft.interfaces.RefTableSlotAware;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Objects;
import java.util.Optional;

public class RefTableContainer extends Container {
    private final RefTableTileEntity tileEntity;
    private FunctionalIntReferenceHolder fireTotalSlot;
    private FunctionalIntReferenceHolder cookProgressSlot;
    private FunctionalIntReferenceHolder fireRemainderSlot;

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

    public RefTableContainer(int windowId, net.minecraft.world.Container playerInventory, RefTableTileEntity refTableTileEntity) {
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
                    public void onTake(Player player, ItemStack stack) {
                        super.onTake(player, stack);

                        if (player instanceof ServerPlayer) {
                            AdvancementsInit.REF_TABLE_TRIGGER.trigger((ServerPlayer) player, stack);
                        }
                    }
                });

                addSlot(new SlotItemHandler(h, RefTableConsts.secondaryOutputSlot, leftOfSecondary, topOfSecondary));
            });

            this.addDataSlot(this.cookProgressSlot = new FunctionalIntReferenceHolder(this.tileEntity::getCookingProgress, this.tileEntity::setCookingProgress));
            this.addDataSlot(this.fireRemainderSlot = new FunctionalIntReferenceHolder(this.tileEntity::getFireRemaining, this.tileEntity::setFireRemaining));
            this.addDataSlot(this.fireTotalSlot = new FunctionalIntReferenceHolder(this.tileEntity::getFireTotal, this.tileEntity::setFireTotal));
        }
    }
    public RefTableContainer(int windowId, Inventory playerInventory, FriendlyByteBuf data) {
        this(windowId, playerInventory, getTileEntity(playerInventory, data));
    }

    private static RefTableTileEntity getTileEntity(final Inventory pi, final FriendlyByteBuf data) {
        Objects.requireNonNull(pi, "PlayerInventory cannot be null");
        Objects.requireNonNull(data, "PacketBuffer cannot be null");
        final BlockEntity te = pi.player.level.getBlockEntity(data.readBlockPos());
        if (te instanceof RefTableTileEntity) {
            return (RefTableTileEntity) te;
        }
        throw new IllegalStateException("Tile Entity is not RefTableTileEntity");
    }

    @Override
    public boolean stillValid(Player player) {
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
        return tileEntity.getTotalSlotCount() - 2;
    }

    @Override
    protected int getOutputSlotCount() {
        return tileEntity.getTotalSlotCount() - getInventorySlotCount();
    }

    @Override
    protected Optional<Integer> getFirstIndexForItem(Item item) {
        if (item instanceof RefTableSlotAware) {
            Optional<RefTableSlotAware.Slot> slot = ((RefTableSlotAware) item).getIdealSlot(
                    tileEntity.getInputItems(),
                    tileEntity.getFuelItem(),
                    tileEntity.getTechItem()
            );
            if (slot.isPresent()) {
                return switch (slot.get()) {
                    case INGREDIENT -> Optional.of(tileEntity.getInputsSlotIndex());
                    case FUEL -> Optional.of(tileEntity.getFuelSlotIndex());
                    case TECH -> Optional.of(tileEntity.getTechSlotIndex());
                };
            }
        }

        return RefTableSlotPreferences.getIdealSlot(
                item,
                tileEntity.getInputItems(),
                tileEntity.getFuelItem(),
                tileEntity.getTechItem()
        );
    }
}
