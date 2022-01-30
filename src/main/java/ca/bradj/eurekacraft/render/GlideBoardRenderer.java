package ca.bradj.eurekacraft.render;

import ca.bradj.eurekacraft.EurekaCraft;
import ca.bradj.eurekacraft.vehicles.EntityRefBoard;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@OnlyIn(Dist.CLIENT)
public class GlideBoardRenderer extends EntityRenderer<EntityRefBoard> {

    Logger logger = LogManager.getLogger(EurekaCraft.MODID);
    private boolean logged = false;

    // TODO: Custom tex
    protected static final ResourceLocation TEXTURE = new ResourceLocation(
                    EurekaCraft.MODID, "textures/blocks/reflection_film_scraper_1.png"
    );
    private final GlideBoardModel model;

    public GlideBoardRenderer(EntityRendererManager renderMan) {
        super(renderMan);
        logger.debug("GlideBoardRenderer created");
        this.model = new GlideBoardModel();
    }

    @Override
    public void render(EntityRefBoard entity, float p_225623_2_, float p_225623_3_, MatrixStack stack, IRenderTypeBuffer buffer, int packedLight) {
        if (!logged) {
            logger.debug("Rendering for " + entity + "-" + p_225623_2_ + "-" + p_225623_3_);
            logged = true;
        }
        IVertexBuilder ivertexbuilder = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(stack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        super.render(entity, p_225623_2_, p_225623_3_, stack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityRefBoard p_110775_1_) {
        return TEXTURE;
    }
}
