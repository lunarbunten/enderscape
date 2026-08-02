package net.penumbra.enderscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.block.state.PurifyingPhase;
import net.penumbra.enderscape.block.state.StateProperties;
import net.penumbra.enderscape.particle.GlowParticleOptions;
import net.penumbra.enderscape.registry.entity.EnderscapeMobEffects;
import net.penumbra.enderscape.registry.sound.EnderscapeBlockSounds;
import net.penumbra.enderscape.registry.tag.EnderscapeEntityTags;

import java.util.List;
import java.util.function.Predicate;

public interface RandomlyPurifiesEntities {

    Predicate<Entity> BASE_ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(EntitySelector.LIVING_ENTITY_STILL_ALIVE);
    Predicate<LivingEntity> CAN_RECEIVE_PURIFICATION = entity -> BASE_ENTITY_PREDICATE.test(entity) && !entity.is(EnderscapeEntityTags.VOID);
    EnumProperty<PurifyingPhase> PHASE = StateProperties.PURIFYING_PHASE;

    int DEFAULT_CHARGING_DURATION = 2 * 20;
    int DEFAULT_ACTIVATED_DURATION = 8 * 20;
    int DEFAULT_COOLDOWN_DURATION = 6 * 20;

    default boolean hasChargingPhase() {
        return true;
    }

    default IntProvider chargingDuration() {
        return ConstantInt.of(DEFAULT_CHARGING_DURATION);
    }

    default IntProvider activatedDuration() {
        return ConstantInt.of(DEFAULT_ACTIVATED_DURATION);
    }

    default IntProvider cooldownDuration() {
        return ConstantInt.of(DEFAULT_COOLDOWN_DURATION);
    }

    default SoundEvent chargeSound() {
        return EnderscapeBlockSounds.BULB_FLOWER_CHARGE;
    }

    default SoundEvent activateSound() {
        return EnderscapeBlockSounds.BULB_FLOWER_ACTIVATE;
    }

    default SoundEvent sparkSound() {
        return EnderscapeBlockSounds.BULB_FLOWER_SPARK;
    }

    default SoundEvent deactivateSound() {
        return EnderscapeBlockSounds.BULB_FLOWER_DEACTIVATE;
    }

    default void purifyRandomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (hasChargingPhase()) {
            setChargingTick(state, level, pos, random);
        } else {
            setActivatedTick(state, level, pos, random);
        }
    }

    default void purifyScheduledTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        switch (state.getValue(PHASE)) {
            case CHARGING -> setActivatedTick(state, level, pos, random);
            case ACTIVE -> setCooldownTick(state, level, pos, random);
            case COOLDOWN -> setInactiveTick(state, level, pos);
        }
    }

    default void setChargingTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        setTemporaryState(level, pos, state, PurifyingPhase.CHARGING, chargingDuration().sample(random));
        playServersideChargingEffects(level, pos);
    }

    default void setActivatedTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        List<LivingEntity> entities = purifiableEntities(level, pos);
        int duration = activatedDuration().sample(random);

        playServersideActivateEffects(level, pos, random);
        setTemporaryState(level, pos, state, PurifyingPhase.ACTIVE, duration);
        entities.forEach(entity -> addPurification(entity, purificationEffectDuration(duration)));
    }

    default void setInactiveTick(BlockState state, ServerLevel level, BlockPos pos) {
        level.setBlock(pos, state.setValue(PHASE, PurifyingPhase.INACTIVE), 2);
    }

    default void setCooldownTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        playServersideDeactivateSound(level, pos);
        setTemporaryState(level, pos, state, PurifyingPhase.COOLDOWN, cooldownDuration().sample(random));
    }

    default void playServersideChargingEffects(ServerLevel level, BlockPos pos) {
        level.playSound(null, pos, chargeSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    default void playServersideActivateEffects(ServerLevel level, BlockPos pos, RandomSource random) {
        playServersideActivateParticles(level, pos, random);
        playServersideActivateSound(level, pos);
    }

    default void playServersideActivateSound(ServerLevel level, BlockPos pos) {
        level.playSound(null, pos, activateSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    default void playServersideActivateParticles(ServerLevel level, BlockPos pos, RandomSource random) {
        Vec3 center = Vec3.atCenterOf(pos);
        level.sendParticles(GlowParticleOptions.DEFAULT, center.x(), center.y() + 0.35, center.z(), 8, 0, 0, 0, 0.1);
    }

    default void playServersideDeactivateSound(ServerLevel level, BlockPos pos) {
        level.playSound(null, pos, deactivateSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    default void purifyAnimateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (PurifyingPhase.charging(state)) {
            playClientsideChargingParticle(level, pos, random);
        } else if (PurifyingPhase.active(state) && random.nextFloat() <= 0.25F) {
            playActivatedClientsideEffects(level, pos, random);
        }
    }

    default void playActivatedClientsideEffects(Level level, BlockPos pos, RandomSource random) {
        playClientsideActivatedParticle(level, pos, random);
        playClientsideActivatedSound(level, pos);
    }

    default void playClientsideChargingParticle(Level level, BlockPos pos, RandomSource random) {
        Vec3 center = Vec3.atCenterOf(pos);

        double maxOffset = 0.8;
        double xo = Mth.clamp(random.nextGaussian() * maxOffset, -maxOffset, maxOffset);
        double zo = Mth.clamp(random.nextGaussian() * maxOffset, -maxOffset, maxOffset);

        Vec3 position = center.add(xo, 0.6, zo);

        double factor = 0.35;
        double xd = (center.x() - position.x()) * factor;
        double zd = (center.z() - position.z()) * factor;

        level.addParticle(GlowParticleOptions.CHARGING, position.x(), position.y(), position.z(), xd, -0.125, zd);
    }

    default void playClientsideActivatedParticle(Level level, BlockPos pos, RandomSource random) {
        Vec3 center = Vec3.atCenterOf(pos);

        double maxOffset = 0.2;
        double xo = Mth.clamp(random.nextGaussian() * maxOffset, -maxOffset, maxOffset);
        double zo = Mth.clamp(random.nextGaussian() * maxOffset, -maxOffset, maxOffset);

        Vec3 position = center.add(xo, 0.45, zo);

        double maxMovement = 0.05;
        double xd = Mth.clamp(random.nextGaussian() * maxMovement, -maxMovement, maxMovement);
        double yd = 0.05 + random.nextDouble() * 0.02;
        double zd = Mth.clamp(random.nextGaussian() * maxMovement, -maxMovement, maxMovement);

        level.addParticle(GlowParticleOptions.DEFAULT, position.x(), position.y(), position.z(), xd, yd, zd);
    }

    default void playClientsideActivatedSound(Level level, BlockPos pos) {
        Vec3 center = Vec3.atCenterOf(pos);
        level.playLocalSound(center.x(), center.y(), center.z(), sparkSound(), SoundSource.BLOCKS, 1.0F, 1.0F, true);
    }

    default void setTemporaryState(ServerLevel level, BlockPos pos, BlockState state, PurifyingPhase active, int duration) {
        level.setBlock(pos, state.setValue(PHASE, active), 2);
        level.scheduleTick(pos, state.getBlock(), duration);
    }

    default void addPurification(LivingEntity entity, int duration) {
        entity.addEffect(new MobEffectInstance(EnderscapeMobEffects.VOID_PURIFICATION, duration, 0, true, false, true));
    }

    default List<LivingEntity> purifiableEntities(ServerLevel level, BlockPos pos) {
        return level.getEntitiesOfClass(LivingEntity.class, purificationArea(pos), CAN_RECEIVE_PURIFICATION);
    }

    default AABB purificationArea(BlockPos pos) {
        return new AABB(pos).inflate(4, 2, 4);
    }

    default int purificationEffectDuration(int duration) {
        return duration;
    }
}