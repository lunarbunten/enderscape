package net.bunten.enderscape.datagen;

import net.bunten.enderscape.registry.EnderscapeRegistries;
import net.bunten.enderscape.biome.util.SkyParameters;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.EnderscapeSkyParameters.SKY_PARAMETERS;

public class EnderscapeSkyParametersProvider extends FabricDynamicRegistryProvider {
    public EnderscapeSkyParametersProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, future);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        SKY_PARAMETERS.forEach((key) -> add(provider, entries, key));
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<SkyParameters> key) {
        entries.add(key, provider.lookupOrThrow(EnderscapeRegistries.SKY_PARAMETERS_KEY).getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Sky Parameters";
    }
}