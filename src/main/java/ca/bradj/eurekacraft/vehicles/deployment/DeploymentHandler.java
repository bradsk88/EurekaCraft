package ca.bradj.eurekacraft.vehicles.deployment;

import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
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
        if (!(evt.getObject() instanceof Player)) {
            return;
        }
        PlayerDeployedBoard cap = new PlayerDeployedBoard();
        evt.addCapability(new ResourceLocation(EurekaCraft.MODID, "board_deployed"), cap);
        evt.addListener(cap::invalidate);
    }

}
