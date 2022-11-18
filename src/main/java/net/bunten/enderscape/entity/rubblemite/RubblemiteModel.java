package net.bunten.enderscape.entity.rubblemite;

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
public class RubblemiteModel extends HierarchicalModel<Rubblemite> {
    private final ModelPart root;
    private final ModelPart shell;
    private final ModelPart head;

    public RubblemiteModel(ModelPart root) {
        this.root = root;
        shell = root.getChild("shell");
        head = shell.getChild("head");
    }

    public static LayerDefinition createLayer() {
        CubeDeformation dilation = CubeDeformation.NONE;

        MeshDefinition data = new MeshDefinition();
        PartDefinition rootData = data.getRoot();

        PartDefinition shellData = rootData.addOrReplaceChild("shell", CubeListBuilder.create().texOffs(0, 0).addBox(-4, -6, -4, 8, 6, 8, dilation), PartPose.offset(0, 24, 0));
        shellData.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 14).addBox(-2, -2, -1, 4, 4, 1, dilation), PartPose.offset(0, -2, -4));

        return LayerDefinition.create(data, 32, 32);
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(Rubblemite mob, float limbAngle, float limbDistance, float age, float headYaw, float headPitch) {
        root().getAllParts().forEach(ModelPart::resetPose);

        float strength = 0.05F;
        float speed = 0.3F;
        float speed2 = speed * 2;

        if (mob.isDashing()) {
            shell.yRot = age;
        } else {
            head.xRot = -(MathUtil.sin(age * speed) * strength);
            head.zRot = -(MathUtil.sin(age * speed2) * strength);

            shell.xRot = (MathUtil.sin(age * speed + MathUtil.PI) * strength) + ((headPitch * 0.017453292F) / 2);
            shell.zRot = (MathUtil.sin(age * speed2 + MathUtil.PI) * strength);
            shell.yRot = ((headYaw * 0.017453292F) / 2);
        }

        head.visible = !mob.isInsideShell() && !mob.isDashing();
    }
}