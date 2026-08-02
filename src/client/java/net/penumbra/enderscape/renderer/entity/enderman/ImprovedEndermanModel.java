package net.penumbra.enderscape.renderer.entity.enderman;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.EndermanRenderState;
import net.minecraft.util.Mth;

import static net.penumbra.enderscape.renderer.entity.enderman.ImprovedEndermanAnimations.*;

public class ImprovedEndermanModel<T extends EndermanRenderState> extends EntityModel<T> {

	private static final float WALK_ANIMATION_SPEED = 1.75F;
	private static final float WALK_ANIMATION_SCALE = 2.5F;

	private static final float CREEPY_JITTER_SPEED = 5.0F;
	private static final float CREEPY_JITTER_SCALE = 0.25F;
	private static final int CREEPY_JITTER_STEP_SIZE = 2;

	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart jaw;
	private final ModelPart rightArm;
	private final ModelPart leftArm;
	private final ModelPart block;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;

	private final KeyframeAnimation creepyFace;
	private final KeyframeAnimation idlePose;
	private final KeyframeAnimation holdingPose;
	private final KeyframeAnimation walkAnimation;
	private final KeyframeAnimation walkHoldingAnimation;
	private final KeyframeAnimation attackAnimation;
	private final KeyframeAnimation attackHoldingAnimation;
	private final KeyframeAnimation walkLeftArmAnimation;
	private final KeyframeAnimation walkRightArmAnimation;

	public ImprovedEndermanModel(ModelPart root) {
        super(root);

        body = root.getChild("body");
		head = root.getChild("head");
        jaw = head.getChild("jaw");
		rightArm = root.getChild("right_arm");
		leftArm = root.getChild("left_arm");
		block = leftArm.getChild("block");
		rightLeg = root.getChild("right_leg");
		leftLeg = root.getChild("left_leg");

		creepyFace = ENDERMAN_FACE_ANGRY.bake(root);
		idlePose = ENDERMAN_ARM_IDLE.bake(root);
		holdingPose = ENDERMAN_ARM_HOLD.bake(root);
		walkAnimation = ENDERMAN_WALK.bake(root);
		walkHoldingAnimation = ENDERMAN_WALK_HOLD.bake(root);
		attackAnimation = ENDERMAN_ATTACK.bake(root);
		attackHoldingAnimation = ENDERMAN_ATTACK_HOLD.bake(root);
		walkLeftArmAnimation = ENDERMAN_ARM_SWING_L.bake(root);
		walkRightArmAnimation = ENDERMAN_ARM_SWING_R.bake(root);
	}

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();

		root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(32, 16).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 0.0F));
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -15.0F, 0.0F));
        head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(56, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 30.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -13.0F, 0.0F));
        PartDefinition left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(56, 0).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 30.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, -13.0F, 0.0F));
        left_arm.addOrReplaceChild("block", CubeListBuilder.create(), PartPose.offsetAndRotation(-5.0F, 23.0F, -1.0F, 0.5236F, 0.0F, 0.0F));
		root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(56, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 30.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -6.0F, 0.0F));
		root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(56, 0).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 30.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, -6.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	public void setupAnim(final T state) {
		super.setupAnim(state);

		ImprovedEndermanRenderState improved = ((ImprovedEndermanRenderState) state);

        boolean carryingBlock = !state.carriedBlock.isEmpty();

		float walkAnimationPos = state.walkAnimationPos;
		float walkAnimationSpeed = state.walkAnimationSpeed;
		float ageInTicks = state.ageInTicks;

		float scaledSpeed = state.isCreepy ? WALK_ANIMATION_SPEED * 2 : WALK_ANIMATION_SPEED;

		head.xRot = state.xRot * (Mth.PI / 180.0F);
		head.yRot = state.yRot * (Mth.PI / 180.0F);

		jaw.y += 0.6F;

		float movementScale = 1.0F - Math.min(walkAnimationSpeed * 2.0F, 1.0F);

		if (state.isCreepy) {
			animateCreepy(ageInTicks, movementScale);
		} else {
			animateIdle(improved, ageInTicks, carryingBlock, movementScale);
		}

		if (!carryingBlock) {
			walkAnimation.applyWalk(walkAnimationPos, walkAnimationSpeed, scaledSpeed, WALK_ANIMATION_SCALE);
			walkLeftArmAnimation.applyWalk(walkAnimationPos, walkAnimationSpeed, scaledSpeed, WALK_ANIMATION_SCALE);
			walkRightArmAnimation.applyWalk(walkAnimationPos, walkAnimationSpeed, scaledSpeed, WALK_ANIMATION_SCALE);
			attackAnimation.apply(improved.attackAnimationState(), ageInTicks, 0.8F);

			if (!improved.attackAnimationState().isStarted()) {
				idlePose.applyStatic();
			}
		} else {
			walkHoldingAnimation.applyWalk(walkAnimationPos, walkAnimationSpeed, scaledSpeed, WALK_ANIMATION_SCALE);
			attackHoldingAnimation.apply(improved.attackAnimationState(), ageInTicks, 0.8F);

			if (!improved.attackAnimationState().isStarted()) {
				holdingPose.applyStatic();
			}
		}
	}

	private void animateIdle(ImprovedEndermanRenderState state, float ageInTicks, boolean carryingBlock, float scale) {
		if (!state.attackAnimationState().isStarted()) {
			head.zRot += (Mth.cos(ageInTicks * 0.05F) * 0.05F) * scale;
			head.xRot += (Mth.cos(ageInTicks * 0.075F + Mth.HALF_PI) * 0.05F) * scale;

			if (!carryingBlock) {
				leftArm.zRot += (Mth.sin(ageInTicks * 0.05F) * 0.05F) * scale;
				rightArm.zRot += (Mth.cos(ageInTicks * 0.1F) * 0.05F) * scale;

				leftArm.xRot += (Mth.sin(ageInTicks * 0.05F + Mth.HALF_PI) * 0.05F) * scale;
				rightArm.xRot += (Mth.cos(ageInTicks * 0.1F + Mth.HALF_PI) * 0.05F) * scale;
			}
		}
	}

	private void animateCreepy(float ageInTicks, float movementScale) {
		creepyFace.applyStatic();

		float jitter = creepyJitterScale(ageInTicks, Math.max(0.5F, movementScale), false);
		float jitterCos = creepyJitterScale(ageInTicks, Math.max(0.5F, movementScale), true);
		float legsJitter = creepyJitterScale(ageInTicks, movementScale, false);
		float legsJitterCos = creepyJitterScale(ageInTicks, movementScale, true);

		head.x += jitter;
		head.z += jitterCos;

		body.x += jitter;
		body.z += jitterCos;

		leftArm.x += jitter;
		leftArm.z += jitterCos;

		rightArm.x += jitter;
		rightArm.z += jitterCos;

		leftLeg.x += legsJitter;
		leftLeg.z += legsJitterCos;

		rightLeg.x += legsJitter;
		rightLeg.z += legsJitterCos;
	}

	private static float creepyJitterScale(float ageInTicks, float scale, boolean cosine) {
		int sin;

		if (cosine) {
			sin = (int) (Mth.cos(ageInTicks * CREEPY_JITTER_SPEED) * CREEPY_JITTER_STEP_SIZE);
		} else {
			sin = (int) (Mth.sin(ageInTicks * CREEPY_JITTER_SPEED) * CREEPY_JITTER_STEP_SIZE);
		}

        return ((float) sin) * (CREEPY_JITTER_SCALE * scale);
	}

	public void applyCarriedBlockTransform(final PoseStack pose) {
		root.translateAndRotate(pose);
		leftArm.translateAndRotate(pose);
		block.translateAndRotate(pose);

		pose.scale(0.5F, 0.5F, 0.5F);
		pose.translate(0.0, -0.15, -0.15);
	}
}