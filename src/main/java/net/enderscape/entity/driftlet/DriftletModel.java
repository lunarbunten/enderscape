package net.enderscape.entity.driftlet;

import net.enderscape.util.EndMath;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;

@Environment(EnvType.CLIENT)
public class DriftletModel extends SinglePartEntityModel<DriftletEntity> {
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

    public DriftletModel(ModelPart root) {
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

        ModelPartData headData = rootData.addChild("head", ModelPartBuilder.create().uv(32, 24).cuboid(-4, -7, -4, 8, 7, 8, dilation), ModelTransform.pivot(0, 20, 0));

        headData.addChild("leftLeg", ModelPartBuilder.create().uv(48, 7).cuboid(-1.5F, 0, -1.5F, 3, 4, 3, dilation), ModelTransform.pivot(1.5F, 0, -0.5F));
        headData.addChild("rightLeg", ModelPartBuilder.create().uv(48, 0).cuboid(-1.5F, 0, -1.5F, 3, 4, 3, dilation), ModelTransform.pivot(-1.5F, 0, -0.5F));

        ModelPartData stemData = headData.addChild("stem", ModelPartBuilder.create().uv(0, 0).cuboid(-2, -6, -2, 4, 6, 4, dilation), ModelTransform.pivot(0, -7, 0));
        ModelPartData bellData = stemData.addChild("bell", ModelPartBuilder.create().uv(0, 0).cuboid(-8, -8, -8, 16, 8, 16, dilation), ModelTransform.pivot(0, -2, 0));
        ModelPartBuilder strandBuilder = ModelPartBuilder.create().uv(0, 24).cuboid(-8, 0, 0, 16, 12, 0, dilation);

        bellData.addChild("strandsN", strandBuilder, ModelTransform.of(0, 0, -7, 0, 0, 0));
        bellData.addChild("strandsW", strandBuilder, ModelTransform.of(7, 0, 0, 0, -1.5708F, 0));
        bellData.addChild("strandsS", strandBuilder, ModelTransform.of(0, 0, 7, 0, 3.1416F, 0));
        bellData.addChild("strandsE", strandBuilder, ModelTransform.of(-7, 0, 0, 0, 1.5708F, 0));

        return TexturedModelData.of(data, 64, 64);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void setAngles(DriftletEntity mob, float limbAngle, float limbDistance, float age, float headYaw, float headPitch) {
        float k = 1;
        if (mob.getRoll() > 4) {
            k = (float) mob.getVelocity().lengthSquared();
            k /= 0.2F;
            k *= k * k;
        }

        k = k < 1 ? 1 : k;

        head.yaw = (headYaw * 0.017453292F);
        head.pitch = (headPitch * 0.017453292F) + (EndMath.sin(age * 0.2F) * 0.1F);
        stem.pitch = -head.pitch;
        bell.pitch = (EndMath.sin(age * 0.2F + 2) * 0.1F);

        strandsN.pitch = -(head.pitch * 0.1F) + (EndMath.sin(age * 0.1F + 1) * 0.3F);
        strandsW.pitch = strandsN.pitch;
        strandsS.pitch = strandsN.pitch;
        strandsE.pitch = strandsN.pitch;

        leftLeg.pitch = -head.pitch + ((EndMath.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance / k) / 4);
        rightLeg.pitch = -head.pitch + ((EndMath.cos(limbAngle * 0.6662F) * 1.4F * limbDistance / k) / 4);

        leftLeg.pitch += EndMath.sin(age * 0.2F) * 0.4F;
        rightLeg.pitch += EndMath.sin(age * 0.2F + 1) * 0.4F;
    }
}