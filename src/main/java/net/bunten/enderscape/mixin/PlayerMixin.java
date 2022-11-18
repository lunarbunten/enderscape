package net.bunten.enderscape.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.bunten.enderscape.config.Config;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.util.MathUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> type, Level world) {
        super(type, world);
    }

    @Unique
    private final Player mob = (Player) (Object) this;

    @Inject(at = @At("HEAD"), method = "startFallFlying")
    public void startFallFlying(CallbackInfo info) {
        if (Config.QOL.playElytraOpenSound()) playSound(EnderscapeSounds.ELYTRA_START, 1, MathUtil.nextFloat(getRandom(), 0.8F, 1.2F));
    }
}