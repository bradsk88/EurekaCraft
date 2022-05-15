package ca.bradj.eurekacraft.render;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.ModelsInit;
import ca.bradj.eurekacraft.vehicles.BoardType;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoard;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID, value= Dist.CLIENT)
public class BoardPlayerRenderHandler {

    private static Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    @SubscribeEvent
    public static void playerRender(final RenderPlayerEvent.Pre event) {
        PlayerDeployedBoard.get(event.getPlayer()).ifPresent(
                (BoardType bt) -> renderPlayerWithBoard(event, bt)
        );
    }

    private static void renderPlayerWithBoard(final RenderPlayerEvent.Pre event, BoardType bt) {
        PoseStack matrixStack = event.getPoseStack();
        matrixStack.pushPose();

        if (BoardType.NONE.equals(bt)) {
            return;
        }

        AbstractBoardModel model = ModelsInit.getModel(bt);

        matrixStack.mulPose(Vector3f.YP.rotationDegrees(90));

        LivingEntity living = event.getEntityLiving();
        living.animationSpeed = 0;
        living.yHeadRot = living.yBodyRot + 90;

        float newYRot = (float) Math.toRadians(-living.yBodyRot);

        VertexConsumer ivertexbuilder = event.getMultiBufferSource().getBuffer(model.getRenderType());
        model.getModelRenderer().yRot = newYRot;
        model.renderToBuffer(
                matrixStack, ivertexbuilder, event.getPackedLight(),
                OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F
        );

        // TODO: Force crouch pose? Maybe legs apart?
    }

    @SubscribeEvent
    public static void playerRenderPost(RenderPlayerEvent.Post event) {
        event.getPoseStack().popPose(); // TODO: Move to Pre?
    }

}

