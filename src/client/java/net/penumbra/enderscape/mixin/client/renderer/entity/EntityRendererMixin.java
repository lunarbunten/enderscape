package net.penumbra.enderscape.mixin.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.penumbra.enderscape.manager.VoidManager;
import net.penumbra.enderscape.renderer.VoidTicksPercentage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {

    @Inject(method = "extractRenderState", at = @At("RETURN"))
    private void Enderscape$extractRenderState(T entity, S state, float partialTicks, CallbackInfo info) {
        if (state instanceof VoidTicksPercentage instance) {
            instance.setVoidTicksPercentage(VoidManager.getVoidTicksPercentage(entity));
        }
    }
}