package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.container.PhotoContainer;
import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
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
import org.jetbrains.annotations.Nullable;

public class Photo extends Item implements MenuProvider {

    public static final String ITEM_ID = "photo";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public Photo() {
        super(PROPS);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand p_41434_) {
        if (level.isClientSide()) {
            return InteractionResultHolder.consume(player.getItemInHand(p_41434_));
        }
        NetworkHooks.openGui((ServerPlayer) player, this);
        return InteractionResultHolder.success(player.getItemInHand(p_41434_));
    }

    @Override
    public Component getDisplayName() {
        return TextComponent.EMPTY;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory p_39955_, Player p_39956_) {
        return new PhotoContainer(windowId, p_39955_, null);
    }
}
