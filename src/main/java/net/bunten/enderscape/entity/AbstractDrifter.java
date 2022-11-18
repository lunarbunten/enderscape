package net.bunten.enderscape.entity;

import java.util.EnumSet;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import net.bunten.enderscape.entity.driftlet.Driftlet;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.util.MathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractDrifter extends Animal {
    private final Ingredient BREEDING_INGREDIENT = Ingredient.of(EnderscapeItems.DRIFTER_FOOD);

    private static final String HOME_BLOCK_POS_KEY = "HomeBlockPos";
    private static final EntityDataAccessor<Optional<BlockPos>> HOME_BLOCK_POS = SynchedEntityData.defineId(AbstractDrifter.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

    private static final String HOME_CHECK_COOLDOWN_KEY = "HomeCheckCooldown";
    protected int homeCheckCooldown = 0;

    public AbstractDrifter(EntityType<? extends AbstractDrifter> type, Level world) {
        super(type, world);

        moveControl = new FlyingMoveControl(this, 20, true);
        homeCheckCooldown = nextHomeCheckCooldown(true);
        
        setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1);
        setPathfindingMalus(BlockPathTypes.WATER, -1);
        setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16);
    }

    public BlockPos getHomePosition() {
        return entityData.get(HOME_BLOCK_POS).orElse(null);
    }

    public void setHomePosition(@Nullable BlockPos value) {
        getEntityData().set(HOME_BLOCK_POS, Optional.ofNullable(value));
    }

    public int getHomeCheckCooldown() {
        return homeCheckCooldown;
    }

    public void setHomeCheckCooldown(int value) {
        homeCheckCooldown = value;
    }

    protected int nextHomeCheckCooldown(boolean init) {
        var value = MathUtil.nextInt(random, 300, 1200);
        return init ? value *= 0.2 : value;
    }

    protected void updateNeighboringDrifters() {
        var list = level.getNearbyEntities(AbstractDrifter.class, TargetingConditions.forNonCombat().range(100), this, getBoundingBox().inflate(20, 8, 20));
        for (var mob : list) {
            mob.setHomePosition(getHomePosition());
            mob.setHomeCheckCooldown(mob.nextHomeCheckCooldown(false));
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();

        entityData.define(HOME_BLOCK_POS, Optional.empty());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putInt(HOME_CHECK_COOLDOWN_KEY, homeCheckCooldown);
        if (getHomePosition() != null) {
            tag.put(HOME_BLOCK_POS_KEY, NbtUtils.writeBlockPos(getHomePosition()));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        homeCheckCooldown = tag.getInt(HOME_CHECK_COOLDOWN_KEY);
        if (tag.contains(HOME_BLOCK_POS_KEY, 10)) {
            setHomePosition(NbtUtils.readBlockPos(tag.getCompound(HOME_BLOCK_POS_KEY)));
        }
    }

    @Override
    protected void usePlayerItem(Player player, InteractionHand hand, ItemStack stack) {
        level.playSound(null, this, getEatingSound(stack), getSoundSource(), getSoundVolume(), getVoicePitch());
        super.usePlayerItem(player, hand, stack);
    }
    
    @Override
    protected PathNavigation createNavigation(Level world) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, world) { 

            @Override
            public boolean isStableDestination(BlockPos pos) {
                return !level.getBlockState(pos.below()).isAir();
            }
        };

        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(false);
        navigation.setCanPassDoors(true);
        
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
        return MathUtil.nextFloat(random, 0.8F, 1.2F);
    }

    @Override
    public SoundEvent getEatingSound(ItemStack stack) {
        return isBaby() ? EnderscapeSounds.DRIFTLET_EAT : EnderscapeSounds.DRIFTER_EAT;
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
    public boolean isSensitiveToWater() {
        return true;
    }

    @Override
    public boolean isBaby() {
        return this instanceof Driftlet;
    }

    @Override
    protected void jumpInLiquid(TagKey<Fluid> tagKey) {
        setDeltaMovement(getDeltaMovement().add(0.0, 0.01, 0.0));
    }

    @Override
    public boolean canBeLeashed(Player mob) {
        return !isLeashed();
    }

    @Override
    public Vec3 getLeashOffset() {
        return isBaby() ? new Vec3(0, getEyeHeight() + 0.26F, getBbWidth() * 0.05F) : new Vec3(0, getEyeHeight() + 0.38F, 0);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return BREEDING_INGREDIENT.test(stack);
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

    protected class DrifterWanderGoal extends Goal {
        private final AbstractDrifter mob;
        private int checkCooldown;
    
        public DrifterWanderGoal(AbstractDrifter mob) {
            this.setFlags(EnumSet.of(Flag.MOVE));
            this.mob = mob;

            checkCooldown = mob.getHomeCheckCooldown();
        }

        @Override
        public void tick() {
            var pos = mob.getHomePosition();

            if (checkCooldown > 0) {
                checkCooldown--;
            } else {
                if (pos == null || isBlockMissing()) {
                    mob.setHomePosition(getNewAttractionBlock());;
                    mob.updateNeighboringDrifters();
                }

                checkCooldown = MathUtil.nextInt(mob.random, 300, 1200);
            }

            mob.setHomeCheckCooldown(checkCooldown);
        }
    
        @Override
        public boolean canUse() {
            return mob.getNavigation().isDone() && mob.getRandom().nextInt(10) == 0;
        }
    
        @Override
        public boolean canContinueToUse() {
            return mob.getNavigation().isInProgress();
        }
    
        @Override
        public void start() {
            BlockPos target = null;
            var pos = mob.getHomePosition();

            if (pos != null && !isWithinDistance(mob.blockPosition(), pos, 32, 24)) {
                target = pos;
            } else {
                target = getRandomLocation();
            }

            if (target != null) {
                mob.getNavigation().moveTo(mob.getNavigation().createPath(target, 1), 1);
            }
        }

        protected boolean isBlockMissing() {
            var pos = mob.getHomePosition();
            return pos != null && !level.getBlockState(pos).is(EnderscapeBlocks.DRIFTER_ATTRACTING_BLOCKS);
        }

        @Nullable
        protected BlockPos getNewAttractionBlock() {
            var block = BlockPos.findClosestMatch(mob.blockPosition(), 64, 64, pos -> level.getBlockState(pos).is(EnderscapeBlocks.DRIFTER_ATTRACTING_BLOCKS));
            return block.isPresent() ? block.get() : null;
        }
    
        @Nullable
        private BlockPos getRandomLocation() {
            Vec3 vec = mob.getViewVector(0);
            Vec3 vec2 = HoverRandomPos.getPos(mob, 8, 7, vec.x, vec.z, 1.5707964f, 3, 1);
            Vec3 vec3 = vec2 != null ? vec2 : AirAndWaterRandomPos.getPos(mob, 8, 4, -2, vec.x, vec.z, 1.5707963705062866);
            return vec3 == null ? null : new BlockPos(vec3);
        }

        protected boolean isWithinDistance(BlockPos pos, BlockPos pos2, double horizontalDistance, double verticalDistance) {
            return getHorizontalDistance(pos, pos2) < MathUtil.square(horizontalDistance) && getVerticalDistance(pos, pos2) < MathUtil.square(verticalDistance);
        }

        protected double getHorizontalDistance(BlockPos pos, BlockPos pos2) {
            double d = pos.getX() - pos2.getX();
            double f = pos.getZ() - pos2.getZ();
            return d * d + f * f;
        }

        protected double getVerticalDistance(BlockPos pos, BlockPos pos2) {
            double d = pos.getY() - pos2.getY();
            return d * d;
        }
    }
}