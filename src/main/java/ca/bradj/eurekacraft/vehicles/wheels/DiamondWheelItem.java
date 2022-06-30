package ca.bradj.eurekacraft.vehicles.wheels;

import java.awt.*;

public class DiamondWheelItem extends Wheel implements IWheel {
    public static final String ITEM_ID = "diamond_wheel";

    public DiamondWheelItem() {
        super(ITEM_ID);
    }

    @Override
    public Color getColor() {
        return Color.CYAN;
    }
}
