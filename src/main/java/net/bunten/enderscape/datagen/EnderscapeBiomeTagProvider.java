package net.bunten.enderscape.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.EnderscapeBiomes.*;
import static net.bunten.enderscape.registry.tag.EnderscapeBiomeTags.*;
import static net.minecraft.tags.BiomeTags.HAS_END_CITY;
import static net.minecraft.tags.BiomeTags.IS_END;
import static net.minecraft.world.level.biome.Biomes.*;

public class EnderscapeBiomeTagProvider extends FabricTagProvider<Biome> {

    public EnderscapeBiomeTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, Registries.BIOME, future);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        getOrCreateTagBuilder(ENDERSCAPE_BIOMES).add(VEILED_WOODLANDS, MAGNIA_CRAGS, CELESTIAL_GROVE, CORRUPT_BARRENS, VOID_SKIES, VOID_SKY_ISLANDS, VOID_DEPTHS);

        getOrCreateTagBuilder(EXCLUDED_FROM_GLOBAL_FEATURE_ADDITIONS).add(THE_END).forceAddTag(ENDERSCAPE_BIOMES);
        getOrCreateTagBuilder(HAS_END_CITY).add(VEILED_WOODLANDS, MAGNIA_CRAGS, CELESTIAL_GROVE);
        getOrCreateTagBuilder(HAS_GATEWAYS).forceAddTag(HAS_END_CITY).add(CORRUPT_BARRENS);
        getOrCreateTagBuilder(HAS_MIRESTONE_RUINS).add(CORRUPT_BARRENS);
        getOrCreateTagBuilder(HAS_MODIFIED_END_AMBIENCE).add(THE_END, END_HIGHLANDS, END_MIDLANDS, END_BARRENS, SMALL_END_ISLANDS);
        getOrCreateTagBuilder(INCLUDES_NEW_BARRENS_CONTENT).add(END_HIGHLANDS, END_MIDLANDS);
        getOrCreateTagBuilder(IS_END).forceAddTag(ENDERSCAPE_BIOMES);

        getOrCreateTagBuilder(DOES_NOT_SPAWN_END_STONE_RUBBLEMITES).add(CORRUPT_BARRENS);
        getOrCreateTagBuilder(DOES_NOT_SPAWN_KURODITE_RUBBLEMITES).add(THE_END, END_HIGHLANDS, END_MIDLANDS, END_BARRENS, SMALL_END_ISLANDS, VEILED_WOODLANDS, MAGNIA_CRAGS, CELESTIAL_GROVE);
        getOrCreateTagBuilder(DOES_NOT_SPAWN_MIRESTONE_RUBBLEMITES);
        getOrCreateTagBuilder(DOES_NOT_SPAWN_VERADITE_RUBBLEMITES).add(CORRUPT_BARRENS);
    }
}