package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.feature.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.function.Supplier;

public abstract class EnderscapeFeatures {

    public static final Supplier<Feature<CeilingOreConfig>> CEILING_ORE = register("ceiling_ore", () -> new CeilingOreFeature(CeilingOreConfig.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> ENDERSCAPE_ISLAND = register("enderscape_island", () -> new EnderscapeIslandFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<GrowthConfig>> GROWTH = register("growth", () -> new GrowthFeature(GrowthConfig.CODEC));
    public static final Supplier<Feature<LargeCelestialChanterelleConfig>> LARGE_CELESTIAL_CHANTERELLE = register("large_celestial_chanterelle", () -> new LargeCelestialChanterelleFeature(LargeCelestialChanterelleConfig.CODEC));
    public static final Supplier<Feature<LargeMurublightChanterelleConfig>> LARGE_MURUBLIGHT_CHANTERELLE = register("large_murublight_chanterelle", () -> new LargeMurublightChanterelleFeature(LargeMurublightChanterelleConfig.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> MURUBLIGHT_BRACKET = register("murublight_bracket", () -> new MurublightShelfFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> MAGNIA_ARCH = register("magnia_arch", () -> new MagniaArchFeature());
    public static final Supplier<Feature<MagniaTowerConfig>> MAGNIA_TOWER = register("magnia_tower", () -> new MagniaTowerFeature(MagniaTowerConfig.CODEC));
    public static final Supplier<Feature<MagniaSpikeConfig>> MAGNIA_SPIKE = register("magnia_spike", () -> new MagniaSpikeFeature(MagniaSpikeConfig.CODEC));
    public static final Supplier<Feature<ScatteredOreConfig>> SCATTERED_ORE = register("scattered_ore", () -> new ScatteredOreFeature(ScatteredOreConfig.CODEC));
    public static final Supplier<Feature<VeiledLeafPileConfig>> VEILED_LEAF_PILE = register("veiled_leaf_pile", () -> new VeiledLeafPileFeature(VeiledLeafPileConfig.CODEC));
    public static final Supplier<Feature<VeiledTreeConfig>> VEILED_TREE = register("veiled_tree", () -> new VeiledTreeFeature(VeiledTreeConfig.CODEC));
    public static final Supplier<Feature<VoidShaleConfig>> VOID_SHALE = register("void_shale", () -> new VoidShaleFeature(VoidShaleConfig.CODEC));

    private static <T extends Feature<?>> Supplier<T> register(String name, Supplier<T> entry) {
        return RegistryHelper.register(BuiltInRegistries.FEATURE, Enderscape.id(name), entry);
    }
}