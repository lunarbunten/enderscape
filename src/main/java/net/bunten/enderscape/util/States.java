package net.bunten.enderscape.util;

import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;

public class States {

    public static final BlockState AIR = Blocks.AIR.defaultBlockState();
    public static final BlockState BULB_FLOWER = EnderscapeBlocks.BULB_FLOWER.defaultBlockState();
    public static final BlockState CELESTIAL_CAP = EnderscapeBlocks.CELESTIAL_CAP.defaultBlockState();
    public static final BlockState CELESTIAL_FUNGUS = EnderscapeBlocks.CELESTIAL_FUNGUS.defaultBlockState();
    public static final BlockState CORRUPT_GROWTH = EnderscapeBlocks.CORRUPT_GROWTH.defaultBlockState();
    public static final BlockState CORRUPT_MYCELIUM = EnderscapeBlocks.CORRUPT_MYCELIUM_BLOCK.defaultBlockState();
    public static final BlockState CORRUPT_PATH = EnderscapeBlocks.CORRUPT_PATH_BLOCK.defaultBlockState();
    public static final BlockState BLINKLIGHT_BODY = EnderscapeBlocks.BLINKLIGHT_VINES_BODY.defaultBlockState();
    public static final BlockState BLINKLIGHT_HEAD = EnderscapeBlocks.BLINKLIGHT_VINES_HEAD.defaultBlockState();
    public static final BlockState CELESTIAL_GROWTH = EnderscapeBlocks.CELESTIAL_GROWTH.defaultBlockState();
    public static final BlockState CELESTIAL_MYCELIUM = EnderscapeBlocks.CELESTIAL_MYCELIUM_BLOCK.defaultBlockState();
    public static final BlockState CELESTIAL_PATH = EnderscapeBlocks.CELESTIAL_PATH_BLOCK.defaultBlockState();
    public static final BlockState CELESTIAL_STEM = EnderscapeBlocks.CELESTIAL_STEM.defaultBlockState();
    public static final BlockState END_STONE = Blocks.END_STONE.defaultBlockState();
    public static final BlockState FLANGER_BERRY = EnderscapeBlocks.FLANGER_BERRY_BLOCK.defaultBlockState();
    public static final BlockState FLANGER_VINE = EnderscapeBlocks.FLANGER_BERRY_VINE.defaultBlockState();
    public static final BlockState MURUSHROOMS = EnderscapeBlocks.MURUSHROOMS.defaultBlockState();
    public static final BlockState MURUSHROOM_CAP = EnderscapeBlocks.MURUSHROOM_CAP.defaultBlockState();
    public static final BlockState MURUSHROOM_STEM = EnderscapeBlocks.MURUSHROOM_STEM.defaultBlockState();
    public static final BlockState NEBULITE_ORE = EnderscapeBlocks.NEBULITE_ORE.defaultBlockState();
    public static final BlockState SHADOW_QUARTZ_ORE = EnderscapeBlocks.SHADOW_QUARTZ_ORE.defaultBlockState();
    public static final BlockState VERADITE = EnderscapeBlocks.VERADITE.defaultBlockState();

    public static final BlockStateProvider BULB_FLOWER_BONEMEAL_PROVIDER = new WeightedStateProvider(new SimpleWeightedRandomList.Builder<BlockState>().add(CELESTIAL_FUNGUS, 3).add(BULB_FLOWER, 1));
}