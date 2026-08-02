package net.penumbra.enderscape.datagen.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.tags.BiomeTags.HAS_END_CITY;
import static net.minecraft.tags.BiomeTags.IS_END;
import static net.minecraft.world.level.biome.Biomes.*;
import static net.penumbra.enderscape.registry.level.EnderscapeBiomes.*;
import static net.penumbra.enderscape.registry.tag.EnderscapeBiomeTags.*;

public class EnderscapeBiomeTagProvider extends FabricTagsProvider<Biome> {

    public EnderscapeBiomeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, Registries.BIOME, future);
    }

    protected TagAppender<ResourceKey<Biome>, Biome> tag(TagKey<Biome> key) {
        return TagAppender.forBuilder(getOrCreateRawBuilder(key));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addTags(HolderLookup.Provider lookup) {
        tag(ENDERSCAPE_BIOMES).addAll(BIOMES);

        tag(DEEP_VOID_BIOMES).add(VOID_DEPTHS);
        tag(SKY_VOID_BIOMES).add(VOID_SKIES, VOID_SKY_ISLANDS);
        tag(VOID_BIOMES).forceAddTag(SKY_VOID_BIOMES).forceAddTag(DEEP_VOID_BIOMES);

        tag(EXCLUDED_FROM_GLOBAL_FEATURE_ADDITIONS).add(THE_END).forceAddTag(ENDERSCAPE_BIOMES);
        tag(HAS_BARRENS_ADDITIONS).add(END_HIGHLANDS, END_MIDLANDS);
        tag(OVERRIDES_DEFAULT_AMBIENCE).add(THE_END, END_HIGHLANDS, END_MIDLANDS, END_BARRENS, SMALL_END_ISLANDS);

        tag(HAS_END_CITY).add(VEILED_WOODLANDS, MAGNIA_FIELDS, CELESTIAL_GROVE);
        tag(HAS_GATEWAYS).forceAddTag(HAS_END_CITY).add(CORRUPT_BARRENS);
        tag(HAS_MIRESTONE_RUINS).add(CORRUPT_BARRENS);

        tag(IS_END).forceAddTag(ENDERSCAPE_BIOMES);
        tag(ConventionalBiomeTags.IS_END).forceAddTag(ENDERSCAPE_BIOMES);
    }
}