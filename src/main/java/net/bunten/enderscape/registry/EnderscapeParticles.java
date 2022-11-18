package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.particles.AmbientParticle;
import net.bunten.enderscape.client.particles.BlinklightSporesParticle;
import net.bunten.enderscape.client.particles.CelestialSporesParticle;
import net.bunten.enderscape.client.particles.ChorusPollenParticle;
import net.bunten.enderscape.client.particles.DrippingJellyParticle;
import net.bunten.enderscape.client.particles.DrippingSpitParticle;
import net.bunten.enderscape.client.particles.NebuliteCloudParticle;
import net.bunten.enderscape.client.particles.RisingNebuliteCloudParticle;
import net.bunten.enderscape.client.particles.VanishingNebuliteCloudParticle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;

public class EnderscapeParticles {

    public static final SimpleParticleType AMBIENT_STARS = register("ambient_stars");
    public static final SimpleParticleType BLINKLIGHT_SPORES = register("blinklight_spores");
    public static final SimpleParticleType CELESTIAL_SPORES = register("celestial_spores");
    public static final SimpleParticleType CHORUS_POLLEN = register("chorus_pollen");
    public static final SimpleParticleType DRIPPING_JELLY = register("dripping_jelly");
    public static final SimpleParticleType DRIPPING_SPIT = register("dripping_spit");

    public static final SimpleParticleType NEBULITE_CLOUD = register("nebulite_cloud");
    public static final SimpleParticleType RISING_NEBULITE_CLOUD = register("rising_nebulite_cloud");
    public static final SimpleParticleType VANISHING_NEBULITE_CLOUD = register("vanishing_nebulite_cloud");

    private static SimpleParticleType register(String name) {
        return Registry.register(Registry.PARTICLE_TYPE, Enderscape.id(name), FabricParticleTypes.simple(false));
    }

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        ParticleFactoryRegistry.getInstance().register(AMBIENT_STARS, AmbientParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(BLINKLIGHT_SPORES, BlinklightSporesParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(CELESTIAL_SPORES, CelestialSporesParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(CHORUS_POLLEN, ChorusPollenParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(DRIPPING_JELLY, DrippingJellyParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(DRIPPING_SPIT, DrippingSpitParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(NEBULITE_CLOUD, NebuliteCloudParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(RISING_NEBULITE_CLOUD, RisingNebuliteCloudParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(VANISHING_NEBULITE_CLOUD, VanishingNebuliteCloudParticle.Provider::new);
    }
}