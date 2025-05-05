package net.bunten.enderscape.block.properties;

import com.mojang.serialization.Codec;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;

import static net.bunten.enderscape.registry.EnderscapeBlockSounds.*;
import static net.bunten.enderscape.registry.tag.EnderscapeBlockTags.BLOCKS_ALLURING_MAGNIA_SPROUTS;
import static net.bunten.enderscape.registry.tag.EnderscapeBlockTags.BLOCKS_REPULSIVE_MAGNIA_SPROUTS;

public enum MagniaType implements StringRepresentable {
    ALLURING("alluring", EnderscapeParticles.ALLURING_MAGNIA, ALLURING_MAGNIA_IDLE, ALLURING_MAGNIA_SPROUT_MOVE, ALLURING_MAGNIA_SPROUT_OVERHEAT, ALLURING_MAGNIA_SPROUT_POWER_OFF, ALLURING_MAGNIA_SPROUT_POWER_ON, BLOCKS_ALLURING_MAGNIA_SPROUTS),
    REPULSIVE("repulsive", EnderscapeParticles.REPULSIVE_MAGNIA, REPULSIVE_MAGNIA_IDLE, REPULSIVE_MAGNIA_SPROUT_MOVE, REPULSIVE_MAGNIA_SPROUT_OVERHEAT, REPULSIVE_MAGNIA_SPROUT_POWER_OFF, REPULSIVE_MAGNIA_SPROUT_POWER_ON, BLOCKS_REPULSIVE_MAGNIA_SPROUTS);

    public static final Codec<MagniaType> CODEC = StringRepresentable.fromEnum(MagniaType::values);

    private final String name;
    private final SoundEvent hum;
    private final SoundEvent move;
    private final SoundEvent overheat;
    private final SoundEvent powerOff;
    private final SoundEvent powerOn;
    private final TagKey<Block> blockedByTag;
    private ParticleOptions particle;

    MagniaType(String name, ParticleOptions particle, SoundEvent hum, SoundEvent move, SoundEvent overheat, SoundEvent powerOff, SoundEvent powerOn, TagKey<Block> blockedByTag) {
        this.name = name;
        this.particle = particle;
        this.hum = hum;
        this.move = move;
        this.overheat = overheat;
        this.powerOff = powerOff;
        this.powerOn = powerOn;
        this.blockedByTag = blockedByTag;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public ParticleOptions getParticle() {
        return particle;
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

    public TagKey<Block> getBlockedByTag() {
        return blockedByTag;
    }
}