package ca.bradj.eurekacraft.client;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.world.waves.ChunkWavesDataManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class WaveBlockRendering {

    private static Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public static void registerDataRenderers(RenderLevelLastEvent evt){
        BlockRenderDispatcher renderer = Minecraft.getInstance().getBlockRenderer();
        ClientLevel world = mc.level;
        BlockState state = BlocksInit.RESIN.get().defaultBlockState();
        BakedModel bm = renderer.getBlockModel(state);
        PoseStack matrixStack = evt.getPoseStack();

        ChunkPos cp = mc.player.chunkPosition();
        // TODO: Render in front of player (unless camere facing backward)
        for (int i = -4; i < 4; i++) {
            for (int j = -4; j < 4; j++) {
                ChunkPos cpj = new ChunkPos(cp.x + i, cp.z + j);
                renderChunkWaves(evt.getPartialTick(), renderer, world, state, bm, matrixStack, cpj);
            }
        }

    }

    private static void renderChunkWaves(float partialTick, BlockRenderDispatcher renderer, ClientLevel world, BlockState state, BakedModel bm, PoseStack matrixStack, ChunkPos cp) {
        // TODO: Make faster by caching previous (eye? block?) position and interpolate
        // https://forums.minecraftforge.net/topic/38327-1710-renderplayerpre-event-how-to-smooth-out-model-jitter/#comment-203576
        Collection<BlockPos> waveBlocks =  ChunkWavesDataManager.getForClient(cp).getWaves();
        for (BlockPos p : waveBlocks) {
            IModelData model = bm.getModelData(world, p, state, ModelDataManager.getModelData(world, new BlockPos(p)));

            matrixStack.pushPose();
            matrixStack.translate(
                    p.getX() - mc.player.getEyePosition().x,
                    p.getY() - mc.player.getEyePosition().y,
                    p.getZ() - mc.player.getEyePosition().z
            );
            renderer.renderSingleBlock(state, matrixStack, mc.renderBuffers().crumblingBufferSource(), 15728880, OverlayTexture.NO_OVERLAY, model);
            matrixStack.popPose();
        }
    }

}
