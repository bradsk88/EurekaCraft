package ca.bradj.eurekacraft.render;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.blocks.TraparWaveBlock;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.client.model.data.EmptyModelData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TraparWaveHandler extends TileEntityRenderer<TraparWaveBlock.TileEntity> {

    private Minecraft mc = Minecraft.getInstance();
    private Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    public TraparWaveHandler(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }
    @Override
    public void render(TraparWaveBlock.TileEntity te, float partialTicks, MatrixStack matrixStackIn,
                       IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {

        ClientPlayerEntity player = mc.player;

        // TODO: Check player for goggles

        int lightLevel = Integer.MAX_VALUE;

        for (Vector3i p : te.children.keySet()) {
            renderBlock(p,
                    matrixStackIn, bufferIn, partialTicks,
                    combinedOverlayIn, lightLevel, 1.0f);
        }
    }

    private void renderBlock(Vector3i translation, MatrixStack matrixStack,
                             IRenderTypeBuffer buffer, float partialTicks, int combinedOverlay, int lightLevel, float scale) {
        matrixStack.pushPose();
        matrixStack.translate(translation.getX(), translation.getY(), translation.getZ());
        matrixStack.scale(scale, scale, scale);

        // FIXME: The rendered blocks are weird (sees through water, etc)

        BlockState bs = BlocksInit.TRAPAR_WAVE_BLOCK.get().defaultBlockState();
//        mc.getItemRenderer().render(stack, ItemCameraTransforms.TransformType.GROUND, true, matrixStack, buffer,
//                lightLevel, combinedOverlay, model);
        mc.getBlockRenderer().renderBlock(bs, matrixStack, buffer, Integer.MAX_VALUE, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
        matrixStack.popPose();
    }
}
