package ca.bradj.eurekacraft.render;// Made with Blockbench 4.1.3
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports


import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class EliteRefBoardModel extends AbstractBoardModel<EliteRefBoardModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(
            EurekaCraft.MODID, "textures/render/elite_ref_board.png"
    );

    public EliteRefBoardModel() {
        super(1, 1, 1);
    }

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
        partdefinition.addOrReplaceChild("board", VoxelShapes, PartPose.rotation((float) Math.PI, 0, 0));
        return LayerDefinition.create(meshdefinition, 0, 0).bakeRoot();
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    public EliteRefBoardModel withColor(float r, float g, float b) {
        EliteRefBoardModel eliteRefBoardModel = new EliteRefBoardModel();
        eliteRefBoardModel.r = r;
        eliteRefBoardModel.g = g;
        eliteRefBoardModel.b = b;
        return eliteRefBoardModel;
    }
}

