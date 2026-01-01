package net.bunten.enderscape.datagen;

import net.bunten.enderscape.biome.util.BiomeParameters;
import net.bunten.enderscape.registry.EnderscapeRegistries;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.EnderscapeBiomeParameters.BIOME_PARAMETERS;

public class EnderscapeBiomeProvidersProvider extends FabricDynamicRegistryProvider {
    public EnderscapeBiomeProvidersProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, future);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        BIOME_PARAMETERS.forEach((key) -> add(provider, entries, key));
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<BiomeParameters> key) {
        entries.add(key, provider.lookupOrThrow(EnderscapeRegistries.BIOME_PARAMETERS).getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Biome Parameters";
    }
}