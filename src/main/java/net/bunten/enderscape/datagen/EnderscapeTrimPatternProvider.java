package net.bunten.enderscape.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.trim.TrimPattern;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.EnderscapeTrimPatterns.TRIM_PATTERNS;

public class EnderscapeTrimPatternProvider extends FabricDynamicRegistryProvider {
    public EnderscapeTrimPatternProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, future);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        TRIM_PATTERNS.forEach((key) -> add(provider, entries, key));
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<TrimPattern> key) {
        entries.add(key, provider.lookupOrThrow(Registries.TRIM_PATTERN).getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Trim Patterns";
    }
}
