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
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.data.EmptyModelData;

public class WaveBlockTileEntityRenderer implements BlockEntityRenderer<TraparWaveChildBlock.TileEntity> {

    private Minecraft mc = Minecraft.getInstance();

    private final BlockEntityRendererProvider.Context context;

    public WaveBlockTileEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.context = ctx;
    }

    @Override
    public void render(TraparWaveChildBlock.TileEntity te, float partialTicks, PoseStack matrixStackIn,
                       MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        boolean worn = DeployedPlayerGoggles.areGogglesBeingWorn(mc.player);
        EurekaCraft.LOGGER.trace("TileEntity: Goggles worn=" + worn + " for player " + mc.player);
        BlockRenderDispatcher disp = this.context.getBlockRenderDispatcher();
        if (!worn) {
            disp.renderSingleBlock(
                    Blocks.AIR.defaultBlockState(),
                    matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE
            );
            return;
        }
        disp.renderSingleBlock(
                BlocksInit.TRAPAR_WAVE_CHILD_BLOCK.get().defaultBlockState(),
                matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE
        );
    }
}
