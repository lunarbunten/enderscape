package net.bunten.enderscape.client.entity.rubblemite;

import net.bunten.enderscape.client.registry.EnderscapeEntityRenderData;
import net.bunten.enderscape.entity.rubblemite.Rubblemite;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class RubblemiteRenderer extends MobRenderer<Rubblemite, RubblemiteRenderState, RubblemiteModel> {
    public RubblemiteRenderer(Context context) {
        super(context, new RubblemiteModel(EnderscapeEntityRenderData.RUBBLEMITE.bakeLayer(context)), 0.4F);
        addLayer(new RubblemiteEyesLayer(this));
    }

    @Override
    protected float getFlipDegrees() {
        return 180.0F;
    }

    @Override
    public RubblemiteRenderState createRenderState() {
        return new RubblemiteRenderState();
    }

    @Override
    public void extractRenderState(Rubblemite mob, RubblemiteRenderState state, float f) {
        super.extractRenderState(mob, state, f);
        state.isDashing = mob.isDashing();
        state.texture = mob.getTexture();

        state.insideShellAnimationState.copyFrom(mob.insideShellAnimationState);
        state.prepareDashAnimationState.copyFrom(mob.prepareDashAnimationState);
        state.dashAnimationState.copyFrom(mob.dashAnimationState);
    }

    @Override
    public Identifier getTextureLocation(RubblemiteRenderState state) {
        return state.texture;
    }
}