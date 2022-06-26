package ca.bradj.eurekacraft.vehicles.control;

import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID)
public class Events {

    private static Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    @SubscribeEvent()
    public static void registerCaps(RegisterCapabilitiesEvent evt) {
        evt.register(Control.class);
    }

    @SubscribeEvent()
    public static void onPlayerAdded(AttachCapabilitiesEvent<Entity> evt) {
        if (!(evt.getObject() instanceof Player)) {
            return;
        }
        if (!evt.getObject().getCapability(PlayerBoardControlProvider.PLAYER_BOARD_CONTROL).isPresent()) {
            PlayerBoardControlProvider cap = new PlayerBoardControlProvider();
            evt.addCapability(new ResourceLocation(EurekaCraft.MODID, "board_control"), cap);
            evt.addListener(cap::invalidate);
        }

    }
}
