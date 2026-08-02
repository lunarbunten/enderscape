package net.penumbra.enderscape.registry.particle;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.particle.DashJumpShockwaveParticleOptions;
import net.penumbra.enderscape.particle.GlowParticleOptions;
import net.penumbra.enderscape.particle.MagniaParticleOptions;

import java.util.function.Function;

public class EnderscapeParticles {

    public static final SimpleParticleType BLINKLIGHT_SPORES = register("blinklight_spores", false);
    public static final SimpleParticleType CELESTIAL_SPORES = register("celestial_spores", false);
    public static final SimpleParticleType CHORUS_POLLEN = register("chorus_pollen", false);
    public static final ParticleType<DashJumpShockwaveParticleOptions> DASH_JUMP_SHOCKWAVE = register("dash_jump_shockwave", true, type -> DashJumpShockwaveParticleOptions.CODEC, type -> DashJumpShockwaveParticleOptions.STREAM_CODEC);
    public static final SimpleParticleType DASH_JUMP_SPARKS = register("dash_jump_sparks", true);
    public static final SimpleParticleType FALLING_DRIFT_JELLY = register("falling_drift_jelly", true);
    public static final SimpleParticleType LANDING_DRIFT_JELLY = register("landing_drift_jelly", true);
    public static final SimpleParticleType DRIPPING_VOID_LACHRYMA = register("dripping_void_lachryma", false);
    public static final SimpleParticleType ENDER_PEARL_VANILLA = register("ender_pearl_vanilla", true);
    public static final SimpleParticleType ENDER_PEARL_ENDERSCAPE = register("ender_pearl_enderscape", true);
    public static final SimpleParticleType END_TRIAL_SPAWNER_DETECTION = register("end_trial_spawner_detection", true);
    public static final SimpleParticleType END_TRIAL_SPAWNER_EXHALE = register("end_trial_spawner_exhale", true);
    public static final SimpleParticleType END_VAULT_CONNECTION = register("end_vault_connection", true);
    public static final SimpleParticleType END_PORTAL_STARS = register("end_portal_stars", false);
    public static final ParticleType<MagniaParticleOptions> ENTITY_EFFECTED_BY_MAGNIA = register("entity_effected_by_magnia", false, MagniaParticleOptions::codec, MagniaParticleOptions::streamCodec);
    public static final SimpleParticleType FALLING_VOID_LACHRYMA = register("falling_void_lachryma", false);
    public static final ParticleType<GlowParticleOptions> GLOW = register("glow", false, GlowParticleOptions::codec, GlowParticleOptions::streamCodec);
    public static final ParticleType<GlowParticleOptions> GLOW_CHARGING = register("glow_charging", false, GlowParticleOptions::codec, GlowParticleOptions::streamCodec);
    public static final SimpleParticleType LANDING_VOID_LACHRYMA = register("landing_void_lachryma", false);
    public static final SimpleParticleType MAGNIA_BLISTERING = register("magnia_blistering", true);
    public static final ParticleType<MagniaParticleOptions> MAGNIA_SPROUT = register("magnia_sprout", true, MagniaParticleOptions::codec, MagniaParticleOptions::streamCodec);
    public static final SimpleParticleType MIRROR_TELEPORT_IN = register("mirror_teleport_in", true);
    public static final SimpleParticleType MIRROR_TELEPORT_OUT = register("mirror_teleport_out", true);
    public static final SimpleParticleType NEBULITE_ORE = register("nebulite_ore", true);
    public static final SimpleParticleType RUSTLE_CONVERTING = register("rustle_converting", true);
    public static final SimpleParticleType RUSTLE_SLEEPING_BUBBLE = register("rustle_sleeping_bubble", true);
    public static final SimpleParticleType RUSTLE_SLEEPING_BUBBLE_POP = register("rustle_sleeping_bubble_pop", true);
    public static final SimpleParticleType SNOWFLAKE = register("snowflake", false);
    public static final SimpleParticleType VEILED_LEAVES = register("veiled_leaves", false);
    public static final SimpleParticleType VOID_ENTITY = register("void_entity", false);
    public static final SimpleParticleType VOID_ENTITY_DESTRUCTION = register("void_entity_destruction", true);
    public static final SimpleParticleType VOID_POOF = register("void_poof", true);
    public static final SimpleParticleType VOID_SPLASH = register("void_splash", false);
    public static final SimpleParticleType VOID_STARS = register("void_stars", false);

    private static SimpleParticleType register(String name, boolean overrideLimiter) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, Enderscape.id(name), FabricParticleTypes.simple(overrideLimiter));
    }

    private static <T extends ParticleOptions> ParticleType<T> register(String name, boolean overrideLimiter, Function<ParticleType<T>, MapCodec<T>> codecFunction, Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> streamCodecFunction) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, Enderscape.id(name), new ParticleType<T>(overrideLimiter) {

            @Override
            public MapCodec<T> codec() {
                return codecFunction.apply(this);
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
                return streamCodecFunction.apply(this);
            }
        });
    }
}