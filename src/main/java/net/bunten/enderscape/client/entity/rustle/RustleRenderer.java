package net.bunten.enderscape.client.entity.rustle;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.registry.EnderscapeEntityRenderData;
import net.bunten.enderscape.entity.rustle.Rustle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class RustleRenderer extends MobRenderer<Rustle, RustleRenderState, RustleModel> {
    public RustleRenderer(Context context) {
        super(context, new RustleModel(EnderscapeEntityRenderData.RUSTLE.bakeLayer(context)), 0.4F);
    }

    @Override
    public RustleRenderState createRenderState() {
        return new RustleRenderState();
    }

    @Override
    protected void scale(RustleRenderState state, PoseStack pose) {
        pose.scale(state.ageScale, state.ageScale, state.ageScale);
    }

    @Override
    protected float getShadowRadius(RustleRenderState state) {
        return state.isBaby ? super.getShadowRadius(state) * 0.5F : super.getShadowRadius(state);
    }

    @Override
    public void extractRenderState(Rustle mob, RustleRenderState state, float f) {
        super.extractRenderState(mob, state, f);
        state.isSheared = mob.isSheared();
        state.isSleeping = mob.isSleeping();
        state.sleepingAnimationState.copyFrom(mob.sleepingAnimationState);
    }

    @Override
    public Identifier getTextureLocation(RustleRenderState mob) {
        return Enderscape.id("textures/entity/rustle/rustle.png");
    }
}