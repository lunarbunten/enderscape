package net.enderscape.mixin;

import net.enderscape.world.biomes.CenterEndBiome;
import net.enderscape.world.biomes.NormalEndBiome;
import net.enderscape.world.biomes.NormalEndIslandsBiome;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeCreator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DefaultBiomeCreator.class)
public abstract class DefaultBiomeCreatorMixin {

    @Inject(method = "createTheEnd", at = @At("INVOKE"), cancellable = true)
    private static void EScreateTheEnd(CallbackInfoReturnable<Biome> info) {
        info.setReturnValue(new CenterEndBiome().getBiome());
    }

    @Inject(method = "createSmallEndIslands", at = @At("INVOKE"), cancellable = true)
    private static void EScreateSmallEndIslands(CallbackInfoReturnable<Biome> info) {
        info.setReturnValue(new NormalEndIslandsBiome().getBiome());
    }

    @Inject(method = "createEndBarrens", at = @At("INVOKE"), cancellable = true)
    private static void EScreateEndBarrens(CallbackInfoReturnable<Biome> info) {
        info.setReturnValue(new NormalEndBiome().getBiome());
    }

    @Inject(method = "createEndMidlands", at = @At("INVOKE"), cancellable = true)
    private static void EScreateEndMidlands(CallbackInfoReturnable<Biome> info) {
        info.setReturnValue(new NormalEndBiome().getBiome());
    }

    @Inject(method = "createEndHighlands", at = @At("INVOKE"), cancellable = true)
    private static void EScreateEndHighlands(CallbackInfoReturnable<Biome> info) {
        info.setReturnValue(new NormalEndBiome().getBiome());
    }
}