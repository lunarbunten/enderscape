package net.penumbra.enderscape.datagen.structure;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.penumbra.enderscape.registry.structure.EnderscapeProcessorLists;

import java.util.concurrent.CompletableFuture;

public class EnderscapeProcessorListProvider extends FabricDynamicRegistryProvider {

    public EnderscapeProcessorListProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, future);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        EnderscapeProcessorLists.PROCESSOR_LISTS.forEach((key) -> add(provider, entries, key));
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<StructureProcessorList> key) {
        entries.add(key, provider.lookupOrThrow(Registries.PROCESSOR_LIST).getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Processor Lists";
    }
}