package net.penumbra.enderscape.mixin.server;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionResult;
import net.penumbra.enderscape.registry.entity.EnderscapeMobEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {

    @Shadow @Final protected ServerPlayer player;

    @Inject(method = "useItem", at = @At("HEAD"), cancellable = true)
    private void Enderscape$cancelUseItem(CallbackInfoReturnable<InteractionResult> info) {
        if (EnderscapeMobEffects.isStunned(player)) info.setReturnValue(InteractionResult.PASS);
    }

    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    private void Enderscape$cancelUseItemOn(CallbackInfoReturnable<InteractionResult> info) {
        if (EnderscapeMobEffects.isStunned(player)) info.setReturnValue(InteractionResult.PASS);
    }

    @Inject(method = "handleBlockBreakAction", at = @At("HEAD"), cancellable = true)
    private void Enderscape$cancelBlockBreak(CallbackInfo info) {
        if (EnderscapeMobEffects.isStunned(player)) info.cancel();
    }
}