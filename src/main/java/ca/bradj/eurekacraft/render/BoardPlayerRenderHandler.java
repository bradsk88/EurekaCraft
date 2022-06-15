package ca.bradj.eurekacraft.render;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.ModelsInit;
import ca.bradj.eurekacraft.vehicles.BoardType;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoard;
import ca.bradj.eurekacraft.vehicles.deployment.PlayerDeployedBoardProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID, value= Dist.CLIENT)
public class BoardPlayerRenderHandler {

    private static Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    @SubscribeEvent
    public static void playerRender(final RenderPlayerEvent.Pre event) {
        PlayerDeployedBoardProvider.getBoardTypeFor(event.getPlayer()).ifPresent(
            (PlayerDeployedBoard.ColoredBoard bt) -> renderPlayerWithBoard(event, bt)
        );
    }

    private static void renderPlayerWithBoard(final RenderPlayerEvent.Pre event, PlayerDeployedBoard.ColoredBoard bt) {
        if (PlayerDeployedBoard.ColoredBoard.NONE.equals(bt)) {
            return;
        }

        AbstractBoardModel model = ModelsInit.getModel(bt.boardType, bt.r, bt.g, bt.b);

        PoseStack matrixStack = event.getPoseStack();
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(90));

        LivingEntity living = event.getEntityLiving();
        living.animationSpeed = 0;
        living.yHeadRot = living.yBodyRot + 90;

        float newYRot = (float) Math.toRadians(-living.yBodyRot);

        VertexConsumer ivertexbuilder = event.getMultiBufferSource().getBuffer(model.getRenderType());
        model.getModelRenderer().yRot = newYRot;
        model.renderToBuffer(
                matrixStack, ivertexbuilder, event.getPackedLight(),
                OverlayTexture.WHITE_OVERLAY_V, 0.0F, 1.0F, 1.0F, 1.0F
        );
    }
}

