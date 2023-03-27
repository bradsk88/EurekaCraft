package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.vehicles.RefBoardStats;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class Blueprints {

    public static Collection<Component> getTooltips(
            Optional<RefBoardStats> stats,
            RefBoardStats defaults
    ) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(new TranslatableComponent("item.eurekacraft.blueprints.subtitle_1").withStyle(ChatFormatting.GRAY));
        tooltip.add(new TranslatableComponent("item.eurekacraft.blueprints.subtitle_2").withStyle(ChatFormatting.GRAY));
        stats.ifPresentOrElse(s -> {
            tooltip.add(prefix("speed", s.speed()));
            tooltip.add(prefix("agility", s.agility()));
            tooltip.add(prefix("lift", s.lift()));
        }, () -> {
            RefBoardStats best = RefBoardStats.FromReferenceWithBestOffsets(defaults);
            RefBoardStats worst = RefBoardStats.FromReferenceWithWorstOffsets(defaults);
            tooltip.add(range("speed", worst.speed(), best.speed()));
            tooltip.add(range("agility", worst.agility(), best.agility()));
            tooltip.add(range("lift", worst.lift(), best.lift()));
        });
        return tooltip;
    }

    private static TranslatableComponent prefix(String name, double stat) {
        return new TranslatableComponent(
                "item.eurekacraft.ref_board_stats." + name + "_prefix",
                (int) (100 * stat)
        );
    }

    private static TranslatableComponent range(String name, double lower, double upper) {
        return new TranslatableComponent(
                "item.eurekacraft.ref_board_stats." + name + "_range",
                (int) (100 * lower), (int) (100 * upper)
        );
    }
}
