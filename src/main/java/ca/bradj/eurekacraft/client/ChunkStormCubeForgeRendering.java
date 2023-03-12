package ca.bradj.eurekacraft.client;

import ca.bradj.eurekacraft.EurekaCraft;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ChunkStormCubeForgeRendering {

    private static final ResourceLocation STORM_LOCATION = new ResourceLocation(
            EurekaCraft.MODID,
            "textures/environment/trapar.png"
    );

    @SubscribeEvent
    public static void handleRenderEvent(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
            return;
        }

        VertexConsumer buffer = Minecraft.getInstance()
                .renderBuffers()
                .bufferSource()
                .getBuffer(RenderType.solid());
        event.getPoseStack().pushPose();
        Vec3 cameraPos = event.getCamera()
                .getPosition();
        event.getPoseStack().translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        int radius = 2;
        if (Minecraft.useFancyGraphics()) {
            radius = 4;
        }

        ChunkPos cp = new ChunkPos(event.getCamera().getBlockPosition());
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                ChunkPos cpj = new ChunkPos(cp.x + i, cp.z + j);
                AABB aabb = new AABB(
                        new BlockPos(cpj.getMinBlockX(), 65, cpj.getMinBlockZ()),
                        new BlockPos(cpj.getMaxBlockX(), 80, cpj.getMaxBlockZ())
                );
                renderLineBox(
                        event.getPoseStack(), buffer,
                        aabb,
                        1f, 1f, 1f, 1f
                );
            }
        }
        event.getPoseStack().popPose();
    }

    public static void renderLineBox(PoseStack pose, VertexConsumer vc, AABB pos, float r, float g, float b, float a) {
        renderLineBox(pose, vc, pos.minX, pos.minY, pos.minZ, pos.maxX, pos.maxY, pos.maxZ, r, g, b, a, r, g, b);
    }

    public static void renderLineBox(
            PoseStack pos, VertexConsumer vc,
            double minX, double minY, double minZ,
            double maxX, double maxY, double maxZ,
            float r, float g, float b, float a, float r2, float g2, float b2) {
        Matrix4f matrix4f = pos.last().pose();
        Matrix3f matrix3f = pos.last().normal();
        float f = (float)minX;
        float f1 = (float)minY;
        float f2 = (float)minZ;
        float f3 = (float)maxX;
        float f4 = (float)maxY;
        float f5 = (float)maxZ;
        // FIXME: Why this ithis crashing?
        VertexConsumer vertex = vc.vertex(matrix4f, f3, f1, f2);
        vertex = vertex.color(r, g2, b2, a);
        vertex = vertex.normal(matrix3f, 1.0F, 0.0F, 0.0F);
        vertex = vertex.uv(1, 1);
        vertex = vertex.uv2(1, 1);
        vertex.endVertex();
        vc.vertex(matrix4f, f, f1, f2).color(r2, g, b2, a).normal(matrix3f, 0.0F, 1.0F, 0.0F).uv(1, 1).uv2(1).endVertex();
        vc.vertex(matrix4f, f, f4, f2).color(r2, g, b2, a).normal(matrix3f, 0.0F, 1.0F, 0.0F).uv(1, 1).uv2(1).endVertex();
        vc.vertex(matrix4f, f, f1, f2).color(r2, g2, b, a).normal(matrix3f, 0.0F, 0.0F, 1.0F).uv(1, 1).uv2(1).endVertex();
        vc.vertex(matrix4f, f, f1, f5).color(r2, g2, b, a).normal(matrix3f, 0.0F, 0.0F, 1.0F).uv(1, 1).uv2(1).endVertex();
        vc.vertex(matrix4f, f3, f1, f2).color(r, g, b, a).normal(matrix3f, 0.0F, 1.0F, 0.0F).uv(1, 1).uv2(1).endVertex();
        vc.vertex(matrix4f, f3, f4, f2).color(r, g, b, a).normal(matrix3f, 0.0F, 1.0F, 0.0F).uv(1, 1).uv2(1).endVertex();
        vc.vertex(matrix4f, f3, f4, f2).color(r, g, b, a).normal(matrix3f, -1.0F, 0.0F, 0.0F).uv(1, 1).uv2(1).endVertex();
        vc.vertex(matrix4f, f, f4, f2).color(r, g, b, a).normal(matrix3f, -1.0F, 0.0F, 0.0F).uv(1, 1).uv2(1).endVertex();
        vc.vertex(matrix4f, f, f4, f2).color(r, g, b, a).normal(matrix3f, 0.0F, 0.0F, 1.0F).uv(1, 1).uv2(1).endVertex();
        vc.vertex(matrix4f, f, f4, f5).color(r, g, b, a).normal(matrix3f, 0.0F, 0.0F, 1.0F).uv(1, 1).uv2(1).endVertex();
        vc.vertex(matrix4f, f, f4, f5).color(r, g, b, a).normal(matrix3f, 0.0F, -1.0F, 0.0F).uv(1, 1).uv2(1).endVertex();
        vc.vertex(matrix4f, f, f1, f5).color(r, g, b, a).normal(matrix3f, 0.0F, -1.0F, 0.0F).uv(1, 1).uv2(1).endVertex();
        vc.vertex(matrix4f, f, f1, f5).color(r, g, b, a).normal(matrix3f, 1.0F, 0.0F, 0.0F).uv(1, 1).uv2(1).endVertex();
        vc.vertex(matrix4f, f3, f1, f5).color(r, g, b, a).normal(matrix3f, 1.0F, 0.0F, 0.0F).uv(1, 1).uv2(1).endVertex();
        vc.vertex(matrix4f, f3, f1, f5).color(r, g, b, a).normal(matrix3f, 0.0F, 0.0F, -1.0F).uv(1, 1).uv2(1).endVertex();
        vc.vertex(matrix4f, f3, f1, f2).color(r, g, b, a).normal(matrix3f, 0.0F, 0.0F, -1.0F).uv(1, 1).uv2(1).endVertex();
        vc.vertex(matrix4f, f, f4, f5).color(r, g, b, a).normal(matrix3f, 1.0F, 0.0F, 0.0F).uv(1, 1).uv2(1).endVertex();
        vc.vertex(matrix4f, f3, f4, f5).color(r, g, b, a).normal(matrix3f, 1.0F, 0.0F, 0.0F).uv(1, 1).uv2(1).endVertex();
        vc.vertex(matrix4f, f3, f1, f5).color(r, g, b, a).normal(matrix3f, 0.0F, 1.0F, 0.0F).uv(1, 1).uv2(1).endVertex();
        vc.vertex(matrix4f, f3, f4, f5).color(r, g, b, a).normal(matrix3f, 0.0F, 1.0F, 0.0F).uv(1, 1).uv2(1).endVertex();
        vc.vertex(matrix4f, f3, f4, f2).color(r, g, b, a).normal(matrix3f, 0.0F, 0.0F, 1.0F).uv(1, 1).uv2(1).endVertex();
        vc.vertex(matrix4f, f3, f4, f5).color(r, g, b, a).normal(matrix3f, 0.0F, 0.0F, 1.0F).uv(1, 1).uv2(1).endVertex();
    }

    private static void renderPart(
            PoseStack p_112156_, VertexConsumer p_112157_,
            float r, float g, float b, float a,
            int y2, int y, float x, float z, float x2, float z2,
            float x4, float z4, float x3, float z3,
            float uv1_2, float uv1, float uv2, float uv2_2
    ) {
        PoseStack.Pose posestack$pose = p_112156_.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        renderQuad(matrix4f, matrix3f, p_112157_, r, g, b, a, y2, y, x, z, x2, z2, uv1_2, uv1, uv2, uv2_2);
        renderQuad(matrix4f, matrix3f, p_112157_, r, g, b, a, y2, y, x3, z3, x4, z4, uv1_2, uv1, uv2, uv2_2);
        renderQuad(matrix4f, matrix3f, p_112157_, r, g, b, a, y2, y, x2, z2, x3, z3, uv1_2, uv1, uv2, uv2_2);
        renderQuad(matrix4f, matrix3f, p_112157_, r, g, b, a, y2, y, x4, z4, x, z, uv1_2, uv1, uv2, uv2_2);
    }

    private static void renderQuad(
            Matrix4f p_112120_, Matrix3f p_112121_, VertexConsumer p_112122_,
            float r, float g, float b, float a,
            int y2, int y, float x, float z, float x2, float z2,
            float uv1_2, float uv1, float uv2, float uv2_2
    ) {
        addVertex(p_112120_, p_112121_, p_112122_, r, g, b, a, y, x, z, uv1, uv2);
        addVertex(p_112120_, p_112121_, p_112122_, r, g, b, a, y2, x, z, uv1, uv2_2);
        addVertex(p_112120_, p_112121_, p_112122_, r, g, b, a, y2, x2, z2, uv1_2, uv2_2);
        addVertex(p_112120_, p_112121_, p_112122_, r, g, b, a, y, x2, z2, uv1_2, uv2);
    }

    private static void addVertex(
            Matrix4f p_112107_,
            Matrix3f p_112108_,
            VertexConsumer p_112109_,
            float r, float g, float b, float a,
            int y, float x, float z,
            float uv1, float uv2
    ) {
        p_112109_
                .vertex(p_112107_, x, (float)y, z)
                .color(r, g, b, a)
                .uv(uv1, uv2)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(15728880)
                .normal(p_112108_, 0.0F, 1.0F, 0.0F)
                .endVertex();
    }
}
