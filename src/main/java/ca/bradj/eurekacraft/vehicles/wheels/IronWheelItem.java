package ca.bradj.eurekacraft.vehicles.wheels;

import java.awt.*;

public class IronWheelItem extends Wheel implements IWheel {
    public static final String ITEM_ID = "iron_wheel";

    public IronWheelItem() {
        super(ITEM_ID);
    }

    @Override
    public Color getColor() {
        return Color.LIGHT_GRAY;
    }
}
