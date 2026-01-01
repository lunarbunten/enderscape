package net.bunten.enderscape.mixin;

import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.entity.magnia.MagniaMoveable;
import net.bunten.enderscape.item.ItemStackContext;
import net.bunten.enderscape.item.component.AttackSounds;
import net.bunten.enderscape.item.component.Enabled;
import net.bunten.enderscape.item.component.EntityMagnet;
import net.bunten.enderscape.particle.MagniaParticleOptions;
import net.bunten.enderscape.registry.EnderscapeCriteria;
import net.bunten.enderscape.registry.EnderscapeItemSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
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

    @Shadow public abstract Inventory getInventory();

    @Shadow public abstract @NotNull ItemStack getWeaponItem();

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

    @ModifyArg(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V",
                    ordinal = 0
            ),
            index = 4
    )
    public SoundEvent Enderscape$changeKnockbackSound(SoundEvent original) {
        ItemStack stack = getWeaponItem();
        return (!stack.isEmpty() && AttackSounds.is(stack)) ? AttackSounds.get(stack).knockback().value() : original;
    }

    @ModifyArg(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V",
                    ordinal = 1
            ),
            index = 4
    )
    public SoundEvent Enderscape$changeSweepSound(SoundEvent original) {
        ItemStack stack = getWeaponItem();
        return (!stack.isEmpty() && AttackSounds.is(stack)) ? AttackSounds.get(stack).sweep().value() : original;
    }

    @ModifyArg(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V",
                    ordinal = 2
            ),
            index = 4
    )
    public SoundEvent Enderscape$changeCritSound(SoundEvent original) {
        ItemStack stack = getWeaponItem();
        return (!stack.isEmpty() && AttackSounds.is(stack)) ? AttackSounds.get(stack).crit().value() : original;
    }

    @ModifyArg(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V",
                    ordinal = 3
            ),
            index = 4
    )
    public SoundEvent Enderscape$changeStrongSound(SoundEvent original) {
        ItemStack stack = getWeaponItem();
        return (!stack.isEmpty() && AttackSounds.is(stack)) ? AttackSounds.get(stack).strong().value() : original;
    }

    @ModifyArg(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V",
                    ordinal = 4
            ),
            index = 4
    )
    public SoundEvent Enderscape$changeWeakSound(SoundEvent original) {
        ItemStack stack = getWeaponItem();
        return (!stack.isEmpty() && AttackSounds.is(stack)) ? AttackSounds.get(stack).weak().value() : original;
    }

    @ModifyArg(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V",
                    ordinal = 5
            ),
            index = 4
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

                    if (!totalRange.contains(item.position())) {
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
    private void Enderscape$pullEntity(ItemStackContext context, Entity entity, EntityMagnet magnet, int abuseCost) {
        ItemStack stack = context.stack();

        if (!Enderscape$magniaTrackedEntities.containsKey(entity)) {
            if (!MagniaMoveable.wasMovedByMagnia(entity)) {
                level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), magnet.pullEntitySound().value(), entity.getSoundSource(), 1.0F, 1.0F);
            }
            Enderscape$magniaTrackedEntities.put(entity, 0);
        }

        Vec3 speed = position().subtract(entity.position()).normalize().scale(entity.isUnderWater() ? 0.04 : 0.2);
        entity.setDeltaMovement(entity.getDeltaMovement().add(speed));
        MagniaMoveable.setMovedByMagnia(entity, true);
        if (level() instanceof ServerLevel server) MagniaMoveable.sendEntityEffectParticles(server, entity, MagniaParticleOptions.MAGNIA_ATTRACTOR, 0.25F);

        if (abuseCost > 0) {
            if (MagniaMoveable.wasMovedByMagnia(entity)) {
                int ticks = Enderscape$pullTickCounters.getOrDefault(entity, 0) + 1;

                if (ticks >= 40) {
                    stack.hurtAndBreak(1, this, getEquipmentSlotForItem(stack));
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