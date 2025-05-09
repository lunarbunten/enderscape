package net.bunten.enderscape.client.entity.rustle;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.registry.EnderscapeEntityRenderData;
import net.bunten.enderscape.entity.rustle.Rustle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class RustleRenderer extends MobRenderer<Rustle, RustleModel> {
    public RustleRenderer(Context context) {
        super(context, new RustleModel(EnderscapeEntityRenderData.RUSTLE.bakeLayer(context)), 0.4F);
    }

    @Override
    protected void scale(Rustle mob, PoseStack pose, float f) {
        pose.scale(mob.getAgeScale(), mob.getAgeScale(), mob.getAgeScale());
    }

    @Override
    protected float getShadowRadius(Rustle mob) {
        return mob.isBaby() ? super.getShadowRadius(mob) * 0.5F : super.getShadowRadius(mob);
    }

    @Override
    public ResourceLocation getTextureLocation(Rustle mob) {
        return Enderscape.id("textures/entity/rustle/rustle.png");
    }
}