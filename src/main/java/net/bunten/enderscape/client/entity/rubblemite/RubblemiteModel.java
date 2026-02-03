package net.bunten.enderscape.client.entity.rubblemite;

import net.bunten.enderscape.entity.rubblemite.Rubblemite;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RubblemiteModel extends HierarchicalModel<Rubblemite> {
    private final ModelPart shell;
    private final ModelPart head;
    private final ModelPart root;

    public RubblemiteModel(ModelPart root) {
        this.root = root;
        shell = root.getChild("shell");
        head = shell.getChild("head");
    }

    @Override
    public ModelPart root() {
        return root;
    }

    public static LayerDefinition createLayer() {
        MeshDefinition data = new MeshDefinition();
        PartDefinition rootData = data.getRoot();

        PartDefinition shell = rootData.addOrReplaceChild("shell", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -3.5F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.5F, 0.0F));
        shell.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 14).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, -4.0F));

        return LayerDefinition.create(data, 32, 32);
    }

    @Override
    public void setupAnim(Rubblemite mob, float animPos, float animSpeed, float age, float headYaw, float headPitch) {
        root().getAllParts().forEach(ModelPart::resetPose);

        if (mob.deathTime > 0) {
            head.zRot = Mth.lerp(0.1F, head.zRot, 0);
            shell.xRot = Mth.lerp(0.1F, head.zRot, 0);
            shell.yRot = Mth.lerp(0.1F, head.zRot, 0);
            shell.zRot = Mth.lerp(0.1F, head.zRot, 0);
        } else {
            head.zRot = Mth.sin(age + (animPos / 3) * 0.06F) * animSpeed * 0.1F;

            shell.xRot += (headPitch * (Mth.PI / 180)) / 2;
            shell.yRot += (headYaw * (Mth.PI / 180)) / 2;
            shell.zRot = Mth.sin(age + (animPos / 3) * 0.03F + Mth.HALF_PI) * animSpeed * 0.15F;
        }

        animate(mob.dashAnimationState, RubblemiteAnimations.DASH, age);
        animate(mob.prepareDashAnimationState, RubblemiteAnimations.PREPARE_DASH, age);
        animate(mob.insideShellAnimationState, RubblemiteAnimations.INSIDE_SHELL, age);
    }
}