package ca.bradj.eurekacraft.render;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.vehicles.EntityRefBoard;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID)
public class BoardPlayerRenderHandler {

    private static Logger logger = LogManager.getLogger(EurekaCraft.MODID);
    public static GlideBoardModel model = new GlideBoardModel();

    // TODO: Custom texture
    protected static final ResourceLocation TEXTURE = new ResourceLocation(
            EurekaCraft.MODID, "textures/items/glide_board.png"
    );

    @SubscribeEvent
    public static void playerRender(final RenderPlayerEvent.Pre event) {
        MatrixStack matrixStack = event.getMatrixStack();
        matrixStack.pushPose();

        if (!EntityRefBoard.isDeployedFor(event.getPlayer().getId())) {
            return;
        }

        matrixStack.mulPose(Vector3f.YP.rotationDegrees(90));

        LivingEntity living = event.getEntityLiving();
        living.animationSpeed = 0;
        living.yHeadRot = living.yBodyRot + 90;

        // TODO: Hide item in hand

        float newYRot = (float) Math.toRadians(-living.yBodyRot);

        IVertexBuilder ivertexbuilder = event.getBuffers().getBuffer(model.renderType(TEXTURE));
        model.getModelRenderer().yRot = newYRot;
        model.renderToBuffer(
                matrixStack, ivertexbuilder, event.getLight(),
                OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F
        );

        // TODO: Force crouch pose?
    }

    @SubscribeEvent
    public static void playerRenderPost(RenderPlayerEvent.Post event) {
        event.getMatrixStack().popPose();
    }

}

