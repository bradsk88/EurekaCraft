package ca.bradj.eurekacraft.render;

import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID)
public class RainRenderHandler {

    @SubscribeEvent
    public worldRender(RenderPlayerEvent event) {
        PlayerEntity player = event.getPlayer()
            for (PlayerEntity p : world.players()) {
                Vector3d position = p.getPosition(0);
                logger.debug("player " + position);
                BlockPos blockPos = new BlockPos(position);
                for (Direction dir : Direction.values()) {
                    BlockPos bp = blockPos.relative(dir);
                    logger.debug("bp " + dir + " " + bp);
                    world.addParticle(ParticleTypes.SMOKE, bp.getX(), bp.getY(), bp.getZ(), 1.0D, 1.0D, 1.0D);
                }
            }
        }

}
