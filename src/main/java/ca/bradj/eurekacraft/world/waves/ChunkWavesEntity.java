package ca.bradj.eurekacraft.world.waves;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.core.init.BlocksInit;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

// ChunkWavesEntity contains (and renders) all of the trapar waves in a chunk
public class ChunkWavesEntity extends Entity {
    public static final String ENTITY_ID = "chunk_wave_entity";

    public ChunkWavesEntity(
            EntityType<ChunkWavesEntity> p_19870_,
            Level p_19871_
    ) {
        this(p_19870_, p_19871_, BlockPos.ZERO);
    }

    private ChunkWavesEntity(
            EntityType<ChunkWavesEntity> p_19870_,
            Level p_19871_,
            BlockPos mbp
    ) {
        this(p_19870_, p_19871_, mbp, ImmutableSet.of());
        this.setPos(mbp.getX(), mbp.getY(), mbp.getZ());
    }

    public ChunkWavesEntity(
            EntityType<ChunkWavesEntity> p_19870_,
            Level world,
            BlockPos mbp,
            Set<BlockPos> waves
    ) {
        super(p_19870_, world);
        this.setPos(mbp.getX(), mbp.getY(), mbp.getZ());
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_20139_) {

    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public @NotNull AABB getBoundingBoxForCulling() {
        AABB bb = super.getBoundingBoxForCulling().inflate(3, 20, 3);
        return bb;
    }

    public static class Renderer extends EntityRenderer<ChunkWavesEntity> {
        private final BlockRenderDispatcher blockRenderer;

        public Renderer(EntityRendererProvider.Context ctx) {
            super(ctx);
            this.blockRenderer = ctx.getBlockRenderDispatcher();
        }

        @Override
        public ResourceLocation getTextureLocation(ChunkWavesEntity p_114482_) {
            return new ResourceLocation(EurekaCraft.MODID, "chunk_wave_entity");
        }

        @Override
        public void render(
                ChunkWavesEntity entity,
                float p_114486_,
                float p_114487_,
                PoseStack matrixStack,
                MultiBufferSource buffer,
                int p_114490_
        ) {
            super.render(entity, p_114486_, p_114487_, matrixStack, buffer, p_114490_);
            BlockState blockstate = BlocksInit.TRAPAR_WAVE_CHILD_BLOCK.get().defaultBlockState();

            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            blockRenderer.renderSingleBlock(
                    blockstate,
                    matrixStack,
                    buffer,
                    150,
                    OverlayTexture.NO_OVERLAY,
                    ModelData.EMPTY,
                    RenderType.translucentMovingBlock()
            );

            BlockPos p = entity.blockPosition();
//            renderDebugBoxes(entity, matrixStack, buffer, blockstate, p);
            renderWaves(entity, matrixStack, buffer, blockstate, p);
        }

        private void renderWaves(
                ChunkWavesEntity entity,
                PoseStack matrixStack,
                MultiBufferSource buffer,
                BlockState blockstate,
                BlockPos p
        ) {
            // TODO: Make this copy when the entity is created?
            Collection<BlockPos> waveBlocks = ImmutableList.copyOf(
                    ChunkWavesDataManager.getForClient(entity.chunkPosition()).getWaves()
            );
            for (BlockPos w : waveBlocks) {
                if (!entity.level.isEmptyBlock(w)) {
                    continue;
                }
                matrixStack.pushPose();
                matrixStack.translate(w.getX() - p.getX(), w.getY() - p.getY(), w.getZ() - p.getZ());
                blockRenderer.renderSingleBlock(
                        blockstate,
                        matrixStack,
                        buffer,
                        150,
                        OverlayTexture.NO_OVERLAY,
                        ModelData.EMPTY,
                        RenderType.translucentMovingBlock()
                );
                matrixStack.popPose();
            }
        }

        private void renderDebugBoxes(
                ChunkWavesEntity entity,
                PoseStack matrixStack,
                MultiBufferSource buffer,
                BlockState blockstate,
                BlockPos p
        ) {
            for (Direction d : Direction.Plane.HORIZONTAL) {
                matrixStack.pushPose();
                BlockPos shifted = p.relative(d).relative(d).relative(d).relative(d).relative(d).relative(d).relative(d).relative(d);
                matrixStack.translate(shifted.getX() - p.getX(), shifted.getY() - p.getY(), shifted.getZ() - p.getZ());
                blockRenderer.renderSingleBlock(
                        blockstate,
                        matrixStack,
                        buffer,
                        150,
                        OverlayTexture.NO_OVERLAY,
                        ModelData.EMPTY,
                        RenderType.solid()
                );
                matrixStack.popPose();
            }
            for (Direction d : Direction.Plane.VERTICAL) {
                BlockPos shifted = p.relative(d).relative(d).relative(d).relative(d).relative(d).relative(d).relative(d).relative(d).relative(d).relative(d).relative(d).relative(d).relative(d).relative(d);
                if (entity.level.isEmptyBlock(shifted)) {
                    continue;
                }
                matrixStack.pushPose();
                matrixStack.translate(shifted.getX() - p.getX(), shifted.getY() - p.getY(), shifted.getZ() - p.getZ());
                blockRenderer.renderSingleBlock(
                        blockstate,
                        matrixStack,
                        buffer,
                        150,
                        OverlayTexture.NO_OVERLAY,
                        ModelData.EMPTY,
                        RenderType.translucentMovingBlock()
                );
                matrixStack.popPose();
            }
        }
    }
}
