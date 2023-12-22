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

public class RedResin extends Item {

    public static final String ITEM_ID = "red_resin";
    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);

    public RedResin() {
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
                Component.translatable("item.eurekacraft.resins.subtitle").
                        withStyle(ChatFormatting.GRAY)
        );
        p_41423_.add(
                Component.translatable("item.eurekacraft.red_resins.subtitle").
                        withStyle(ChatFormatting.GRAY)
        );
    }
}
