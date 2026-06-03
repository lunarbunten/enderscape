package net.bunten.enderscape.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BannerPattern;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.EnderscapeBannerPatterns.CRESCENT;
import static net.bunten.enderscape.registry.tag.EnderscapeBannerPatternTags.PATTERN_ITEM_CRESCENT;

public class EnderscapeBannerPatternTagProvider extends FabricTagsProvider<BannerPattern> {

    public EnderscapeBannerPatternTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, Registries.BANNER_PATTERN, future);
    }

    protected TagAppender<BannerPattern> tag(TagKey<BannerPattern> key) {
        return TagAppender.forBuilder(getOrCreateRawBuilder(key));
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(PATTERN_ITEM_CRESCENT).add(CRESCENT);
    }
}