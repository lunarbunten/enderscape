package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.block.state.StateProperties;
import net.bunten.enderscape.feature.*;
import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.minecraft.data.worldgen.features.FeatureUtils.register;
import static net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider.simple;

public class EnderscapeConfiguredFeatures {

    public static final List<ResourceKey<ConfiguredFeature<?, ?>>> CONFIGURED_FEATURES = new ArrayList<>();

    public static final ResourceKey<ConfiguredFeature<?, ?>> BLINKLIGHT_VINES = registerKey("blinklight_vines");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BULB_FLOWER = registerKey("bulb_flower");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CEILING_NEBULITE_ORE = registerKey("ceiling_nebulite_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CELESTIAL_GROVE_VEGETATION = registerKey("celestial_grove_vegetation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CELESTIAL_GROWTH = registerKey("celestial_growth");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CHORUS_SPROUTS = registerKey("chorus_sprouts");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CORRUPT_BARRENS_VEGETATION = registerKey("corrupt_barrens_vegetation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> DOWNWARD_CORRUPT_GROWTH = registerKey("downward_corrupt_growth");
    public static final ResourceKey<ConfiguredFeature<?, ?>> DOWNWARD_LARGE_MURUBLIGHT_CHANTERELLE = registerKey("downward_large_murublight_chanterelle");
    public static final ResourceKey<ConfiguredFeature<?, ?>> DOWNWARD_REPULSIVE_MAGNIA_SPROUT = registerKey("downward_repulsive_magnia_sprout");
    public static final ResourceKey<ConfiguredFeature<?, ?>> DOWNWARD_TALL_CORRUPT_GROWTH = registerKey("downward_tall_corrupt_growth");
    public static final ResourceKey<ConfiguredFeature<?, ?>> DRY_END_GROWTH = registerKey("dry_end_growth");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ENDERSCAPE_ISLAND = registerKey("enderscape_island");
    public static final ResourceKey<ConfiguredFeature<?, ?>> KURODITE = registerKey("kurodite");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_CELESTIAL_CHANTERELLE = registerKey("large_celestial_chanterelle");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_MAGNIA_SPIKE = registerKey("large_magnia_spike");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MAGNIA_ARCH = registerKey("magnia_arch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MAGNIA_SPIKE = registerKey("magnia_spike");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MAGNIA_TOWER = registerKey("magnia_tower");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MIRESTONE_BLOB = registerKey("mirestone_blob");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MIRESTONE_PILLARS = registerKey("mirestone_pillars");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MURUBLIGHT_BRACKET = registerKey("murublight_bracket");
    public static final ResourceKey<ConfiguredFeature<?, ?>> NEBULITE_ORE = registerKey("nebulite_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SCATTERED_SHADOLINE_ORE = registerKey("scattered_shadoline_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SHADOLINE_ORE = registerKey("shadoline_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> UPWARD_ALLURING_MAGNIA_SPROUT = registerKey("upward_alluring_magnia_sprout");
    public static final ResourceKey<ConfiguredFeature<?, ?>> UPWARD_CORRUPT_GROWTH = registerKey("upward_corrupt_growth");
    public static final ResourceKey<ConfiguredFeature<?, ?>> UPWARD_LARGE_MURUBLIGHT_CHANTERELLE = registerKey("upward_large_murublight_chanterelle");
    public static final ResourceKey<ConfiguredFeature<?, ?>> UPWARD_TALL_CORRUPT_GROWTH = registerKey("upward_tall_corrupt_growth");
    public static final ResourceKey<ConfiguredFeature<?, ?>> VEILED_TREE = registerKey("veiled_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> VEILED_TREE_FROM_SAPLING = registerKey("veiled_tree_from_sapling");
    public static final ResourceKey<ConfiguredFeature<?, ?>> VEILED_WOODLANDS_VEGETATION = registerKey("veiled_woodlands_vegetation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> VERADITE = registerKey("veradite");
    public static final ResourceKey<ConfiguredFeature<?, ?>> VOID_SHALE = registerKey("void_shale");
    public static final ResourceKey<ConfiguredFeature<?, ?>> VOID_SHALE_BLOB = registerKey("void_shale_blob");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WISP_FLOWER_PATCH = registerKey("wisp_flower_patch");

    private static final List<OreConfiguration.TargetBlockState> KURODITE_TARGETS = List.of(
            OreConfiguration.target(new TagMatchTest(EnderscapeBlockTags.ORE_REPLACEABLE), EnderscapeBlocks.KURODITE.defaultBlockState())
    );

    private static final List<OreConfiguration.TargetBlockState> NEBULITE_ORE_TARGETS = List.of(
            OreConfiguration.target(new BlockMatchTest(Blocks.END_STONE), EnderscapeBlocks.NEBULITE_ORE.defaultBlockState()),
            OreConfiguration.target(new BlockMatchTest(EnderscapeBlocks.MIRESTONE), EnderscapeBlocks.MIRESTONE_NEBULITE_ORE.defaultBlockState())
    );

    private static final List<OreConfiguration.TargetBlockState> SHADOLINE_ORE_TARGETS = List.of(
            OreConfiguration.target(new BlockMatchTest(Blocks.END_STONE), EnderscapeBlocks.SHADOLINE_ORE.defaultBlockState()),
            OreConfiguration.target(new BlockMatchTest(EnderscapeBlocks.MIRESTONE), EnderscapeBlocks.MIRESTONE_SHADOLINE_ORE.defaultBlockState())
    );

    private static final List<OreConfiguration.TargetBlockState> VERADITE_TARGETS = List.of(
            OreConfiguration.target(new TagMatchTest(EnderscapeBlockTags.ORE_REPLACEABLE), EnderscapeBlocks.VERADITE.defaultBlockState())
    );

    private static final List<OreConfiguration.TargetBlockState> VOID_SHALE_TARGETS = List.of(
            OreConfiguration.target(new BlockMatchTest(EnderscapeBlocks.MIRESTONE), EnderscapeBlocks.VOID_SHALE.defaultBlockState())
    );

    private static final List<OreConfiguration.TargetBlockState> VOID_SHALE_BLOBS_TARGETS = List.of(
            OreConfiguration.target(new TagMatchTest(EnderscapeBlockTags.ORE_REPLACEABLE), EnderscapeBlocks.VOID_SHALE.defaultBlockState())
    );

    private static final List<OreConfiguration.TargetBlockState> MIRESTONE_BLOB_TARGETS = List.of(
            OreConfiguration.target(new BlockMatchTest(Blocks.END_STONE), EnderscapeBlocks.MIRESTONE.defaultBlockState())
    );

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        registerVeiledWoodlands(context);
        registerMagniaCrags(context);
        registerCelestialGrove(context);
        registerCorruptBarrens(context);

        register(context, ENDERSCAPE_ISLAND, EnderscapeFeatures.ENDERSCAPE_ISLAND, NoneFeatureConfiguration.NONE);

        register(context, CHORUS_SPROUTS, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(simple(EnderscapeBlocks.CHORUS_SPROUTS)));
        register(context, DRY_END_GROWTH, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(simple(EnderscapeBlocks.DRY_END_GROWTH)));
        register(context, MURUBLIGHT_BRACKET, EnderscapeFeatures.MURUBLIGHT_BRACKET, NoneFeatureConfiguration.NONE);

        register(context, NEBULITE_ORE, Feature.ORE, new OreConfiguration(NEBULITE_ORE_TARGETS, 4));
        register(context, CEILING_NEBULITE_ORE, EnderscapeFeatures.CEILING_ORE, new CeilingOreConfig(NEBULITE_ORE_TARGETS, ConstantInt.of(8)));

        register(context, SHADOLINE_ORE, Feature.ORE, new OreConfiguration(SHADOLINE_ORE_TARGETS, 10));
        register(context, SCATTERED_SHADOLINE_ORE, EnderscapeFeatures.SCATTERED_ORE, new ScatteredOreConfig(SHADOLINE_ORE_TARGETS, UniformInt.of(70, 90), UniformInt.of(-1, 1), ConstantInt.of(1)));

        register(context, VERADITE, Feature.ORE, new OreConfiguration(VERADITE_TARGETS, 28));

        register(context, VOID_SHALE, EnderscapeFeatures.VOID_SHALE, new VoidShaleConfig(VOID_SHALE_TARGETS, ConstantInt.of(5)));
        register(context, VOID_SHALE_BLOB, Feature.ORE, new OreConfiguration(VOID_SHALE_BLOBS_TARGETS, 16, 1));

        register(context, MIRESTONE_BLOB, Feature.ORE, new OreConfiguration(MIRESTONE_BLOB_TARGETS, 16, 0.5F));
    }

    private static void registerVeiledWoodlands(BootstrapContext<ConfiguredFeature<?, ?>> context) {

        register(context, VEILED_WOODLANDS_VEGETATION, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
                new WeightedStateProvider(new SimpleWeightedRandomList.Builder<BlockState>()
                        .add(EnderscapeBlocks.WISP_SPROUTS.defaultBlockState(), 4)
                        .add(EnderscapeBlocks.WISP_GROWTH.defaultBlockState(), 1)
                )
        ));

        register(context, WISP_FLOWER_PATCH, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(
                Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(EnderscapeBlocks.WISP_FLOWER)), List.of(), 16)
        );

        register(context, VEILED_TREE, EnderscapeFeatures.VEILED_TREE, new VeiledTreeConfig(
                UniformInt.of(1, 2),
                UniformInt.of(3, 4),
                ConstantInt.of(3),
                UniformInt.of(4, 5),
                ConstantFloat.of(0.6F),
                Optional.of(new GrowthConfig(
                        EnderscapeBlocks.VEILED_VINES.defaultBlockState(),
                        UniformInt.of(1, 2),
                        UniformInt.of(1, 6),
                        0.15F)
                ),
                Optional.of(new VeiledLeafPileConfig(
                        UniformFloat.of(6.0F, 10.0F),
                        ConstantFloat.of(0.2F),
                        UniformInt.of(1, 2)
                ))
        ));

        register(context, VEILED_TREE_FROM_SAPLING, EnderscapeFeatures.VEILED_TREE, new VeiledTreeConfig(
                UniformInt.of(1, 2),
                UniformInt.of(3, 4),
                ConstantInt.of(3),
                UniformInt.of(4, 5),
                ConstantFloat.of(0.6F),
                Optional.of(new GrowthConfig(
                        EnderscapeBlocks.VEILED_VINES.defaultBlockState(),
                        UniformInt.of(1, 2),
                        UniformInt.of(1, 6),
                        0.15F)
                ),
                Optional.empty()
        ));
    }

    private static void registerMagniaCrags(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        register(context, UPWARD_ALLURING_MAGNIA_SPROUT, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(simple(EnderscapeBlocks.ALLURING_MAGNIA_SPROUT)));

        register(context, DOWNWARD_REPULSIVE_MAGNIA_SPROUT, Feature.BLOCK_COLUMN,
                new BlockColumnConfiguration(List.of(
                        BlockColumnConfiguration.layer(
                                ConstantInt.of(1),
                                simple(EnderscapeBlocks.REPULSIVE_MAGNIA_SPROUT.defaultBlockState().setValue(StateProperties.FACING, Direction.DOWN))
                        )
                ), Direction.DOWN, BlockPredicate.ONLY_IN_AIR_PREDICATE, true));

        register(context, MAGNIA_ARCH, EnderscapeFeatures.MAGNIA_ARCH, new NoneFeatureConfiguration());

        register(context, MAGNIA_TOWER, EnderscapeFeatures.MAGNIA_TOWER, new MagniaTowerConfig(
                UniformFloat.of(6.0F, 8.0F),
                UniformInt.of(12, 18),
                ConstantInt.of(3),
                ConstantInt.of(7),
                ConstantFloat.of(0.025F),
                ConstantFloat.of(0.05F))
        );

        register(context, MAGNIA_SPIKE, EnderscapeFeatures.MAGNIA_SPIKE, new MagniaSpikeConfig(
                UniformInt.of(3, 8),
                ConstantFloat.of(0.05F),
                2)
        );

        register(context, LARGE_MAGNIA_SPIKE, EnderscapeFeatures.MAGNIA_SPIKE, new MagniaSpikeConfig(
                UniformInt.of(6, 12),
                ConstantFloat.of(0.05F),
                6)
        );
    }

    private static void registerCelestialGrove(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        register(context, BULB_FLOWER, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(simple(EnderscapeBlocks.BULB_FLOWER)));
        register(context, CELESTIAL_GROVE_VEGETATION, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
                new WeightedStateProvider(new SimpleWeightedRandomList.Builder<BlockState>()
                        .add(EnderscapeBlocks.CELESTIAL_CHANTERELLE.defaultBlockState(), 8)
                        .add(EnderscapeBlocks.BULB_FLOWER.defaultBlockState(), 1)
                )
        ));
        register(context, CELESTIAL_GROWTH, EnderscapeFeatures.GROWTH, new GrowthConfig(EnderscapeBlocks.CELESTIAL_GROWTH.defaultBlockState(), UniformInt.of(1, 2), UniformInt.of(1, 3), 0.125F));
        register(context, LARGE_CELESTIAL_CHANTERELLE, EnderscapeFeatures.LARGE_CELESTIAL_CHANTERELLE, new LargeCelestialChanterelleConfig(UniformInt.of(10, 35), 4, 0.75F, 1, 64, 16));
    }

    private static void registerCorruptBarrens(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        register(context, MIRESTONE_PILLARS, Feature.BLOCK_COLUMN, BlockColumnConfiguration.simple(BiasedToBottomInt.of(1, 4), simple(EnderscapeBlocks.MIRESTONE)));

        register(context, KURODITE, Feature.ORE, new OreConfiguration(KURODITE_TARGETS, 28));

        register(context, BLINKLIGHT_VINES, Feature.BLOCK_COLUMN,
                new BlockColumnConfiguration(List.of(
                        BlockColumnConfiguration.layer(UniformInt.of(4, 15), simple(EnderscapeBlocks.BLINKLIGHT_VINES_BODY)),
                        BlockColumnConfiguration.layer(ConstantInt.of(1), simple(EnderscapeBlocks.BLINKLIGHT_VINES_HEAD.defaultBlockState().setValue(StateProperties.AGE_25, 25)))
                ), Direction.DOWN, BlockPredicate.ONLY_IN_AIR_PREDICATE, true));

        register(context, CORRUPT_BARRENS_VEGETATION, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
                new WeightedStateProvider(new SimpleWeightedRandomList.Builder<BlockState>()
                        .add(EnderscapeBlocks.MURUBLIGHT_CHANTERELLE.defaultBlockState(), 1)
                ))
        );

        register(context, UPWARD_CORRUPT_GROWTH, EnderscapeFeatures.GROWTH, new GrowthConfig(EnderscapeBlocks.CORRUPT_GROWTH.defaultBlockState().setValue(StateProperties.FACING, Direction.UP), ConstantInt.of(1), UniformInt.of(0, 1), 0.3F));
        register(context, UPWARD_TALL_CORRUPT_GROWTH, EnderscapeFeatures.GROWTH, new GrowthConfig(EnderscapeBlocks.CORRUPT_GROWTH.defaultBlockState().setValue(StateProperties.FACING, Direction.UP), UniformInt.of(3, 4), ConstantInt.of(0), 0.0F));

        register(context, DOWNWARD_CORRUPT_GROWTH, EnderscapeFeatures.GROWTH, new GrowthConfig(EnderscapeBlocks.CORRUPT_GROWTH.defaultBlockState().setValue(StateProperties.FACING, Direction.DOWN), ConstantInt.of(1), UniformInt.of(1, 2), 0.5F));
        register(context, DOWNWARD_TALL_CORRUPT_GROWTH, EnderscapeFeatures.GROWTH, new GrowthConfig(EnderscapeBlocks.CORRUPT_GROWTH.defaultBlockState().setValue(StateProperties.FACING, Direction.DOWN), UniformInt.of(4, 8), ConstantInt.of(0), 0.0F));

        register(context, DOWNWARD_LARGE_MURUBLIGHT_CHANTERELLE, EnderscapeFeatures.LARGE_MURUBLIGHT_CHANTERELLE, new LargeMurublightChanterelleConfig(Direction.DOWN, UniformInt.of(15, 30), UniformInt.of(4, 6), 32));
        register(context, UPWARD_LARGE_MURUBLIGHT_CHANTERELLE, EnderscapeFeatures.LARGE_MURUBLIGHT_CHANTERELLE, new LargeMurublightChanterelleConfig(Direction.UP, UniformInt.of(15, 30), UniformInt.of(4, 6), 32));
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        ResourceKey<ConfiguredFeature<?, ?>> key = ResourceKey.create(Registries.CONFIGURED_FEATURE, Enderscape.id(name));
        CONFIGURED_FEATURES.add(key);
        return key;
    }
}
