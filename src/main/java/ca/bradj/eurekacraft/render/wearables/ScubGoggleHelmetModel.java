package ca.bradj.eurekacraft.render.wearables;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class ScubGoggleHelmetModel extends BipedModel<LivingEntity> {
	public ScubGoggleHelmetModel(float p_i1148_1_) {
		super(RenderType::entityTranslucent, p_i1148_1_, 0.0F, 64, 64);
		texWidth = 64;
		texHeight = 64;

		ModelRenderer goggles = new ModelRenderer(this);
		goggles.setPos(0.0F, 21.6667F, -3.1667F);
		goggles.texOffs(0, 11).addBox(4.0F, -4.6667F, -4.8333F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		goggles.texOffs(0, 3).addBox(-5.0F, -4.6667F, -4.8333F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		goggles.texOffs(11, 14).addBox(1.0F, -2.6667F, -4.8333F, 3.0F, 1.0F, 1.0F, 0.0F, false);
		goggles.texOffs(0, 0).addBox(-1.0F, -3.6667F, -4.8333F, 2.0F, 1.0F, 1.0F, 0.0F, false);
		goggles.texOffs(11, 11).addBox(-4.0F, -2.6667F, -4.8333F, 3.0F, 1.0F, 1.0F, 0.0F, false);
		goggles.texOffs(11, 0).addBox(-5.0F, -5.6667F, -4.8333F, 10.0F, 1.0F, 1.0F, 0.0F, false);
		goggles.texOffs(0, 11).addBox(4.0F, -5.6667F, -3.8333F, 1.0F, 2.0F, 8.0F, 0.0F, false);
		goggles.texOffs(0, 0).addBox(-5.0F, -5.6667F, -3.8333F, 1.0F, 2.0F, 8.0F, 0.0F, false);
		goggles.texOffs(11, 3).addBox(-4.0F, -5.6667F, 3.1667F, 8.0F, 2.0F, 1.0F, 0.0F, false);
		this.head = goggles;
	}

}