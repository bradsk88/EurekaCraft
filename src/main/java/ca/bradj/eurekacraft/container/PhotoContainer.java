package ca.bradj.eurekacraft.container;

import ca.bradj.eurekacraft.core.init.ContainerTypesInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class PhotoContainer extends AbstractContainerMenu {

    public PhotoContainer(int windowId, Inventory inv, FriendlyByteBuf data) {
        super(ContainerTypesInit.PHOTO.get(), windowId);
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return true;
    }
}
