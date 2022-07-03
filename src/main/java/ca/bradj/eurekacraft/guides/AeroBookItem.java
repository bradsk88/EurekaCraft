package ca.bradj.eurekacraft.guides;

import ca.bradj.eurekacraft.vehicles.EurekaCraftItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AeroBookItem extends EurekaCraftItem {
    public static final String ITEM_ID = "aero_book";

    public AeroBookItem() {
        super(ITEM_ID);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        return super.use(p_41432_, p_41433_, p_41434_);
    }
}
