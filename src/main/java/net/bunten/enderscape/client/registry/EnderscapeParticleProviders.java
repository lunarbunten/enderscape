package net.bunten.enderscape.client.registry;

import net.bunten.enderscape.client.particle.*;
import net.bunten.enderscape.mixin.LevelLightEngineAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.FlyTowardsPositionParticle;
import net.minecraft.client.particle.TrialSpawnerDetectionParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.lighting.LightEngine;

import static net.bunten.enderscape.registry.EnderscapeParticles.*;

@Environment(EnvType.CLIENT)
public class EnderscapeParticleProviders {

    static {
        ParticleProviderRegistry.getInstance().register(BLINKLIGHT_SPORES, BlinklightSporesParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(CELESTIAL_SPORES, CelestialSporesParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(CHORUS_POLLEN, ChorusPollenParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(CORRUPT_SPORES, CorruptSporesParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(DASH_JUMP_SHOCKWAVE, DashJumpShockwaveParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(DASH_JUMP_SPARKS, DashJumpSparksParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(DRIFT_JELLY_DRIPPING, DriftJellyDrippingParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(ENDER_PEARL, EnderPearlParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(END_PORTAL_STARS, EndPortalStarParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(END_TRIAL_SPAWNER_DETECTION, TrialSpawnerDetectionParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(END_TRIAL_SPAWNER_EXHALE, EndTrialSpawnerExhaleParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(END_VAULT_CONNECTION, FlyTowardsPositionParticle.VaultConnectionProvider::new);
        ParticleProviderRegistry.getInstance().register(ENTITY_EFFECTED_BY_MAGNIA, EntityAffectedByMagniaParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(MAGNIA_BLISTERING, MagniaBlisteringParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(MAGNIA_SPROUT, MagniaSproutParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(MIRROR_TELEPORT_IN, MirrorTeleportInParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(MIRROR_TELEPORT_OUT, MirrorTeleportOutParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(NEBULITE_ORE, NebuliteOreParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(RUSTLE_SLEEPING_BUBBLE, RustleSleepingBubbleParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(RUSTLE_SLEEPING_BUBBLE_POP, RustleSleepingBubblePopParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(VEILED_LEAVES, VeiledLeavesParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(VOID_FIRE_FLAME, FlameParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(VOID_POOF, VoidPoofParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(VOID_STARS, VoidStarParticle.Provider::new);
    }

    public static int scaledLight(float factor) {
        return (15 << 20) | ((int) (15 * factor) << 4);
    }

    public static int blockLightAt(LevelAccessor level, BlockPos pos, int fallback) {
        LightEngine<?, ?> blockEngine = ((LevelLightEngineAccessor) level.getLightEngine()).getBlockEngine();
        return blockEngine != null ? blockEngine.getLightValue(pos) : fallback;
    }
}