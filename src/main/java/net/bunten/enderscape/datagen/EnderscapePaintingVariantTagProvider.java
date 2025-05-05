package net.bunten.enderscape.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraft.world.entity.decoration.PaintingVariant;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.EnderscapePaintingVariants.GRAPE_STATIC;

public class EnderscapePaintingVariantTagProvider extends FabricTagProvider<PaintingVariant> {

    public EnderscapePaintingVariantTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, Registries.PAINTING_VARIANT, future);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        getOrCreateTagBuilder(PaintingVariantTags.PLACEABLE).add(GRAPE_STATIC);
    }
}
