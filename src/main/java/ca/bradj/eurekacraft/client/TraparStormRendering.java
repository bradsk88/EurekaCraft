package ca.bradj.eurekacraft.client;

import ca.bradj.eurekacraft.core.network.msg.TraparStormMessage;
import ca.bradj.eurekacraft.world.storm.StormSavedData;

public class TraparStormRendering {

    public static void init() {
        // TODO: Reimplement weather rendering
    }

    public static void updateFromMessage(TraparStormMessage traparStormMessage) {
        StormSavedData.updateFromMessage(traparStormMessage);
    }
}
