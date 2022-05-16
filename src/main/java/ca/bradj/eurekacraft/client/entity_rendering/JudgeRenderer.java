package ca.bradj.eurekacraft.client.entity_rendering;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.ModelsInit;
import ca.bradj.eurekacraft.entity.JudgeEntity;
import ca.bradj.eurekacraft.render.AbstractBoardModel;
import ca.bradj.eurekacraft.vehicles.BoardType;
import ca.bradj.eurekacraft.vehicles.EliteRefBoard;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public abstract class JudgeRenderer extends MobRenderer<JudgeEntity, VillagerModel<JudgeEntity>> {
    public static final ResourceLocation TEXTURE =
            new ResourceLocation(EurekaCraft.MODID, "textures/entity/judge.png");

    public JudgeRenderer() {
        // TODO: Remove this stub
        super(null, null, 0.0f);
    }

//    public JudgeRenderer(EntityRendererProvider<JudgeEntity> manager) {
//        ModelPart part = null; // TODO: Implement this
//        super(manager, new VillagerModel<JudgeEntity>(part), 0.5f);
//    }

    @Override
    public ResourceLocation getTextureLocation(JudgeEntity p_110775_1_) {
        return TEXTURE;
    }

    @Override
    public void render(JudgeEntity living, float p_225623_2_, float p_225623_3_, PoseStack matrixStack, MultiBufferSource buffer, int lightMaybe) {
        super.render(living, p_225623_2_, p_225623_3_, matrixStack, buffer, lightMaybe);

        if (living.isOnGround()) {
            return;
        }

        matrixStack.pushPose();

        BoardType bt = EliteRefBoard.ID;

        if (BoardType.NONE.equals(bt)) {
            return;
        }

        AbstractBoardModel model = ModelsInit.getModel(bt);

        matrixStack.mulPose(Vector3f.YP.rotationDegrees(90));

        living.animationSpeed = 0;
        living.yHeadRot = living.yBodyRot + 90;

        float newYRot = (float) Math.toRadians(-living.yBodyRot);

        VertexConsumer ivertexbuilder = buffer.getBuffer(model.getRenderType());
        model.getModelRenderer().yRot = newYRot;
        model.renderToBuffer(
                matrixStack, ivertexbuilder, lightMaybe,
                OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F
        );

        matrixStack.popPose();
    }
}
