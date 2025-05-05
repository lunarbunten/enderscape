package net.bunten.enderscape.registry;

import net.bunten.enderscape.biome.util.SkyParameters;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class EnderscapeRegistries {
    public static final ResourceKey<Registry<SkyParameters>> SKY_PARAMETERS_KEY = ResourceKey.createRegistryKey(ResourceLocation.withDefaultNamespace("end_sky_parameters"));

    static {
        DynamicRegistries.registerSynced(SKY_PARAMETERS_KEY, SkyParameters.CODEC);
    }
}
