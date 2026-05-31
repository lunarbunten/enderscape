package net.bunten.enderscape.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.EnderscapePaintingVariants.PAINTING_VARIANTS;

public class EnderscapePaintingVariantProvider extends FabricDynamicRegistryProvider {

    public EnderscapePaintingVariantProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, future);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        PAINTING_VARIANTS.forEach((key) -> add(provider, entries, key));
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<PaintingVariant> key) {
        entries.add(key, provider.lookupOrThrow(Registries.PAINTING_VARIANT).getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Painting Variant";
    }
}
