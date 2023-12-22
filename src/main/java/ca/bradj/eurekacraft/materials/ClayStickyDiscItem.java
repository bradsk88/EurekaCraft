package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ClayStickyDiscItem extends Item {
    public static final String ITEM_ID = "clay_sticky_disc";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public ClayStickyDiscItem() {
        super(PROPS);
    }

    @Override
    public void appendHoverText(
            ItemStack p_41421_,
            @Nullable Level p_41422_,
            List<Component> p_41423_,
            TooltipFlag p_41424_
    ) {
        p_41423_.add(
                Component.translatable("item.eurekacraft.clay_sticky_disc.subtitle").
                        withStyle(ChatFormatting.GRAY)
        );
    }
}
