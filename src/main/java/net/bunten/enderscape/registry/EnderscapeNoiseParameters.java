package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.ArrayList;
import java.util.List;

public class EnderscapeNoiseParameters {

    public static final List<ResourceKey<NormalNoise.NoiseParameters>> NOISE_PARAMETERS = new ArrayList<>();

    public static final ResourceKey<NormalNoise.NoiseParameters> CELESTIAL_SURFACE = createKey("celestial_surface");
    public static final ResourceKey<NormalNoise.NoiseParameters> CORRUPTION_CEILING = createKey("corruption_ceiling");
    public static final ResourceKey<NormalNoise.NoiseParameters> VEILED_SURFACE = createKey("veiled_surface");

    private static ResourceKey<NormalNoise.NoiseParameters> createKey(String string) {
        ResourceKey<NormalNoise.NoiseParameters> key = ResourceKey.create(Registries.NOISE, Enderscape.id(string));
        NOISE_PARAMETERS.add(key);
        return key;
    }

    public static void bootstrap(BootstrapContext<NormalNoise.NoiseParameters> context) {
        register(context, CELESTIAL_SURFACE, new NormalNoise.NoiseParameters(-4, 1.0D, 0.5D, 1.0D));
        register(context, CORRUPTION_CEILING, new NormalNoise.NoiseParameters(-4, 1.0D, 0.5D, 1.0D));
        register(context, VEILED_SURFACE, new NormalNoise.NoiseParameters(-5, 1.0D, 0.5D, 1.0D));
    }

    private static void register(BootstrapContext<NormalNoise.NoiseParameters> context, ResourceKey<NormalNoise.NoiseParameters> key, NormalNoise.NoiseParameters object) {
        context.register(key, object);
    }
}
