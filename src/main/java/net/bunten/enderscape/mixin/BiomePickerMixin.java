package net.bunten.enderscape.mixin;

import org.betterx.bclib.api.v2.generator.BiomePicker;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BiomePicker.class)
public abstract class BiomePickerMixin {

    @Inject(at = @At("HEAD"), method = "addBiome", cancellable = true, remap = false)
    private void addBiome(BCLBiome biome, CallbackInfo info) {
        if (biome == BiomeAPI.THE_END || biome == BiomeAPI.END_MIDLANDS || biome == BiomeAPI.END_BARRENS) {
            info.cancel();
        }
    }
}