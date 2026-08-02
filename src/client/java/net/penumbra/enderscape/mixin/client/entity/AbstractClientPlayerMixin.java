package net.penumbra.enderscape.mixin.client.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.manager.DashJumpManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin extends Player {

    public AbstractClientPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Unique
    private final EnderscapeConfig config = EnderscapeConfig.getInstance();

    @Unique
    private int Enderscape$elytraFlightTime = 0;

    @Unique
    private float getIntensity(int intensity) {
        float configIntensity = (float) intensity / 100.0F;
        return (float) (configIntensity * Minecraft.getInstance().options.fovEffectScale().get());
    }

    @Unique
    private float Enderscape$getElytraFovModifier() {
        if (config.elytraFovEffectIntensity <= 0) {
            return 1.0F;
        } else {
            float fovModifier = 1.0F;

            if (isFallFlying()) {
                Enderscape$elytraFlightTime++;

                if (isRemoved() || (Enderscape$elytraFlightTime > 20 && !isFallFlying())) return fovModifier;

                float speed = (float) getDeltaMovement().lengthSqr();
                float factor = Mth.clamp(speed / 20, 0, 1.5F) * getIntensity(config.elytraFovEffectIntensity);

                if (Enderscape$elytraFlightTime < 20) {
                    return fovModifier;
                } else if (Enderscape$elytraFlightTime < 40) {
                    fovModifier = 1.0F + ((float) (Enderscape$elytraFlightTime - 20) / 20.0F) * factor;
                } else {
                    fovModifier = 1.0F + factor;
                }
            }

            return fovModifier;
        }
    }

    @Unique
    private float Enderscape$getDashJumpFovModifier() {
        if (config.rubbleShieldFovEffectIntensity <= 0) {
            return 1.0F;
        } else {
            float fovModifier = 1.0F;

            if (DashJumpManager.dashJumping(this)) {
                if (isRemoved()) return fovModifier;

                float speed = (float) getDeltaMovement().lengthSqr();
                float factor = Mth.clamp(speed / 20, 0, 1.5F) * getIntensity(config.rubbleShieldFovEffectIntensity);

                fovModifier = 1.0F + factor;
            }

            return fovModifier;
        }
    }

    @ModifyReturnValue(method = "getFieldOfViewModifier", at = @At("RETURN"))
    private float getFieldOfViewModifier(float original) {
        return original * Mth.clamp(
                Math.max(
                        Enderscape$getElytraFovModifier(),
                        Enderscape$getDashJumpFovModifier()
                ),
                1.0F,
                1.5F
        );
    }
}