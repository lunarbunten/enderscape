package net.bunten.enderscape.mixin;

import net.minecraft.world.entity.projectile.ThrownPotion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

//TODO: ADD CONFIG FOR THIS
@Mixin(ThrownPotion.class)
public abstract class ThrownPotionMixin {

    @ModifyArgs(method = "makeAreaOfEffectCloud", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/AreaEffectCloud;setRadiusOnUse(F)V"))
    private void Enderscape$updateSetRadiusOnUse(Args args) {
        args.set(0, 0.0F);
    }

    @ModifyArgs(method = "makeAreaOfEffectCloud", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/AreaEffectCloud;setRadiusPerTick(F)V"))
    private void Enderscape$updateSetRadiusPerTick(Args args) {
        args.set(0, 0.0F);
    }
}