package ca.bradj.eurekacraft.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public abstract class AbstractBoardModel<M extends AbstractBoardModel<M>> extends EntityModel<Entity> {
    private final ModelPart VoxelShapes;
    private final ResourceLocation texture;
    protected float r;
    protected float g;
    protected float b;

    protected AbstractBoardModel() {
        this(1, 1, 1);
    }

    public AbstractBoardModel(float r, float g, float b) {
        this.VoxelShapes = this.build();
        this.texture = this.getTexture();
        this.r = r; this.g = g; this.b = b;
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        //previously the render function, render code was moved to a method below
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        VoxelShapes.render(
                matrixStack, buffer, packedLight, packedOverlay,
                red * this.r, green * this.g, blue * this.b, alpha
        );
    }

    public ModelPart getModelRenderer() {
        return VoxelShapes;
    }

    public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    protected abstract ModelPart build();


    public RenderType getRenderType() {
        return this.renderType(this.texture);
    }

    protected abstract ResourceLocation getTexture();

    public abstract M withColor(float r, float g, float b);
}
