package ca.bradj.eurekacraft.entity.board;

import ca.bradj.eurekacraft.vehicles.RefBoardStats;

public class TurnSpeed {

    public static double ForStats(RefBoardStats stats) {
        double s = (0.95 * stats.agility()) + (0.95 * (1 - stats.weight()));
        return 0.05 + (s/2);
    }

}
