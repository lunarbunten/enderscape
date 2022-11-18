package net.bunten.enderscape.entity.drifter;

import net.bunten.enderscape.util.MathUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

@Environment(EnvType.CLIENT)
public class DrifterModel extends HierarchicalModel<Drifter> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;
    private final ModelPart stem;
    private final ModelPart bell;
    private final ModelPart strandsN;
    private final ModelPart strandsW;
    private final ModelPart strandsS;
    private final ModelPart strandsE;

    public DrifterModel(ModelPart root) {
        this.root = root;
        head = root.getChild("head");
        leftLeg = head.getChild("leftLeg");
        rightLeg = head.getChild("rightLeg");
        stem = head.getChild("stem");
        bell = stem.getChild("bell");
        strandsN = bell.getChild("strandsN");
        strandsW = bell.getChild("strandsW");
        strandsS = bell.getChild("strandsS");
        strandsE = bell.getChild("strandsE");
    }

    public static LayerDefinition createLayer() {
        CubeDeformation dilation = CubeDeformation.NONE;

        MeshDefinition data = new MeshDefinition();
        PartDefinition rootData = data.getRoot();

        PartDefinition headData = rootData.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4, -8, -4, 8, 16, 8, dilation), PartPose.offset(0, 10, 0));

        headData.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(96, 0).addBox(-2, 0, -2, 4, 6, 4, dilation), PartPose.offset(2, 8, 0));
        headData.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(96, 10).addBox(-2, 0, -2, 4, 6, 4, dilation), PartPose.offset(-2, 8, 0));

        PartDefinition stemData = headData.addOrReplaceChild("stem", CubeListBuilder.create().texOffs(96, 20).addBox(-4, -10, 0, 8, 10, 0, dilation), PartPose.offset(0, -8, 0));
        stemData.addOrReplaceChild("stem2", CubeListBuilder.create().texOffs(96, 20).addBox(-4, -10, 0, 8, 10, 0, dilation), PartPose.offsetAndRotation(0, 0, 0, 0, -1.5708F, 0));

        PartDefinition bellData = stemData.addOrReplaceChild("bell", CubeListBuilder.create().texOffs(0, 0).addBox(-16, -16, -16, 32, 16, 32, dilation), PartPose.offset(0, -10, 0));
        CubeListBuilder strandBuilder = CubeListBuilder.create().texOffs(0, 48).addBox(-16, 0, 0, 32, 32, 0, dilation);

        bellData.addOrReplaceChild("strandsN", strandBuilder, PartPose.offsetAndRotation(0, 0, -15, 0, 0, 0));
        bellData.addOrReplaceChild("strandsW", strandBuilder, PartPose.offsetAndRotation(15, 0, 0, 0, -1.5708F, 0));
        bellData.addOrReplaceChild("strandsS", strandBuilder, PartPose.offsetAndRotation(0, 0, 15, 0, 3.1416F, 0));
        bellData.addOrReplaceChild("strandsE", strandBuilder, PartPose.offsetAndRotation(-15, 0, 0, 0, 1.5708F, 0));

        return LayerDefinition.create(data, 128, 80);
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(Drifter mob, float limbAngle, float limbDistance, float age, float headYaw, float headPitch) {
        float k = 1;
        if (mob.getFallFlyingTicks() > 4) {
            k = (float) mob.getDeltaMovement().lengthSqr();
            k /= 0.2F;
            k *= k * k;
        }

        k = k < 1 ? 1 : k;

        head.yRot = (headYaw * 0.017453292F);
        head.xRot = (headPitch * 0.017453292F) + (MathUtil.sin(age * 0.2F) * 0.1F);
        head.zRot = 0.1F * MathUtil.sin(limbAngle * 0.8F) * 2 * (limbDistance * 0.25F);
        head.xRot += 0.1F * MathUtil.sin(limbAngle * 0.8F) * 4 * (limbDistance * 0.25F);

        stem.xRot = -head.xRot;
        bell.xRot = (MathUtil.sin(age * 0.2F + MathUtil.HALF_PI) * 0.1F);
        bell.zRot = 0.1F * -(MathUtil.sin(limbAngle * 0.8F) * 3 * (limbDistance * 0.5F));

        strandsN.xRot = -(head.xRot * 0.1F) + (MathUtil.sin(age * 0.1F + MathUtil.HALF_PI) * 0.3F);

        strandsN.xRot += 0.2F * MathUtil.sin(limbAngle * 0.8F) * (limbDistance * 0.5F);

        strandsW.xRot = strandsN.xRot;
        strandsS.xRot = strandsN.xRot;
        strandsE.xRot = strandsN.xRot;

        leftLeg.xRot = (head.xRot / 2) + MathUtil.cos(limbAngle * 0.6662F + (MathUtil.PI / 2)) * 0.6F * limbDistance / k;
        rightLeg.xRot = (head.xRot / 2) + MathUtil.cos(limbAngle * 0.6662F) * 0.6F * limbDistance / k;

        leftLeg.xRot += MathUtil.sin(age * 0.2F) * 0.4F;
        rightLeg.xRot += MathUtil.sin(age * 0.2F + MathUtil.HALF_PI) * 0.4F;

        leftLeg.zRot = -head.zRot + 0.1F * MathUtil.sin(limbAngle * 0.4F + (MathUtil.PI / 2)) * 4 * (limbDistance * 0.5F);
        rightLeg.zRot = -head.zRot + 0.1F * MathUtil.sin(limbAngle * 0.4F) * 4 * (limbDistance * 0.5F);
    }
}