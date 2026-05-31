package net.bunten.enderscape.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.EnderscapeBiomes.*;
import static net.bunten.enderscape.registry.tag.EnderscapeBiomeTags.*;
import static net.minecraft.tags.BiomeTags.HAS_END_CITY;
import static net.minecraft.tags.BiomeTags.IS_END;
import static net.minecraft.world.level.biome.Biomes.*;

public class EnderscapeBiomeTagProvider extends FabricTagsProvider<Biome> {

    public EnderscapeBiomeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, Registries.BIOME, future);
    }

    protected TagAppender<ResourceKey<Biome>, Biome> tag(TagKey<Biome> key) {
        return TagAppender.forBuilder(getOrCreateRawBuilder(key));
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(ENDERSCAPE_BIOMES).add(VEILED_WOODLANDS, MAGNIA_FIELDS, CELESTIAL_GROVE, CORRUPT_BARRENS, VOID_SKIES, VOID_SKY_ISLANDS, VOID_DEPTHS);
        tag(IS_END).forceAddTag(ENDERSCAPE_BIOMES);

        tag(EXCLUDED_FROM_GLOBAL_FEATURE_ADDITIONS).add(THE_END).addTag(ENDERSCAPE_BIOMES);
        tag(HAS_BARRENS_ADDITIONS).add(END_HIGHLANDS, END_MIDLANDS);
        tag(HAS_END_CITY).add(VEILED_WOODLANDS, MAGNIA_FIELDS, CELESTIAL_GROVE);
        tag(HAS_GATEWAYS).addTag(HAS_END_CITY).add(CORRUPT_BARRENS);
        tag(HAS_MIRESTONE_RUINS).add(CORRUPT_BARRENS);
        tag(OVERRIDES_DEFAULT_AMBIENCE).add(THE_END, END_HIGHLANDS, END_MIDLANDS, END_BARRENS, SMALL_END_ISLANDS);
    }
}