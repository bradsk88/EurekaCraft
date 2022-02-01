package ca.bradj.eurekacraft.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractBoardModel extends EntityModel<Entity> {
    private final ModelRenderer VoxelShapes;
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
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        VoxelShapes.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public ModelRenderer getModelRenderer() {
        return VoxelShapes;
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    protected abstract ModelRenderer build();


    public RenderType getRenderType() {
        return this.renderType(this.texture);
    }

    protected abstract ResourceLocation getTexture();
}
