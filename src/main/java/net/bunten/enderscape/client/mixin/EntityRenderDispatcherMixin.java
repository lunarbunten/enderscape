package net.bunten.enderscape.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.bunten.enderscape.entity.drifter.Drifter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

    @Inject(method = "renderHitbox", at = @At("TAIL"))
    private static void Enderscape$renderHitbox(PoseStack pose, VertexConsumer consumer, Entity entity, float f, float g, float h, float i, CallbackInfo ci) {
        if (entity instanceof Drifter drifter) {
            AABB aABB = drifter.getBounceHitbox().move(-entity.getX(), -entity.getY(), -entity.getZ());
            ShapeRenderer.renderLineBox(pose, consumer, aABB, g, h, i, 1.0F);
        }
    }
}