package net.enderscape.world;

import net.enderscape.Enderscape;
import net.enderscape.world.features.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;

public abstract class EndFeatures {

    public static final Feature<DefaultFeatureConfig> CELESTIAL_FUNGUS = register("celestial_fungus", new CelestialFungusFeature(DefaultFeatureConfig.CODEC));

    public static final Feature<DefaultFeatureConfig> CELESTIAL_ISLAND = register("celestial_island", new CelestialIslandFeature(DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> END_ISLAND = register("end_island", new EndIslandFeature(DefaultFeatureConfig.CODEC));

    public static final Feature<SingleStateFeatureConfig> GROWTH = register("growth", new GrowthFeature(SingleStateFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> MURUSHROOM = register("murushroom", new MurushroomFeature(DefaultFeatureConfig.CODEC));

    public static final Feature<DefaultFeatureConfig> SCRAP_PILLARS = register("scrap_pillars", new ScrapPillarsFeature(DefaultFeatureConfig.CODEC));

    public static final Feature<SingleStateFeatureConfig> SCATTERED_ORE = register("scattered_ore", new ScatteredOreFeature(SingleStateFeatureConfig.CODEC));
    public static final Feature<SingleStateFeatureConfig> VOID_FACING_ORE = register("void_facing_ore", new VoidOreFeature(SingleStateFeatureConfig.CODEC));

    private static <T extends Feature<?>> T register(String name, T entry) {
        return Registry.register(Registry.FEATURE, Enderscape.id(name), entry);
    }

    public static void init() {
    }
}