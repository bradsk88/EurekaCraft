package ca.bradj.eurekacraft.render;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.blocks.TraparWaveChildBlock;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.wearables.deployment.DeployedPlayerGoggles;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

public class WaveBlockTileEntityRenderer implements BlockEntityRenderer<TraparWaveChildBlock.TileEntity> {

    private Minecraft mc = Minecraft.getInstance();

    private final BlockEntityRendererProvider.Context context;

    public WaveBlockTileEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.context = ctx;
    }

    @Override
    public void render(
            TraparWaveChildBlock.TileEntity te,
            float partialTicks,
            PoseStack matrixStackIn,
            MultiBufferSource bufferIn,
            int combinedLightIn,
            int combinedOverlayIn
    ) {
        BlockRenderDispatcher renderer = Minecraft.getInstance().getBlockRenderer();
        BlockState state = BlocksInit.TRAPAR_WAVE_CHILD_BLOCK.get().defaultBlockState();
        BakedModel bm = renderer.getBlockModel(state);
        boolean worn = DeployedPlayerGoggles.areGogglesBeingWorn(mc.player);
        EurekaCraft.LOGGER.debug("TileEntity: Goggles worn=" + worn + " for player " + mc.player);
        BlockRenderDispatcher disp = this.context.getBlockRenderDispatcher();
        if (!worn) {
            return;
        }
        @NotNull ModelData md = bm.getModelData(te.getLevel(), te.getBlockPos(), state, null);
        disp.renderSingleBlock(
                BlocksInit.TRAPAR_WAVE_CHILD_BLOCK.get().defaultBlockState(),
                matrixStackIn,
                bufferIn,
                combinedLightIn,
                combinedOverlayIn,
                md,
                RenderType.translucent()
        );
    }
}
