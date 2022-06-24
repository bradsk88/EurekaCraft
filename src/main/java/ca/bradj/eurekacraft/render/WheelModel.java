package ca.bradj.eurekacraft.render;

import ca.bradj.eurekacraft.EurekaCraft;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.awt.*;

public class WheelModel extends EntityModel<Entity> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(
            EurekaCraft.MODID, "textures/render/ref_board.png"
    );

    private final ModelPart VoxelShapes;
    protected final Color color;

    public WheelModel() {
        this(Color.RED);
    }

    public WheelModel(Color color) {
        this.VoxelShapes = this.build();
        this.color = color;
    }

    private ModelPart build() {
        CubeListBuilder VoxelShapes = CubeListBuilder.create();
//        VoxelShapes.setPos(0.0F, 24.0F, 0.0F);
        VoxelShapes.texOffs(0, 0).addBox(-10.0F, 2.0F, -1.0F, 3.0F, -1.0F, 2.0F);

        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("wheel", VoxelShapes, PartPose.rotation( (float) Math.PI, 0, 0));
        return LayerDefinition.create(meshdefinition, 0, 0).bakeRoot();
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        //previously the render function, render code was moved to a method below
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        float r = this.color.getRed() / 255f;
        float g = this.color.getGreen() / 255f;
        float b = this.color.getBlue() / 255f;
        VoxelShapes.render(
                matrixStack, buffer, packedLight, packedOverlay,
                red * r, green * g, blue * b, alpha
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

    public RenderType getRenderType() {
        return this.renderType(TEXTURE);
    }

}
