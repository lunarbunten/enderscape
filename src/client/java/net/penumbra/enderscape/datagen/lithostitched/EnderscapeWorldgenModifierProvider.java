package net.penumbra.enderscape.datagen.lithostitched;

import dev.worldgen.lithostitched.api.registry.LithostitchedRegistries;
import dev.worldgen.lithostitched.api.worldgen.modifier.WorldgenModifier;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;

import java.util.concurrent.CompletableFuture;

import static net.penumbra.enderscape.registry.lithostitched.EnderscapeWorldgenModifiers.WORLDGEN_MODIFIERS;

public class EnderscapeWorldgenModifierProvider extends FabricDynamicRegistryProvider {

    public EnderscapeWorldgenModifierProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, future);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        WORLDGEN_MODIFIERS.forEach((key) -> add(provider, entries, key));
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<WorldgenModifier> key) {
        entries.add(key, provider.lookupOrThrow(LithostitchedRegistries.WORLDGEN_MODIFIER).getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Lithostitched Worldgen Modifier";
    }
}
