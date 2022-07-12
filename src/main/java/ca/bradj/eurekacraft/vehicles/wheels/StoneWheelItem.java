package ca.bradj.eurekacraft.vehicles.wheels;

import java.awt.*;

public class StoneWheelItem extends Wheel implements IWheel {
    public static final String ITEM_ID = "stone_wheel";

    public StoneWheelItem() {
        super(ITEM_ID);
    }

    @Override
    public Color getColor() {
        return Color.DARK_GRAY.brighter();
    }
}
