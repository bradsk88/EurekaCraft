package ca.bradj.eurekacraft.render;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.wearables.deployment.DeployedPlayerGoggles;
import ca.bradj.eurekacraft.world.storm.StormSavedData;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.client.extensions.IForgeDimensionSpecialEffects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

import static net.minecraft.client.renderer.LevelRenderer.getLightColor;

public class TraparStormRenderHandler implements IForgeDimensionSpecialEffects { //implements IWeatherRenderHandler {

    private static final Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    private static final ResourceLocation RAIN_LOCATION = new ResourceLocation(EurekaCraft.MODID, "textures/environment/trapar.png");

    private final float[] rainSizeX = new float[1024];
    private final float[] rainSizeZ = new float[1024];
    private final Minecraft minecraft = Minecraft.getInstance();

    public TraparStormRenderHandler() {
        for (int i = 0; i < 32; ++i) {
            for (int j = 0; j < 32; ++j) {
                float f = (float) (j - 16);
                float f1 = (float) (i - 16);
                float f2 = Mth.sqrt(f * f + f1 * f1);
                this.rainSizeX[i << 5 | j] = -f1 / f2;
                this.rainSizeZ[i << 5 | j] = f / f2;
            }
        }
    }

    @Override
    public boolean renderClouds(
            ClientLevel level,
            int ticks,
            float partialTick,
            PoseStack poseStack,
            double blockPosXIn,
            double blockPosYIn,
            double blockPosZIn,
            Matrix4f projectionMatrix
    ) {
        if (minecraft.getCameraEntity() == null) {
            return false;
        }

        if (!DeployedPlayerGoggles.areGogglesBeingWorn(minecraft.getCameraEntity())) {
            logger.error("Trapar Storm Renderer is still installed but player is not wearing goggles");
            return false;
        }

        float f = 1.0f; // rain level

//        lightMapIn.turnOnLightLayer();
        int xInt = Mth.floor(blockPosXIn);
        int cameraYInt = Mth.floor(blockPosYIn);
        int zInt = Mth.floor(blockPosZIn);
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
        float f1 = (float) ticks + partialTick;
        RenderSystem.setShader(GameRenderer::getParticleShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int loopZ = zInt - radius; loopZ <= zInt + radius; ++loopZ) {
            for (int loopX = xInt - radius; loopX <= xInt + radius; ++loopX) {
                int loopBitCoord = (loopZ - zInt + 16) * 32 + loopX - xInt + 16;
                double rainSizeX = (double) this.rainSizeX[loopBitCoord] * 0.5D;
                double rainSizeZ = (double) this.rainSizeZ[loopBitCoord] * 0.5D;
                blockpos$mutableblockpos.set((double) loopX, blockPosYIn, (double) loopZ);

                if (!StormSavedData.forBlockPosition(blockpos$mutableblockpos).storming) {
                    continue;
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

                    int i3 = ticks + loopX * loopX * 3121 + loopX * 45238971 + loopZ * loopZ * 418711 + loopZ * 13761 & 31;
                    float f2 = ((float) i3 + partialTick) / 32.0F * (3.0F + random.nextFloat());
                    double d2 = (double) loopX + 0.5D - blockPosXIn;
                    double d4 = (double) loopZ + 0.5D - blockPosZIn;
                    float f3 = (float) Math.sqrt(d2 * d2 + d4 * d4) / (float) radius;
                    float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F) * f;
                    blockpos$mutableblockpos.set(loopX, groundYAboveCamera, loopZ);
                    int uv = getLightColor(level, blockpos$mutableblockpos);
                    bufferbuilder.vertex((double) loopX - blockPosXIn - rainSizeX + 0.5D, (double) rainCubeTop - blockPosYIn, (double) loopZ - blockPosZIn - rainSizeZ + 0.5D).uv(0.0F, (float) rainCubeBottom * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(uv).endVertex();
                    bufferbuilder.vertex((double) loopX - blockPosXIn + rainSizeX + 0.5D, (double) rainCubeTop - blockPosYIn, (double) loopZ - blockPosZIn + rainSizeZ + 0.5D).uv(1.0F, (float) rainCubeBottom * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(uv).endVertex();
                    bufferbuilder.vertex((double) loopX - blockPosXIn + rainSizeX + 0.5D, (double) rainCubeBottom - blockPosYIn, (double) loopZ - blockPosZIn + rainSizeZ + 0.5D).uv(1.0F, (float) rainCubeTop * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(uv).endVertex();
                    bufferbuilder.vertex((double) loopX - blockPosXIn - rainSizeX + 0.5D, (double) rainCubeBottom - blockPosYIn, (double) loopZ - blockPosZIn - rainSizeZ + 0.5D).uv(0.0F, (float) rainCubeTop * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(uv).endVertex();
                }
            }
        }

        if (i1 >= 0) {
            tesselator.end();
        }

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
//        lightMapIn.turnOffLightLayer();
        return true;
    }
}