package ca.bradj.eurekacraft.render;

import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public class GlideBoardModel extends AbstractBoardModel {

    private static final ResourceLocation TEXTURE = new ResourceLocation(
            EurekaCraft.MODID, "textures/render/oak_planks_quad.png"
    );

    @Override
    protected LayerDefinition build() {
        texWidth = 16;
        texHeight = 16;

        ModelRenderer VoxelShapes = new ModelRenderer(this);
        VoxelShapes.setPos(0.0F, 0.0F, 0.0F);
        VoxelShapes.texOffs(0, 3).addBox(-10.0F, -1.0F, -4.0F, 24.0F, 1.0F, 8.0F, 0.0F, false);
        VoxelShapes.texOffs(8, 3).addBox(-12.0F, -1.0F, -4.0F, 2.0F, 1.0F, 8.0F, 0.0F, false);
        VoxelShapes.texOffs(3, 4).addBox(-14.0F, -1.0F, -3.0F, 2.0F, 1.0F, 6.0F, 0.0F, false);
        VoxelShapes.texOffs(0, 0).addBox(7.0F, 1.0F, -2.0F, 5.0F, -1.0F, 1.0F, 0.0F, false);
        VoxelShapes.texOffs(0, 0).addBox(6.0F, 2.0F, -2.0F, 5.0F, -1.0F, 1.0F, 0.0F, false);
        VoxelShapes.texOffs(0, 0).addBox(7.0F, 1.0F, 1.0F, 5.0F, -1.0F, 1.0F, 0.0F, false);
        VoxelShapes.texOffs(0, 0).addBox(6.0F, 2.0F, 1.0F, 5.0F, -1.0F, 1.0F, 0.0F, false);
        VoxelShapes.texOffs(9, 5).addBox(-10.0F, -1.0F, -7.0F, 17.0F, 1.0F, 3.0F, 0.0F, false);
        VoxelShapes.texOffs(3, 2).addBox(-8.0F, -1.0F, -10.0F, 13.0F, 1.0F, 3.0F, 0.0F, false);
        VoxelShapes.texOffs(2, 1).addBox(-8.0F, -1.0F, 7.0F, 13.0F, 1.0F, 3.0F, 0.0F, false);
        VoxelShapes.texOffs(1, 6).addBox(-10.0F, -1.0F, 4.0F, 17.0F, 1.0F, 3.0F, 0.0F, false);
        VoxelShapes.texOffs(0, 0).addBox(7.0F, -1.0F, -5.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        VoxelShapes.texOffs(0, 0).addBox(7.0F, -1.0F, 4.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        VoxelShapes.xRot = (float) Math.PI;
        return VoxelShapes;
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}