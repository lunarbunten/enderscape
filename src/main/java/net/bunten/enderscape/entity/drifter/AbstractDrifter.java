package net.bunten.enderscape.entity.drifter;

import com.mojang.serialization.Dynamic;
import net.bunten.enderscape.registry.EnderscapeEntitySounds;
import net.bunten.enderscape.registry.tag.EnderscapeItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractDrifter extends Animal {

    public AbstractDrifter(EntityType<? extends AbstractDrifter> type, Level world) {
        super(type, world);

        moveControl = new FlyingMoveControl(this, 20, true);

        setPathfindingMalus(PathType.WATER, -1);
        setPathfindingMalus(PathType.WATER_BORDER, 16);
    }

    protected static AttributeSupplier.Builder createBaseDrifterAttributes() {
        return createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.1).add(Attributes.FOLLOW_RANGE, 24).add(Attributes.GRAVITY, 0.005).add(Attributes.JUMP_STRENGTH, 0.1);
    }

    public static boolean canSpawn(EntityType<?> type, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return true;
    }

    @Override
    protected Brain.Provider<AbstractDrifter> brainProvider() {
        return Brain.provider(DrifterAI.MEMORY_TYPES, DrifterAI.SENSOR_TYPES);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return DrifterAI.makeBrain(brainProvider().makeBrain(dynamic));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Brain<AbstractDrifter> getBrain() {
        return (Brain<AbstractDrifter>) super.getBrain();
    }

    @Override
    protected void customServerAiStep() {
        if (level() instanceof ServerLevel server) {
            ProfilerFiller profiler = server.getProfiler();

            profiler.push("drifterBrain");
            getBrain().tick(server, this);
            profiler.pop();

            profiler.push("drifterActivityUpdate");
            DrifterAI.updateActivity(this);
            profiler.pop();
        }

        super.customServerAiStep();
    }

    public abstract SoundEvent getJumpSound();

    @Override
    public void aiStep() {
        super.aiStep();

        if (onGround()) jumpFromGround();
        if (isInLiquid()) setDeltaMovement(getDeltaMovement().add(0, 0.025, 0));
    }

    @Override
    public void jumpFromGround() {
        super.jumpFromGround();
        if (isAlive()) playSound(getJumpSound(), 1.0F, 1.0F);
    }

    @Override
    protected void usePlayerItem(Player player, InteractionHand hand, ItemStack stack) {
        super.usePlayerItem(player, hand, stack);
        if (isFood(stack)) playEatingSound();
    }

    protected void playEatingSound() {
        SoundEvent sound = isBaby() ? EnderscapeEntitySounds.DRIFTLET_EAT : EnderscapeEntitySounds.DRIFTER_EAT;
        level().playSound(null, this, sound, getSoundSource(), getSoundVolume(), getVoicePitch());
    }
    
    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(false);
        return navigation;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader world) {
        return world.getBlockState(pos).isAir() ? 10 : 0;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public float getVoicePitch() {
        return Mth.nextFloat(random, 0.8F, 1.2F);
    }

    @Override
    public boolean causeFallDamage(float f, float g, DamageSource damageSource) {
        return false;
    }

    @Override
    protected void checkFallDamage(double d, boolean bl, BlockState blockState, BlockPos blockPos) {
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    @Override
    public boolean isBaby() {
        return this instanceof Driftlet;
    }

    @Override
    protected void jumpInLiquid(TagKey<Fluid> tagKey) {
        setDeltaMovement(getDeltaMovement().add(0.0, 0.1, 0.0));
    }

    @Override
    public Vec3 getLeashOffset() {
        return isBaby() ? new Vec3(0, getEyeHeight() + 0.26F, getBbWidth() * 0.05F) : new Vec3(0, getEyeHeight() + 0.38F, 0);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(EnderscapeItemTags.DRIFTER_FOOD);
    }

}