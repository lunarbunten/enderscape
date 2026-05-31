package net.bunten.enderscape.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.structure.EnderscapeStructures.STRUCTURES;

public class EnderscapeStructureProvider extends FabricDynamicRegistryProvider {

    public EnderscapeStructureProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, future);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        STRUCTURES.forEach((key) -> add(provider, entries, key));
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<Structure> key) {
        entries.add(key, provider.lookupOrThrow(Registries.STRUCTURE).getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Structures";
    }
}