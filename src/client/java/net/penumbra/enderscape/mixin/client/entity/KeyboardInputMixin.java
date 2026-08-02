package net.penumbra.enderscape.mixin.client.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;
import net.penumbra.enderscape.registry.entity.EnderscapeMobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin extends ClientInput {

    @Inject(method = "tick", at = @At("TAIL"))
    public void Enderscape$cancelInput(CallbackInfo info) {
        if (EnderscapeMobEffects.isStunned(Minecraft.getInstance().player)) {
            keyPresses = Input.EMPTY;
            moveVector = Vec2.ZERO;
        }
    }
}
