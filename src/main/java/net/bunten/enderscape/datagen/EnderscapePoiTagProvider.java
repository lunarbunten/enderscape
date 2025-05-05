package net.bunten.enderscape.datagen;

import net.bunten.enderscape.registry.EnderscapePoi;
import net.bunten.enderscape.registry.tag.EnderscapePoiTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;

import java.util.concurrent.CompletableFuture;

public class EnderscapePoiTagProvider extends FabricTagProvider<PoiType> {

    public EnderscapePoiTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, Registries.POINT_OF_INTEREST_TYPE, future);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        getOrCreateTagBuilder(EnderscapePoiTags.DRIFTER_HOME).add(EnderscapePoi.DRIFTER_HOME);
        getOrCreateTagBuilder(EnderscapePoiTags.RUSTLE_SLEEPING_SPOT).add(EnderscapePoi.RUSTLE_SLEEPING_SPOT);
    }
}