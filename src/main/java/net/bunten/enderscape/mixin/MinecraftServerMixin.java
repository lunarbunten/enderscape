package net.bunten.enderscape.mixin;

import net.bunten.enderscape.registry.EnderscapeSurfaceRuleData;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    
    @Shadow
    public abstract RegistryAccess.Frozen registryAccess();

    @Inject(at = @At("TAIL"), method = "createLevels")
    private void addSurfaceRules(ChunkProgressListener listener, CallbackInfo ci) {
        HolderLookup.RegistryLookup<LevelStem> value = registryAccess().lookupOrThrow(Registries.LEVEL_STEM);
        LevelStem stem = value.getOrThrow(LevelStem.END).value();

        if (stem.generator() instanceof NoiseBasedChunkGenerator generator) {
            NoiseGeneratorSettings settings = generator.generatorSettings().value();
            NoiseGeneratorSettingsAccessor accessor = (NoiseGeneratorSettingsAccessor) (Object) settings;

            accessor.setSurfaceRule(SurfaceRules.sequence(EnderscapeSurfaceRuleData.makeRules(), settings.surfaceRule()));
        }
    }
}