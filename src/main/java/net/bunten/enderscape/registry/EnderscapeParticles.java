package net.bunten.enderscape.registry;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.particle.DashJumpShockwaveParticleOptions;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;

public class EnderscapeParticles {

    public static final SimpleParticleType ALLURING_MAGNIA = register("alluring_magnia", true);
    public static final SimpleParticleType BLINKLIGHT_SPORES = register("blinklight_spores", false);
    public static final SimpleParticleType CELESTIAL_SPORES = register("celestial_spores", false);
    public static final SimpleParticleType CHORUS_POLLEN = register("chorus_pollen", false);
    public static final SimpleParticleType CORRUPT_SPORES = register("corrupt_spores", false);

    public static final ParticleType<DashJumpShockwaveParticleOptions> DASH_JUMP_SHOCKWAVE = register(
            "dash_jump_shockwave", true, type -> DashJumpShockwaveParticleOptions.CODEC, type -> DashJumpShockwaveParticleOptions.STREAM_CODEC
    );

    public static final SimpleParticleType DASH_JUMP_SPARKS = register("dash_jump_sparks", true);
    public static final SimpleParticleType DRIFT_JELLY_DRIPPING = register("drift_jelly_dripping", true);
    public static final SimpleParticleType ENDER_PEARL = register("ender_pearl", true);
    public static final SimpleParticleType END_TRIAL_SPAWNER_DETECTION = register("end_trial_spawner_detection", true);
    public static final SimpleParticleType END_TRIAL_SPAWNER_EXHALE = register("end_trial_spawner_exhale", true);
    public static final SimpleParticleType END_VAULT_CONNECTION = register("end_vault_connection", true);
    public static final SimpleParticleType END_PORTAL_STARS = register("end_portal_stars", false);
    public static final SimpleParticleType MIRROR_TELEPORT_IN = register("mirror_teleport_in", true);
    public static final SimpleParticleType MIRROR_TELEPORT_OUT = register("mirror_teleport_out", true);
    public static final SimpleParticleType NEBULITE_ORE = register("nebulite_ore", true);
    public static final SimpleParticleType REPULSIVE_MAGNIA = register("repulsive_magnia", true);
    public static final SimpleParticleType RUSTLE_SLEEPING_BUBBLE = register("rustle_sleeping_bubble", true);
    public static final SimpleParticleType RUSTLE_SLEEPING_BUBBLE_POP = register("rustle_sleeping_bubble_pop", true);
    public static final SimpleParticleType VEILED_LEAVES = register("veiled_leaves", false);
    public static final SimpleParticleType VOID_POOF = register("void_poof", true);
    public static final SimpleParticleType VOID_STARS = register("void_stars", false);

    private static SimpleParticleType register(String name, boolean alwaysSpawn) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, Enderscape.id(name), FabricParticleTypes.simple(alwaysSpawn));
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