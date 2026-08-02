package net.penumbra.enderscape.registry.level;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.feature.*;

public abstract class EnderscapeFeatures {

    public static final Feature<CeilingOreFeature.Config> CEILING_ORE = register("ceiling_ore", new CeilingOreFeature(CeilingOreFeature.Config.CODEC));
    public static final Feature<NoneFeatureConfiguration> ENDERSCAPE_ISLAND = register("enderscape_island", new EnderscapeIslandFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<GrowthFeature.Config> GROWTH = register("growth", new GrowthFeature(GrowthFeature.Config.CODEC));
    public static final Feature<LargeCelestialChanterelleFeature.Config> LARGE_CELESTIAL_CHANTERELLE = register("large_celestial_chanterelle", new LargeCelestialChanterelleFeature(LargeCelestialChanterelleFeature.Config.CODEC));
    public static final Feature<LargeMurublightChanterelleFeature.Config> LARGE_MURUBLIGHT_CHANTERELLE = register("large_murublight_chanterelle", new LargeMurublightChanterelleFeature(LargeMurublightChanterelleFeature.Config.CODEC));
    public static final Feature<NoneFeatureConfiguration> MURUBLIGHT_BRACKET = register("murublight_bracket", new MurublightBracketFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<NoneFeatureConfiguration> MAGNIA_ARCH = register("magnia_arch", new MagniaArchFeature());
    public static final Feature<MagniaTowerFeature.Config> MAGNIA_TOWER = register("magnia_tower", new MagniaTowerFeature(MagniaTowerFeature.Config.CODEC));
    public static final Feature<MagniaSpikeFeature.Config> MAGNIA_SPIKE = register("magnia_spike", new MagniaSpikeFeature(MagniaSpikeFeature.Config.CODEC));
    public static final Feature<ScatteredOreFeature.Config> SCATTERED_ORE = register("scattered_ore", new ScatteredOreFeature(ScatteredOreFeature.Config.CODEC));
    public static final Feature<VeiledLeafPileFeature.Config> VEILED_LEAF_PILE = register("veiled_leaf_pile", new VeiledLeafPileFeature(VeiledLeafPileFeature.Config.CODEC));
    public static final Feature<VeiledTreeFeature.Config> VEILED_TREE = register("veiled_tree", new VeiledTreeFeature(VeiledTreeFeature.Config.CODEC));
    public static final Feature<VoidLakeFeature.Config> VOID_LAKE = register("void_lake", new VoidLakeFeature(VoidLakeFeature.Config.CODEC));
    public static final Feature<VoidShaleFeature.Config> VOID_SHALE = register("void_shale", new VoidShaleFeature(VoidShaleFeature.Config.CODEC));

    private static <T extends Feature<?>> T register(String name, T entry) {
        return Registry.register(BuiltInRegistries.FEATURE, Enderscape.id(name), entry);
    }
}