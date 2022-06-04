package net.bunten.enderscape.entity.drifter;

import net.bunten.enderscape.util.MathUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;

@Environment(EnvType.CLIENT)
public class DrifterModel extends SinglePartEntityModel<DrifterEntity> {
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

    public static TexturedModelData getTexturedModelData() {
        Dilation dilation = Dilation.NONE;

        ModelData data = new ModelData();
        ModelPartData rootData = data.getRoot();

        ModelPartData headData = rootData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4, -8, -4, 8, 16, 8, dilation), ModelTransform.pivot(0, 10, 0));

        headData.addChild("leftLeg", ModelPartBuilder.create().uv(96, 0).cuboid(-2, 0, -2, 4, 6, 4, dilation), ModelTransform.pivot(2, 8, 0));
        headData.addChild("rightLeg", ModelPartBuilder.create().uv(96, 10).cuboid(-2, 0, -2, 4, 6, 4, dilation), ModelTransform.pivot(-2, 8, 0));

        ModelPartData stemData = headData.addChild("stem", ModelPartBuilder.create().uv(96, 20).cuboid(-4, -10, 0, 8, 10, 0, dilation), ModelTransform.pivot(0, -8, 0));
        stemData.addChild("stem2", ModelPartBuilder.create().uv(96, 20).cuboid(-4, -10, 0, 8, 10, 0, dilation), ModelTransform.of(0, 0, 0, 0, -1.5708F, 0));

        ModelPartData bellData = stemData.addChild("bell", ModelPartBuilder.create().uv(0, 0).cuboid(-16, -16, -16, 32, 16, 32, dilation), ModelTransform.pivot(0, -10, 0));
        ModelPartBuilder strandBuilder = ModelPartBuilder.create().uv(0, 48).cuboid(-16, 0, 0, 32, 32, 0, dilation);

        bellData.addChild("strandsN", strandBuilder, ModelTransform.of(0, 0, -15, 0, 0, 0));
        bellData.addChild("strandsW", strandBuilder, ModelTransform.of(15, 0, 0, 0, -1.5708F, 0));
        bellData.addChild("strandsS", strandBuilder, ModelTransform.of(0, 0, 15, 0, 3.1416F, 0));
        bellData.addChild("strandsE", strandBuilder, ModelTransform.of(-15, 0, 0, 0, 1.5708F, 0));

        return TexturedModelData.of(data, 128, 128);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void setAngles(DrifterEntity mob, float limbAngle, float limbDistance, float age, float headYaw, float headPitch) {
        float k = 1;
        if (mob.getRoll() > 4) {
            k = (float) mob.getVelocity().lengthSquared();
            k /= 0.2F;
            k *= k * k;
        }

        k = k < 1 ? 1 : k;

        head.yaw = (headYaw * 0.017453292F);
        head.pitch = (headPitch * 0.017453292F) + (MathUtil.sin(age * 0.2F) * 0.1F);
        head.roll = 0.1F * MathUtil.sin(limbAngle * 0.8F) * 2 * (limbDistance * 0.25F);
        head.pitch += 0.1F * MathUtil.sin(limbAngle * 0.8F) * 4 * (limbDistance * 0.25F);

        stem.pitch = -head.pitch;
        bell.pitch = (MathUtil.sin(age * 0.2F + 2) * 0.1F);
        bell.roll = 0.1F * -(MathUtil.sin(limbAngle * 0.8F) * 3 * (limbDistance * 0.5F));

        strandsN.pitch = -(head.pitch * 0.1F) + (MathUtil.sin(age * 0.1F + 1) * 0.3F);

        strandsN.pitch += 0.2F * MathUtil.sin(limbAngle * 0.8F) * (limbDistance * 0.5F);

        strandsW.pitch = strandsN.pitch;
        strandsS.pitch = strandsN.pitch;
        strandsE.pitch = strandsN.pitch;

        leftLeg.pitch = (head.pitch / 2) + MathUtil.cos(limbAngle * 0.6662F + 3.1415927F) * 0.6F * limbDistance / k;
        rightLeg.pitch = (head.pitch / 2) + MathUtil.cos(limbAngle * 0.6662F) * 0.6F * limbDistance / k;

        leftLeg.pitch += MathUtil.sin(age * 0.2F) * 0.4F;
        rightLeg.pitch += MathUtil.sin(age * 0.2F + 1) * 0.4F;

        leftLeg.roll = -head.roll + 0.1F * MathUtil.sin(limbAngle * 0.4F + 3.14F) * 4 * (limbDistance * 0.5F);
        rightLeg.roll = -head.roll + 0.1F * MathUtil.sin(limbAngle * 0.4F) * 4 * (limbDistance * 0.5F);
    }
}