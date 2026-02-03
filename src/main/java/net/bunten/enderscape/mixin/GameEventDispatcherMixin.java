package net.bunten.enderscape.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.bunten.enderscape.registry.EnderscapeAttributes;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventDispatcher;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameEventDispatcher.class)
public abstract class GameEventDispatcherMixin {

    @ModifyExpressionValue(
            method = "post",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/gameevent/GameEvent;notificationRadius()I"
            )
    )
    private int Enderscape$adjustNotificationRadius(
            int original,
            Holder<GameEvent> holder,
            Vec3 vec3,
            GameEvent.Context context
    ) {
        Entity source = context.sourceEntity();
        double multiplier = source != null ? EnderscapeAttributes.getStealthMultiplier(source) : 1.0;
        return (int) Math.round((original * multiplier));
    }
}