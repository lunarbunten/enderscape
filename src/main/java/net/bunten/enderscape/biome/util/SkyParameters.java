package net.bunten.enderscape.biome.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.bunten.enderscape.registry.EnderscapeBiomes;
import net.bunten.enderscape.registry.EnderscapeRegistries;
import net.bunten.enderscape.util.RGBA;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;

public record SkyParameters(ResourceLocation location, int nebulaColor, float nebulaAlpha, int starColor, float starAlpha, float fogStartDensity, float fogEndDensity) {

    public static final RGBA DEFAULT_NEBULA_COLOR = new RGBA(EnderscapeBiomes.DEFAULT_NEBULA_COLOR, EnderscapeBiomes.DEFAULT_NEBULA_ALPHA);
    public static final RGBA DEFAULT_STAR_COLOR = new RGBA(EnderscapeBiomes.DEFAULT_STAR_COLOR, EnderscapeBiomes.DEFAULT_STAR_ALPHA);
    public static final float DEFAULT_FOG_START_DENSITY = 1.0F;
    public static final float DEFAULT_FOG_END_DENSITY = 1.0F;

    public static final Codec<SkyParameters> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        (ResourceLocation.CODEC.fieldOf("biome")).forGetter(config -> config.location),
        (Codec.intRange(0x000000, 0xFFFFFF).fieldOf("nebula_color")).forGetter(config -> config.nebulaColor),
        (Codec.floatRange(0, 1).fieldOf("nebula_alpha")).forGetter(config -> config.nebulaAlpha),
        (Codec.intRange(0x000000, 0xFFFFFF).fieldOf("star_color")).forGetter(config -> config.starColor),
        (Codec.floatRange(0, 1).fieldOf("star_alpha")).forGetter(config -> config.starAlpha),
            (Codec.floatRange(1, 4).fieldOf("fog_start_density")).forGetter(config -> config.fogStartDensity),
            (Codec.floatRange(1, 4).fieldOf("fog_end_density")).forGetter(config -> config.fogEndDensity)
    ).apply(instance, SkyParameters::new));

    public RGBA nebulaRGBA() {
        return new RGBA(nebulaColor, nebulaAlpha);
    }

    public RGBA starRGBA() {
        return new RGBA(starColor, starAlpha);
    }

    public static Optional<SkyParameters> getSkyParametersFor(Holder<Biome> biomeHolder) {
        if (biomeHolder == null) return Optional.empty();

        Registry<SkyParameters> lookup = Minecraft.getInstance().level.registryAccess().registryOrThrow(EnderscapeRegistries.SKY_PARAMETERS_KEY);

        Optional<ResourceKey<Biome>> biomeResourceKey = biomeHolder.unwrapKey();
        if (biomeResourceKey.isPresent()) {
            SkyParameters value = lookup.get(biomeResourceKey.get().location());
            return value != null ? Optional.of(value) : Optional.empty();
        }

        return Optional.empty();
    }
}