package net.bunten.enderscape.client.entity.rustle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

@Environment(EnvType.CLIENT)
public class RustleRenderState extends LivingEntityRenderState {
    boolean isSheared;
    boolean isSleeping;

    public final AnimationState sleepingAnimationState = new AnimationState();
}