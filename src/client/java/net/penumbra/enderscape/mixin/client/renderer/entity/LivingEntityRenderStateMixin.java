package net.penumbra.enderscape.mixin.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.penumbra.enderscape.renderer.StunTicksPercentage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderState.class)
public abstract class LivingEntityRenderStateMixin implements StunTicksPercentage {

    @Unique
    private float stunTicksPercentage;

    @Override
    public void setStunTicksPercentage(float value) {
        stunTicksPercentage = value;
    }

    @Override
    public float getStunTicksPercentage() {
        return stunTicksPercentage;
    }
}