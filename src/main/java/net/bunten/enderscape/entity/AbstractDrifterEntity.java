package net.bunten.enderscape.entity;

import java.util.EnumSet;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import net.bunten.enderscape.entity.driftlet.DriftletEntity;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.util.MathUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public abstract class AbstractDrifterEntity extends AnimalEntity {
    private final Ingredient BREEDING_INGREDIENT = Ingredient.fromTag(EnderscapeItems.DRIFTER_FOOD);

    private static final String HOME_BLOCK_POS_KEY = "HomeBlockPos";
    private static final TrackedData<Optional<BlockPos>> HOME_BLOCK_POS = DataTracker.registerData(AbstractDrifterEntity.class, TrackedDataHandlerRegistry.OPTIONAL_BLOCK_POS);

    private static final String HOME_CHECK_COOLDOWN_KEY = "HomeCheckCooldown";
    protected int homeCheckCooldown = 0;

    public AbstractDrifterEntity(EntityType<? extends AbstractDrifterEntity> type, World world) {
        super(type, world);

        moveControl = new FlightMoveControl(this, 20, true);
        homeCheckCooldown = nextHomeCheckCooldown(true);
        
        setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1);
        setPathfindingPenalty(PathNodeType.WATER, -1);
        setPathfindingPenalty(PathNodeType.WATER_BORDER, 16);
    }

    public BlockPos getHomePosition() {
        return dataTracker.get(HOME_BLOCK_POS).orElse(null);
    }

    public void setHomePosition(@Nullable BlockPos value) {
        getDataTracker().set(HOME_BLOCK_POS, Optional.ofNullable(value));
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

    protected void initDataTracker() {
        super.initDataTracker();

        dataTracker.startTracking(HOME_BLOCK_POS, Optional.empty());
    }

    public void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);

        tag.putInt(HOME_CHECK_COOLDOWN_KEY, homeCheckCooldown);
        if (getHomePosition() != null) {
            tag.put(HOME_BLOCK_POS_KEY, NbtHelper.fromBlockPos(getHomePosition()));
        }
    }

    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);

        homeCheckCooldown = tag.getInt(HOME_CHECK_COOLDOWN_KEY);
        if (tag.contains(HOME_BLOCK_POS_KEY, 10)) {
            setHomePosition(NbtHelper.toBlockPos(tag.getCompound(HOME_BLOCK_POS_KEY)));
        }
    }

    protected void updateNeighboringDrifters() {
        var list = world.getTargets(AbstractDrifterEntity.class, TargetPredicate.createNonAttackable().setBaseMaxDistance(100), this, getBoundingBox().expand(20, 8, 20));
        for (var mob : list) {
            mob.setHomePosition(getHomePosition());
            mob.setHomeCheckCooldown(mob.nextHomeCheckCooldown(false));
        }
    }

    protected void eat(PlayerEntity player, Hand hand, ItemStack stack) {
        world.playSoundFromEntity(null, this, getEatSound(stack), getSoundCategory(), getSoundVolume(), getSoundPitch());
        super.eat(player, hand, stack);
    }
    
    @Override
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation navigation = new BirdNavigation(this, world) { 

            @Override
            public boolean isValidPosition(BlockPos pos) {
                return !world.getBlockState(pos.down()).isAir();
            }
        };

        navigation.setCanPathThroughDoors(false);
        navigation.setCanSwim(false);
        navigation.setCanEnterOpenDoors(true);
        
        return navigation;
    }

    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        return world.getBlockState(pos).isAir() ? 10 : 0;
    }

    @Override
    public boolean canSpawn(WorldView world) {
        return world.doesNotIntersectEntities(this);
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public float getSoundPitch() {
        return MathUtil.nextFloat(random, 0.8F, 1.2F);
    }

    @Override
    public SoundEvent getEatSound(ItemStack stack) {
        return isBaby() ? EnderscapeSounds.ENTITY_DRIFTLET_EAT : EnderscapeSounds.ENTITY_DRIFTER_EAT;
    }

    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource source) {
        return false;
    }

    public boolean hurtByWater() {
        return true;
    }

    public boolean isBaby() {
        return this instanceof DriftletEntity;
    }

    @Override
    public boolean canBeLeashedBy(PlayerEntity mob) {
        return !isLeashed();
    }

    @Override
    public Vec3d getLeashOffset() {
        return isBaby() ? new Vec3d(0, getStandingEyeHeight() + 0.26F, getWidth() * 0.05F) : new Vec3d(0, getStandingEyeHeight() + 0.38F, 0);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return BREEDING_INGREDIENT.test(stack);
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

    protected class DrifterWanderGoal extends Goal {
        private final AbstractDrifterEntity mob;
        private int checkCooldown;
    
        public DrifterWanderGoal(AbstractDrifterEntity mob) {
            this.setControls(EnumSet.of(Control.MOVE));
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
        public boolean canStart() {
            return mob.getNavigation().isIdle() && mob.getRandom().nextInt(10) == 0;
        }
    
        @Override
        public boolean shouldContinue() {
            return mob.getNavigation().isFollowingPath();
        }
    
        @Override
        public void start() {
            BlockPos target = null;
            var pos = mob.getHomePosition();

            if (pos != null && !isWithinDistance(mob.getBlockPos(), pos, 32, 24)) {
                target = pos;
            } else {
                target = getRandomLocation();
            }

            if (target != null) {
                mob.getNavigation().startMovingAlong(mob.getNavigation().findPathTo(target, 1), 1);
            }
        }

        protected boolean isBlockMissing() {
            var pos = mob.getHomePosition();
            return pos != null && !world.getBlockState(pos).isIn(EnderscapeBlocks.DRIFTER_ATTRACTING_BLOCKS);
        }

        @Nullable
        protected BlockPos getNewAttractionBlock() {
            var block = BlockPos.findClosest(mob.getBlockPos(), 64, 64, pos -> world.getBlockState(pos).isIn(EnderscapeBlocks.DRIFTER_ATTRACTING_BLOCKS));
            return block.isPresent() ? block.get() : null;
        }
    
        @Nullable
        private BlockPos getRandomLocation() {
            Vec3d vec = mob.getRotationVec(0);
            Vec3d vec2 = AboveGroundTargeting.find(mob, 8, 7, vec.x, vec.z, 1.5707964f, 3, 1);
            Vec3d vec3 = vec2 != null ? vec2 : NoPenaltySolidTargeting.find(mob, 8, 4, -2, vec.x, vec.z, 1.5707963705062866);
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