package ca.bradj.eurekacraft.render;

//import ca.bradj.eurekacraft.EurekaCraft;
//import ca.bradj.eurekacraft.core.init.items.ItemsInit;
//import net.minecraft.client.renderer.DimensionSpecialEffects;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.entity.EquipmentSlot;
//import net.minecraft.world.item.ItemStack;
//import net.minecraftforge.client.IWeatherRenderHandler;
//import net.minecraftforge.event.TickEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

public class TraparStormRenderStarter {

    // TODO: Figure out how to hook into the weather renderer (or render an overlay)
//    private static final Logger logger = LogManager.getLogger(EurekaCraft.MODID);
//
//    private final IWeatherRenderHandler traparRenderer;
//    private final IWeatherRenderHandler defaultRenderer;
//    private final DimensionSpecialEffects dimension;
//
//    private boolean gogglesOn = false;
//
//    public TraparStormRenderStarter(DimensionSpecialEffects dimensionRenderInfo, IWeatherRenderHandler traparRenderer, IWeatherRenderHandler defaultRenderer) {
//        this.dimension = dimensionRenderInfo;
//        this.traparRenderer = traparRenderer;
//        this.defaultRenderer = defaultRenderer;
//    }
//
//    @SubscribeEvent
//    public void onArmorChange(TickEvent.PlayerTickEvent evt) {
//        if (evt.player instanceof ServerPlayer) {
//            return;
//        }
//
//        boolean oldWearing = this.gogglesOn;
//
//        ItemStack headwear = evt.player.getItemBySlot(EquipmentSlot.HEAD);
//        this.gogglesOn = headwear.sameItemStackIgnoreDurability(ItemsInit.SCUB_GOGGLES.get().getDefaultInstance());
//
//        if (oldWearing == this.gogglesOn) {
//            return;
//        }
//
//        updateFromState();
//    }
//
//    @SubscribeEvent
//    public void onWeather(TickEvent.WorldTickEvent evt) {
//        if (evt.world.isRaining()) {
//            setToDefaultRenderer();
//        } else {
//            updateFromState();
//        }
//    }
//
//    private void updateFromState() {
//        if (gogglesOn) {
//            setToTraparRenderer();
//        } else {
//            setToDefaultRenderer();
//        }
//    }
//
//    private void setToDefaultRenderer() {
//        if (this.dimension.getWeatherRenderHandler() == this.defaultRenderer) {
//            return;
//        }
//        this.dimension.setWeatherRenderHandler(this.defaultRenderer);
//    }
//
//    private void setToTraparRenderer() {
//        if (this.dimension.getWeatherRenderHandler() == this.traparRenderer) {
//            return;
//        }
//        this.dimension.setWeatherRenderHandler(this.traparRenderer);
//    }

}
