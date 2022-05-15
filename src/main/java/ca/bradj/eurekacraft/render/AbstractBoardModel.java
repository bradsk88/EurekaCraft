package ca.bradj.eurekacraft.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public abstract class AbstractBoardModel extends EntityModel<Entity> {
    private final LayerDefinition VoxelShapes;
    private final ResourceLocation texture;

    public AbstractBoardModel() {
        this.VoxelShapes = this.build();
        this.texture = this.getTexture();
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        //previously the render function, render code was moved to a method below
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        VoxelShapes.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public ModelPart getModelRenderer() {
        return VoxelShapes;
    }

    public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    protected abstract LayerDefinition build();


    public RenderType getRenderType() {
        return this.renderType(this.texture);
    }

    protected abstract ResourceLocation getTexture();
}
