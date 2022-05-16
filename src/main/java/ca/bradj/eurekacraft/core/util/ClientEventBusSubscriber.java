package ca.bradj.eurekacraft.core.util;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.client.gui.RefTableScreen;
import ca.bradj.eurekacraft.client.gui.SandingMachineScreen;
import ca.bradj.eurekacraft.core.init.ContainerTypesInit;
import com.mojang.blaze3d.platform.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value= Dist.CLIENT)
public class ClientEventBusSubscriber {


    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        // TODO: Reimplement
//        ScreenManager.register(ContainerTypesInit.REF_TABLE.get(), RefTableScreen::new);
//        ScreenManager.register(ContainerTypesInit.SANDING_MACHINE.get(), SandingMachineScreen::new);
    }

}
