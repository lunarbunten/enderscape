package net.bunten.enderscape.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.bunten.enderscape.registry.EnderscapeMusic;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BiomeAdditionsSound;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.BiomeParticleConfig;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.TheEndBiomeCreator;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;

@Mixin(value = TheEndBiomeCreator.class, priority = 100)
public abstract class TheEndBiomeCreatorMixin {

    private static final BiomeEffects getEndBiomeEffects() {
        var builder = new BiomeEffects.Builder();

        builder.waterColor(0x3F76E4);
        builder.waterFogColor(0x50533);

        builder.skyColor(0);
        builder.fogColor(0x20192D);

        builder.music(EnderscapeMusic.NORMAL_END);
        builder.loopSound(EnderscapeSounds.AMBIENT_NORMAL_END_LOOP);
        builder.additionsSound(new BiomeAdditionsSound(EnderscapeSounds.AMBIENT_NORMAL_END_ADDITIONS, 0.001));
        builder.particleConfig(new BiomeParticleConfig(ParticleTypes.MYCELIUM, 0.001F));

        return builder.build();
    }

    @Inject(method = "createEndBiome", at = @At("RETURN"), cancellable = true)
    private static void createEndBiome(GenerationSettings.Builder builder, CallbackInfoReturnable<Biome> info) {
        SpawnSettings.Builder builder2 = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addEndMobs(builder2);

        var biome = new Biome.Builder();
        biome.precipitation(Biome.Precipitation.NONE);
        biome.temperature(0.5f);
        biome.downfall(0.5f);
        biome.effects(getEndBiomeEffects());
        biome.spawnSettings(builder2.build());
        biome.generationSettings(builder.build());
        
        info.setReturnValue(biome.build());
    }
}