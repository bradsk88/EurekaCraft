package ca.bradj.eurekacraft.client;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.wearables.deployment.DeployedPlayerGoggles;
import ca.bradj.eurekacraft.world.storm.StormSavedData;
import ca.bradj.eurekacraft.world.waves.ChunkWavesDataManager;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;
import java.util.Random;

import static net.minecraft.client.renderer.LevelRenderer.getLightColor;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ChunkStormForgeRendering {

    private static final ResourceLocation RAIN_LOCATION = new ResourceLocation(
            EurekaCraft.MODID,
            "textures/environment/trapar.png"
    );

    private static final float[] rainSizeX = new float[1024];
    private static final float[] rainSizeZ = new float[1024];

    static {
        for (int i = 0; i < 32; ++i) {
            for (int j = 0; j < 32; ++j) {
                float f = (float) (j - 16);
                float f1 = (float) (i - 16);
                float f2 = Mth.sqrt(f * f + f1 * f1);
                rainSizeX[i << 5 | j] = -f1 / f2;
                rainSizeZ[i << 5 | j] = f / f2;
            }
        }
    }

    private static Minecraft mc = Minecraft.getInstance();
    private static Vec3 lastPos;

    @SubscribeEvent
    public static void handleRenderEvent(RenderLevelStageEvent evt) {
        if (true) {
            return;
        }

        if (evt.getStage() != RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
            return;
        }

        if (!DeployedPlayerGoggles.areGogglesBeingWorn(mc.player)) {
            return;
        }

        ClientLevel world = mc.level;
        ChunkPos cp = mc.player.chunkPosition();
        Vec3 eyePos = mc.gameRenderer.getMainCamera()
                .getPosition();
        for (int i = -4; i < 4; i++) {
            for (int j = -4; j < 4; j++) {
                ChunkPos cpj = new ChunkPos(
                        cp.x + i,
                        cp.z + j
                );
                renderStorm(
                        eyePos,
                        world,
                        cpj,
                        evt.getRenderTick(),
                        evt.getPartialTick()
                );
            }
        }

        evt.getLevelRenderer()
                .needsUpdate();
    }

    private static void renderStorm(
            Vec3 iPos,
            ClientLevel level,
            ChunkPos cp,
            int startTick,
            float partialTick
    ) {
        // TODO: Uncomment once working
//        if (!StormSavedData.forChunk(cp).storming) {
//            return;
//        }

        float f = 1.0f; // rain level

        Minecraft.getInstance().gameRenderer.lightTexture()
                .turnOnLightLayer();

        int cameraXInt = Mth.floor(iPos.x);
        int cameraYInt = Mth.floor(iPos.y);
        int cameraZInt = Mth.floor(iPos.z);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        int radius = 5;
        if (Minecraft.useFancyGraphics()) {
            radius = 10;
        }

        RenderSystem.depthMask(Minecraft.useShaderTransparency());
        int i1 = -1;
        float f1 = (float) startTick + partialTick;
        RenderSystem.setShader(GameRenderer::getParticleShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int loopZ = cameraZInt - radius; loopZ <= cameraZInt + radius; ++loopZ) {
            for (int loopX = cameraXInt - radius; loopX <= cameraXInt + radius; ++loopX) {
                int loopBitCoord = (loopZ - cameraZInt + 16) * 32 + loopX - cameraXInt + 16;
                double rainSizeX = (double) ChunkStormForgeRendering.rainSizeX[loopBitCoord] * 0.5D;
                double rainSizeZ = (double) ChunkStormForgeRendering.rainSizeZ[loopBitCoord] * 0.5D;
                blockpos$mutableblockpos.set((double) loopX, iPos.y, (double) loopZ);

//                if (!StormSavedData.forBlockPosition(blockpos$mutableblockpos).storming) {
//                    continue;
//                }

                if (blockpos$mutableblockpos.getX() % 10 != 0) {
                    if (blockpos$mutableblockpos.getZ() % 10 != 0) {
                        continue;
                    }
                }

                int groundHeight = level.getHeight(Heightmap.Types.MOTION_BLOCKING, loopX, loopZ);
                int rainCubeBottom = cameraYInt - radius;
                int rainCubeTop = cameraYInt + radius;
                if (rainCubeBottom < groundHeight) {
                    rainCubeBottom = groundHeight;
                }

                if (rainCubeTop < groundHeight) {
                    rainCubeTop = groundHeight;
                }

                int groundYAboveCamera = groundHeight;
                if (groundHeight < cameraYInt) {
                    groundYAboveCamera = cameraYInt;
                }

                if (rainCubeBottom != rainCubeTop) {
                    Random random = new Random((long) (loopX * loopX * 3121 + loopX * 45238971 ^ loopZ * loopZ * 418711 + loopZ * 13761));
                    blockpos$mutableblockpos.set(loopX, rainCubeBottom, loopZ);
                    if (i1 != 0) {
                        if (i1 >= 0) {
                            tesselator.end();
                        }

                        i1 = 0;
                        RenderSystem.setShaderTexture(0, RAIN_LOCATION);
                        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
                    }

                    int i3 = startTick + loopX * loopX * 3121 + loopX * 45238971 + loopZ * loopZ * 418711 + loopZ * 13761 & 31;
                    float f2 = ((float) i3 + partialTick) / 32.0F * (3.0F + random.nextFloat());
                    double d2 = (double) loopX + 0.5D - iPos.x;
                    double d4 = (double) loopZ + 0.5D - iPos.z;
                    float f3 = (float) Math.sqrt(d2 * d2 + d4 * d4) / (float) radius;
                    float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F) * f;
                    blockpos$mutableblockpos.set(loopX, groundYAboveCamera, loopZ);
                    int uv = getLightColor(level, blockpos$mutableblockpos);
                    bufferbuilder.vertex((double) loopX - iPos.x - rainSizeX + 0.5D, (double) rainCubeTop - iPos.y, (double) loopZ - iPos.z - rainSizeZ + 0.5D).uv(0.0F, (float) rainCubeBottom * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(uv).endVertex();
                    bufferbuilder.vertex((double) loopX - iPos.x + rainSizeX + 0.5D, (double) rainCubeTop - iPos.y, (double) loopZ - iPos.z + rainSizeZ + 0.5D).uv(1.0F, (float) rainCubeBottom * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(uv).endVertex();
                    bufferbuilder.vertex((double) loopX - iPos.x + rainSizeX + 0.5D, (double) rainCubeBottom - iPos.y, (double) loopZ - iPos.z + rainSizeZ + 0.5D).uv(1.0F, (float) rainCubeTop * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(uv).endVertex();
                    bufferbuilder.vertex((double) loopX - iPos.x - rainSizeX + 0.5D, (double) rainCubeBottom - iPos.y, (double) loopZ - iPos.z - rainSizeZ + 0.5D).uv(0.0F, (float) rainCubeTop * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(uv).endVertex();
                }
            }
        }

        if (i1 >= 0) {
            tesselator.end();
        }

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        Minecraft.getInstance().gameRenderer.lightTexture()
                .turnOffLightLayer();
    }

}
