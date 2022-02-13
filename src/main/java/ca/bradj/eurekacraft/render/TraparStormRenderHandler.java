package ca.bradj.eurekacraft.render;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.wearables.deployment.DeployedPlayerGoggles;
import ca.bradj.eurekacraft.world.storm.StormSavedData;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.client.IWeatherRenderHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

import static net.minecraft.client.renderer.WorldRenderer.getLightColor;

public class TraparStormRenderHandler implements IWeatherRenderHandler {

    private static final Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    private static final ResourceLocation RAIN_LOCATION = new ResourceLocation(EurekaCraft.MODID, "textures/environment/trapar.png");

    private final float[] rainSizeX = new float[1024];
    private final float[] rainSizeZ = new float[1024];
    private final Minecraft minecraft = Minecraft.getInstance();

    public TraparStormRenderHandler() {
        for(int i = 0; i < 32; ++i) {
            for(int j = 0; j < 32; ++j) {
                float f = (float)(j - 16);
                float f1 = (float)(i - 16);
                float f2 = MathHelper.sqrt(f * f + f1 * f1);
                this.rainSizeX[i << 5 | j] = -f1 / f2;
                this.rainSizeZ[i << 5 | j] = f / f2;
            }
        }
    }

    @Override
    public void render(int ticks, float partialTicks, ClientWorld world, Minecraft mc, LightTexture lightMapIn, double xIn, double yIn, double zIn) {
        if (mc.getCameraEntity() == null) {
            return;
        }

        if (!DeployedPlayerGoggles.areGogglesBeingWorn(mc.getCameraEntity())) {
            logger.error("Trapar Storm Renderer is still installed but player is not wearing goggles");
            return;
        }

        float f = 1.0f; // rain level

        lightMapIn.turnOnLightLayer();
        int i = MathHelper.floor(xIn);
        int j = MathHelper.floor(yIn);
        int k = MathHelper.floor(zIn);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        RenderSystem.enableAlphaTest();
        RenderSystem.disableCull();
        RenderSystem.normal3f(0.0F, 1.0F, 0.0F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.enableDepthTest();
        int l = 5;
        if (Minecraft.useFancyGraphics()) {
            l = 10;
        }

        RenderSystem.depthMask(Minecraft.useShaderTransparency());
        int i1 = -1;
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for (int j1 = k - l; j1 <= k + l; ++j1) {
            for (int k1 = i - l; k1 <= i + l; ++k1) {
                int l1 = (j1 - k + 16) * 32 + k1 - i + 16;
                double d0 = (double) this.rainSizeX[l1] * 0.5D;
                double d1 = (double) this.rainSizeZ[l1] * 0.5D;
                blockpos$mutable.set(k1, 0, j1);

                if (!StormSavedData.forBlockPosition(blockpos$mutable).storming) {
                    continue;
                }

                int i2 = world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING, blockpos$mutable).getY();
                int j2 = j - l;
                int k2 = j + l;
                if (j2 < i2) {
                    j2 = i2;
                }

                if (k2 < i2) {
                    k2 = i2;
                }

                int l2 = i2;
                if (i2 < j) {
                    l2 = j;
                }

                if (j2 != k2) {
                    Random random = new Random(k1 * k1 * 3121 + k1 * 45238971 ^ j1 * j1 * 418711 + j1 * 13761);
                    blockpos$mutable.set(k1, j2, j1);
                    if (i1 != 0) {
                        if (i1 >= 0) {
                            tessellator.end();
                        }

                        i1 = 0;
                        this.minecraft.getTextureManager().bind(RAIN_LOCATION);
                        bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE);
                    }

                    int i3 = ticks + k1 * k1 * 3121 + k1 * 45238971 + j1 * j1 * 418711 + j1 * 13761 & 31;
                    float f3 = (0.2f) * ((float) i3 + partialTicks) / 32.0F * (3.0F + random.nextFloat());
                    double d2 = (double) ((float) k1 + 0.5F) - xIn;
                    double d4 = (double) ((float) j1 + 0.5F) - zIn;
                    float f4 = MathHelper.sqrt(d2 * d2 + d4 * d4) / (float) l;
                    float f5 = ((1.0F - f4 * f4) * 0.5F + 0.5F) * f;
                    blockpos$mutable.set(k1, l2, j1);
                    int j3 = getLightColor(world, blockpos$mutable);
                    bufferbuilder.vertex((double) k1 - xIn - d0 + 0.5D, (double) k2 - yIn, (double) j1 - zIn - d1 + 0.5D).uv(0.0F, (float) j2 * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).uv2(j3).endVertex();
                    bufferbuilder.vertex((double) k1 - xIn + d0 + 0.5D, (double) k2 - yIn, (double) j1 - zIn + d1 + 0.5D).uv(1.0F, (float) j2 * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).uv2(j3).endVertex();
                    bufferbuilder.vertex((double) k1 - xIn + d0 + 0.5D, (double) j2 - yIn, (double) j1 - zIn + d1 + 0.5D).uv(1.0F, (float) k2 * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).uv2(j3).endVertex();
                    bufferbuilder.vertex((double) k1 - xIn - d0 + 0.5D, (double) j2 - yIn, (double) j1 - zIn - d1 + 0.5D).uv(0.0F, (float) k2 * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).uv2(j3).endVertex();
                }
            }
        }

        if (i1 >= 0) {
            tessellator.end();
        }

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.disableAlphaTest();
        lightMapIn.turnOffLightLayer();
    }
}