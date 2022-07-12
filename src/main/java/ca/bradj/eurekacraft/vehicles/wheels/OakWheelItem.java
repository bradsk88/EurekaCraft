package ca.bradj.eurekacraft.vehicles.wheels;

import ca.bradj.eurekacraft.materials.paint.PaintItem;

import java.awt.*;

public class OakWheelItem extends Wheel implements IWheel {

    private final Color color = PaintItem.of100(69, 56, 33);

    public static final String ITEM_ID = "oak_wood_wheel";

    public OakWheelItem() {
        super(ITEM_ID);
    }

    @Override
    public Color getColor() {
        return color;
    }
}
