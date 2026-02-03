package net.bunten.enderscape.registry;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.particle.DashJumpShockwaveParticleOptions;
import net.bunten.enderscape.particle.MagniaParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;
import java.util.function.Supplier;

public class EnderscapeParticles {

    public static final Supplier<SimpleParticleType> BLINKLIGHT_SPORES = register("blinklight_spores", false);
    public static final Supplier<SimpleParticleType> CELESTIAL_SPORES = register("celestial_spores", false);
    public static final Supplier<SimpleParticleType> CHORUS_POLLEN = register("chorus_pollen", false);
    public static final Supplier<SimpleParticleType> CORRUPT_SPORES = register("corrupt_spores", false);

    public static final Supplier<ParticleType<DashJumpShockwaveParticleOptions>> DASH_JUMP_SHOCKWAVE = register(
            "dash_jump_shockwave", true, type -> DashJumpShockwaveParticleOptions.CODEC, type -> DashJumpShockwaveParticleOptions.STREAM_CODEC
    );

    public static final Supplier<SimpleParticleType> DASH_JUMP_SPARKS = register("dash_jump_sparks", true);
    public static final Supplier<SimpleParticleType> DRIFT_JELLY_DRIPPING = register("drift_jelly_dripping", true);
    public static final Supplier<SimpleParticleType> ENDER_PEARL = register("ender_pearl", true);
    public static final Supplier<SimpleParticleType> END_TRIAL_SPAWNER_DETECTION = register("end_trial_spawner_detection", true);
    public static final Supplier<SimpleParticleType> END_TRIAL_SPAWNER_EXHALE = register("end_trial_spawner_exhale", true);
    public static final Supplier<SimpleParticleType> END_VAULT_CONNECTION = register("end_vault_connection", true);
    public static final Supplier<SimpleParticleType> END_PORTAL_STARS = register("end_portal_stars", false);

    public static final Supplier<ParticleType<MagniaParticleOptions>> ENTITY_EFFECTED_BY_MAGNIA = register("entity_effected_by_magnia", false, MagniaParticleOptions::codec, MagniaParticleOptions::streamCodec);
    public static final Supplier<SimpleParticleType> MAGNIA_BLISTERING = register("magnia_blistering", true);
    public static final Supplier<ParticleType<MagniaParticleOptions>> MAGNIA_SPROUT = register("magnia_sprout", true, MagniaParticleOptions::codec, MagniaParticleOptions::streamCodec);

    public static final Supplier<SimpleParticleType> MIRROR_TELEPORT_IN = register("mirror_teleport_in", true);
    public static final Supplier<SimpleParticleType> MIRROR_TELEPORT_OUT = register("mirror_teleport_out", true);
    public static final Supplier<SimpleParticleType> NEBULITE_ORE = register("nebulite_ore", true);
    public static final Supplier<SimpleParticleType> RUSTLE_SLEEPING_BUBBLE = register("rustle_sleeping_bubble", true);
    public static final Supplier<SimpleParticleType> RUSTLE_SLEEPING_BUBBLE_POP = register("rustle_sleeping_bubble_pop", true);
    public static final Supplier<SimpleParticleType> VEILED_LEAVES = register("veiled_leaves", false);

    public static final Supplier<SimpleParticleType> VOID_FIRE_FLAME = register("void_fire_flame", false);
    public static final Supplier<SimpleParticleType> VOID_POOF = register("void_poof", true);
    public static final Supplier<SimpleParticleType> VOID_STARS = register("void_stars", false);

    private static Supplier<SimpleParticleType> register(String name, boolean alwaysSpawn) {
        return RegistryHelper.register(BuiltInRegistries.PARTICLE_TYPE, Enderscape.id(name), () -> new SimpleParticleType(alwaysSpawn));
    }

    private static <T extends ParticleOptions> Supplier<ParticleType<T>> register(String name, boolean overrideLimiter, Function<ParticleType<T>, MapCodec<T>> codecFunction, Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> streamCodecFunction) {
        return RegistryHelper.register(BuiltInRegistries.PARTICLE_TYPE, Enderscape.id(name), () -> new ParticleType<T>(overrideLimiter) {

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