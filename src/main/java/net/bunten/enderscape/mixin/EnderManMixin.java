package net.bunten.enderscape.mixin;

import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.registry.EnderscapeServerNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderMan.class)
public abstract class EnderManMixin extends Monster {

    @Unique
    private final EnderMan enderman = (EnderMan) (Object) this;

    protected EnderManMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "isBeingStaredBy", at = @At("RETURN"))
    public void Enderscape$sendStarePayload(Player player, CallbackInfoReturnable<Boolean> info) {
        if (info.getReturnValue() && player instanceof ServerPlayer server && enderman.canAttack(server)) EnderscapeServerNetworking.sendStareOverlayPayload(server);
    }

    @Inject(method = "playStareSound", at = @At("HEAD"), cancellable = true)
    public void Enderscape$playStareSound(CallbackInfo info) {
        if (EnderscapeConfig.getInstance().endermanStereoStareSound) info.cancel();
    }

    @Inject(method = "setTarget", at = @At("TAIL"))
    public void Enderscape$setTarget(LivingEntity target, CallbackInfo ci) {
        if (target instanceof ServerPlayer server && enderman.canAttack(server) && EnderscapeConfig.getInstance().endermanStereoStareSound) EnderscapeServerNetworking.sendStareSoundPayload(server, getId());
    }
}