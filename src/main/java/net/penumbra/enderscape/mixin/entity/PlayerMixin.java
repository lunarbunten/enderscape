package net.penumbra.enderscape.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.entity.magnia.MagniaAffected;
import net.penumbra.enderscape.item.ItemStackContext;
import net.penumbra.enderscape.item.component.*;
import net.penumbra.enderscape.manager.ElytraManager;
import net.penumbra.enderscape.manager.VoidManager;
import net.penumbra.enderscape.particle.MagniaParticleOptions;
import net.penumbra.enderscape.registry.entity.EnderscapeAttachments;
import net.penumbra.enderscape.registry.entity.EnderscapeDamageTypes;
import net.penumbra.enderscape.registry.entity.EnderscapeMobEffects;
import net.penumbra.enderscape.registry.server.EnderscapeCriteria;
import net.penumbra.enderscape.registry.sound.EnderscapeEntitySounds;
import net.penumbra.enderscape.registry.tag.EnderscapeDamageTypeTags;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    @Shadow
    public abstract Inventory getInventory();

    @Shadow
    public abstract @NotNull ItemStack getWeaponItem();

    protected PlayerMixin(EntityType<? extends LivingEntity> type, Level world) {
        super(type, world);
    }

    @Unique
    private final Player player = (Player) (Object) this;

    @ModifyReturnValue(method = "createAttackSource", at = @At("RETURN"))
    public DamageSource Enderscape$createAttackSource(DamageSource original) {
        if (player.hasAttached(EnderscapeAttachments.PERFORMING_STUN_ATTACK)) {
            return level().damageSources().source(EnderscapeDamageTypes.STUN_ATTACK, player);
        } else {
            return original;
        }
    }

    @Inject(method = "actuallyHurt", at = @At("HEAD"))
    public void Enderscape$applyStun(ServerLevel level, DamageSource source, float amount, CallbackInfo info) {
        if (amount > 0.0F && source.is(EnderscapeDamageTypes.STUN_ATTACK)) {
            StunAttack.applyStun(level, player, source);
        }
    }

    @ModifyReturnValue(method = "isMobilityRestricted", at = @At("RETURN"))
    public boolean Enderscape$isMobilityRestricted(boolean original) {
        return original || EnderscapeMobEffects.isStunned(player);
    }

    @ModifyReturnValue(method = "wantsToStopRiding", at = @At("RETURN"))
    public boolean Enderscape$cancelDismount(boolean original) {
        return original && !EnderscapeMobEffects.isStunned(player);
    }

    @WrapOperation(method = "interactOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;interact(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/InteractionResult;"))
    public InteractionResult Enderscape$overrideEntityInteract(Entity entity, Player user, InteractionHand hand, Vec3 location, Operation<InteractionResult> original) {
        if (entity instanceof LivingEntity && StunAttack.is(user.getItemInHand(hand))) {
            return InteractionResult.PASS;
        } else {
            return original.call(entity, user, hand, location);
        }
    }

    @WrapOperation(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setHealth(F)V"))
    public void Enderscape$convertVoidDamage(Player instance, float health, Operation<Void> original, @Local(argsOnly = true) DamageSource source) {
        original.call(instance, VoidManager.convertVoidDamage(instance, source, health));
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void Enderscape$tick(CallbackInfo info) {
        Enderscape$tickMagniaAttractorItemMovement();

        ElytraManager.tickPlayerAtTail(player);
    }

    @Inject(at = @At("RETURN"), method = "getHurtSound", cancellable = true)
    public void Enderscape$getHurtSound(DamageSource source, CallbackInfoReturnable<SoundEvent> info) {
        if (source.is(EnderscapeDamageTypeTags.VOIDS_HEALTH)) {
            info.setReturnValue(EnderscapeEntitySounds.PLAYER_HURT_VOID);
        }
    }

    @Unique
    private final Map<Entity, Integer> Enderscape$magniaTrackedEntities = new HashMap<>();

    @Unique
    private final Map<Entity, Integer> Enderscape$pullTickCounters = new HashMap<>();

    @ModifyArg(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;playServerSideSound(Lnet/minecraft/sounds/SoundEvent;)V",
                    ordinal = 0
            )
    )
    public SoundEvent Enderscape$changeKnockbackSound(SoundEvent original) {
        ItemStack stack = getWeaponItem();
        return (!stack.isEmpty() && AttackSounds.is(stack)) ? AttackSounds.get(stack).knockback().value() : original;
    }

    @ModifyArg(
            method = "doSweepAttack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;playServerSideSound(Lnet/minecraft/sounds/SoundEvent;)V"
            )
    )
    public SoundEvent Enderscape$changeSweepSound(SoundEvent original) {
        ItemStack stack = getWeaponItem();
        return (!stack.isEmpty() && AttackSounds.is(stack)) ? AttackSounds.get(stack).sweep().value() : original;
    }

    @ModifyArg(
            method = "attackVisualEffects",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;playServerSideSound(Lnet/minecraft/sounds/SoundEvent;)V",
                    ordinal = 0
            )
    )
    public SoundEvent Enderscape$changeCritSound(SoundEvent original) {
        ItemStack stack = getWeaponItem();
        return (!stack.isEmpty() && AttackSounds.is(stack)) ? AttackSounds.get(stack).crit().value() : original;
    }

    @ModifyArg(
            method = "attackVisualEffects",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;playServerSideSound(Lnet/minecraft/sounds/SoundEvent;)V",
                    ordinal = 1
            )
    )
    public SoundEvent Enderscape$changeStrongAndWeakSound(SoundEvent original) {
        ItemStack stack = getWeaponItem();
        if (!stack.isEmpty() && AttackSounds.is(stack)) {
            AttackSounds sounds = AttackSounds.get(stack);
            SoundEvent strong = sounds.strong().value();
            SoundEvent weak = sounds.weak().value();

            return original == SoundEvents.PLAYER_ATTACK_WEAK ? weak : strong;
        } else {
            return original;
        }
    }

    @ModifyArg(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;playServerSideSound(Lnet/minecraft/sounds/SoundEvent;)V",
                    ordinal = 1
            )
    )
    public SoundEvent Enderscape$changeNoDamageSound(SoundEvent original) {
        ItemStack stack = getWeaponItem();
        return (!stack.isEmpty() && AttackSounds.is(stack)) ? AttackSounds.get(stack).noDamage().value() : original;
    }

    @Unique
    private void Enderscape$tickMagniaAttractorItemMovement() {
        if (isAlive() && !isSpectator() && !level().isClientSide()) {
            ItemStack stack = EntityMagnet.getFirstUsableMagnet(getInventory());

            if (!stack.isEmpty() && Enabled.get(stack)) {
                EntityMagnet magnet = EntityMagnet.get(stack);

                AABB totalRange = getBoundingBox().inflate(magnet.pullRange().x, magnet.pullRange().y, magnet.pullRange().x);
                ItemStackContext context = new ItemStackContext(stack, level(), (Player) (Object) this);

                level().getEntitiesOfClass(Entity.class, totalRange, entity -> EntityMagnet.CAN_PULL_ENTITY.test(entity, magnet)).forEach(entity -> Enderscape$pullEntity(context, entity, magnet, EntityMagnet.abuseCost(entity, magnet)));

                Enderscape$magniaTrackedEntities.entrySet().removeIf(entry -> {
                    Entity item = entry.getKey();
                    int cooldown = entry.getValue();

                    if (item.isRemoved()) return true;

                    if (!totalRange.contains(item.position())) {
                        if (cooldown >= 20) {
                            MagniaAffected.setMoved(item, false);
                            return true;
                        }
                        entry.setValue(cooldown + 1);
                    } else {
                        entry.setValue(0);
                    }

                    return false;
                });
            } else {
                Enderscape$magniaTrackedEntities.keySet().forEach(item -> MagniaAffected.setMoved(item, false));
                Enderscape$magniaTrackedEntities.clear();
            }

            Enderscape$pullTickCounters.entrySet().removeIf(entry -> !MagniaAffected.wasMoved(entry.getKey()));
        }
    }

    @Unique
    private void Enderscape$pullEntity(ItemStackContext context, Entity entity, EntityMagnet magnet, int abuseCost) {
        if (!Enderscape$magniaTrackedEntities.containsKey(entity)) {
            if (!MagniaAffected.wasMoved(entity)) {
                level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), magnet.pullEntitySound().value(), entity.getSoundSource(), 1.0F, 1.0F);
            }
            Enderscape$magniaTrackedEntities.put(entity, 0);
        }

        Vec3 speed = position().subtract(entity.position()).normalize().scale(entity.isUnderWater() ? 0.04 : 0.2);
        entity.setDeltaMovement(entity.getDeltaMovement().add(speed));
        MagniaAffected.setMoved(entity, true);
        if (level() instanceof ServerLevel server)
            MagniaAffected.sendEntityEffectParticles(server, entity, MagniaParticleOptions.MAGNIA_ATTRACTOR, 0.25F);

        if (abuseCost > 0) {
            if (MagniaAffected.wasMoved(entity)) {
                int ticks = Enderscape$pullTickCounters.getOrDefault(entity, 0) + 1;

                if (ticks >= 40) {
                    FueledTool.useFuelOrDamage(context, 1, context.user().getEquipmentSlotForItem(context.stack()));
                    ticks = 0;
                }

                Enderscape$pullTickCounters.put(entity, ticks);
            } else {
                Enderscape$pullTickCounters.remove(entity);
            }
        }

        if (context.user() instanceof ServerPlayer server) EnderscapeCriteria.PULL_ENTITY.trigger(server, entity);
    }

    @Inject(at = @At("HEAD"), method = "startFallFlying")
    public void Enderscape$startFallFlying(CallbackInfo info) {
        ElytraManager.onStartFallFlying(player);
    }
}