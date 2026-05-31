package net.bunten.enderscape.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.EnderscapeDamageTypes.DAMAGE_TYPES;

public class EnderscapeDamageTypeProvider extends FabricDynamicRegistryProvider {
    public EnderscapeDamageTypeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, future);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        DAMAGE_TYPES.forEach((key) -> add(provider, entries, key));
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<DamageType> key) {
        entries.add(key, provider.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Damage Types";
    }
}
