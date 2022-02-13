package ca.bradj.eurekacraft.render;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.ItemsInit;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.client.IWeatherRenderHandler;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TraparStormRenderStarter {

    private static final Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    private final IWeatherRenderHandler traparRenderer;
    private final IWeatherRenderHandler defaultRenderer;
    private final DimensionRenderInfo dimension;

    private boolean gogglesOn = false;

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

    private void updateFromState() {
        if (gogglesOn) {
            if (this.dimension.getWeatherRenderHandler() == this.traparRenderer) {
                return;
            }
            this.dimension.setWeatherRenderHandler(this.traparRenderer);
        } else {
            if (this.dimension.getWeatherRenderHandler() == this.defaultRenderer) {
                return;
            }
            this.dimension.setWeatherRenderHandler(this.defaultRenderer);
        }
    }

}
