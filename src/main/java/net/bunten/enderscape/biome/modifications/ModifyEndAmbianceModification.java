package net.bunten.enderscape.biome.modifications;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.registry.EnderscapeBiomeSounds;
import net.bunten.enderscape.registry.EnderscapeBiomes;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.bunten.enderscape.registry.tag.EnderscapeBiomeTags;
import net.minecraft.core.Holder;
import net.minecraft.sounds.Music;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;

public final class ModifyEndAmbianceModification implements BiomeModifier {
    private ModifyEndAmbianceModification() {}

    public static final ModifyEndAmbianceModification INSTANCE = new ModifyEndAmbianceModification();

    public static final MapCodec<ModifyEndAmbianceModification> CODEC = MapCodec.unit(INSTANCE);

    private static final EnderscapeConfig CONFIG = EnderscapeConfig.getInstance();

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (biome.is(EnderscapeBiomeTags.OVERRIDES_DEFAULT_AMBIENCE)) {
            if (CONFIG.ambienceUpdateMusicPools)
                builder.getSpecialEffects().backgroundMusic(new Music(EnderscapeBiomeSounds.DEFAULT_END.music(), 12000, 24000, false));

            if (CONFIG.ambienceUpdateLoopSounds) builder.getSpecialEffects().ambientLoopSound(EnderscapeBiomeSounds.DEFAULT_END.loop());
            if (CONFIG.ambienceUpdateAdditionSounds) builder.getSpecialEffects().ambientAdditionsSound(new AmbientAdditionsSettings(EnderscapeBiomeSounds.DEFAULT_END.additions(), 0.00075));
            if (CONFIG.ambienceUpdateMoodSounds) builder.getSpecialEffects().ambientMoodSound(new AmbientMoodSettings(EnderscapeBiomeSounds.DEFAULT_END.mood(), 6000, 8, 2));
            if (CONFIG.ambienceUpdateParticles) builder.getSpecialEffects().ambientParticle(new AmbientParticleSettings(EnderscapeParticles.VOID_STARS.get(), 0.003F));

            if (CONFIG.ambienceUpdateSkyColors) builder.getSpecialEffects().skyColor(EnderscapeBiomes.DEFAULT_SKY_COLOR);
            if (CONFIG.ambienceUpdateFogColors) builder.getSpecialEffects().fogColor(EnderscapeBiomes.DEFAULT_FOG_COLOR);

            if (CONFIG.ambienceUpdateGrassColors) builder.getSpecialEffects().grassColorOverride(EnderscapeBiomes.DEFAULT_GRASS_COLOR);
            if (CONFIG.ambienceUpdateFoliageColors) builder.getSpecialEffects().foliageColorOverride(EnderscapeBiomes.DEFAULT_FOLIAGE_COLOR);

            if (CONFIG.ambienceUpdateWaterColors) builder.getSpecialEffects().waterColor(EnderscapeBiomes.DEFAULT_WATER_COLOR);
            if (CONFIG.ambienceUpdateWaterFogColors) builder.getSpecialEffects().waterFogColor(EnderscapeBiomes.DEFAULT_WATER_FOG_COLOR);
        }
    }

    @Override
    public MapCodec<? extends BiomeModifier> codec() {
        return CODEC;
    }
}
