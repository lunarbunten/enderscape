package net.bunten.enderscape.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.bunten.enderscape.config.Config;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.level.Level;

/*
 *  Tridents returning from void
 */
@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin extends AbstractArrow {
    protected ThrownTridentMixin(EntityType<? extends AbstractArrow> entityType, Level world) {
        super(entityType, world);
    }

    @Final
    @Shadow
    private static EntityDataAccessor<Byte> ID_LOYALTY;

    @Shadow
    private boolean dealtDamage;

    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo info) {
        if (getOwner() != null && getY() < level.getMinBuildHeight() && entityData.get(ID_LOYALTY) > 0 && Config.QOL.shouldTridentsReturnInVoid()) {
            dealtDamage = true;
            teleportTo(getOwner().getX(), getOwner().getY(), getOwner().getZ());
            setDeltaMovement(0, 0, 0);
            playSound(EnderscapeSounds.TRIDENT_WARP);
        }
    }
}