package ca.bradj.eurekacraft.vehicles;

import net.minecraft.network.chat.Component;

import java.util.*;

public class RefBoardStatsUtils {


    public static RefBoardStats BoostAvg(
            Collection<RefBoardStats> stats,
            RandomSource random,
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

    public static Collection<? extends Component> getTooltips(
            Optional<RefBoardStats> stats,
            RefBoardStats defaults
    ) {
        List<Component> tooltip = new ArrayList<>();
        stats.ifPresentOrElse(s -> {
            tooltip.add(Prefix("speed", s.speed()));
            tooltip.add(Prefix("agility", s.agility()));
            tooltip.add(Prefix("lift", s.lift()));
        }, () -> {
            RefBoardStats best = RefBoardStats.FromReferenceWithBestOffsets(defaults);
            RefBoardStats worst = RefBoardStats.FromReferenceWithWorstOffsets(defaults);
            tooltip.add(range("speed", worst.speed(), best.speed()));
            tooltip.add(range("agility", worst.agility(), best.agility()));
            tooltip.add(range("lift", worst.lift(), best.lift()));
        });
        return tooltip;
    }

    public static Component Prefix(
            String name,
            double stat
    ) {
        String value = String.valueOf((int) (100 * stat));
        if (stat < 0) {
            value = "???";
        }
        return Component.translatable(
                "item.eurekacraft.ref_board_stats." + name + "_prefix",
                value
        );
    }

    private static Component range(
            String name,
            double lower,
            double upper
    ) {
        return Component.translatable(
                "item.eurekacraft.ref_board_stats." + name + "_range",
                (int) (100 * lower), (int) (100 * upper)
        );
    }
}
