package ca.bradj.eurekacraft.entity.board;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.AdvancementsInit;
import ca.bradj.eurekacraft.entity.JudgeEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class TrickEvents {

    @SubscribeEvent
    public static void FirstRideJudgeSpawn(AdvancementEvent event) {
        if (event.getPlayer().level.isClientSide()) {
            return;
        }
        ResourceLocation firstFlight = new ResourceLocation(EurekaCraft.MODID, AdvancementsInit.IDs.FirstFlight);
        ResourceLocation id = event.getAdvancement().getId();
        if (firstFlight.equals(id)) {
            JudgeEntity.spawnToRewardPlayer((ServerPlayerEntity) event.getPlayer());
        }
    }
}
