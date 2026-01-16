package net.bunten.enderscape.mixin;

import net.bunten.enderscape.entity.magnia.MagniaMoveable;
import net.bunten.enderscape.entity.magnia.MagniaProperties;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin extends Entity implements MagniaMoveable {

    @Unique
    private final AbstractMinecart entity = (AbstractMinecart) (Object) this;

    public AbstractMinecartMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Unique
    @Override
    public MagniaProperties createMagniaProperties() {
        return new MagniaProperties(
                entity -> true,
                entity -> 0.6F,
                entity -> 0.2F,
                DEFAULT_MAGNIA_PREDICATE,
                entity -> entity.setNoGravity(true),
                entity -> entity.setNoGravity(false)
        );
    }


    @Unique
    private static final EntityDataAccessor<Integer> MAGNIA_COOLDOWN_DATA = SynchedEntityData.defineId(AbstractMinecart.class, EntityDataSerializers.INT);

    @Unique
    @Override
    public EntityDataAccessor<Integer> Enderscape$magniaCooldownData() {
        return MAGNIA_COOLDOWN_DATA;
    }

    @Inject(at = @At("TAIL"), method = "defineSynchedData")
    public void Enderscape$addAdditionalSaveData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        defineMagniaData(builder);
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void Enderscape$tick(CallbackInfo info) {
        MagniaMoveable.tickMagniaCooldown(entity);
    }
}