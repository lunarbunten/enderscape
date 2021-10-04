package net.enderscape.world;

import com.google.common.collect.ImmutableSet;
import net.enderscape.Enderscape;
import net.enderscape.registry.EndBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.ConfiguredDecorator;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.HeightmapDecoratorConfig;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;

import java.util.Set;

public class EndConfiguredFeatures {

    private static final Set<Block> PLAINS_SET = ImmutableSet.of(EndBlocks.CELESTIAL_MYCELIUM_BLOCK, Blocks.END_STONE);

    public static final ConfiguredDecorator<HeightmapDecoratorConfig> HEIGHTMAP_SPREAD_DOUBLE = Decorator.HEIGHTMAP_SPREAD_DOUBLE.configure(new HeightmapDecoratorConfig(Type.MOTION_BLOCKING));
    public static final ConfiguredDecorator<?> SQUARE_HEIGHTMAP_SPREAD_DOUBLE = HEIGHTMAP_SPREAD_DOUBLE.spreadHorizontally();

    public static final ConfiguredFeature<?, ?> NEBULITE_ORE = ore("nebulite_ore", EndBlocks.NEBULITE_ORE.getDefaultState(), 4, 54, 2);
    public static final ConfiguredFeature<?, ?> VOID_NEBULITE_ORE = register("void_nebulite_ore", EndFeatures.VOID_FACING_ORE.configure(new SingleStateFeatureConfig(EndBlocks.NEBULITE_ORE.getDefaultState())).uniformRange(YOffset.getBottom(), YOffset.fixed(20)).spreadHorizontally());
    public static final ConfiguredFeature<?, ?> SCATTERED_SHADOW_QUARTZ_ORE = register("scattered_shadow_quartz_ore", EndFeatures.SCATTERED_ORE.configure(new SingleStateFeatureConfig(EndBlocks.SHADOW_QUARTZ_ORE.getDefaultState())).uniformRange(YOffset.getBottom(), YOffset.fixed(60)).spreadHorizontally().repeat(6));
    public static final ConfiguredFeature<?, ?> CHORUS_TREE = register("chorus_tree", ConfiguredFeatures.CHORUS_PLANT.uniformRange(YOffset.fixed(59), YOffset.fixed(70)).decorate(SQUARE_HEIGHTMAP_SPREAD_DOUBLE));
    public static final ConfiguredFeature<?, ?> SPARSE_CHORUS_TREE = register("sparse_chorus_tree", CHORUS_TREE.applyChance(2));
    public static final ConfiguredFeature<?, ?> MURUSHROOM = register("murushroom", EndFeatures.MURUSHROOM.configure(FeatureConfig.DEFAULT).uniformRange(YOffset.fixed(25), YOffset.fixed(50)).spreadHorizontally().repeat(1));
    public static final ConfiguredFeature<?, ?> CELESTIAL_FUNGUS = register("celestial_fungus", EndFeatures.CELESTIAL_FUNGUS.configure(FeatureConfig.DEFAULT).uniformRange(YOffset.fixed(55), YOffset.fixed(65)).spreadHorizontally().repeat(7));
    public static final ConfiguredFeature<?, ?> CELESTIAL_GROWTH = register("celestial_growth", EndFeatures.GROWTH.configure(new SingleStateFeatureConfig(EndBlocks.CELESTIAL_GROWTH.getDefaultState())).uniformRange(YOffset.fixed(50), YOffset.fixed(70)).spreadHorizontally().repeat(7));
    public static final ConfiguredFeature<?, ?> VERY_UNCOMMON_CELESTIAL_GROWTH = register("very_uncommon_celestial_roots", CELESTIAL_GROWTH.applyChance(50));
    public static final ConfiguredFeature<?, ?> END_ISLAND = register("end_island", EndFeatures.END_ISLAND.configure(FeatureConfig.DEFAULT).uniformRange(YOffset.fixed(50), YOffset.fixed(70)).spreadHorizontally()).applyChance(6);
    public static final ConfiguredFeature<?, ?> CELESTIAL_ISLAND = register("celestial_island", EndFeatures.CELESTIAL_ISLAND.configure(FeatureConfig.DEFAULT).uniformRange(YOffset.fixed(50), YOffset.fixed(70)).spreadHorizontally()).applyChance(6);
    public static final ConfiguredFeature<?, ?> CELESTIAL_FUNGI_PATCH = patch("celestial_fungi_patch", EndBlocks.CELESTIAL_FUNGUS, PLAINS_SET, 8, 8, 8, 8, 1);
    public static final ConfiguredFeature<?, ?> BULB_FLOWER_PATCH = patch("bulb_flower_patch", EndBlocks.BULB_FLOWER, PLAINS_SET, 8, 8, 8, 3, 11);

    // Islands

    @SuppressWarnings("unused")
    private static ConfiguredFeature<?, ?> ore(String name, Block block, BlockState state, int size, int range, int count) {
        return register(name, ((Feature.ORE.configure(new OreFeatureConfig(new BlockMatchRuleTest(block), state, size)).uniformRange(YOffset.getBottom(), YOffset.fixed(range)).spreadHorizontally()).repeat(count)));
    }

    private static ConfiguredFeature<?, ?> ore(String name, BlockState state, int size, int range, int count) {
        return register(name, ((Feature.ORE.configure(new OreFeatureConfig(new TagMatchRuleTest(BlockTags.BASE_STONE_OVERWORLD), state, size)).uniformRange(YOffset.getBottom(), YOffset.fixed(range)).spreadHorizontally()).repeat(count)));
    }

    private static ConfiguredFeature<?, ?> patch(String name, Block block, Set<Block> whitelist, int tries, int spreadX, int spreadZ, int count, int chance) {
        RandomPatchFeatureConfig config = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(block.getDefaultState()), SimpleBlockPlacer.INSTANCE)).tries(tries).spreadX(spreadX).spreadZ(spreadZ).whitelist(whitelist).build();
        return register(name, Feature.RANDOM_PATCH.configure(config).decorate(SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(count).applyChance(chance).spreadHorizontally());
    }

    private static <T extends ConfiguredFeature<?, ?>> T register(String name, T entry) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, Enderscape.id(name), entry);
    }

    public static void init() {
    }
}