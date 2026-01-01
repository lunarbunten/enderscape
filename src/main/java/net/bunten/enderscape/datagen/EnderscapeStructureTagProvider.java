package net.bunten.enderscape.datagen;

import net.bunten.enderscape.structure.EnderscapeStructures;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.concurrent.CompletableFuture;

public class EnderscapeStructureTagProvider extends FabricTagProvider<Structure> {

    public EnderscapeStructureTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, Registries.STRUCTURE, future);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(StructureTags.EYE_OF_ENDER_LOCATED).add(EnderscapeStructures.STRONGHOLD);
    }
}
