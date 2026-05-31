package net.bunten.enderscape.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.EnderscapeEnchantments.ENCHANTMENTS;

public class EnderscapeEnchantmentProvider extends FabricDynamicRegistryProvider {

    public EnderscapeEnchantmentProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, future);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        ENCHANTMENTS.forEach((key) -> add(provider, entries, key));
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<Enchantment> key) {
        entries.add(key, provider.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Enchantments";
    }
}
