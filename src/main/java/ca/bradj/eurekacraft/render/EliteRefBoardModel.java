package ca.bradj.eurekacraft.render;// Made with Blockbench 4.1.3
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports


import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ResourceLocation;

public class EliteRefBoardModel extends AbstractBoardModel {

    private static final ResourceLocation TEXTURE = new ResourceLocation(
            EurekaCraft.MODID, "textures/render/elite_ref_board.png"
    );

    @Override
    protected LayerDefinition build() {
        ModelRenderer VoxelShapes = new ModelRenderer(this);
        VoxelShapes.setPos(0.0F, 0.0F, 0.0F);
        VoxelShapes.texOffs(0, 0).addBox(0.0F, 0.0F, -4.0F, 15.0F, -1.0F, 8.0F, 0.0F, false);
        VoxelShapes.texOffs(0, 0).addBox(-2.0F, 0.0F, -5.0F, 2.0F, -1.0F, 10.0F, 0.0F, false);
        VoxelShapes.texOffs(0, 0).addBox(-8.0F, 0.0F, -6.0F, 6.0F, -1.0F, 12.0F, 0.0F, false);
        VoxelShapes.texOffs(0, 0).addBox(-10.0F, 0.0F, -5.0F, 2.0F, -1.0F, 10.0F, 0.0F, false);
        VoxelShapes.texOffs(0, 0).addBox(-12.0F, 0.0F, -4.0F, 2.0F, -1.0F, 8.0F, 0.0F, false);
        VoxelShapes.texOffs(0, 0).addBox(-14.0F, 0.0F, -3.0F, 2.0F, -1.0F, 6.0F, 0.0F, false);
        VoxelShapes.texOffs(0, 0).addBox(5.0F, 0.0F, -5.0F, 6.0F, -1.0F, 10.0F, 0.0F, false);
        VoxelShapes.texOffs(0, 0).addBox(8.0F, 1.0F, -2.0F, 5.0F, -1.0F, 1.0F, 0.0F, false);
        VoxelShapes.texOffs(0, 0).addBox(7.0F, 2.0F, -2.0F, 5.0F, -1.0F, 1.0F, 0.0F, false);
        VoxelShapes.texOffs(0, 0).addBox(8.0F, 1.0F, 1.0F, 5.0F, -1.0F, 1.0F, 0.0F, false);
        VoxelShapes.texOffs(0, 0).addBox(7.0F, 2.0F, 1.0F, 5.0F, -1.0F, 1.0F, 0.0F, false);
        VoxelShapes.texOffs(0, 0).addBox(-10.0F, 1.0F, -2.0F, 5.0F, -1.0F, 4.0F, 0.0F, false);
        VoxelShapes.texOffs(0, 0).addBox(-10.0F, 2.0F, -1.0F, 3.0F, -1.0F, 2.0F, 0.0F, false);
        VoxelShapes.xRot = (float) Math.PI;
        return VoxelShapes;
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}

