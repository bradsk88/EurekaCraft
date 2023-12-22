package ca.bradj.eurekacraft.render;

//import ca.bradj.eurekacraft.EurekaCraft;
//import ca.bradj.eurekacraft.wearables.deployment.DeployedPlayerGoggles;
//import ca.bradj.eurekacraft.world.storm.StormSavedData;
//import com.mojang.blaze3d.systems.RenderSystem;
//import com.mojang.blaze3d.vertex.BufferBuilder;
//import com.mojang.blaze3d.vertex.DefaultVertexFormat;
//import com.mojang.blaze3d.vertex.Tesselator;
//import com.mojang.blaze3d.vertex.VertexFormat;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.multiplayer.ClientLevel;
//import net.minecraft.client.renderer.GameRenderer;
//import net.minecraft.client.renderer.LightTexture;
//import net.minecraft.core.BlockPos;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.util.Mth;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.levelgen.Heightmap;
//import net.minecraftforge.client.IWeatherRenderHandler;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.util.Random;
//
//import static net.minecraft.client.renderer.LevelRenderer.getLightColor;

public class TraparStormRenderHandler { // implements IWeatherRenderHandler {

    // TODO: The weather overlay renderer changed in 1.19 - try to bring it back

//    private static final Logger logger = LogManager.getLogger(EurekaCraft.MODID);
//
//    private static final ResourceLocation RAIN_LOCATION = new ResourceLocation(EurekaCraft.MODID, "textures/environment/trapar.png");
//
//    private final float[] rainSizeX = new float[1024];
//    private final float[] rainSizeZ = new float[1024];
//    private final Minecraft minecraft = Minecraft.getInstance();
//
//    public TraparStormRenderHandler() {
//        for (int i = 0; i < 32; ++i) {
//            for (int j = 0; j < 32; ++j) {
//                float f = (float) (j - 16);
//                float f1 = (float) (i - 16);
//                float f2 = Mth.sqrt(f * f + f1 * f1);
//                this.rainSizeX[i << 5 | j] = -f1 / f2;
//                this.rainSizeZ[i << 5 | j] = f / f2;
//            }
//        }
//    }
//
//    @Override
//    public void render(int ticks, float partialTicks, ClientLevel world, Minecraft mc, LightTexture lightMapIn, double xIn, double yIn, double zIn) {
//        // TODO: Reimplement
//        if (mc.getCameraEntity() == null) {
//            return;
//        }
//
//        if (!DeployedPlayerGoggles.areGogglesBeingWorn(mc.getCameraEntity())) {
//            logger.error("Trapar Storm Renderer is still installed but player is not wearing goggles");
//            return;
//        }
//
//        float f = 1.0f; // rain level
//
//        lightMapIn.turnOnLightLayer();
//        Level level = this.minecraft.level;
//        int i = Mth.floor(xIn);
//        int j = Mth.floor(yIn);
//        int k = Mth.floor(zIn);
//        Tesselator tesselator = Tesselator.getInstance();
//        BufferBuilder bufferbuilder = tesselator.getBuilder();
//        RenderSystem.disableCull();
//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
//        RenderSystem.enableDepthTest();
//        int l = 5;
//        if (Minecraft.useFancyGraphics()) {
//            l = 10;
//        }
//
//        RenderSystem.depthMask(Minecraft.useShaderTransparency());
//        int i1 = -1;
//        float f1 = (float) ticks + partialTicks;
//        RenderSystem.setShader(GameRenderer::getParticleShader);
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
//
//        for (int j1 = k - l; j1 <= k + l; ++j1) {
//            for (int k1 = i - l; k1 <= i + l; ++k1) {
//                int l1 = (j1 - k + 16) * 32 + k1 - i + 16;
//                double d0 = (double) this.rainSizeX[l1] * 0.5D;
//                double d1 = (double) this.rainSizeZ[l1] * 0.5D;
//                blockpos$mutableblockpos.set((double) k1, yIn, (double) j1);
//
//                if (!StormSavedData.forBlockPosition(blockpos$mutableblockpos).storming) {
//                    continue;
//                }
//
//                int i2 = level.getHeight(Heightmap.Types.MOTION_BLOCKING, k1, j1);
//                int j2 = j - l;
//                int k2 = j + l;
//                if (j2 < i2) {
//                    j2 = i2;
//                }
//
//                if (k2 < i2) {
//                    k2 = i2;
//                }
//
//                int l2 = i2;
//                if (i2 < j) {
//                    l2 = j;
//                }
//
//                if (j2 != k2) {
//                    Random random = new Random((long) (k1 * k1 * 3121 + k1 * 45238971 ^ j1 * j1 * 418711 + j1 * 13761));
//                    blockpos$mutableblockpos.set(k1, j2, j1);
//                    if (i1 != 0) {
//                        if (i1 >= 0) {
//                            tesselator.end();
//                        }
//
//                        i1 = 0;
//                        RenderSystem.setShaderTexture(0, RAIN_LOCATION);
//                        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
//                    }
//
//                    int i3 = ticks + k1 * k1 * 3121 + k1 * 45238971 + j1 * j1 * 418711 + j1 * 13761 & 31;
//                    float f2 = ((float) i3 + partialTicks) / 32.0F * (3.0F + random.nextFloat());
//                    double d2 = (double) k1 + 0.5D - xIn;
//                    double d4 = (double) j1 + 0.5D - zIn;
//                    float f3 = (float) Math.sqrt(d2 * d2 + d4 * d4) / (float) l;
//                    float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F) * f;
//                    blockpos$mutableblockpos.set(k1, l2, j1);
//                    int j3 = getLightColor(level, blockpos$mutableblockpos);
//                    bufferbuilder.vertex((double) k1 - xIn - d0 + 0.5D, (double) k2 - yIn, (double) j1 - zIn - d1 + 0.5D).uv(0.0F, (float) j2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();
//                    bufferbuilder.vertex((double) k1 - xIn + d0 + 0.5D, (double) k2 - yIn, (double) j1 - zIn + d1 + 0.5D).uv(1.0F, (float) j2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();
//                    bufferbuilder.vertex((double) k1 - xIn + d0 + 0.5D, (double) j2 - yIn, (double) j1 - zIn + d1 + 0.5D).uv(1.0F, (float) k2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();
//                    bufferbuilder.vertex((double) k1 - xIn - d0 + 0.5D, (double) j2 - yIn, (double) j1 - zIn - d1 + 0.5D).uv(0.0F, (float) k2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();
//                }
//            }
//        }
//
//        if (i1 >= 0) {
//            tesselator.end();
//        }
//
//        RenderSystem.enableCull();
//        RenderSystem.disableBlend();
//        lightMapIn.turnOffLightLayer();
//    }
}