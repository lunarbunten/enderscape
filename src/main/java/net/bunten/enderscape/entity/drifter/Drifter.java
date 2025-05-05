package net.bunten.enderscape.entity.drifter;

import net.bunten.enderscape.entity.ai.behavior.DrifterStartOrStopLeakingJelly;
import net.bunten.enderscape.registry.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Drifter extends AbstractDrifter {

    private static final String DRIPPING_JELLY_KEY = "DrippingJelly";
    private static final EntityDataAccessor<Boolean> DRIPPING_JELLY = SynchedEntityData.defineId(Drifter.class, EntityDataSerializers.BOOLEAN);

    public Drifter(EntityType<? extends Drifter> type, Level world) {
        super(type, world);
        
        DrifterStartOrStopLeakingJelly.refreshCooldown(this);
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return createBaseDrifterAttributes().add(Attributes.MAX_HEALTH, 16).add(Attributes.FLYING_SPEED, 0.4);
    }

    public boolean isDrippingJelly() {
        return entityData.get(DRIPPING_JELLY);
    }

    public void setDrippingJelly(boolean value) {
        entityData.set(DRIPPING_JELLY, value);
    }

    protected Vec3 getEntityBounceVelocity(LivingEntity mob) {
        var vel = mob.getDeltaMovement();
        float boost = mob.isFallFlying() ? 1.5F : 1;

        double height = 1.6;
        if (mob.isFallFlying()) height += 0.4;
        if (mob.isShiftKeyDown()) height /= 2;

        return new Vec3(vel.x * boost, height, vel.z * boost);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DRIPPING_JELLY, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean(DRIPPING_JELLY_KEY, isDrippingJelly());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setDrippingJelly(tag.getBoolean(DRIPPING_JELLY_KEY));
    }

    @Override
    public SoundEvent getJumpSound() {
        return EnderscapeEntitySounds.DRIFTER_JUMP;
    }

    private boolean hasFeatherFalling(LivingEntity mob) {
        try {
            var registry = level().registryAccess().lookup(Registries.ENCHANTMENT).orElse(null);
            if (registry == null) return false;

            var enchantment = registry.getValue(Enchantments.FEATHER_FALLING);
            if (enchantment == null) return false;

            var holder = registry.wrapAsHolder(enchantment);
            return EnchantmentHelper.getItemEnchantmentLevel(holder, mob.getItemBySlot(EquipmentSlot.FEET)) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (isAlive() && !level().isClientSide()) {
            level().getEntities(this, getBounceHitbox()).forEach((entity) -> {
                if (entity instanceof LivingEntity mob && mob.isAlive() && !mob.isSpectator() && !(mob instanceof AbstractDrifter)) collide(mob, getBounceHitbox());
            });
        }

        if (level() instanceof ServerLevel server) {
            if (isInLove() && !isDrippingJelly() && random.nextInt(8) == 0) {
                Vec3 pos = position();
                server.sendParticles(EnderscapeParticles.DRIFT_JELLY_DRIPPING, pos.x, pos.y + 0.5, pos.z, 1, 0.4F, 1, 0.4F, 0.1);
            }

            if (isDrippingJelly() && random.nextBoolean()) {
                Vec3 pos = position();
                server.sendParticles(EnderscapeParticles.DRIFT_JELLY_DRIPPING, pos.x, pos.y + 0.5, pos.z, 1, 0.4F, 1, 0.4F, 0.1);
            }
        }
    }

    @NotNull
    public AABB getBounceHitbox() {
        return getBoundingBox().inflate(0.25, 0, 0.25).deflate(0, 0.85, 0).move(0, 1, 0);
    }

    private void collide(LivingEntity mob, AABB bounceHitbox) {
        boolean isLeashOwner = isLeashed() && getLeashData().leashHolder == mob;
        if (!mob.onGround() && !isLeashOwner && mob.getDeltaMovement().y < -0.1F && bounceHitbox.intersects(mob.getBoundingBox())) {
            Vec3 velocity = new Vec3(mob.getDeltaMovement().x, mob.isShiftKeyDown() ? -0.4 : 0.6, mob.getDeltaMovement().z);
            setDeltaMovement(velocity);

            mob.setDeltaMovement(getEntityBounceVelocity(mob));
            gameEvent(GameEvent.STEP, mob);

            if (mob instanceof ServerPlayer player) {
                EnderscapeCriteria.BOUNCE_ON_DRIFTER.trigger(player, this);
                player.awardStat(EnderscapeStats.DRIFTER_BOUNCE);
                player.connection.send(new ClientboundSetEntityMotionPacket(mob));
            }

            mob.fallDistance = 0;
            playSound(EnderscapeEntitySounds.DRIFTER_BOUNCE, 1, 1);
            hurt(level().damageSources().source(EnderscapeDamageTypes.STOMP, mob), hasFeatherFalling(mob) ? 0 : 1);
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(Items.GLASS_BOTTLE) && isDrippingJelly()) {
            if (!level().isClientSide()) {
                stack.consume(1, player);

                ItemStack jelly = new ItemStack(EnderscapeItems.DRIFT_JELLY_BOTTLE);
                if (!player.getInventory().add(jelly)) player.drop(jelly, false);

                gameEvent(GameEvent.ENTITY_INTERACT, player);
                playSound(EnderscapeEntitySounds.DRIFTER_MILK, 0.5F, 1);
                setDrippingJelly(false);

                DrifterStartOrStopLeakingJelly.refreshCooldown(this);

                return InteractionResult.SUCCESS_SERVER;
            }
            return InteractionResult.CONSUME;
        } else {
            return super.mobInteract(player, hand);
        }
    }

    @Override
    public void spawnChildFromBreeding(ServerLevel world, Animal other) {
        super.spawnChildFromBreeding(world, other);
        if (!world.isClientSide()) {
            ((Drifter) other).setDrippingJelly(true);
            setDrippingJelly(true);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return EnderscapeEntitySounds.DRIFTER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        if (source.is(EnderscapeDamageTypes.STOMP) && source.getEntity() instanceof LivingEntity mob && hasFeatherFalling(mob)) return EnderscapeEntitySounds.DRIFTER_HURT_SILENT;
        return EnderscapeEntitySounds.DRIFTER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return EnderscapeEntitySounds.DRIFTER_DEATH;
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return EnderscapeEntities.DRIFTLET.create(world, EntitySpawnReason.BREEDING);
    }

    @Override
    public void setBaby(boolean baby) {
    }

}