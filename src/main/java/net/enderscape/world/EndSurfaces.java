package net.enderscape.world;

import net.enderscape.Enderscape;
import net.enderscape.world.surface.CelestialPlainsSurfaceBuilder;
import net.enderscape.world.surface.EndSurfaceBuilder;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceConfig;

public class EndSurfaces {

    public static ConfiguredSurfaceBuilder<?> END = register("end", register("end", new EndSurfaceBuilder()).withConfig(SurfaceBuilder.END_CONFIG));
    public static ConfiguredSurfaceBuilder<?> CELESTIAL = register("celestial", register("celestial", new CelestialPlainsSurfaceBuilder()).withConfig(SurfaceBuilder.END_CONFIG));

    private static <C extends SurfaceConfig, F extends SurfaceBuilder<C>> F register(String name, F builder) {
        return Registry.register(Registry.SURFACE_BUILDER, Enderscape.id(name), builder);
    }

    private static <SC extends SurfaceConfig> ConfiguredSurfaceBuilder<SC> register(String name, ConfiguredSurfaceBuilder<SC> builder) {
        return BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_SURFACE_BUILDER, Enderscape.id(name), builder);
    }

    public static void init() {
    }
}