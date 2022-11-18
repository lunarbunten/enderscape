package net.bunten.enderscape.entity.driftlet;

import net.bunten.enderscape.entity.AbstractDrifter;
import net.bunten.enderscape.entity.drifter.Drifter;
import net.bunten.enderscape.entity.rubblemite.Rubblemite;
import net.bunten.enderscape.registry.EnderscapeEntities;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class Driftlet extends AbstractDrifter {
    public static int MAX_GROWTH_AGE = 24000;
    private int growthAge;
    
    public Driftlet(EntityType<? extends AbstractDrifter> type, Level world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new PanicGoal(this, 2));
        goalSelector.addGoal(2, new TemptGoal(this, 1.25D, Ingredient.of(EnderscapeItems.DRIFTER_FOOD), false));
        goalSelector.addGoal(3, new FollowParentGoal(this, 1.25));
        goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Rubblemite.class, 8, 2.2D, 2.2D));
        goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Slime.class, 8, 2.2D, 2.2D));
        goalSelector.addGoal(5, new DrifterWanderGoal(this));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Driftlet.class, 8));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes()
        .add(Attributes.MAX_HEALTH, 8)
        .add(Attributes.FOLLOW_RANGE, 16)
        .add(Attributes.MOVEMENT_SPEED, 0.1)
        .add(Attributes.FLYING_SPEED, 0.35)
        .add(Attributes.ATTACK_DAMAGE, 2);
    }

    private void growUp() {
        if (level instanceof ServerLevel server) {
            Drifter mob = EnderscapeEntities.DRIFTER.create(level);
            
            mob.moveTo(getX(), getY(), getZ(), getYRot(), getXRot());
            mob.finalizeSpawn(server, level.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.CONVERSION, null, null);
            mob.setNoAi(isNoAi());

            if (hasCustomName()) {
                mob.setCustomName(getCustomName());
                mob.setCustomNameVisible(isCustomNameVisible());
            }

            if (isLeashed()) dropLeash(true, true);
            if (isPassenger()) mob.startRiding(getVehicle());

            mob.setPersistenceRequired();
            server.addFreshEntityWithPassengers(mob);
            discard();
        }
    }

    private void setGrowthAge(int value) {
        growthAge = value;
        if (growthAge >= MAX_GROWTH_AGE && level.noCollision(getBoundingBox().inflate(1))) {
            growUp();
        }
    }

    private void increaseAge(int seconds) {
        setGrowthAge(growthAge + seconds * 20);
    }

    private int getTicksUntilGrowth() {
        return Math.max(0, MAX_GROWTH_AGE - growthAge);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level.isClientSide()) {
            setGrowthAge(growthAge + 1);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("Age", growthAge);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        setGrowthAge(nbt.getInt("Age"));
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(EnderscapeItems.DRIFTER_FOOD)) {
            usePlayerItem(player, hand, stack);
            increaseAge(AgeableMob.getSpeedUpSecondsWhenFeeding(getTicksUntilGrowth()));
            level.addParticle(ParticleTypes.HAPPY_VILLAGER, getRandomX(1), getRandomY() + 0.5, getRandomZ(1), 0, 0, 0);
            return InteractionResult.SUCCESS;
        } else {
            return super.mobInteract(player, hand);
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 0.5F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return EnderscapeSounds.DRIFTLET_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return EnderscapeSounds.DRIFTLET_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return EnderscapeSounds.DRIFTLET_DEATH;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return null;
    }
}