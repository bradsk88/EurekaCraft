package ca.bradj.eurekacraft.vehicles;

import com.google.common.collect.ImmutableList;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class RefBoardStatsUtilsTest {

    @org.junit.jupiter.api.Test
    void boostAvg_ShouldBoostByMinimumIfStatsAreAllSame() {

        RefBoardStats sameBoard = RefBoardStats.StandardBoard.
                WithAgility(0.5f).
                WithSpeed(0.5f).
                WithLift(0.5f);

        ImmutableList<RefBoardStats> inputs = ImmutableList.of(
                sameBoard,
                sameBoard
        );

        float baseBoost = 1.10f;
        float randBoost = 1.25f;
        double baseBoostedStat = 0.5 * baseBoost;
        double randomBoostedStat = 0.5 * randBoost;
        double expectedTotal = baseBoostedStat + baseBoostedStat + randomBoostedStat;

        RefBoardStats resultStats = RefBoardStatsUtils.BoostAvg(
                inputs,
                new Random(),
                baseBoost,
                randBoost
        );
        double resultTotal = resultStats.speed() + resultStats.agility() + resultStats.lift();

        assertEquals(
                expectedTotal,
                resultTotal
        );

    }

    @org.junit.jupiter.api.Test
    void boostAvg_ShouldBoostByHalfDifferenceIfStatsAreDifferent() {

        RefBoardStats lowBoard = RefBoardStats.StandardBoard.
                WithAgility(0.0f).
                WithSpeed(0.0f).
                WithLift(0.0f);
        RefBoardStats highBoard = RefBoardStats.StandardBoard.
                WithAgility(1.0f).
                WithSpeed(1.0f).
                WithLift(1.0f);

        ImmutableList<RefBoardStats> inputs = ImmutableList.of(
                lowBoard,
                highBoard
        );

        float baseBoost = 1.10f;
        float randBoost = 1.25f;
        double baseBoostedStat = 0.5 * baseBoost;
        double randomBoostedStat = 0.5 * randBoost;
        double expectedTotal = baseBoostedStat + baseBoostedStat + randomBoostedStat;

        RefBoardStats resultStats = RefBoardStatsUtils.BoostAvg(
                inputs,
                new Random(),
                baseBoost,
                randBoost
        );
        double resultTotal = resultStats.speed() + resultStats.agility() + resultStats.lift();

        assertEquals(
                expectedTotal,
                resultTotal
        );

    }
}