package ca.bradj.eurekacraft.blocks;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.config.EurekaConfig;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.crop.FreshSeedsCrop;
import ca.bradj.eurekacraft.entity.board.EntityRefBoard;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.BiConsumer;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class CropEvents {

    @SubscribeEvent
    public static void ClientTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return;
        }
        if (!(event.player instanceof ServerPlayer sp)) {
            return;
        }

        int frequency = 160;
        long gt = sp.getLevel().getGameTime();

        BiConsumer<BlockPos, Float> tryAddBoost = (p, amt) -> {
            BlockState kb = sp.getLevel().getBlockState(p);

            if (kb.is(BlocksInit.FRESH_SEEDS_CROP.get())) {
                int ageFrom = FreshSeedsCrop.getAgeFrom(kb);
                if (ageFrom == 0) {
                    return;
                }
                if (gt % (frequency / ageFrom) == 0)
                    EntityRefBoard.addBoost(sp.getLevel(), sp.getUUID(), amt.intValue(), p);
            }
        };

        BlockPos kneePos = sp.getOnPos().above();
        float amount = EurekaConfig.trapar_amount_from_fresh_crop.get().floatValue();
        tryAddBoost.accept(kneePos, amount);
//        amount = 0.5f * amount; // TODO: Add less charge for crops further away (currently this would lower to 0)
        for (Direction d : Direction.Plane.HORIZONTAL) {
            tryAddBoost.accept(kneePos.relative(d), amount);
        }
    }
}
