package ca.bradj.eurekacraft.render;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.ItemsInit;
import ca.bradj.eurekacraft.core.network.msg.TraparStormMessage;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.IWeatherRenderHandler;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class TraparStormRenderStarter {

    private static final Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    private final IWeatherRenderHandler traparRenderer;
    private final IWeatherRenderHandler defaultRenderer;
    private final DimensionRenderInfo dimension;

    private boolean gogglesOn = false;
    private boolean storming = false;

    public TraparStormRenderStarter(DimensionRenderInfo dimensionRenderInfo, IWeatherRenderHandler traparRenderer, IWeatherRenderHandler defaultRenderer) {
        this.dimension = dimensionRenderInfo;
        this.traparRenderer = traparRenderer;
        this.defaultRenderer = defaultRenderer;
    }

    @SubscribeEvent
    public void onArmorChange(LivingEquipmentChangeEvent evt) {
        if (!EquipmentSlotType.HEAD.equals(evt.getSlot())) {
            // TODO: Support tinker (and other mods) goggle slot
            return;
        }
        this.gogglesOn = evt.getTo().sameItemStackIgnoreDurability(
                ItemsInit.SCUB_GOGGLES.get().getDefaultInstance()
        );
        updateFromState();
    }

    public boolean handleMessage(
            TraparStormMessage traparStormMessage,
            Supplier<NetworkEvent.Context> ctx
    ) {
        final AtomicBoolean success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                    () -> () -> {
                        storming = traparStormMessage.isStorming();
                        updateFromState();
                    }
            );
        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }

    private void updateFromState() {
        if (gogglesOn && storming) {
            logger.debug("Setting renderer to trapar");
            this.dimension.setWeatherRenderHandler(this.traparRenderer);
        } else {
            logger.debug("Setting renderer to default: " + this.defaultRenderer);
            this.dimension.setWeatherRenderHandler(this.defaultRenderer);
        }
    }
}
