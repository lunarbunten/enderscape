package net.bunten.enderscape.mixin;

import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.registry.EnderscapeItemSounds;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
        if (EnderscapeConfig.getInstance().tridentsReturnFromVoid && getOwner() != null && getY() < level().getMinY() && entityData.get(ID_LOYALTY) > 0) {
            dealtDamage = true;
            teleportTo(getOwner().getX(), getOwner().getY(), getOwner().getZ());
            setDeltaMovement(0, 0, 0);
            playSound(EnderscapeItemSounds.TRIDENT_WARP);
        }
    }
}