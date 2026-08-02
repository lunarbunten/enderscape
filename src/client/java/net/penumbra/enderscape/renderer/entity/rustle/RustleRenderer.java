package net.penumbra.enderscape.renderer.entity.rustle;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.entity.rustle.Rustle;
import net.penumbra.enderscape.registry.renderer.EnderscapeModelLayers;

public class RustleRenderer extends AgeableMobRenderer<Rustle, RustleRenderState, EntityModel<RustleRenderState>> {

    public static final Identifier ADULT_TEXTURE = Enderscape.id("textures/entity/rustle/rustle.png");
    public static final Identifier BABY_TEXTURE = Enderscape.id("textures/entity/rustle/rustle_baby.png");

    public RustleRenderer(EntityRendererProvider.Context context) {
        super(context, new RustleModel(context.bakeLayer(EnderscapeModelLayers.RUSTLE)), new BabyRustleModel(context.bakeLayer(EnderscapeModelLayers.RUSTLE_BABY)), 0.4F);
    }

    @Override
    public RustleRenderState createRenderState() {
        return new RustleRenderState();
    }

    @Override
    public void extractRenderState(Rustle entity, RustleRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);

        entity.getQueuedRecipe().ifPresentOrElse(recipe -> state.swellAnimationSpeed = (20.0F / recipe.effects().swellDuration()), () -> state.swellAnimationSpeed = 1.0F);

        state.isSheared = entity.isSheared();
        state.isSleeping = entity.isSleeping();

        state.sleepingAnimationState.copyFrom(entity.sleepingAnimationState);
        state.conversionBeginAnimationState.copyFrom(entity.conversionBeginAnimationState);
        state.conversionAnimationState.copyFrom(entity.conversionAnimationState);
        state.conversionEndAnimationState.copyFrom(entity.conversionEndAnimationState);
    }

    @Override
    public Identifier getTextureLocation(RustleRenderState state) {
        return state.isBaby ? BABY_TEXTURE : ADULT_TEXTURE;
    }
}