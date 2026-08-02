package net.penumbra.enderscape.block.properties;

import com.mojang.serialization.Codec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.block.MagniaSproutBlockEntity;
import net.penumbra.enderscape.entity.magnia.MagniaMovementContext;
import net.penumbra.enderscape.particle.MagniaParticleOptions;

import java.util.function.BiFunction;
import java.util.function.Consumer;

import static net.penumbra.enderscape.registry.sound.EnderscapeBlockSounds.*;

public enum MagniaPolarity implements StringRepresentable {
    ALLURING(
            "alluring",
            MagniaSproutBlockEntity::allureEntity,
            0x8CC9FF,
            MagniaParticleOptions.SPROUT_ALLURING,
            (start, end) -> end,
            (start, end) -> start.subtract(end).scale(0.2),
            UniformFloat.of(-0.25F, 0.25F),
            MagniaParticleOptions.ENTITY_ALLURED,
            ALLURING_MAGNIA_IDLE,
            ALLURING_MAGNIA_SPROUT_MOVE,
            ALLURING_MAGNIA_SPROUT_OVERHEAT,
            ALLURING_MAGNIA_SPROUT_POWER_OFF,
            ALLURING_MAGNIA_SPROUT_POWER_ON
    ),
    REPULSIVE(
            "repulsive",
            MagniaSproutBlockEntity::repulseEntity,
            0xFF9E9B,
            MagniaParticleOptions.SPROUT_REPULSIVE,
            (start, end) -> start,
            (start, end) -> start.subtract(end).scale(-0.2),
            UniformFloat.of(-0.25F, 0.25F),
            MagniaParticleOptions.ENTITY_REPULSED,
            REPULSIVE_MAGNIA_IDLE,
            REPULSIVE_MAGNIA_SPROUT_MOVE,
            REPULSIVE_MAGNIA_SPROUT_OVERHEAT,
            REPULSIVE_MAGNIA_SPROUT_POWER_OFF,
            REPULSIVE_MAGNIA_SPROUT_POWER_ON
    );

    public static final Codec<MagniaPolarity> CODEC = StringRepresentable.fromEnum(MagniaPolarity::values);

    private final String name;
    private final Consumer<MagniaMovementContext<?>> movement;
    private final int rangeHitboxColor;

    private final MagniaParticleOptions sproutParticleOptions;
    private final BiFunction<Vec3, Vec3, Vec3> sproutParticlePosition;
    private final BiFunction<Vec3, Vec3, Vec3> sproutParticleSpeed;
    private final FloatProvider sproutParticleOffset;

    private final MagniaParticleOptions effectParticleOptions;

    private final SoundEvent hum;
    private final SoundEvent move;
    private final SoundEvent overheat;
    private final SoundEvent powerOff;
    private final SoundEvent powerOn;

    MagniaPolarity(
            String name,
            Consumer<MagniaMovementContext<?>> movement,
            int rangeHitboxColor,
            MagniaParticleOptions sproutParticleOptions,
            BiFunction<Vec3, Vec3, Vec3> sproutParticlePosition,
            BiFunction<Vec3, Vec3, Vec3> sproutParticleSpeed,
            FloatProvider sproutParticleOffset,
            MagniaParticleOptions effectParticleOptions,
            SoundEvent hum,
            SoundEvent move,
            SoundEvent overheat,
            SoundEvent powerOff,
            SoundEvent powerOn
    ) {
        this.name = name;
        this.rangeHitboxColor = rangeHitboxColor;
        this.movement = movement;
        this.sproutParticleOptions = sproutParticleOptions;
        this.sproutParticlePosition = sproutParticlePosition;
        this.sproutParticleSpeed = sproutParticleSpeed;
        this.sproutParticleOffset = sproutParticleOffset;
        this.effectParticleOptions = effectParticleOptions;
        this.hum = hum;
        this.move = move;
        this.overheat = overheat;
        this.powerOff = powerOff;
        this.powerOn = powerOn;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public Consumer<MagniaMovementContext<?>> getMovement() {
        return movement;
    }

    public int getRangeHitboxColor() {
        return rangeHitboxColor;
    }

    public MagniaParticleOptions getSproutParticleOptions() {
        return sproutParticleOptions;
    }

    public MagniaParticleOptions getEffectParticleOptions() {
        return effectParticleOptions;
    }

    public BiFunction<Vec3, Vec3, Vec3> getSproutParticlePosition() {
        return sproutParticlePosition;
    }

    public BiFunction<Vec3, Vec3, Vec3> getSproutParticleSpeed() {
        return sproutParticleSpeed;
    }

    public FloatProvider getSproutParticleOffset() {
        return sproutParticleOffset;
    }

    public SoundEvent getHumSound() {
        return hum;
    }

    public SoundEvent getMoveSound() {
        return move;
    }

    public SoundEvent getOverheatSound() {
        return overheat;
    }

    public SoundEvent getPowerOffSound() {
        return powerOff;
    }

    public SoundEvent getPowerOnSound() {
        return powerOn;
    }
}