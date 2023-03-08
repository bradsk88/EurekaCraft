package ca.bradj.eurekacraft.client;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.wearables.deployment.DeployedPlayerGoggles;
import ca.bradj.eurekacraft.world.waves.ChunkWavesDataManager;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ChunkWaveBlocksForgeRendering {

    private static Minecraft mc = Minecraft.getInstance();
    private static Vec3 lastPos;

    @SubscribeEvent
    public static void handleRenderEvent(RenderLevelStageEvent evt) {
        if (evt.getStage() != RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
            return;
        }

        if (!DeployedPlayerGoggles.areGogglesBeingWorn(mc.player)) {
            return;
        }

        BlockRenderDispatcher renderer = Minecraft.getInstance().getBlockRenderer();
        ClientLevel world = mc.level;
        BlockState state = BlocksInit.TRAPAR_WAVE_CHILD_BLOCK.get().defaultBlockState();
        BakedModel bm = renderer.getBlockModel(state);
        PoseStack matrixStack = evt.getPoseStack();

        ChunkPos cp = mc.player.chunkPosition();
        Vec3 eyePos = mc.gameRenderer.getMainCamera().getPosition();
        // TODO: Render in front of player (unless camere facing backward)
        for (int i = -4; i < 4; i++) {
            for (int j = -4; j < 4; j++) {
                ChunkPos cpj = new ChunkPos(cp.x + i, cp.z + j);
                renderChunkWaves(renderer, eyePos, world, state, bm, matrixStack, cpj);
            }
        }

        evt.getLevelRenderer().needsUpdate();
    }

    private static void renderChunkWaves(
            BlockRenderDispatcher renderer,
            Vec3 iPos,
            ClientLevel world,
            BlockState state,
            BakedModel bm,
            PoseStack matrixStack,
            ChunkPos cp
    ) {
        Collection<BlockPos> waveBlocks = ImmutableList.copyOf(ChunkWavesDataManager.getForClient(cp).getWaves());

        for (BlockPos p : waveBlocks) {
            ModelData model = bm.getModelData(world, p, state, null);

            matrixStack.pushPose();
            matrixStack.translate(
                    p.getX() - iPos.x,
                    p.getY() - iPos.y,
                    p.getZ() - iPos.z
            );
            matrixStack.scale(1, 2, 1);
            renderer.renderSingleBlock(
                    state,
                    matrixStack,
                    mc.renderBuffers().crumblingBufferSource(),
                    15728880,
                    OverlayTexture.NO_OVERLAY,
                    model,
                    RenderType.translucent()
            );
            matrixStack.popPose();
        }
    }

}
