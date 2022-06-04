package net.bunten.enderscape.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import ru.bclib.api.biomes.BiomeAPI;
import ru.bclib.world.biomes.BCLBiome;
import ru.bclib.world.generator.BiomePicker;

@Mixin(BiomePicker.class)
public abstract class BiomePickerMixin {

    @Inject(at = @At("RETURN"), method = "getBiomes", cancellable = true, remap = false)
    public void addBiome(CallbackInfoReturnable<List<BCLBiome>> info) {
        var list = info.getReturnValue();
        if (list.contains(BiomeAPI.THE_END)) {
            list.remove(BiomeAPI.THE_END);
        }
        if (list.contains(BiomeAPI.END_MIDLANDS)) {
            list.remove(BiomeAPI.END_MIDLANDS);
        }
        if (list.contains(BiomeAPI.END_BARRENS)) {
            list.remove(BiomeAPI.END_BARRENS);
        }
	}
}