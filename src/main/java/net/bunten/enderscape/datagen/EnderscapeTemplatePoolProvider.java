package net.bunten.enderscape.datagen;

import net.bunten.enderscape.structure.EnderscapeTemplatePools;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.concurrent.CompletableFuture;

public class EnderscapeTemplatePoolProvider extends FabricDynamicRegistryProvider {

    public EnderscapeTemplatePoolProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, future);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        EnderscapeTemplatePools.TEMPLATE_POOLS.forEach((key) -> add(provider, entries, key));
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<StructureTemplatePool> key) {
        entries.add(key, provider.lookupOrThrow(Registries.TEMPLATE_POOL).getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Template Pools";
    }
}