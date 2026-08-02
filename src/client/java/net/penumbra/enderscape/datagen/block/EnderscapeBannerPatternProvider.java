package net.penumbra.enderscape.datagen.block;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BannerPattern;

import java.util.concurrent.CompletableFuture;

import static net.penumbra.enderscape.registry.block.EnderscapeBannerPatterns.BANNER_PATTERNS;

public class EnderscapeBannerPatternProvider extends FabricDynamicRegistryProvider {
    public EnderscapeBannerPatternProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, future);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        BANNER_PATTERNS.forEach((key) -> add(provider, entries, key));
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<BannerPattern> key) {
        entries.add(key, provider.lookupOrThrow(Registries.BANNER_PATTERN).getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Banner Patterns";
    }
}
