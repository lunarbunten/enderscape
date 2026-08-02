package net.penumbra.enderscape.references;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.Fluid;
import net.penumbra.enderscape.Enderscape;

public class EnderscapeFluidIds {
    public static final ResourceKey<Fluid> FLOWING_VOID_LACHRYMA = of("flowing_void_lachryma");
    public static final ResourceKey<Fluid> VOID_LACHRYMA = of("void_lachryma");

    private static ResourceKey<Fluid> of(String name) {
        return ResourceKey.create(Registries.FLUID, Enderscape.id(name));
    }
}