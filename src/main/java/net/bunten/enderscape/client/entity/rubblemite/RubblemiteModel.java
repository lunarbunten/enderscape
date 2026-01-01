package net.bunten.enderscape.client.entity.rubblemite;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class RubblemiteModel extends EntityModel<RubblemiteRenderState> {
    private final ModelPart shell;
    private final ModelPart head;

    private final KeyframeAnimation dashAnimation;
    private final KeyframeAnimation prepareDashAnimation;
    private final KeyframeAnimation insideShellAnimation;

    public RubblemiteModel(ModelPart root) {
        super(root);
        shell = root.getChild("shell");
        head = shell.getChild("head");

        dashAnimation = RubblemiteAnimations.DASH.bake(root);
        prepareDashAnimation = RubblemiteAnimations.PREPARE_DASH.bake(root);
        insideShellAnimation = RubblemiteAnimations.INSIDE_SHELL.bake(root);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition data = new MeshDefinition();
        PartDefinition rootData = data.getRoot();

        PartDefinition shell = rootData.addOrReplaceChild("shell", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -3.5F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.5F, 0.0F));
        shell.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 14).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, -4.0F));

        return LayerDefinition.create(data, 32, 32);
    }

    @Override
    public void setupAnim(RubblemiteRenderState state) {
        super.setupAnim(state);

        float age = state.ageInTicks;
        float animPos = state.walkAnimationPos;
        float animSpeed = state.walkAnimationSpeed;

        if (state.deathTime > 0) {
            head.zRot = Mth.lerp(0.1F, head.zRot, 0);
            shell.xRot = Mth.lerp(0.1F, head.zRot, 0);
            shell.yRot = Mth.lerp(0.1F, head.zRot, 0);
            shell.zRot = Mth.lerp(0.1F, head.zRot, 0);
        } else {
            head.zRot = Mth.sin(age + (animPos / 3) * 0.06F) * animSpeed * 0.1F;

            shell.xRot += (state.xRot * (Mth.PI / 180)) / 2;
            shell.yRot += (state.yRot * (Mth.PI / 180)) / 2;
            shell.zRot = Mth.sin(age + (animPos / 3) * 0.03F + Mth.HALF_PI) * animSpeed * 0.15F;
        }

        dashAnimation.apply(state.dashAnimationState, age);
        prepareDashAnimation.apply(state.prepareDashAnimationState, age);
        insideShellAnimation.apply(state.insideShellAnimationState, age);
    }
}