package ca.bradj.eurekacraft.vehicles;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BrokenRefBoard extends RefBoardItem {
    public static final String ITEM_ID = "broken_ref_board";
    public static final BoardType ID = new BoardType("broken_ref_board");

    public BrokenRefBoard() {
        super(RefBoardStats.HeavyBoard.damaged(), ID);
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            @Nullable Level world,
            List<Component> tooltip,
            TooltipFlag flagIn
    ) {
        tooltip.add(RefBoardStatsUtils.Prefix("speed", -1));
        tooltip.add(RefBoardStatsUtils.Prefix("agility", -1));
        tooltip.add(RefBoardStatsUtils.Prefix("lift", -1));
    }

    @Override
    public ItemStack getDefaultInstance() {
        return super.getDefaultInstance();
    }
}
