package ca.bradj.eurekacraft.entity.board;

import ca.bradj.eurekacraft.vehicles.RefBoardStats;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TurnSpeedTest {

    RefBoardStats baseStats = RefBoardStats.StandardBoard;

    @Test
    void forStats() {

        double ts = TurnSpeed.ForStats(baseStats.WithAgility(1.0).WithWeight(0.0));
        assertEquals(1, ts); // 100 is essentially "turn on a dime"

        ts = TurnSpeed.ForStats(baseStats.WithAgility(0.0).WithWeight(1.0));
        assertEquals(0.05, ts);

        ts = TurnSpeed.ForStats(baseStats.WithAgility(1.0).WithWeight(1.0));
        assertEquals(0.525, ts);

        ts = TurnSpeed.ForStats(RefBoardStats.BadBoard);
        assertEquals(0.16875, ts);

        ts = TurnSpeed.ForStats(RefBoardStats.StandardBoard);
        assertEquals(0.2875, ts);

        ts = TurnSpeed.ForStats(RefBoardStats.EliteBoard);
        assertEquals(0.7625, ts);


    }
}