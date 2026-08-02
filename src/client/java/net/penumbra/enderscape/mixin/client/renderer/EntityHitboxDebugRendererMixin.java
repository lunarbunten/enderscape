package net.penumbra.enderscape.mixin.client.renderer;

import net.minecraft.client.renderer.debug.EntityHitboxDebugRenderer;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.entity.drifter.Drifter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityHitboxDebugRenderer.class)
public class EntityHitboxDebugRendererMixin {

    @Inject(method = "showHitboxes", at = @At("TAIL"))
    public void Enderscape$applyInput(Entity entity, float f, boolean bl, CallbackInfo ci) {
        if (entity instanceof Drifter drifter) {
            Vec3 pos = drifter.position();
            Vec3 oldPos = drifter.getPosition(f);
            Vec3 offset = oldPos.subtract(pos);
            Gizmos.cuboid(drifter.getBounceHitbox().move(offset), GizmoStyle.stroke(ARGB.colorFromFloat(1.0F, 0.25F, 1.0F, 0.0F)));
        }
    }
}