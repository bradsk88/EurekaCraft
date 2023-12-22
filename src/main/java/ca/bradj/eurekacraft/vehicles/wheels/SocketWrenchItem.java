package ca.bradj.eurekacraft.vehicles.wheels;

import ca.bradj.eurekacraft.vehicles.EurekaCraftItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SocketWrenchItem extends EurekaCraftItem {
    public static final String ITEM_ID = "socket_wrench";

    public SocketWrenchItem() {
        super(ITEM_ID, BASE_PROPS().stacksTo(1));
    }

    @Override
    public void appendHoverText(
            ItemStack p_41421_,
            @Nullable Level p_41422_,
            List<Component> p_41423_,
            TooltipFlag p_41424_
    ) {
        p_41423_.add(
                Component.translatable("item.eurekacraft.socket_wrench.subtitle").
                        withStyle(ChatFormatting.GRAY)
        );
    }
}
