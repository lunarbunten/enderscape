package net.enderscape.registry;

import net.enderscape.Enderscape;
import net.enderscape.client.particles.DragonEmberParticle;
import net.enderscape.client.particles.DrippingJellyParticle;
import net.enderscape.client.particles.EndSporesParticle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.registry.Registry;

public class EndParticles {

    public static final DefaultParticleType CELESTIAL_SPORES = register("celestial_spores");
    public static final DefaultParticleType DRAGON_EMBER = register("dragon_ember");
    public static final DefaultParticleType DRIPPING_JELLY = register("dripping_jelly");

    private static DefaultParticleType register(String name) {
        return Registry.register(Registry.PARTICLE_TYPE, Enderscape.id(name), FabricParticleTypes.simple(false));
    }

    @Environment(EnvType.CLIENT)
    public static void init() {
        ParticleFactoryRegistry.getInstance().register(CELESTIAL_SPORES, EndSporesParticle.DefaultFactory::new);
        ParticleFactoryRegistry.getInstance().register(DRAGON_EMBER, DragonEmberParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(DRIPPING_JELLY, DrippingJellyParticle.DefaultFactory::new);
    }
}