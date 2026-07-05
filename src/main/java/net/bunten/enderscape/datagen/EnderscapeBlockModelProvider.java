package net.bunten.enderscape.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;

public class EnderscapeBlockModelProvider extends FabricModelProvider {
    public EnderscapeBlockModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generators) {
        generators.family(EnderscapeBlockFamilies.CELESTIAL_PLANKS.getBaseBlock()).generateFor(EnderscapeBlockFamilies.CELESTIAL_PLANKS);
        generators.family(EnderscapeBlockFamilies.MURUBLIGHT_PLANKS.getBaseBlock()).generateFor(EnderscapeBlockFamilies.MURUBLIGHT_PLANKS);
        generators.family(EnderscapeBlockFamilies.VEILED_PLANKS.getBaseBlock()).generateFor(EnderscapeBlockFamilies.VEILED_PLANKS);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
    }
}
