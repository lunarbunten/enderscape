package net.bunten.enderscape.mixin;

import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.entity.magnia.MagniaMoveable;
import net.bunten.enderscape.item.MagniaAttractorItem;
import net.bunten.enderscape.item.NebuliteToolContext;
import net.bunten.enderscape.registry.EnderscapeCriteria;
import net.bunten.enderscape.registry.EnderscapeItemSounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    @Shadow public abstract Inventory getInventory();

    @Unique
    private int Enderscape$airTicks = 0;

    protected PlayerMixin(EntityType<? extends LivingEntity> type, Level world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void Enderscape$tick(CallbackInfo info) {
        Enderscape$tickMagniaAttractorItemMovement();

        Enderscape$airTicks = (!onGround() && !isInWater()) ? Enderscape$airTicks + 1 : 0;

        if (isFallFlying() && isShiftKeyDown() && EnderscapeConfig.getInstance().elytraSneakToStopGliding) ((Player) (Object) this).stopFallFlying();
    }

    @Unique
    private final Map<Entity, Integer> Enderscape$magniaTrackedEntities = new HashMap<>();

    @Unique
    private final Map<Entity, Integer> Enderscape$pullTickCounters = new HashMap<>();

    @Unique
    private void Enderscape$tickMagniaAttractorItemMovement() {
        if (isAlive() && !isSpectator() && !level().isClientSide()) {
            ItemStack stack = MagniaAttractorItem.getValidAttractor(getInventory());

            if (!stack.isEmpty() && MagniaAttractorItem.isEnabled(stack)) {
                int range = MagniaAttractorItem.getEntityPullRange(stack);

                AABB inflated = getBoundingBox().inflate(range, 4, range);
                NebuliteToolContext context = new NebuliteToolContext(stack, level(), (Player) (Object) this);
                level().getEntitiesOfClass(ItemEntity.class, inflated).stream().filter(item -> !item.hasPickUpDelay()).forEach(entity -> Enderscape$pullEntity(context, entity, 1));
                level().getEntitiesOfClass(ExperienceOrb.class, inflated).stream().filter(orb -> orb.tickCount >= 20).forEach(entity -> Enderscape$pullEntity(context, entity, 0));

                Enderscape$magniaTrackedEntities.entrySet().removeIf(entry -> {
                    Entity item = entry.getKey();
                    int cooldown = entry.getValue();

                    if (position().distanceTo(item.position()) > range) {
                        if (cooldown >= 20) {
                            MagniaMoveable.setMovedByMagnia(item, false);
                            return true;
                        }
                        entry.setValue(cooldown + 1);
                    } else {
                        entry.setValue(0);
                    }

                    return false;
                });
            }

            Enderscape$pullTickCounters.entrySet().removeIf(entry -> !MagniaMoveable.wasMovedByMagnia(entry.getKey()));
        }
    }

    @Unique
    private void Enderscape$pullEntity(NebuliteToolContext context, Entity entity, int abuseCost) {
        ItemStack stack = context.stack();

        if (!Enderscape$magniaTrackedEntities.containsKey(entity)) {
            if (!MagniaMoveable.wasMovedByMagnia(entity)) {
                level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), EnderscapeItemSounds.MAGNIA_ATTRACTOR_MOVE, entity.getSoundSource(), 1.0F, 1.0F);
            }
            Enderscape$magniaTrackedEntities.put(entity, 0);
        }

        Vec3 speed = position().subtract(entity.position()).normalize().scale(entity.isUnderWater() ? 0.04 : 0.2);
        entity.setDeltaMovement(entity.getDeltaMovement().add(speed));
        MagniaMoveable.setMovedByMagnia(entity, true);

        if (abuseCost > 0) {
            if (MagniaMoveable.wasMovedByMagnia(entity)) {
                int ticks = Enderscape$pullTickCounters.getOrDefault(entity, 0) + 1;

                if (ticks >= 40) {
                    MagniaAttractorItem.incrementEntitiesPulled(stack, abuseCost);
                    MagniaAttractorItem.tryUseFuel(context, abuseCost - MagniaAttractorItem.getEntitiesPulledToUseFuel(stack));
                    ticks = 0;
                }

                Enderscape$pullTickCounters.put(entity, ticks);
            } else {
                Enderscape$pullTickCounters.remove(entity);
            }
        }

        if (context.user() instanceof ServerPlayer server) EnderscapeCriteria.PULL_ENTITY.trigger(server, entity);
    }

    @Inject(at = @At("HEAD"), method = "tryToStartFallFlying", cancellable = true)
    public void Enderscape$tryToStartFallFlying(CallbackInfoReturnable<Boolean> cir) {
        if (Enderscape$airTicks < 1) cir.setReturnValue(false);
    }

    @Inject(at = @At("HEAD"), method = "startFallFlying")
    public void Enderscape$startFallFlying(CallbackInfo info) {
        if (EnderscapeConfig.getInstance().elytraAddOpenCloseSounds && getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) playSound(EnderscapeItemSounds.ELYTRA_START_GLIDING, 1, Mth.nextFloat(getRandom(), 0.8F, 1.2F));
        level().broadcastEntityEvent(this, (byte) -68);
    }
}