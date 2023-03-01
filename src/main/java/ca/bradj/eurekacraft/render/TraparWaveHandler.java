package ca.bradj.eurekacraft.render;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.blocks.TraparWaveChildBlock;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.wearables.deployment.DeployedPlayerGoggles;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.EmptyModelData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TraparWaveHandler implements BlockEntityRenderer<TraparWaveChildBlock.TileEntity> {

    private Minecraft mc = Minecraft.getInstance();
    private Logger logger = LogManager.getLogger(EurekaCraft.MODID);

    private final BlockEntityRendererProvider.Context context;

    public TraparWaveHandler(BlockEntityRendererProvider.Context ctx) {
        this.context = ctx;
    }

    @Override
    public void render(TraparWaveChildBlock.TileEntity te, float partialTicks, PoseStack matrixStackIn,
                       MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
//        if (!DeployedPlayerGoggles.areGogglesBeingWorn(mc.player)) {
//            return;
//        }
        BlockRenderDispatcher disp = this.context.getBlockRenderDispatcher();
        disp.renderSingleBlock(
                BlocksInit.TRAPAR_WAVE_CHILD_BLOCK.get().defaultBlockState(),
                matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE
        );
    }
}
