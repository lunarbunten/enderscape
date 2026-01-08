package net.bunten.enderscape.client.entity.rustle;

import net.bunten.enderscape.entity.rustle.Rustle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class RustleModel extends HierarchicalModel<Rustle> {
	private final ModelPart body;
	private final ModelPart crossSpines;
	private final ModelPart middleSpines;
	private final ModelPart backSpines;
	private final ModelPart tail;
	private final ModelPart head;
	private final ModelPart rightAntenna;
	private final ModelPart leftAntenna;
	private final ModelPart frontSpines;
	private final ModelPart root;

	public RustleModel(ModelPart root) {
		this.root = root;

		body = root.getChild("body");
		crossSpines = body.getChild("crossSpines");
		middleSpines = body.getChild("middleSpines");
		backSpines = body.getChild("backSpines");
		tail = body.getChild("tail");
		head = root.getChild("head");
		rightAntenna = head.getChild("rightAntenna");
		leftAntenna = head.getChild("leftAntenna");
		frontSpines = head.getChild("frontSpines");
	}

	@Override
	public ModelPart root() {
		return root;
	}

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -5.0F, -3.0F, 8.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition crossSpines = body.addOrReplaceChild("crossSpines", CubeListBuilder.create().texOffs(0, 27).addBox(-7.0F, -5.0F, 0.25F, 14.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 1.75F));

        crossSpines.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(1, 27).addBox(-6.0F, -1.0F, 0.0F, 13.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 0.25F, 0.0F, 1.5708F, 0.0F));
        body.addOrReplaceChild("middleSpines", CubeListBuilder.create().texOffs(0, 20).addBox(-7.0F, -4.5F, 0.0F, 14.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.5F, 0.0F));
        body.addOrReplaceChild("backSpines", CubeListBuilder.create().texOffs(0, 20).mirror().addBox(-7.0F, -4.5F, 0.0F, 14.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -2.5F, 5.0F));
        body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(36, 9).addBox(-2.0F, -1.5F, 0.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, 7.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(26, 0).addBox(-3.0F, -3.0F, -4.0F, 6.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 22.0F, -3.0F));

        head.addOrReplaceChild("rightAntenna", CubeListBuilder.create().texOffs(46, -7).addBox(0.0F, -7.0F, -5.5F, 0.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -3.0F, -2.5F));
        head.addOrReplaceChild("leftAntenna", CubeListBuilder.create().texOffs(46, -7).addBox(0.0F, -7.0F, -5.5F, 0.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -3.0F, -2.5F));
        head.addOrReplaceChild("frontSpines", CubeListBuilder.create().texOffs(0, 15).addBox(-7.0F, -2.0F, 0.0F, 14.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -3.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(Rustle mob, float animPos, float animSpeed, float age, float headYaw, float headPitch) {
		root().getAllParts().forEach(ModelPart::resetPose);

        leftAntenna.yRot = Mth.sin(age + (animPos / 3) * 0.1F) * animSpeed * 0.3F;
		rightAntenna.yRot = Mth.sin(age + (animPos / 3) * 0.1F + Mth.HALF_PI) * animSpeed * 0.8F;

		leftAntenna.xRot = Mth.sin(age + (animPos / 3) * 0.1F + Mth.HALF_PI) * animSpeed * 0.8F;
		rightAntenna.xRot = Mth.sin(age + (animPos / 3) * 0.1F + (Mth.HALF_PI * 3)) * animSpeed * 0.8F;

		head.xRot = (headPitch * (Mth.PI / 180)) / 2;
		head.yRot = (headYaw * (Mth.PI / 180)) / 2;
		head.zRot = Mth.sin(age + (animPos / 3) * 0.06F) * animSpeed * 0.5F;

		body.zRot = Mth.sin(age + (animPos / 3) * 0.03F) * animSpeed * 0.25F;
		tail.zRot = Mth.sin(age + (animPos / 3) * 0.06F) * animSpeed * 0.5F;

		frontSpines.yRot = Mth.sin(age + (animPos / 3) * 0.1F) * animSpeed * 0.8F;
		middleSpines.yRot = Mth.sin(age + (animPos / 3) * 0.1F + Mth.HALF_PI) * animSpeed * 0.8F;
		backSpines.yRot = Mth.sin(age + (animPos / 3) * 0.1F + Mth.PI) * animSpeed * 0.8F;

		animate(mob.sleepingAnimationState, RustleAnimations.SLEEPING, age, 1.0F);

		crossSpines.visible = !mob.isSheared();
    }
}