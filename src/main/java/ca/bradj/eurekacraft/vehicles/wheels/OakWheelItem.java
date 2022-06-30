package ca.bradj.eurekacraft.vehicles.wheels;

import java.awt.*;

public class OakWheelItem extends Wheel implements IWheel {
    public static final String ITEM_ID = "oak_wood_wheel";

    public OakWheelItem() {
        super(ITEM_ID);
    }

    @Override
    public Color getColor() {
        return Color.ORANGE.darker(); // lol @TC
    }
}
