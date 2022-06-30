package ca.bradj.eurekacraft.vehicles.wheels;

import ca.bradj.eurekacraft.vehicles.EurekaCraftItem;

public class SocketWrenchItem extends EurekaCraftItem {
    public static final String ITEM_ID = "socket_wrench";

    public SocketWrenchItem() {
        super(ITEM_ID, BASE_PROPS().stacksTo(1));
    }
}
