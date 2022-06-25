package ca.bradj.eurekacraft.vehicles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public class BoardColor {

    private static final String NBT_KEY_COLOR = "ca.bradj.eurekcraft.board_color";
    private static final String TAG_KEY_RGB = "rgb";

    public static void AddToStack(ItemStack s, Color c) {
        if (s.getTag() == null) {
            s.setTag(new CompoundTag());
        }
        s.getTag().put(NBT_KEY_COLOR, BoardColor.serializeNBT(c));
    }

    public static Color FromStack(ItemStack s) {
        CompoundTag tag = s.getTag();
        if (tag == null) {
            return Color.WHITE;
        }
        CompoundTag color = tag.getCompound(NBT_KEY_COLOR);
        return BoardColor.deserializeNBT(color);
    }

    private static Color deserializeNBT(CompoundTag tag) {
        if (!tag.contains(TAG_KEY_RGB)) {
            return Color.WHITE;
        }
        int rgb = tag.getInt(TAG_KEY_RGB);
        return new Color(rgb);
    }

    public static CompoundTag serializeNBT(Color color) {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putInt(TAG_KEY_RGB, color.getRGB());
        return compoundTag;
    }
}
