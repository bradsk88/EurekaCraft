package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.container.PhotoContainer;
import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.interfaces.IInitializable;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Photo extends Item implements IInitializable {

    public static final String ITEM_ID = "photo";

    private static final String NBT_KEY_PHOTO_ID = "photo_id";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    private static int getPhotoId(ItemStack stack) {
        return stack.getOrCreateTag().getInt(NBT_KEY_PHOTO_ID);
    }

    public Photo() {
        super(PROPS);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand p_41434_) {
        if (level.isClientSide()) {
            return InteractionResultHolder.consume(player.getItemInHand(p_41434_));
        }
        ItemStack stack = player.getItemInHand(p_41434_);
        int photoId = getPhotoId(stack);

        NetworkHooks.openGui((ServerPlayer) player, new MenuProvider() {
            @Override
            public @NotNull Component getDisplayName() {
                return TextComponent.EMPTY;
            }

            @Override
            public @NotNull AbstractContainerMenu createMenu(
                    int windowId,
                    @NotNull Inventory inv,
                    @NotNull Player p
            ) {
                return new PhotoContainer(windowId, photoId);
            }
        }, data -> data.writeInt(photoId));
        return InteractionResultHolder.success(player.getItemInHand(p_41434_));
    }

    @Override
    public void initialize(
            ItemStack target,
            RandomSource random
    ) {
        target.getOrCreateTag().putInt(NBT_KEY_PHOTO_ID, random.nextInt(9) + 1);
    }
}
