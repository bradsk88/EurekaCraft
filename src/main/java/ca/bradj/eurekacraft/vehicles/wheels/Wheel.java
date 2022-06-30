package ca.bradj.eurekacraft.vehicles.wheels;

import ca.bradj.eurekacraft.vehicles.EurekaCraftItem;

public abstract class Wheel extends EurekaCraftItem implements IWheel {
    public Wheel(String itemId) {
        super(itemId);
    }
}
