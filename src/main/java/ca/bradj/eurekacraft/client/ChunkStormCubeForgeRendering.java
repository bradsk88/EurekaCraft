package ca.bradj.eurekacraft.client;

import ca.bradj.eurekacraft.EurekaCraft;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ChunkStormCubeForgeRendering {

    @SubscribeEvent
    public static void handleRenderEvent(RenderLevelStageEvent evt) {
        // TODO: Make this work:
        //  https://forums.minecraftforge.net/topic/100752-1165-render-lines-to-world/

        PoseStack stack = evt.getPoseStack();
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer builder = buffer.getBuffer(RenderType.cutout()); //SpellRender.QUADS is a personal RenderType, of VertexFormat POSITION_COLOR.

        stack.pushPose();

        Vec3 cam = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        stack.translate(-cam.x, -cam.y, -cam.z);

        Matrix4f mat = stack.last().pose();

        VertexConsumer v1 = builder.vertex(
                        mat,
                        0,
                        57,
                        0
                )
                .color(255,
                        255,
                        255,
                        255
                ).uv(0, 0).uv2(0);
        v1.endVertex();
        builder.vertex(mat, 40, 67, 0).color(255, 255, 255, 255).uv(0, 0).uv2(0).normal(0, 0, 0).endVertex();
        builder.vertex(mat, 40, 68, 0).color(255, 255, 255, 255).uv(0, 0).uv2(0).normal(0, 0, 0).endVertex();
        builder.vertex(mat, 0, 58, 0).color(255, 255, 255, 255).uv(0, 0).uv2(0).normal(0, 0, 0).endVertex();

        stack.popPose();

        buffer.endBatch(RenderType.cutout());
    }
}
