package ca.bradj.eurekacraft.vehicles.deployment;

import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID)
public class DeploymentHandler {

    private static Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    @SubscribeEvent()
    public static void onPlayerAdded(AttachCapabilitiesEvent<Entity> evt) {
        if (!(evt.getObject() instanceof PlayerEntity)) {
            return;
        }
        PlayerDeployedBoard cap = new PlayerDeployedBoard();
        evt.addCapability(new ResourceLocation(EurekaCraft.MODID, "board_deployed"), cap);
        evt.addListener(cap::invalidate);
    }

}
