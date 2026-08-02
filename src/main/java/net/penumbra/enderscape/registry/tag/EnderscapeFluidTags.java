package net.penumbra.enderscape.registry.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.penumbra.enderscape.Enderscape;

public class EnderscapeFluidTags {
    public static final TagKey<Fluid> VOID_LACHRYMA = register("void_lachryma");

    private static TagKey<Fluid> register(String name) {
        return TagKey.create(Registries.FLUID, Enderscape.id(name));
    }
}
