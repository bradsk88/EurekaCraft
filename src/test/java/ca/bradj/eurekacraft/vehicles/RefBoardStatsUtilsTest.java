package ca.bradj.eurekacraft.vehicles;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class RefBoardStatsUtilsTest {

    static class TrueRandom implements RandomSource {
        @Override
        public RandomSource fork() {
            return this;
        }

        @Override
        public PositionalRandomFactory forkPositional() {
            return null;
        }

        @Override
        public void setSeed(long p_216342_) {
        }

        @Override
        public int nextInt() {
            return new Random().nextInt();
        }

        @Override
        public int nextInt(int p_216331_) {
            return new Random().nextInt(p_216331_);
        }

        @Override
        public long nextLong() {
            return new Random().nextLong();
        }

        @Override
        public boolean nextBoolean() {
            return new Random().nextBoolean();
        }

        @Override
        public float nextFloat() {
            return new Random().nextFloat();
        }

        @Override
        public double nextDouble() {
            return new Random().nextDouble();
        }

        @Override
        public double nextGaussian() {
            return new Random().nextGaussian();
        }
    }

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
                new TrueRandom(),
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
                new TrueRandom(),
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