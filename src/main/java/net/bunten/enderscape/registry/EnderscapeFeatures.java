package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.feature.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public abstract class EnderscapeFeatures {

    public static final Feature<CeilingOreConfig> CEILING_ORE = register("ceiling_ore", new CeilingOreFeature(CeilingOreConfig.CODEC));
    public static final Feature<NoneFeatureConfiguration> ENDERSCAPE_ISLAND = register("enderscape_island", new EnderscapeIslandFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<GrowthConfig> GROWTH = register("growth", new GrowthFeature(GrowthConfig.CODEC));
    public static final Feature<LargeCelestialChanterelleConfig> LARGE_CELESTIAL_CHANTERELLE = register("large_celestial_chanterelle", new LargeCelestialChanterelleFeature(LargeCelestialChanterelleConfig.CODEC));
    public static final Feature<LargeMurublightChanterelleConfig> LARGE_MURUBLIGHT_CHANTERELLE = register("large_murublight_chanterelle", new LargeMurublightChanterelleFeature(LargeMurublightChanterelleConfig.CODEC));
    public static final Feature<NoneFeatureConfiguration> MURUBLIGHT_SHELF = register("murublight_shelf", new MurublightShelfFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<NoneFeatureConfiguration> MAGNIA_ARCH = register("magnia_arch", new MagniaArchFeature());
    public static final Feature<MagniaTowerConfig> MAGNIA_TOWER = register("magnia_tower", new MagniaTowerFeature(MagniaTowerConfig.CODEC));
    public static final Feature<ScatteredOreConfig> SCATTERED_ORE = register("scattered_ore", new ScatteredOreFeature(ScatteredOreConfig.CODEC));
    public static final Feature<VeiledLeafPileConfig> VEILED_LEAF_PILE = register("veiled_leaf_pile", new VeiledLeafPileFeature(VeiledLeafPileConfig.CODEC));
    public static final Feature<VeiledTreeConfig> VEILED_TREE = register("veiled_tree", new VeiledTreeFeature(VeiledTreeConfig.CODEC));
    public static final Feature<VoidShaleConfig> VOID_SHALE = register("void_shale", new VoidShaleFeature(VoidShaleConfig.CODEC));

    private static <T extends Feature<?>> T register(String name, T entry) {
        return Registry.register(BuiltInRegistries.FEATURE, Enderscape.id(name), entry);
    }
}