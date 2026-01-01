package net.bunten.enderscape.client.registry;

import net.bunten.enderscape.client.particle.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.FlyTowardsPositionParticle;
import net.minecraft.client.particle.TrialSpawnerDetectionParticle;

import static net.bunten.enderscape.registry.EnderscapeParticles.*;

@Environment(EnvType.CLIENT)
public class EnderscapeParticleProviders {

    static {
        ParticleFactoryRegistry.getInstance().register(BLINKLIGHT_SPORES, BlinklightSporesParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(CELESTIAL_SPORES, CelestialSporesParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(CHORUS_POLLEN, ChorusPollenParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(CORRUPT_SPORES, CorruptSporesParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(DASH_JUMP_SHOCKWAVE, DashJumpShockwaveParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(DASH_JUMP_SPARKS, DashJumpSparksParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(DRIFT_JELLY_DRIPPING, DriftJellyDrippingParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(ENDER_PEARL, EnderPearlParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(END_PORTAL_STARS, EndPortalStarParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(END_TRIAL_SPAWNER_DETECTION, TrialSpawnerDetectionParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(END_TRIAL_SPAWNER_EXHALE, EndTrialSpawnerExhaleParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(END_VAULT_CONNECTION, FlyTowardsPositionParticle.VaultConnectionProvider::new);
        ParticleFactoryRegistry.getInstance().register(ENTITY_EFFECTED_BY_MAGNIA, EntityAffectedByMagniaParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(MAGNIA_BLISTERING, MagniaBlisteringParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(MAGNIA_SPROUT, MagniaSproutParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(MIRROR_TELEPORT_IN, MirrorTeleportInParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(MIRROR_TELEPORT_OUT, MirrorTeleportOutParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(NEBULITE_ORE, NebuliteOreParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(RUSTLE_SLEEPING_BUBBLE, RustleSleepingBubbleParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(RUSTLE_SLEEPING_BUBBLE_POP, RustleSleepingBubblePopParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(VEILED_LEAVES, VeiledLeavesParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(VOID_FIRE_FLAME, FlameParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(VOID_POOF, VoidPoofParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(VOID_STARS, VoidStarParticle.Provider::new);
    }
}
