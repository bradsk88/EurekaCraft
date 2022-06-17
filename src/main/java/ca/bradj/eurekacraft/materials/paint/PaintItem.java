package ca.bradj.eurekacraft.materials.paint;

import ca.bradj.eurekacraft.core.init.ModItemGroup;
import ca.bradj.eurekacraft.interfaces.IColorSource;
import net.minecraft.world.item.Item;

import java.awt.*;

public class PaintItem extends Item implements IColorSource {

    public static String BLACK_ITEM_ID = "paint_bucket_black";
    public static String BLUE_ITEM_ID = "paint_bucket_blue";
    public static String BROWN_ITEM_ID = "paint_bucket_brown";
    public static String CYAN_ITEM_ID = "paint_bucket_cyan";
    public static String GRAY_ITEM_ID = "paint_bucket_gray";
    public static String GREEN_ITEM_ID = "paint_bucket_green";
    public static String LIGHT_BLUE_ITEM_ID = "paint_bucket_light_blue";
    public static String LIGHT_GRAY_ITEM_ID = "paint_bucket_light_gray";
    public static String LIME_ITEM_ID = "paint_bucket_lime";
    public static String MAGENTA_ITEM_ID = "paint_bucket_magenta";
    public static String ORANGE_ITEM_ID = "paint_bucket_orange";
    public static String PINK_ITEM_ID = "paint_bucket_pink";
    public static String PURPLE_ITEM_ID = "paint_bucket_purple";
    public static String RED_ITEM_ID = "paint_bucket_red";
    public static String WHITE_ITEM_ID = "paint_bucket_white";
    public static String YELLOW_ITEM_ID = "paint_bucket_yellow";

    private static final Properties PROPS = new Properties().tab(ModItemGroup.EUREKACRAFT_GROUP);
    private final Color color;

    PaintItem(Color color) {
        super(PROPS);
        this.color = color;
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    public static PaintItem black() {
        return new PaintItem(new Color(0, 0, 0));
    }

    public static PaintItem blue() {
        return new PaintItem(of100(15, 20, 100));
    }

    public static PaintItem brown() {
        return new PaintItem(of100(48, 28, 13));
    }

    public static PaintItem cyan() {
        return new PaintItem(of100(32, 80, 88));
    }

    public static PaintItem gray() {
        return new PaintItem(of100(59, 59, 59));
    }

    public static PaintItem green() {
        return new PaintItem(of100(39, 53, 18));
    }

    public static PaintItem lightblue() {
        return new PaintItem(of100(56, 73, 96));
    }

    public static PaintItem lightgray() {
        return new PaintItem(of100(78, 78, 78));
    }

    public static PaintItem lime() {
        return new PaintItem(of100(68, 90, 23));
    }

    public static PaintItem magenta() {
        return new PaintItem(of100(86, 48, 84));
    }

    public static PaintItem orange() {
        return new PaintItem(of100(100, 56, 17));
    }

    public static PaintItem pink() {
        return new PaintItem(of100(97, 70, 84));
    }

    public static PaintItem purple() {
        return new PaintItem(of100(65, 33, 81));
    }

    public static PaintItem red() {
        return new PaintItem(of100(100, 0, 0));
    }

    public static PaintItem white() {
        return new PaintItem(of100(100, 100, 100));
    }

    public static PaintItem yellow() {
        return new PaintItem(of100(100, 100, 0));
    }

    private static Color of100(int r, int g, int b) {
        int r1 = (int) (255 * (r / 100f));
        int g1 = (int) (255 * (g / 100f));
        int b1 = (int) (255 * (b / 100f));
        return new Color(r1, g1, b1);
    }

}
