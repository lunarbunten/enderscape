package net.bunten.enderscape.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.util.MathUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    private final PlayerEntity mob = (PlayerEntity) (Object) this;

    @Inject(at = @At("HEAD"), method = "startFallFlying")
    public void ESstartFallFlying(CallbackInfo info) {
        playSound(EnderscapeSounds.ITEM_ELYTRA_START, 1, MathUtil.nextFloat(mob.getRandom(), 0.8F, 1.2F));
    }
}