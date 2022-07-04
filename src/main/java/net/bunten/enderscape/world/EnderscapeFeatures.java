package net.bunten.enderscape.world;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.world.features.CelestialIslandFeature;
import net.bunten.enderscape.world.features.CelestialIslandFeatureConfig;
import net.bunten.enderscape.world.features.ores.ScatteredOreFeature;
import net.bunten.enderscape.world.features.ores.VoidOreFeature;
import net.bunten.enderscape.world.features.vegetation.CelestialGrowthFeature;
import net.bunten.enderscape.world.features.vegetation.CelestialGrowthFeatureConfig;
import net.bunten.enderscape.world.features.vegetation.CelestialVegetationFeature;
import net.bunten.enderscape.world.features.vegetation.CelestialVegetationFeatureConfig;
import net.bunten.enderscape.world.features.vegetation.LargeCelestialFungusFeature;
import net.bunten.enderscape.world.features.vegetation.LargeCelestialFungusFeatureConfig;
import net.bunten.enderscape.world.features.vegetation.MurushroomFeature;
import net.bunten.enderscape.world.features.vegetation.MurushroomFeatureConfig;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;

public abstract class EnderscapeFeatures {

    public static final Feature<CelestialGrowthFeatureConfig> CELESTIAL_GROWTH = register("celestial_growth", new CelestialGrowthFeature(CelestialGrowthFeatureConfig.CODEC));
    public static final Feature<CelestialIslandFeatureConfig> CELESTIAL_ISLAND = register("celestial_island", new CelestialIslandFeature(CelestialIslandFeatureConfig.CODEC));
    public static final Feature<CelestialVegetationFeatureConfig> CELESTIAL_VEGETATION = register("celestial_vegetation", new CelestialVegetationFeature(CelestialVegetationFeatureConfig.CODEC));
    public static final Feature<LargeCelestialFungusFeatureConfig> LARGE_CELESTIAL_FUNGUS = register("large_celestial_fungus", new LargeCelestialFungusFeature(LargeCelestialFungusFeatureConfig.CODEC));
    public static final Feature<MurushroomFeatureConfig> MURUSHROOM = register("murushroom", new MurushroomFeature(MurushroomFeatureConfig.CODEC));
    public static final Feature<SingleStateFeatureConfig> SCATTERED_ORE = register("scattered_ore", new ScatteredOreFeature(SingleStateFeatureConfig.CODEC));
    public static final Feature<SingleStateFeatureConfig> VOID_FACING_ORE = register("void_facing_ore", new VoidOreFeature(SingleStateFeatureConfig.CODEC));

    private static <T extends Feature<?>> T register(String name, T entry) {
        return Registry.register(Registry.FEATURE, Enderscape.id(name), entry);
    }

    public static void init() {
    }
}