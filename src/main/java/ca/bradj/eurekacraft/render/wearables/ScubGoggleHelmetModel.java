package ca.bradj.eurekacraft.render.wearables;

import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;

public class ScubGoggleHelmetModel extends HumanoidModel<LivingEntity> implements HeadedModel {
	public ScubGoggleHelmetModel() {
		super(buildModel(), RenderType::armorCutoutNoCull);
	}

	public static ModelPart buildModel() {
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
		partdefinition.addOrReplaceChild("head", goggles, PartPose.ZERO);
		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);
		return LayerDefinition.create(meshdefinition, 64, 64).bakeRoot();
	}
}