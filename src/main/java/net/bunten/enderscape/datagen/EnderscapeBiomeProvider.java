package net.bunten.enderscape.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.EnderscapeBiomes.BIOMES;

public class EnderscapeBiomeProvider extends FabricDynamicRegistryProvider {
    public EnderscapeBiomeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, future);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        BIOMES.forEach((key) -> add(provider, entries, key));
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<Biome> key) {
        entries.add(key, provider.lookupOrThrow(Registries.BIOME).getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Biomes";
    }
}