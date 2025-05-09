package net.bunten.enderscape.mixin;

import net.bunten.enderscape.entity.magnia.MagniaMoveable;
import net.bunten.enderscape.entity.magnia.MagniaProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin extends Entity implements MagniaMoveable {

    @Unique
    private final AbstractMinecart entity = (AbstractMinecart) (Object) this;

    @Shadow
    protected abstract void moveAlongTrack(BlockPos blockPos, BlockState blockState);

    @Shadow protected abstract void comeOffTrack();

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
                entity -> {
                    entity.setNoGravity(true);
                    if (random.nextInt(16) == 0 && level() instanceof ServerLevel server) {
                        server.sendParticles(ParticleTypes.END_ROD, position().x, position().y + 0.5, position().z, 1, 0.3F, 0.3, 0.3F, 0);
                    }
                },
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

        if (MagniaMoveable.wasMovedByMagnia(entity) && entity.level() instanceof ServerLevel serverLevel) {
            comeOffTrack();
        }
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;moveAlongTrack(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"))
    public void tick(AbstractMinecart entity, BlockPos blockPos, BlockState blockState) {
        if (!MagniaMoveable.wasMovedByMagnia(entity)) moveAlongTrack(blockPos, blockState);
    }
}