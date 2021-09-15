package net.enderscape.entity.motu;

import net.enderscape.registry.EndSounds;
import net.enderscape.util.EndMath;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MotuEntity extends HostileEntity {
    public MotuEntity(EntityType<? extends MotuEntity> type, World world) {
        super(type, world);
        setPathfindingPenalty(PathNodeType.WATER, -1);
        experiencePoints = EndMath.nextInt(random, 5, 10);
        moveControl = new MotuMoveControl(this);
        stepHeight = 1;
    }

    protected void initGoals() {
        goalSelector.add(1, new MotuAttackGoal(this));
        goalSelector.add(5, new WanderAroundFarGoal(this, 1));
        goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8));
        goalSelector.add(6, new LookAroundGoal(this));
        targetSelector.add(1, new RevengeGoal(this));
        targetSelector.add(2, new FollowTargetGoal<>(this, PlayerEntity.class, true));
    }

    public void tick() {
        super.tick();
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(EndSounds.ENTITY_MOTU_STEP, 1, 1);
    }

    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 1.02F;
    }

    public boolean hurtByWater() {
        return true;
    }

    protected boolean canClimb() {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return EndSounds.ENTITY_MOTU_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return EndSounds.ENTITY_MOTU_HURT;
    }

    protected SoundEvent getDeathSound() {
        return EndSounds.ENTITY_MOTU_DEATH;
    }
}