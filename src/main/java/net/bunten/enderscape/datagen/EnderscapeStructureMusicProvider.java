package net.bunten.enderscape.datagen;

import net.bunten.enderscape.registry.EnderscapeRegistries;
import net.bunten.enderscape.sound.StructureMusic;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.EnderscapeStructureMusic.STRUCTURE_MUSIC;

public class EnderscapeStructureMusicProvider extends FabricDynamicRegistryProvider {

    public EnderscapeStructureMusicProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, future);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        STRUCTURE_MUSIC.forEach((key) -> add(provider, entries, key));
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<StructureMusic> key) {
        entries.add(key, provider.lookupOrThrow(EnderscapeRegistries.STRUCTURE_MUSIC).getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Structure Music";
    }
}
