package net.bunten.enderscape.mixin;

import net.bunten.enderscape.entity.magnia.MagniaMoveable;
import net.bunten.enderscape.entity.magnia.MagniaProperties;
import net.bunten.enderscape.registry.EnderscapeStats;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity implements MagniaMoveable {

    @Shadow public abstract ItemStack getItem();

    @Unique
    private final ItemEntity entity = (ItemEntity) (Object) this;

    public ItemEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Unique
    @Override
    public MagniaProperties createMagniaProperties() {
        return new MagniaProperties(
                item -> false,
                item -> 0.6F,
                item -> 0.8F,
                item -> true,
                item -> {
                    entity.setPickUpDelay(20);
                    entity.setNoGravity(true);
                    if (random.nextInt(16) == 0 && level() instanceof ServerLevel server) {
                        server.sendParticles(ParticleTypes.END_ROD, position().x, position().y + 0.5, position().z, 1, 0.3F, 0.3, 0.3F, 0);
                    }
                },
                item -> item.setNoGravity(false)
        );
    }

    @Unique
    private static final EntityDataAccessor<Integer> MAGNIA_COOLDOWN_DATA = SynchedEntityData.defineId(ItemEntity.class, EntityDataSerializers.INT);

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

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;awardStat(Lnet/minecraft/stats/Stat;I)V", shift = At.Shift.AFTER), method = "playerTouch")
    private void Enderscape$awardItemsPulledStat(Player player, CallbackInfo info) {
        if (player instanceof ServerPlayer server && MagniaMoveable.wasMovedByMagnia(entity)) server.awardStat(EnderscapeStats.ITEMS_ATTRACTED, getItem().getCount());
    }
}