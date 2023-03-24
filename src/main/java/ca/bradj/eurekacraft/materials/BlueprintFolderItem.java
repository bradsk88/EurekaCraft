package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.container.FolderContainer;
import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.core.init.TagsInit;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class BlueprintFolderItem extends Item implements MenuProvider {

    private static final String STACK_ITEMS_NBT = "eurekacraft_items";

    public static final String ITEM_ID = "blueprint_folder";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public BlueprintFolderItem() {
        super(PROPS);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public ICapabilityProvider initCapabilities(
            ItemStack stack,
            @org.jetbrains.annotations.Nullable CompoundTag nbt
    ) {
        IItemHandler handler = new BlueprintFolderItem.ItemHandler(stack);
        return new ICapabilityProvider() {
            @NotNull
            @Override
            public <T> LazyOptional<T> getCapability(
                    @NotNull Capability<T> cap,
                    @org.jetbrains.annotations.Nullable Direction side
            ) {
                if (cap == ForgeCapabilities.ITEM_HANDLER) {
                    return LazyOptional.of(() -> handler)
                            .cast();
                }
                return LazyOptional.empty();
            }
        };
    }

    @Override
    public InteractionResultHolder<ItemStack> use(
            Level level,
            Player player,
            InteractionHand p_41434_
    ) {
        if (level.isClientSide()) {
            return InteractionResultHolder.consume(player.getItemInHand(p_41434_));
        }
        NetworkHooks.openScreen(
                (ServerPlayer) player,
                this
        );
        return InteractionResultHolder.success(player.getItemInHand(p_41434_));
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            @Nullable Level world,
            List<Component> tooltip,
            TooltipFlag flagIn
    ) {
        CompoundTag nbt = stack.getOrCreateTag();
        int size = nbt.contains(STACK_ITEMS_NBT) && nbt.getCompound(STACK_ITEMS_NBT).contains(
                "Items"
        ) ? nbt.getCompound(STACK_ITEMS_NBT).getList("Items", Tag.TAG_COMPOUND).size() : 0;
        tooltip.add(
                // TODO: Translate
                Component.literal("Contains " + size + " blueprints")
        );

        tooltip.add(
                Component.translatable(
                        "item." + EurekaCraft.MODID + "." + ITEM_ID + ".subtitle"
                ).withStyle(ChatFormatting.GRAY)
        );
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container." + EurekaCraft.MODID + ".folder").withStyle(ChatFormatting.WHITE);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public AbstractContainerMenu createMenu(
            int windowId,
            Inventory p_39955_,
            Player p_39956_
    ) {
        return new FolderContainer(
                windowId,
                p_39955_,
                null
        );
    }

    private static class ItemHandler extends ItemStackHandler {
        private final ItemStack host;

        public ItemHandler(ItemStack host) {
            super(9);
            this.host = host;
            CompoundTag hostTag = host.getOrCreateTag();
            if (hostTag.contains(STACK_ITEMS_NBT)) {
                this.deserializeNBT(hostTag.getCompound(STACK_ITEMS_NBT));
            }
        }

        @Override
        protected void onContentsChanged(int slot) {
            this.host.getOrCreateTag()
                    .put(
                            STACK_ITEMS_NBT,
                            this.serializeNBT()
                    );
        }

        @Override
        public boolean isItemValid(
                int slot,
                @NotNull ItemStack stack
        ) {
            return Ingredient.of(TagsInit.Items.BLUEPRINTS)
                    .test(stack);
        }
    }
}
