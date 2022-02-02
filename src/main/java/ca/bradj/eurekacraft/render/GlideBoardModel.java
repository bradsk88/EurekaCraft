package ca.bradj.eurekacraft.render;

import ca.bradj.eurekacraft.EurekaCraft;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public class GlideBoardModel extends AbstractBoardModel {

    private static final ResourceLocation TEXTURE = new ResourceLocation(
            EurekaCraft.MODID, "textures/items/glide_board.png"
    );

    @Override
    protected ModelRenderer build() {
        texWidth = 16;
        texHeight = 16;

        ModelRenderer VoxelShapes = new ModelRenderer(this);
        VoxelShapes.setPos(0.0F, 0.0F, 0.0F);
        VoxelShapes.texOffs(0, 0).addBox(-8.0F, -1.0F, -4.0F, 16.0F, 1.0F, 8.0F, 0.0F, false);
        return VoxelShapes;
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}