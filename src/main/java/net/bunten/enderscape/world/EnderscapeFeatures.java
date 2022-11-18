package net.bunten.enderscape.world;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.world.features.CelestialIslandFeature;
import net.bunten.enderscape.world.features.CelestialIslandFeatureConfig;
import net.bunten.enderscape.world.features.ores.ScatteredOreFeature;
import net.bunten.enderscape.world.features.ores.ScatteredOreFeatureConfig;
import net.bunten.enderscape.world.features.ores.VoidOreFeature;
import net.bunten.enderscape.world.features.vegetation.GrowthConfig;
import net.bunten.enderscape.world.features.vegetation.GrowthFeature;
import net.bunten.enderscape.world.features.vegetation.LargeCelestialFungusFeature;
import net.bunten.enderscape.world.features.vegetation.LargeCelestialFungusFeatureConfig;
import net.bunten.enderscape.world.features.vegetation.LargeMurushroomFeature;
import net.bunten.enderscape.world.features.vegetation.LargeMurushroomFeatureConfig;
import net.bunten.enderscape.world.features.vegetation.MurushroomFeature;
import net.bunten.enderscape.world.features.vegetation.MurushroomFeatureConfig;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

public abstract class EnderscapeFeatures {

    public static final Feature<CelestialIslandFeatureConfig> CELESTIAL_ISLAND = register("celestial_island", new CelestialIslandFeature(CelestialIslandFeatureConfig.CODEC));
    public static final Feature<GrowthConfig> GROWTH = register("growth", new GrowthFeature(GrowthConfig.CODEC));
    public static final Feature<LargeCelestialFungusFeatureConfig> LARGE_CELESTIAL_FUNGUS = register("large_celestial_fungus", new LargeCelestialFungusFeature(LargeCelestialFungusFeatureConfig.CODEC));
    public static final Feature<LargeMurushroomFeatureConfig> LARGE_MURUSHROOM = register("large_murushroom", new LargeMurushroomFeature(LargeMurushroomFeatureConfig.CODEC));
    public static final Feature<MurushroomFeatureConfig> MURUSHROOM = register("murushroom", new MurushroomFeature(MurushroomFeatureConfig.CODEC));
    public static final Feature<ScatteredOreFeatureConfig> SCATTERED_ORE = register("scattered_ore", new ScatteredOreFeature(ScatteredOreFeatureConfig.CODEC));
    public static final Feature<BlockStateConfiguration> VOID_FACING_ORE = register("void_facing_ore", new VoidOreFeature(BlockStateConfiguration.CODEC));

    private static <T extends Feature<?>> T register(String name, T entry) {
        return Registry.register(Registry.FEATURE, Enderscape.id(name), entry);
    }
}