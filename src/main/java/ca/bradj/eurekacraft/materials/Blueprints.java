package ca.bradj.eurekacraft.materials;

import ca.bradj.eurekacraft.vehicles.RefBoardStats;
import ca.bradj.eurekacraft.vehicles.RefBoardStatsUtils;
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
        tooltip.addAll(RefBoardStatsUtils.getTooltips(stats, defaults));
        return tooltip;
    }
}
