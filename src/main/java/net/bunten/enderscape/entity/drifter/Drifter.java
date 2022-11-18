package net.bunten.enderscape.entity.drifter;

import java.util.EnumSet;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.bunten.enderscape.criteria.EnderscapeCriteria;
import net.bunten.enderscape.entity.AbstractDrifter;
import net.bunten.enderscape.entity.rubblemite.Rubblemite;
import net.bunten.enderscape.registry.EnderscapeEntities;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.registry.EnderscapeStats;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class Drifter extends AbstractDrifter {
    protected final DamageSource BOUNCE_DAMAGE_SOURCE = new BounceDamageSource("bounce");

    private static final String DRIPPING_JELLY_KEY = "DrippingJelly";
    private static final String LAST_JELLY_CHANGE_KEY = "TimeSinceJellyChange";

    private static final EntityDataAccessor<Boolean> DRIPPING_JELLY = SynchedEntityData.defineId(Drifter.class, EntityDataSerializers.BOOLEAN);
    public int lastJellyTicks = 0;

    public Drifter(EntityType<? extends Drifter> type, Level world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new PanicGoal(this, 2));
        goalSelector.addGoal(2, new DrifterMateGoal(this));
        goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.of(EnderscapeItems.DRIFTER_FOOD), false));
        goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Rubblemite.class, 8, 2.2D, 2.2D));
        goalSelector.addGoal(5, new DrifterWanderGoal(this));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes()
        .add(Attributes.MAX_HEALTH, 16)
        .add(Attributes.FOLLOW_RANGE, 32)
        .add(Attributes.MOVEMENT_SPEED, 0.1)
        .add(Attributes.FLYING_SPEED, 0.3)
        .add(Attributes.ATTACK_DAMAGE, 10);
    }

    public boolean isDrippingJelly() {
        return entityData.get(DRIPPING_JELLY);
    }

    public void setDrippingJelly(boolean value) {
        entityData.set(DRIPPING_JELLY, value);
    }

    public InteractionResult extractJelly(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            level.playSound(null, this, EnderscapeSounds.DRIFTER_MILK, SoundSource.PLAYERS, 0.5F, 1);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            ItemStack jelly = new ItemStack(EnderscapeItems.DRIFT_JELLY_BOTTLE);
            if (!player.getInventory().add(jelly)) {
                player.drop(jelly, false);
            }
            setDrippingJelly(false);
            gameEvent(GameEvent.ENTITY_INTERACT, player);
            lastJellyTicks = 0;
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    protected double getEntityBounceHeight(LivingEntity mob) {
        double height = 1.2;
        if (mob.isFallFlying()) height += 0.4;
        if (mob.isShiftKeyDown()) height /= 2;
        return height;
    }

    protected Vec3 getEntityBounceVelocity(LivingEntity mob) {
        var vel = mob.getDeltaMovement();
        float boost = mob.isFallFlying() ? 1.5F : 1;
        return new Vec3(vel.x * boost, getEntityBounceHeight(mob), vel.z * boost);
    }

    protected void bounceEntity(LivingEntity mob) {
        var vel = getDeltaMovement();
        setDeltaMovement(vel.x, mob.isShiftKeyDown() ? -0.4 : 0.3, vel.z);

        if (mob instanceof ServerPlayer player && player.isFallFlying() && player.getDeltaMovement().lengthSqr() > 2) EnderscapeCriteria.HIGHS_AND_LOWS.trigger(player);
        mob.setDeltaMovement(getEntityBounceVelocity(mob));

        if (mob instanceof ServerPlayer player) {
            player.awardStat(EnderscapeStats.DRIFTER_BOUNCE);
            player.connection.send(new ClientboundSetEntityMotionPacket(mob));
        }
        
        mob.fallDistance = 0;
        playSound(EnderscapeSounds.DRIFTER_BOUNCE, 1, 1);
        hurt(BOUNCE_DAMAGE_SOURCE, EnchantmentHelper.getEnchantmentLevel(Enchantments.FALL_PROTECTION, mob) > 0 ? 0 : 1);
    }

    private void collide(LivingEntity mob) {
        boolean bl = isAlive() && !mob.isOnGround() && mob.getDeltaMovement().y < -0.1F && !mob.isSpectator();
        boolean bl2 = mob.position().y() >= position().y() + 2 && distanceTo(mob) < 3;

        if (bl && bl2 && !level.isClientSide()) {
            bounceEntity(mob);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DRIPPING_JELLY, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putBoolean(DRIPPING_JELLY_KEY, isDrippingJelly());
        tag.putInt(LAST_JELLY_CHANGE_KEY, lastJellyTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        setDrippingJelly(tag.getBoolean(DRIPPING_JELLY_KEY));
        lastJellyTicks = tag.getInt(LAST_JELLY_CHANGE_KEY);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (lastJellyTicks > (8 * 60 * 20) && random.nextFloat() < 0.025F && !level.isClientSide()) {
            setDrippingJelly(!isDrippingJelly());
            lastJellyTicks = 0;
        }

        List<Entity> list = level.getEntities(this, getBoundingBox().inflate(1, 0.5D, 1));
        for (Entity entity : list) {
            if (entity instanceof LivingEntity mob && !entity.isRemoved() && !(entity instanceof AbstractDrifter)) {
                collide(mob);
            }
        }

        if (level instanceof ServerLevel server) {
            if (isInLove() && !isDrippingJelly() && random.nextInt(8) == 0) {
                Vec3 pos = position();
                server.sendParticles(EnderscapeParticles.DRIPPING_JELLY, pos.x, pos.y + 0.5, pos.z, 1, 0.4F, 1, 0.4F, 0.1);
            }

            if (isDrippingJelly() && random.nextBoolean()) {
                Vec3 pos = position();
                server.sendParticles(EnderscapeParticles.DRIPPING_JELLY, pos.x, pos.y + 0.5, pos.z, 1, 0.4F, 1, 0.4F, 0.1);
            }
        }

        lastJellyTicks++;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(Items.GLASS_BOTTLE) && isDrippingJelly()) {
            return extractJelly(player, hand);
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
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 1.08F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return EnderscapeSounds.DRIFTER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return EnderscapeSounds.DRIFTER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return EnderscapeSounds.DRIFTER_DEATH;
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return EnderscapeEntities.DRIFTLET.create(world);
    }

    @Override
    public void setBaby(boolean baby) {
    }

    protected class BounceDamageSource extends DamageSource {
        public BounceDamageSource(String name) {
            super(name);
        }
    
        @Override
        public Component getLocalizedDeathMessage(LivingEntity entity) {
            String string = "death.bounce." + msgId;
            return Component.translatable(string, entity.getDisplayName());
        }
    
        @Override
        public boolean scalesWithDifficulty() {
            return false;
        }
    }

    public class DrifterMateGoal extends Goal {
        private static final TargetingConditions VALID_MATE_PREDICATE = TargetingConditions.forNonCombat().range(8).ignoreLineOfSight();

        protected final Animal mob;
        protected final Level world;

        @Nullable
        protected Animal mate;
        private int timer;
    
        public DrifterMateGoal(Animal mob) {
            this.mob = mob;
            this.world = mob.level;
            setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }
    
        @Override
        public boolean canUse() {
            if (!mob.isInLove()) {
                return false;
            }
            mate = findMate();
            return mate != null;
        }
    
        @Override
        public boolean canContinueToUse() {
            return mate.isAlive() && mate.isInLove() && timer < 60;
        }
    
        @Override
        public void stop() {
            mate = null;
            timer = 0;
        }
    
        @Override
        public void tick() {
            mob.getLookControl().setLookAt(mate, 10, mob.getMaxHeadXRot());
            mob.getNavigation().moveTo(mate, 1);
            ++timer;
            if (timer >= adjustedTickDelay(60) && mob.distanceToSqr(mate) < 9) {
                breed();
            }
        }
    
        @Nullable
        private Animal findMate() {
            List<? extends Animal> list = world.getNearbyEntities(Drifter.class, VALID_MATE_PREDICATE, mob, mob.getBoundingBox().inflate(16));
            double d = Double.MAX_VALUE;
            Animal mate = null;
            for (Animal other : list) {
                if (!mob.canMate(other) || !(mob.distanceToSqr(other) < d)) continue;
                mate = other;
                d = mob.distanceToSqr(other);
            }
            return mate;
        }
    
        protected void breed() {
            mob.spawnChildFromBreeding((ServerLevel) world, mate);
        }
    }
}