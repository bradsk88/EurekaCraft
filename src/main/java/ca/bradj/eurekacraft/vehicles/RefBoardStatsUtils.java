package ca.bradj.eurekacraft.vehicles;

import java.util.Collection;
import java.util.Random;

public class RefBoardStatsUtils {


    public static RefBoardStats BoostAvg(
            Collection<RefBoardStats> stats,
            Random random,
            float baseBoost,
            float randomStatBoost
    ) {
        RefBoardStats avgBoardStats = RefBoardStats.Average(
                "avg",
                stats
        );

        double avgAgility = avgBoardStats.agility();
        double avgSpeed = avgBoardStats.speed();
        double avgLift = avgBoardStats.lift();

        avgBoardStats = avgBoardStats.WithAgility(avgAgility * baseBoost);
        avgBoardStats = avgBoardStats.WithSpeed(avgSpeed * baseBoost);
        avgBoardStats = avgBoardStats.WithLift(avgLift * baseBoost);

        switch (random.nextInt(3)) {
            case 0:
                avgBoardStats = avgBoardStats.WithAgility(avgAgility * randomStatBoost);
                break;
            case 1:
                avgBoardStats = avgBoardStats.WithSpeed(avgSpeed * randomStatBoost);
                break;
            case 2:
                avgBoardStats = avgBoardStats.WithLift(avgLift * randomStatBoost);
                break;
        }
        return avgBoardStats;
    }

}
