package net.penumbra.enderscape.datagen.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.penumbra.enderscape.registry.entity.EnderscapePoi;
import net.penumbra.enderscape.registry.tag.EnderscapePoiTags;

import java.util.concurrent.CompletableFuture;

public class EnderscapePoiTagProvider extends FabricTagsProvider<PoiType> {

    public EnderscapePoiTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, Registries.POINT_OF_INTEREST_TYPE, future);
    }

    protected TagAppender<ResourceKey<PoiType>, PoiType> tag(TagKey<PoiType> key) {
        return TagAppender.forBuilder(getOrCreateRawBuilder(key));
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(EnderscapePoiTags.DRIFTER_HOME).add(EnderscapePoi.DRIFTER_HOME);
        tag(EnderscapePoiTags.RUSTLE_SLEEPING_SPOT).add(EnderscapePoi.RUSTLE_SLEEPING_SPOT);
    }
}