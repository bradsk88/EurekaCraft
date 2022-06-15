package ca.bradj.eurekacraft.render;

import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class GlideBoardModel extends AbstractBoardModel<GlideBoardModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(
            EurekaCraft.MODID, "textures/render/oak_planks_quad.png"
    );

    public GlideBoardModel() {
        super(1, 1, 1);
    }

    @Override
    protected ModelPart build() {
        CubeListBuilder VoxelShapes = CubeListBuilder.create();
        VoxelShapes.texOffs(0, 3).addBox(-10.0F, -1.0F, -4.0F, 24.0F, 1.0F, 8.0F);
        VoxelShapes.texOffs(8, 3).addBox(-12.0F, -1.0F, -4.0F, 2.0F, 1.0F, 8.0F);
        VoxelShapes.texOffs(3, 4).addBox(-14.0F, -1.0F, -3.0F, 2.0F, 1.0F, 6.0F);
        VoxelShapes.texOffs(0, 0).addBox(7.0F, 1.0F, -2.0F, 5.0F, -1.0F, 1.0F);
        VoxelShapes.texOffs(0, 0).addBox(6.0F, 2.0F, -2.0F, 5.0F, -1.0F, 1.0F);
        VoxelShapes.texOffs(0, 0).addBox(7.0F, 1.0F, 1.0F, 5.0F, -1.0F, 1.0F);
        VoxelShapes.texOffs(0, 0).addBox(6.0F, 2.0F, 1.0F, 5.0F, -1.0F, 1.0F);
        VoxelShapes.texOffs(9, 5).addBox(-10.0F, -1.0F, -7.0F, 17.0F, 1.0F, 3.0F);
        VoxelShapes.texOffs(3, 2).addBox(-8.0F, -1.0F, -10.0F, 13.0F, 1.0F, 3.0F);
        VoxelShapes.texOffs(2, 1).addBox(-8.0F, -1.0F, 7.0F, 13.0F, 1.0F, 3.0F);
        VoxelShapes.texOffs(1, 6).addBox(-10.0F, -1.0F, 4.0F, 17.0F, 1.0F, 3.0F);
        VoxelShapes.texOffs(0, 0).addBox(7.0F, -1.0F, -5.0F, 4.0F, 1.0F, 1.0F);
        VoxelShapes.texOffs(0, 0).addBox(7.0F, -1.0F, 4.0F, 4.0F, 1.0F, 1.0F);

        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("board", VoxelShapes, PartPose.rotation((float) Math.PI, 0, 0));
        return LayerDefinition.create(meshdefinition, 0, 0).bakeRoot();
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    public GlideBoardModel withColor(float r, float g, float b) {
        return this; // TODO: Colorable?
    }
}