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
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
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
        ItemStack helmet = player.getItemBySlot(EquipmentSlotType.HEAD);
        if (helmet.isEmpty()) {
            return;
        }

        // TODO: Check player for goggles

        for (BlockPos p : te.getShape().getAsRelativeBlockPositions()) {
            renderBlock(p, matrixStackIn, bufferIn,  1.0f);
        }
    }

    private void renderBlock(Vector3i translation, MatrixStack matrixStack,
                             IRenderTypeBuffer buffer, float scale) {
        matrixStack.pushPose();
        matrixStack.translate(translation.getX(), translation.getY(), translation.getZ());
        matrixStack.scale(scale, scale, scale);

        // FIXME: The rendered blocks are weird (sees through water, etc)

        BlockState bs = BlocksInit.TRAPAR_WAVE_CHILD_BLOCK.get().defaultBlockState();
        mc.getBlockRenderer().renderBlock(bs, matrixStack, buffer, Integer.MAX_VALUE, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
        matrixStack.popPose();
    }
}
