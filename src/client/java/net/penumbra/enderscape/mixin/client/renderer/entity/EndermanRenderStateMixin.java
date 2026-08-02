package net.penumbra.enderscape.mixin.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.EndermanRenderState;
import net.minecraft.world.entity.AnimationState;
import net.penumbra.enderscape.renderer.entity.StoresSkylightFactor;
import net.penumbra.enderscape.renderer.entity.enderman.ImprovedEndermanRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(EndermanRenderState.class)
public abstract class EndermanRenderStateMixin implements ImprovedEndermanRenderState, StoresSkylightFactor {

    @Unique private AnimationState attackAnimationState = new AnimationState();

    @Unique private float skyLightFactor = 0.0F;

    @Override @Unique
    public AnimationState attackAnimationState() {
        return attackAnimationState;
    }

    @Override @Unique
    public float skyLightFactor() {
        return skyLightFactor;
    }

    @Override @Unique
    public void setSkyLightFactor(float value) {
        skyLightFactor = value;
    }
}