package net.penumbra.enderscape.renderer.entity.rustle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

@Environment(EnvType.CLIENT)
public class RustleRenderState extends LivingEntityRenderState {
    float swellAnimationSpeed = 1.0F;

    boolean isSheared;
    boolean isSleeping;

    public final AnimationState sleepingAnimationState = new AnimationState();
    public final AnimationState conversionBeginAnimationState = new AnimationState();
    public final AnimationState conversionAnimationState = new AnimationState();
    public final AnimationState conversionEndAnimationState = new AnimationState();
}