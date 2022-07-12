package ca.bradj.eurekacraft.vehicles.wheels;

import java.awt.*;

public class GoldWheelItem extends Wheel implements IWheel {
    public static final String ITEM_ID = "gold_wheel";

    public GoldWheelItem() {
        super(ITEM_ID);
    }

    @Override
    public Color getColor() {
        return Color.YELLOW.darker();
    }
}
