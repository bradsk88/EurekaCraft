package ca.bradj.eurekacraft.render;

import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class BrokenRefBoardModel extends AbstractBoardModel<BrokenRefBoardModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(
            EurekaCraft.MODID, "textures/render/dark_oak_planks.png"
    );
    @Override
    protected ModelPart build() {
        CubeListBuilder VoxelShapes = CubeListBuilder.create();
        VoxelShapes.texOffs(0, 0).addBox(0.0F, 0.0F, -4.0F, 15.0F, -1.0F, 8.0F);
        VoxelShapes.texOffs(0, 0).addBox(-2.0F, 0.0F, -5.0F, 2.0F, -1.0F, 10.0F);
        VoxelShapes.texOffs(0, 0).addBox(-8.0F, 0.0F, -6.0F, 6.0F, -1.0F, 12.0F);
        VoxelShapes.texOffs(0, 0).addBox(-10.0F, 0.0F, -5.0F, 2.0F, -1.0F, 10.0F);
        VoxelShapes.texOffs(0, 0).addBox(-12.0F, 0.0F, -4.0F, 2.0F, -1.0F, 8.0F);
        VoxelShapes.texOffs(0, 0).addBox(-14.0F, 0.0F, -3.0F, 2.0F, -1.0F, 6.0F);
        VoxelShapes.texOffs(0, 0).addBox(5.0F, 0.0F, -5.0F, 6.0F, -1.0F, 10.0F);
        VoxelShapes.texOffs(0, 0).addBox(8.0F, 1.0F, -2.0F, 5.0F, -1.0F, 1.0F);
        VoxelShapes.texOffs(0, 0).addBox(7.0F, 2.0F, -2.0F, 5.0F, -1.0F, 1.0F);
        VoxelShapes.texOffs(0, 0).addBox(8.0F, 1.0F, 1.0F, 5.0F, -1.0F, 1.0F);
        VoxelShapes.texOffs(0, 0).addBox(7.0F, 2.0F, 1.0F, 5.0F, -1.0F, 1.0F);
        VoxelShapes.texOffs(0, 0).addBox(-10.0F, 1.0F, -2.0F, 5.0F, -1.0F, 4.0F);
        VoxelShapes.texOffs(0, 0).addBox(-10.0F, 2.0F, -1.0F, 3.0F, -1.0F, 2.0F);

        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("board", VoxelShapes, PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 0, 0).bakeRoot();
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    public BrokenRefBoardModel withColor(Color color) {
        return this; // TODO: Color?
    }
}
