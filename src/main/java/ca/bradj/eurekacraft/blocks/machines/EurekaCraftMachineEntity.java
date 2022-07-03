package ca.bradj.eurekacraft.blocks.machines;

import ca.bradj.eurekacraft.materials.NoisyCraftingItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class EurekaCraftMachineEntity extends BlockEntity {

    private int noiseCooldown = 0;
    private final ItemStackHandler itemHandler;
    private LazyOptional<IItemHandler> handler;

    public EurekaCraftMachineEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_, int totalSlots) {
        super(p_155228_, p_155229_, p_155230_);
        this.itemHandler = new ItemStackHandler(totalSlots) {
            @Override
            protected void validateSlotIndex(int slot) {
                super.validateSlotIndex(slot); // TODO: Be more forgiving in case number of slots changes?
            }

            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
        this.handler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        handler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(this.store(nbt));
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        handler.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return this.handler.cast();
        }
        return super.getCapability(cap, side);
    }

    public List<ItemStack> getItemsStacksForDrop() {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < this.itemHandler.getSlots(); i++) {
            ItemStack iStack = itemHandler.getStackInSlot(i);
            if (iStack.isEmpty()) {
                continue;
            }
            items.add(iStack);
        }
        return items;
    }

    protected CompoundTag store(CompoundTag tag) {
        tag.put("inv", itemHandler.serializeNBT());
        return tag;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.store(new CompoundTag());
    }

    protected ItemStack getStackInSlot(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    protected void insertItem(int slot, ItemStack stack) {
        itemHandler.insertItem(slot, stack, false);
    }

    protected void extractItem(int slot, int amount) {
        itemHandler.extractItem(slot, amount, false);
    }

    public int getTotalSlotCount() {
        return itemHandler.getSlots();
    }

    protected void makeCraftingNoise(Optional<? extends Recipe<?>> recipe) {
        assert this.level != null;

        if (this.noiseCooldown > 0) {
            this.noiseCooldown--;
            return;
        }

        recipe.ifPresent((r) -> {

            ItemStack stackInSlot = getItemForCraftingNoise();
            Item item = stackInSlot.getItem();
            if (!(item instanceof NoisyCraftingItem)) {
                return;
            }

            ((NoisyCraftingItem) item).getCraftingSound().ifPresent((s) -> {
                float volume = 0.5f;
                float pitch = 0.5f;
                this.level.playSound(
                        null, this.getBlockPos(), s.event, SoundSource.BLOCKS, volume, pitch
                );
                this.noiseCooldown = s.noiseCooldown;
            });
        });

    }

    protected abstract ItemStack getItemForCraftingNoise();
}
