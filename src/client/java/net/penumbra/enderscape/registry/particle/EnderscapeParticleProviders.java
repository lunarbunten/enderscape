package net.penumbra.enderscape.registry.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import net.penumbra.enderscape.particle.*;
import net.penumbra.enderscape.particle.GlowParticle;
import net.penumbra.enderscape.particle.SnowflakeParticle;
import org.joml.Vector3f;

import static net.penumbra.enderscape.registry.block.EnderscapeFluids.VOID_LACHRYMA;
import static net.penumbra.enderscape.registry.particle.EnderscapeParticles.*;

@Environment(EnvType.CLIENT)
public class EnderscapeParticleProviders {

    public static final Vector3f DRIFT_JELLY_PARTICLE_COLOR = ARGB.vector3fFromRGB24(0x57C1A5);
    public static final Vector3f VOID_LACHRYMA_PARTICLE_COLOR = new Vector3f(0.18F, 0.0F, 0.27F);

    static {
        ParticleProviderRegistry.getInstance().register(BLINKLIGHT_SPORES, BlinklightSporesParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(CELESTIAL_SPORES, CelestialSporesParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(CHORUS_POLLEN, ChorusPollenParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(DASH_JUMP_SHOCKWAVE, DashJumpShockwaveParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(DASH_JUMP_SPARKS, DashJumpSparksParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(DRIPPING_VOID_LACHRYMA, DrippingVoidLachrymaProvider::new);
        ParticleProviderRegistry.getInstance().register(ENDER_PEARL_ENDERSCAPE, EnderPearlParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(ENDER_PEARL_VANILLA, VanillaEnderPearlParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(END_PORTAL_STARS, EndPortalStarParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(END_TRIAL_SPAWNER_DETECTION, TrialSpawnerDetectionParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(END_TRIAL_SPAWNER_EXHALE, EndTrialSpawnerExhaleParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(END_VAULT_CONNECTION, FlyTowardsPositionParticle.VaultConnectionProvider::new);
        ParticleProviderRegistry.getInstance().register(ENTITY_EFFECTED_BY_MAGNIA, EntityAffectedByMagniaParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(FALLING_DRIFT_JELLY, DriftJellyFallingParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(FALLING_VOID_LACHRYMA, FallingVoidLachrymaProvider::new);
        ParticleProviderRegistry.getInstance().register(GLOW, GlowParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(GLOW_CHARGING, GlowParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(LANDING_DRIFT_JELLY, DriftJellyLandParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(LANDING_VOID_LACHRYMA, LandingVoidLachrymaProvider::new);
        ParticleProviderRegistry.getInstance().register(MAGNIA_BLISTERING, MagniaBlisteringParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(MAGNIA_SPROUT, MagniaSproutParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(MIRROR_TELEPORT_IN, MirrorTeleportInParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(MIRROR_TELEPORT_OUT, MirrorTeleportOutParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(NEBULITE_ORE, NebuliteOreParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(RUSTLE_CONVERTING, MagniaBlisteringParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(RUSTLE_SLEEPING_BUBBLE, RustleSleepingBubbleParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(RUSTLE_SLEEPING_BUBBLE_POP, RustleSleepingBubblePopParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(SNOWFLAKE, SnowflakeParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(VEILED_LEAVES, VeiledLeavesParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(VOID_ENTITY, EndermanVoidParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(VOID_ENTITY_DESTRUCTION, VoidEntityDestructionParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(VOID_POOF, VoidPoofParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(VOID_SPLASH, SplashParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(VOID_STARS, VoidStarParticle.Provider::new);
    }

    public static class DrippingVoidLachrymaProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public DrippingVoidLachrymaProvider(final SpriteSet sprite) {
            this.sprite = sprite;
        }

        public Particle createParticle(
                final SimpleParticleType options,
                final ClientLevel level,
                final double x,
                final double y,
                final double z,
                final double xAux,
                final double yAux,
                final double zAux,
                final RandomSource random
        ) {
            return new DripParticle.CoolingDripHangParticle(level, x, y, z, VOID_LACHRYMA, FALLING_VOID_LACHRYMA, sprite.get(random));
        }
    }

    public static class FallingVoidLachrymaProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public FallingVoidLachrymaProvider(final SpriteSet sprite) {
            this.sprite = sprite;
        }

        public Particle createParticle(
                final SimpleParticleType options,
                final ClientLevel level,
                final double x,
                final double y,
                final double z,
                final double xAux,
                final double yAux,
                final double zAux,
                final RandomSource random
        ) {
            DripParticle particle = new DripParticle.FallAndLandParticle(level, x, y, z, VOID_LACHRYMA, LANDING_VOID_LACHRYMA, sprite.get(random));
            particle.setColor(
                    VOID_LACHRYMA_PARTICLE_COLOR.x(),
                    VOID_LACHRYMA_PARTICLE_COLOR.y(),
                    VOID_LACHRYMA_PARTICLE_COLOR.z()
            );
            return particle;
        }
    }

    public static class LandingVoidLachrymaProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public LandingVoidLachrymaProvider(final SpriteSet sprite) {
            this.sprite = sprite;
        }

        public Particle createParticle(
                final SimpleParticleType options,
                final ClientLevel level,
                final double x,
                final double y,
                final double z,
                final double xAux,
                final double yAux,
                final double zAux,
                final RandomSource random
        ) {
            DripParticle particle = new DripParticle.DripLandParticle(level, x, y, z, VOID_LACHRYMA, sprite.get(random));
            particle.setColor(
                    VOID_LACHRYMA_PARTICLE_COLOR.x(),
                    VOID_LACHRYMA_PARTICLE_COLOR.y(),
                    VOID_LACHRYMA_PARTICLE_COLOR.z()
            );
            return particle;
        }
    }
}