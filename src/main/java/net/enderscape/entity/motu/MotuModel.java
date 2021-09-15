package net.enderscape.entity.motu;

import net.enderscape.util.EndMath;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;

@Environment(EnvType.CLIENT)
public class MotuModel extends SinglePartEntityModel<MotuEntity> {
    private final ModelPart root;
    private final ModelPart base;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;

    public MotuModel(ModelPart root) {
        this.root = root;
        base = root.getChild("base");
        neck = base.getChild("neck");
        head = neck.getChild("head");
        leftLeg = base.getChild("leftLeg");
        rightLeg = base.getChild("rightLeg");
    }

    public static TexturedModelData getTexturedModelData() {
        Dilation dilation = Dilation.NONE;

        ModelData data = new ModelData();
        ModelPartData rootData = data.getRoot();

        ModelPartData baseData = rootData.addChild("base", ModelPartBuilder.create().uv(0, 20).cuboid(-3, -1, -3, 6, 2, 6, dilation), ModelTransform.pivot(0, 15, 0));
        ModelPartData neckData = baseData.addChild("neck", ModelPartBuilder.create().uv(0, 0).cuboid(-1, -5, -1, 2, 5, 2, dilation), ModelTransform.pivot(0, -1, 0));
        neckData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-5, -10, -5, 10, 10, 10, dilation), ModelTransform.pivot(0, -2, 0));

        baseData.addChild("leftLeg", ModelPartBuilder.create().uv(30, 0).cuboid(-1, 0, -1, 2, 8, 2, dilation), ModelTransform.pivot(2, 1, 0));
        baseData.addChild("rightLeg", ModelPartBuilder.create().uv(38, 0).cuboid(-1, 0, -1, 2, 8, 2, dilation), ModelTransform.pivot(-2, 1, 0));

        return TexturedModelData.of(data, 64, 32);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void setAngles(MotuEntity mob, float limbSwing, float limbDistance, float age, float headYaw, float headPitch) {
        float k = 1;
        if (mob.getRoll() > 4) {
            k = (float) mob.getVelocity().lengthSquared();
            k /= 0.2F;
            k *= k * k;
        }

        k = k < 1 ? 1 : k;

        head.pitch = EndMath.sin(limbSwing) * (limbDistance * 0.1F);
        head.yaw = EndMath.sin(limbSwing * 0.5F) * (limbDistance * 0.1F);
        head.roll = EndMath.sin(limbSwing * 0.75F) * (limbDistance * 0.1F);

        leftLeg.pitch = EndMath.cos(limbSwing * 0.6662F) * 1.4F * limbDistance / k;
        rightLeg.pitch = EndMath.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbDistance / k;
    }
}