package net.bunten.enderscape.entity.drifter;

import java.util.EnumSet;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.bunten.enderscape.entity.AbstractDrifterEntity;
import net.bunten.enderscape.entity.rubblemite.RubblemiteEntity;
import net.bunten.enderscape.registry.EnderscapeEntities;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.registry.EnderscapeStats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class DrifterEntity extends AbstractDrifterEntity {
    protected final DamageSource BOUNCE_DAMAGE_SOURCE = new BounceDamageSource("bounce");

    private static final String DRIPPING_JELLY_KEY = "DrippingJelly";
    private static final String LAST_JELLY_CHANGE_KEY = "TimeSinceJellyChange";

    private static final TrackedData<Boolean> DRIPPING_JELLY = DataTracker.registerData(DrifterEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected int lastJellyTicks = 0;

    public DrifterEntity(EntityType<? extends DrifterEntity> type, World world) {
        super(type, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return createMobAttributes()
        .add(EntityAttributes.GENERIC_MAX_HEALTH, 16)
        .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32)
        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1)
        .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.3)
        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10);
    }

    @Override
    protected void initGoals() {
        goalSelector.add(1, new EscapeDangerGoal(this, 2));
        goalSelector.add(2, new DrifterMateGoal(this));
        goalSelector.add(3, new TemptGoal(this, 1.25D, Ingredient.fromTag(EnderscapeItems.DRIFTER_FOOD), false));
        goalSelector.add(4, new FleeEntityGoal<>(this, RubblemiteEntity.class, 8, 2.2D, 2.2D));
        goalSelector.add(5, new DrifterWanderGoal(this));
        goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8));
        goalSelector.add(7, new LookAroundGoal(this));
    }

    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(DRIPPING_JELLY, false);
    }

    public void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);

        tag.putBoolean(DRIPPING_JELLY_KEY, isDrippingJelly());
        tag.putInt(LAST_JELLY_CHANGE_KEY, lastJellyTicks);
    }

    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);

        setDrippingJelly(tag.getBoolean(DRIPPING_JELLY_KEY));
        lastJellyTicks = tag.getInt(LAST_JELLY_CHANGE_KEY);
    }

    public boolean isDrippingJelly() {
        return dataTracker.get(DRIPPING_JELLY);
    }

    public void setDrippingJelly(boolean value) {
        dataTracker.set(DRIPPING_JELLY, value);
    }

    public void tick() {
        super.tick();
        if (lastJellyTicks > 7000 && random.nextFloat() < 0.03F && !world.isClient()) {
            setDrippingJelly(!isDrippingJelly());
            lastJellyTicks = 0;
        }

        List<Entity> list = world.getOtherEntities(this, getBoundingBox().expand(1, 0.5D, 1));
        for (Entity entity : list) {
            if (entity instanceof LivingEntity mob && !entity.isRemoved() && !(entity instanceof AbstractDrifterEntity)) {
                collide(mob);
            }
        }

        if (world instanceof ServerWorld server) {
            if (isInLove() && !isDrippingJelly() && random.nextInt(8) == 0) {
                Vec3d pos = getPos();
                server.spawnParticles(EnderscapeParticles.DRIPPING_JELLY, pos.x, pos.y + 0.5, pos.z, 1, 0.4F, 1, 0.4F, 0.1);
            }

            if (isDrippingJelly() && random.nextBoolean()) {
                Vec3d pos = getPos();
                server.spawnParticles(EnderscapeParticles.DRIPPING_JELLY, pos.x, pos.y + 0.5, pos.z, 1, 0.4F, 1, 0.4F, 0.1);
            }
        }

        lastJellyTicks++;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.isOf(Items.GLASS_BOTTLE) && isDrippingJelly()) {
            return extractJelly(player, hand);
        } else {
            return super.interactMob(player, hand);
        }
    }

    public ActionResult extractJelly(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (!world.isClient()) {
            world.playSoundFromEntity(null, this, EnderscapeSounds.ENTITY_DRIFTER_MILK, SoundCategory.PLAYERS, 0.5F, 1);
            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }
            ItemStack jelly = new ItemStack(EnderscapeItems.DRIFT_JELLY_BOTTLE);
            if (!player.getInventory().insertStack(jelly)) {
                player.dropItem(jelly, false);
            }
            setDrippingJelly(false);
            emitGameEvent(GameEvent.SHEAR, player);

            lastJellyTicks = 0;
            return ActionResult.SUCCESS;
        }
        return ActionResult.CONSUME;
    }

    public void breed(ServerWorld world, AnimalEntity other) {
        super.breed(world, other);
        if (!world.isClient()) {
            ((DrifterEntity) other).setDrippingJelly(true);
            setDrippingJelly(true);
        }
    }

    protected double getEntityBounceHeight(LivingEntity mob) {
        double height = 1.2;
        if (mob.isFallFlying()) {
            height += 0.4;
        }
        return mob.isSneaking() ? height -= 0.6 : height;
    }

    protected Vec3d getEntityBounceVelocity(LivingEntity mob) {
        var vel = mob.getVelocity();
        return new Vec3d(vel.x, getEntityBounceHeight(mob), vel.z);
    }

    protected void bounceEntity(LivingEntity mob) {
        var vel = getVelocity();
        setVelocity(vel.x, mob.isSneaking() ? -0.4 : 0.3, vel.z);
        mob.setVelocity(getEntityBounceVelocity(mob));

        if (mob instanceof PlayerEntity player) {
            player.incrementStat(EnderscapeStats.DRIFTER_BOUNCE);
        }

        if (mob instanceof ServerPlayerEntity server) {
            server.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(mob));
        }
        
        mob.fallDistance = 0;
        playSound(EnderscapeSounds.ENTITY_DRIFTER_BOUNCE, 1, 1);
        damage(BOUNCE_DAMAGE_SOURCE, 1);
    }

    private void collide(LivingEntity mob) {
        boolean bl = isAlive() && !mob.isOnGround() && mob.getVelocity().y < -0.1F && !mob.isSpectator();
        boolean bl2 = mob.getPos().getY() >= getPos().getY() + 2 && distanceTo(mob) < 3;

        if (bl && bl2 && !world.isClient()) {
            bounceEntity(mob);
        }
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 1.08F;
    }

    protected SoundEvent getAmbientSound() {
        return EnderscapeSounds.ENTITY_DRIFTER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return EnderscapeSounds.ENTITY_DRIFTER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return EnderscapeSounds.ENTITY_DRIFTER_DEATH;
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
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
        public Text getDeathMessage(LivingEntity entity) {
            String string = "death.bounce." + name;
            return Text.translatable(string, entity.getDisplayName());
        }
    
        @Override
        public boolean isScaledWithDifficulty() {
            return false;
        }
    }

    public class DrifterMateGoal extends Goal {
        private static final TargetPredicate VALID_MATE_PREDICATE = TargetPredicate.createNonAttackable().setBaseMaxDistance(8).ignoreVisibility();

        protected final AnimalEntity mob;
        protected final World world;

        @Nullable
        protected AnimalEntity mate;
        private int timer;
    
        public DrifterMateGoal(AnimalEntity mob) {
            this.mob = mob;
            this.world = mob.world;
            setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
        }
    
        @Override
        public boolean canStart() {
            if (!mob.isInLove()) {
                return false;
            }
            mate = findMate();
            return mate != null;
        }
    
        @Override
        public boolean shouldContinue() {
            return mate.isAlive() && mate.isInLove() && timer < 60;
        }
    
        @Override
        public void stop() {
            mate = null;
            timer = 0;
        }
    
        @Override
        public void tick() {
            mob.getLookControl().lookAt(mate, 10, mob.getMaxLookPitchChange());
            mob.getNavigation().startMovingTo(mate, 1);
            ++timer;
            if (timer >= getTickCount(60) && mob.squaredDistanceTo(mate) < 9) {
                breed();
            }
        }
    
        @Nullable
        private AnimalEntity findMate() {
            List<? extends AnimalEntity> list = world.getTargets(DrifterEntity.class, VALID_MATE_PREDICATE, mob, mob.getBoundingBox().expand(16));
            double d = Double.MAX_VALUE;
            AnimalEntity mate = null;
            for (AnimalEntity other : list) {
                if (!mob.canBreedWith(other) || !(mob.squaredDistanceTo(other) < d)) continue;
                mate = other;
                d = mob.squaredDistanceTo(other);
            }
            return mate;
        }
    
        protected void breed() {
            mob.breed((ServerWorld) world, mate);
        }
    }
}