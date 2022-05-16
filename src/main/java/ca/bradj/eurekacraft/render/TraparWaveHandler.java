package ca.bradj.eurekacraft.render;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.blocks.TraparWaveBlock;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.wearables.deployment.DeployedPlayerGoggles;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.EmptyModelData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TraparWaveHandler implements BlockEntityRenderer<TraparWaveBlock.TileEntity> {

    private Minecraft mc = Minecraft.getInstance();
    private Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    public TraparWaveHandler() {
    }

    @Override
    public void render(TraparWaveBlock.TileEntity te, float partialTicks, PoseStack matrixStackIn,
                       MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {

        if (!DeployedPlayerGoggles.areGogglesBeingWorn(mc.player)) {
            return;
        }

        for (BlockPos p : te.getShape().getAsRelativeBlockPositions()) {
            renderBlock(p, matrixStackIn, bufferIn,  1.0f);
        }
    }

    private void renderBlock(BlockPos translation, PoseStack matrixStack,
                             MultiBufferSource buffer, float scale) {
        matrixStack.pushPose();
        matrixStack.translate(translation.getX(), translation.getY(), translation.getZ());
        matrixStack.scale(scale, scale, scale);

        // FIXME: The rendered blocks are weird (sees through water, etc)

        BlockState bs = BlocksInit.TRAPAR_WAVE_CHILD_BLOCK.get().defaultBlockState();
        mc.getBlockRenderer().renderSingleBlock(bs, matrixStack, buffer, Integer.MAX_VALUE, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
        matrixStack.popPose();
    }
}
