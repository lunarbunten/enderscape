package net.bunten.enderscape.world.biomes;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.jetbrains.annotations.Nullable;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.world.EnderscapeBiomes;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.Biome;

public abstract class EnderscapeBiome {

    public final String name;

    public static final int DEFAULT_SKY_COLOR = 0x1E172B;
    public static final int DEFAULT_FOG_COLOR = 0x15101E;
    public static final int DEFAULT_FOG_DENSITY = 1;

    public static final int DEFAULT_WATER_COLOR = 0x3F76E4;
    public static final int DEFAULT_WATER_FOG_COLOR = 0x50533;

    public static final int DEFAULT_NEBULA_COLOR = 0x805AA8;
    public static final float DEFAULT_NEBULA_ALPHA = 0.05F;

    public static final int DEFAULT_STAR_COLOR = 0xE989FF;
    public static final float DEFAULT_STAR_ALPHA = 0.12F;

    public static final int DEFAULT_LIGHT_COLOR = 0xD3FFDF;
    public static final float DEFAULT_LIGHT_INTENSITY = 0.125F;

    protected int nebulaColor = DEFAULT_NEBULA_COLOR;
    protected float nebulaAlpha = DEFAULT_NEBULA_ALPHA;
    protected int starColor = DEFAULT_STAR_COLOR;
    protected float starAlpha = DEFAULT_STAR_ALPHA;
    protected int lightColor = DEFAULT_LIGHT_COLOR;
    protected float lightIntensity = DEFAULT_LIGHT_INTENSITY;

    public EnderscapeBiome(String name) {
        this.name = name;
    }

    public abstract BCLBiome getBCLBiome();

    public String getName() {
        return name;
    }

    public int getNebulaColor() {
        return nebulaColor;
    }

    public float getNebulaAlpha() {
        return nebulaAlpha;
    }

    public int getStarColor() {
        return starColor;
    }

    public float getStarAlpha() {
        return starAlpha;
    }

    public int getLightColor() {
        return lightColor;
    }

    public float getLightIntensity() {
        return lightIntensity;
    }

    public ResourceLocation getIdentifier() {
        return Enderscape.id(name);
    }

    public BCLBiomeBuilder createBuilder() {
        return BCLBiomeBuilder.start(getIdentifier());
    }

    public SoundEvent createAdditions() {
        return EnderscapeSounds.additions(name);
    }

    public SoundEvent createLoop() {
        return EnderscapeSounds.loop(name);
    }

    public SoundEvent createMood() {
        return EnderscapeSounds.mood(name);
    }

    public Music createMusic() {
        return EnderscapeSounds.biomeMusic(name);
    }

    @Nullable
    public static EnderscapeBiome getBiomeAt(Holder<Biome> holder) {
        ResourceLocation location = holder.unwrapKey().get().location();
        EnderscapeBiome biome = EnderscapeBiomes.BIOME_REGISTRY.get(new ResourceLocation(location.getNamespace(), "enderscape_" + location.getPath()));
        return biome;
    }
}