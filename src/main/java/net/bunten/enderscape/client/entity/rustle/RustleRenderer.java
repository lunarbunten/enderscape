package net.bunten.enderscape.client.entity.rustle;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.registry.EnderscapeModelLayers;
import net.bunten.enderscape.entity.rustle.Rustle;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RustleRenderer extends AgeableMobRenderer<Rustle, RustleRenderState, EntityModel<RustleRenderState>> {

    public static final ResourceLocation ADULT_TEXTURE = Enderscape.id("textures/entity/rustle/rustle.png");
    public static final ResourceLocation BABY_TEXTURE = Enderscape.id("textures/entity/rustle/baby.png");

    public RustleRenderer(EntityRendererProvider.Context context) {
        super(context, new RustleModel(context.bakeLayer(EnderscapeModelLayers.RUSTLE)), new BabyRustleModel(context.bakeLayer(EnderscapeModelLayers.BABY_RUSTLE)), 0.4F);
    }

    @Override
    public RustleRenderState createRenderState() {
        return new RustleRenderState();
    }

    @Override
    public void extractRenderState(Rustle mob, RustleRenderState state, float f) {
        super.extractRenderState(mob, state, f);
        state.isSheared = mob.isSheared();
        state.isSleeping = mob.isSleeping();
        state.sleepingAnimationState.copyFrom(mob.sleepingAnimationState);
    }

    @Override
    public ResourceLocation getTextureLocation(RustleRenderState state) {
        return state.isBaby ? BABY_TEXTURE : ADULT_TEXTURE;
    }
}