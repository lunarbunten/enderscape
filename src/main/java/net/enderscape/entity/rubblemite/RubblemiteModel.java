package net.enderscape.entity.rubblemite;

import net.enderscape.util.EndMath;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;

@Environment(EnvType.CLIENT)
public class RubblemiteModel extends SinglePartEntityModel<RubblemiteEntity> {
    private final ModelPart root;
    private final ModelPart shell;
    private final ModelPart head;

    public RubblemiteModel(ModelPart root) {
        this.root = root;
        shell = root.getChild("shell");
        head = shell.getChild("head");
    }

    public static TexturedModelData getTexturedModelData() {
        Dilation dilation = Dilation.NONE;

        ModelData data = new ModelData();
        ModelPartData rootData = data.getRoot();

        ModelPartData shellData = rootData.addChild("shell", ModelPartBuilder.create().uv(0, 0).cuboid(-4, -6, -4, 8, 6, 8, dilation), ModelTransform.pivot(0, 24, 0));
        shellData.addChild("head", ModelPartBuilder.create().uv(0, 14).cuboid(-2, -2, -1, 4, 4, 1, dilation), ModelTransform.pivot(0, -2, -4));

        return TexturedModelData.of(data, 32, 32);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void setAngles(RubblemiteEntity mob, float limbSwing, float limbDistance, float age, float headYaw, float headPitch) {
        float strength = 0.05F;
        float speed = 0.3F;
        float speed2 = speed * 2;

        head.pitch = -(EndMath.sin(age * speed) * strength);
        head.roll = -(EndMath.sin(age * speed2) * strength);

        if (!mob.isDashing()) {
            if (mob.insideShell()) {
                head.visible = false;
                shell.pitch = 0;
                shell.roll = 0;
                shell.yaw = 0;
            } else {
                head.visible = true;
                shell.pitch = (EndMath.sin(age * speed + 2) * strength) + ((headPitch * 0.017453292F) / 2);
                shell.roll = (EndMath.sin(age * speed2 + 2) * strength);
                shell.yaw = ((headYaw * 0.017453292F) / 2);
            }
        } else {
            head.visible = false;
            shell.pitch = 0;
            shell.roll = 0;
            shell.yaw = age;
        }
    }
}