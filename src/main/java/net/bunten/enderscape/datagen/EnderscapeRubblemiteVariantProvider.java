package net.bunten.enderscape.datagen;

import net.bunten.enderscape.entity.rubblemite.RubblemiteVariant;
import net.bunten.enderscape.registry.EnderscapeRegistries;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.EnderscapeRubblemiteVariants.RUBBLEMITE_VARIANTS;

public class EnderscapeRubblemiteVariantProvider extends FabricDynamicRegistryProvider {
    public EnderscapeRubblemiteVariantProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, future);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        RUBBLEMITE_VARIANTS.forEach((key) -> add(provider, entries, key));
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<RubblemiteVariant> key) {
        entries.add(key, provider.lookupOrThrow(EnderscapeRegistries.RUBBLEMITE_VARIANT).getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Rubblemite Variant";
    }
}