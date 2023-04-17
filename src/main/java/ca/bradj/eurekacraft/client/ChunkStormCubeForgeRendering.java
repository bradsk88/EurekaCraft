package ca.bradj.eurekacraft.client;

import ca.bradj.eurekacraft.EurekaCraft;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
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
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) {
            return;
        }

        VertexConsumer buffer = Minecraft.getInstance()
                .renderBuffers()
                .bufferSource()
                .getBuffer(RenderType.translucent());

        int radius = 2;
        if (Minecraft.useFancyGraphics()) {
            radius = 4;
        }

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false); // FIXME: This makes land invisible 🤔

        RenderSystem.setShaderTexture(0, new ResourceLocation(EurekaCraft.MODID, "textures/blocks/trapar_wave_block_25.png"));

        ChunkPos cp = new ChunkPos(event.getCamera().getBlockPosition());
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                ChunkPos cpj = new ChunkPos(cp.x + i, cp.z + j);
                AABB aabb = new AABB(
                        new BlockPos(cpj.getMinBlockX(), 40, cpj.getMinBlockZ()),
                        new BlockPos(cpj.getMaxBlockX(), 80, cpj.getMaxBlockZ())
                );
                event.getPoseStack().pushPose();
                Vec3 cameraPos = event.getCamera()
                        .getPosition();
                event.getPoseStack().translate(
                        cpj.getMiddleBlockPosition(0).getX() - cameraPos.x,
                        cpj.getMiddleBlockPosition(0).getY() - cameraPos.y,
                        cpj.getMiddleBlockPosition(0).getZ() - cameraPos.z
                );
                renderBox(
                        event.getPoseStack(), buffer,
                        aabb,
                        1f, 1f, 1f, 1f
                );
                event.getPoseStack().popPose();
            }
        }
    }

    public static void renderBox(PoseStack pose, VertexConsumer vc, AABB pos, float r, float g, float b, float a) {
        renderBox(pose, vc, pos.minX, pos.minY, pos.minZ, pos.maxX, pos.maxY, pos.maxZ, r, g, b, a, r, g, b);
    }

    public static void renderBox(
            PoseStack pos, VertexConsumer vc,
            double minX, double minY, double minZ,
            double maxX, double maxY, double maxZ,
            float r, float g, float b, float a, float r2, float g2, float b2) {
        Matrix4f matrix4f = pos.last().pose();
        Matrix3f matrix3f = pos.last().normal();
        float fMinX = (float)minX;
        float fMinY = (float)minY;
        float fMinZ = (float)minZ;
        float fMaxX = (float)maxX;
        float fMaxY = (float)maxY;
        float fMaxZ = (float)maxZ;

        RenderSystem.enableCull();

        // TODO: Render opposite sides

        // North
        vc.vertex(matrix4f, fMinX, fMinY, fMinZ).color(r2, g2, b, a).uv(0, 1).uv2(1).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        vc.vertex(matrix4f, fMinX, fMaxY, fMinZ).color(r2, g2, b, a).uv(0, 0).uv2(1).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        vc.vertex(matrix4f, fMaxX, fMaxY, fMinZ).color(r2, g2, b, a).uv(1, 0).uv2(1).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        vc.vertex(matrix4f, fMaxX, fMinY, fMinZ).color(r2, g2, b, a).uv(1, 1).uv2(1).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();

        // South
        vc.vertex(matrix4f, fMaxX, fMinY, fMaxZ).color(r2, g2, b, a).uv(0, 0).uv2(1).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
        vc.vertex(matrix4f, fMaxX, fMaxY, fMaxZ).color(r2, g2, b, a).uv(0, 0).uv2(1).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
        vc.vertex(matrix4f, fMinX, fMaxY, fMaxZ).color(r2, g2, b, a).uv(1, 0).uv2(1).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
        vc.vertex(matrix4f, fMinX, fMinY, fMaxZ).color(r2, g2, b, a).uv(1, 1).uv2(1).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();

        // West
        vc.vertex(matrix4f, fMinX, fMinY, fMaxZ).color(r2, g, b2, a).uv(0, 1).uv2(1).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        vc.vertex(matrix4f, fMinX, fMaxY, fMaxZ).color(r2, g, b2, a).uv(0, 0).uv2(1).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        vc.vertex(matrix4f, fMinX, fMaxY, fMinZ).color(r2, g, b2, a).uv(1, 0).uv2(1).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        vc.vertex(matrix4f, fMinX, fMinY, fMinZ).color(r2, g, b2, a).uv(1, 1).uv2(1).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();

        // East
        vc.vertex(matrix4f, fMaxX, fMinY, fMinZ).color(r2, g, b2, a).uv(0, 1).uv2(1).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
        vc.vertex(matrix4f, fMaxX, fMaxY, fMinZ).color(r2, g, b2, a).uv(0, 0).uv2(1).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
        vc.vertex(matrix4f, fMaxX, fMaxY, fMaxZ).color(r2, g, b2, a).uv(1, 0).uv2(1).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
        vc.vertex(matrix4f, fMaxX, fMinY, fMaxZ).color(r2, g, b2, a).uv(1, 1).uv2(1).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
    }
}
