package ca.bradj.eurekacraft.render;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.blocks.TraparWaveBlock;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import ca.bradj.eurekacraft.vehicles.EntityRefBoard;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = EurekaCraft.MODID)
public class TraparWaveHandler {

    private static Logger logger = LogManager.getLogger(EurekaCraft.MODID);
//
    @SubscribeEvent
    public static void render(RenderWorldLastEvent event) {
        logger.debug("Render. Waves near players = " + TraparWaveBlock.wavesNearPlayers.values());
        for (TraparWaveBlock.TileEntity b : TraparWaveBlock.wavesNearPlayers.values()) {
            renderBlock(event.getMatrixStack(), b.getBlockPos().offset(0, 4, 0), BlocksInit.TRAPAR_WAVE_CHILD_BLOCK.get().defaultBlockState());
        }
//        Vec3d location; // some location in world
//        BlockState state; // some BlockState (does not have to be part of world)
//        renderBlock(event.getMatrixStack(), location, state);
    }

    public static void renderBlock(MatrixStack matrixStack, BlockPos pos, BlockState state) {
        BlockRendererDispatcher renderer = Minecraft.getInstance().getBlockRenderer();
        ClientWorld world = Minecraft.getInstance().level;
        IModelData model = renderer.getBlockModel(state).getModelData(world, pos, state, ModelDataManager.getModelData(world, pos));
        Minecraft.getInstance().getBlockRenderer().renderBlock(state, matrixStack, Minecraft.getInstance().renderBuffers().bufferSource(), 15728880, OverlayTexture.NO_OVERLAY, model);

    }

}

