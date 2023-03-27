package ca.bradj.eurekacraft.container;

import ca.bradj.eurekacraft.core.init.ContainerTypesInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class PhotoContainer extends AbstractContainerMenu {

    private final int photoId;

    public PhotoContainer(
            int windowId,
            int photoId
    ) {
        super(ContainerTypesInit.PHOTO.get(), windowId);
        this.photoId = photoId;
    }

    public PhotoContainer(int windowId, Inventory inv, FriendlyByteBuf data) {
        this(windowId, data.readInt());
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return true;
    }

    public int GetPhotoId() {
        return this.photoId;
    }
}
