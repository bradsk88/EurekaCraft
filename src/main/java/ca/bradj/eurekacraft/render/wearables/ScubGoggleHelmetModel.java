package ca.bradj.eurekacraft.render.wearables;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;

public class ScubGoggleHelmetModel extends EntityModel<LivingEntity> implements HeadedModel {
	private final ModelPart head;

	public ScubGoggleHelmetModel(float p_i1148_1_) {
		super(RenderType::entityTranslucent);

		CubeListBuilder goggles = CubeListBuilder.create();
		goggles.texOffs(0, 11).addBox(4.0F, -4.6667F, -4.8333F, 1.0F, 2.0F, 1.0F);
		goggles.texOffs(0, 3).addBox(-5.0F, -4.6667F, -4.8333F, 1.0F, 2.0F, 1.0F);
		goggles.texOffs(11, 14).addBox(1.0F, -2.6667F, -4.8333F, 3.0F, 1.0F, 1.0F);
		goggles.texOffs(0, 0).addBox(-1.0F, -3.6667F, -4.8333F, 2.0F, 1.0F, 1.0F);
		goggles.texOffs(11, 11).addBox(-4.0F, -2.6667F, -4.8333F, 3.0F, 1.0F, 1.0F);
		goggles.texOffs(11, 0).addBox(-5.0F, -5.6667F, -4.8333F, 10.0F, 1.0F, 1.0F);
		goggles.texOffs(0, 11).addBox(4.0F, -5.6667F, -3.8333F, 1.0F, 2.0F, 8.0F);
		goggles.texOffs(0, 0).addBox(-5.0F, -5.6667F, -3.8333F, 1.0F, 2.0F, 8.0F);
		goggles.texOffs(11, 3).addBox(-4.0F, -5.6667F, 3.1667F, 8.0F, 2.0F, 1.0F);

		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("board", goggles, PartPose.ZERO);
		this.head = LayerDefinition.create(meshdefinition, 21, -3).bakeRoot();
	}

	@Override
	public void setupAnim(LivingEntity p_102618_, float p_102619_, float p_102620_, float p_102621_, float p_102622_, float p_102623_) {

	}

	@Override
	public ModelPart getHead() {
		return this.head;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vCon, int posX, int posY, float posZ, float sizeX, float sizeY, float sizeZ) {
		this.head.render(poseStack, vCon, posX, posY, posZ, sizeX, sizeY, sizeZ);
	}
}