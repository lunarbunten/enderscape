package net.bunten.enderscape.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.EnderscapePlacedFeatures.PLACED_FEATURES;

public class EnderscapePlacedFeatureProvider extends FabricDynamicRegistryProvider {

    public EnderscapePlacedFeatureProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, future);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        PLACED_FEATURES.forEach((key) -> add(provider, entries, key));
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<PlacedFeature> key) {
        entries.add(key, provider.lookupOrThrow(Registries.PLACED_FEATURE).getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Placed Features";
    }
}
