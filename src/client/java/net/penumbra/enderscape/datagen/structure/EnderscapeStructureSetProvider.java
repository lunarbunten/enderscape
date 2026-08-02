package net.penumbra.enderscape.datagen.structure;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import java.util.concurrent.CompletableFuture;

import static net.penumbra.enderscape.registry.structure.EnderscapeStructureSets.STRUCTURE_SETS;

public class EnderscapeStructureSetProvider extends FabricDynamicRegistryProvider {

    public EnderscapeStructureSetProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, future);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        STRUCTURE_SETS.forEach((key) -> add(provider, entries, key));
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<StructureSet> key) {
        entries.add(key, provider.lookupOrThrow(Registries.STRUCTURE_SET).getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Structure Sets";
    }
}