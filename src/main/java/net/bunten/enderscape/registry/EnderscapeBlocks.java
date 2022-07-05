package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.blocks.BulbFlowerBlock;
import net.bunten.enderscape.blocks.BulbLanternBlock;
import net.bunten.enderscape.blocks.CelestialFungusBlock;
import net.bunten.enderscape.blocks.CelestialGrowthBlock;
import net.bunten.enderscape.blocks.CelestialMyceliumBlock;
import net.bunten.enderscape.blocks.CelestialPathBlock;
import net.bunten.enderscape.blocks.DriftJellyBlock;
import net.bunten.enderscape.blocks.EnderscapeDoor;
import net.bunten.enderscape.blocks.EnderscapeFlowerPotBlock;
import net.bunten.enderscape.blocks.EnderscapeSignBlock;
import net.bunten.enderscape.blocks.EnderscapeSignType;
import net.bunten.enderscape.blocks.EnderscapeTrapdoor;
import net.bunten.enderscape.blocks.EnderscapeWallSignBlock;
import net.bunten.enderscape.blocks.FlangerBerryBlock;
import net.bunten.enderscape.blocks.FlangerBerryVine;
import net.bunten.enderscape.blocks.MurushroomsBlock;
import net.bunten.enderscape.blocks.MushroomCapBlock;
import net.bunten.enderscape.blocks.NebuliteCauldronBlock;
import net.bunten.enderscape.blocks.NebuliteOreBlock;
import net.bunten.enderscape.blocks.ShadowQuartzOreBlock;
import net.bunten.enderscape.blocks.SmallPillarBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.WoodenButtonBlock;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.TagKey;
import net.minecraft.util.SignType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

public class EnderscapeBlocks {

    public static final TagKey<Block> BULB_FLOWER_PLANTABLE_ON = register("bulb_flower_plantable_on");
    public static final TagKey<Block> CELESTIAL_STEMS = register("celestial_stems");
    public static final TagKey<Block> DRIFTER_ATTRACTING_BLOCKS = register("drifter_attracting_blocks");
    public static final TagKey<Block> END_MYCELIUM_BLOCKS = register("end_mycelium_blocks");
    public static final TagKey<Block> END_ORE_BLOCKS = register("end_ore_blocks");
    public static final TagKey<Block> FLANGER_BERRY_VINE_SUPPORT_BLOCKS = register("flanger_berry_vine_support_blocks");
    public static final TagKey<Block> FUNGUS_PLANTABLE_ON = register("fungus_plantable_on");
    public static final TagKey<Block> GROWTH_PLANTABLE_OM = register("growth_plantable_on");
    public static final TagKey<Block> LARGE_CELESTIAL_FUNGUS_GROWABLE_ON = register("large_celestial_fungus_growable_on");
    public static final TagKey<Block> LARGE_CELESTIAL_FUNGUS_GENERATABLE = register("large_celestial_fungus_generatable");
    public static final TagKey<Block> MIRROR_LODESTONE_BLOCKS = register("mirror_lodestone_blocks");
    public static final TagKey<Block> ORE_REPLACEABLES = register("ore_replaceables");
    public static final TagKey<Block> SHADOW_QUARTZ_BLOCKS = register("shadow_quartz_blocks");
    public static final TagKey<Block> SHADOW_STEEL_BLOCKS = register("shadow_steel_blocks");
    public static final TagKey<Block> SUPPORTS_END_CRYSTAL = register("supports_end_crystal");

    public static final TagKey<Block> CHORUS_SOUND_BLOCKS = register("chorus_sound_blocks");
    public static final TagKey<Block> PURPUR_SOUND_BLOCKS = register("purpur_sound_blocks");
    public static final TagKey<Block> END_ROD_SOUND_BLOCKS = register("end_rod_sound_blocks");
    public static final TagKey<Block> END_STONE_SOUND_BLOCKS = register("end_stone_sound_blocks");
    public static final TagKey<Block> END_STONE_BRICK_SOUND_BLOCKS = register("end_stone_brick_sound_blocks");
    public static final TagKey<Block> SHULKER_SOUND_BLOCKS = register("shulker_sound_blocks");

    private static final SignType CELESTIAL_TYPE = SignType.register(new EnderscapeSignType("celestial"));

    // Celestial Vegetation

    public static final Block CELESTIAL_MYCELIUM_BLOCK = new CelestialMyceliumBlock(FabricBlockSettings.of(Material.STONE, MapColor.TERRACOTTA_YELLOW).requiresTool().strength(3, 9).sounds(EnderscapeSounds.EYLIUM).ticksRandomly());
    public static final Block CELESTIAL_PATH_BLOCK = new CelestialPathBlock(FabricBlockSettings.of(Material.STONE, MapColor.TERRACOTTA_YELLOW).requiresTool().strength(3, 9).sounds(EnderscapeSounds.EYLIUM).blockVision(EnderscapeBlocks::always).suffocates(EnderscapeBlocks::always));

    public static final Block CELESTIAL_GROWTH = new CelestialGrowthBlock(FabricBlockSettings.of(Material.REPLACEABLE_PLANT, MapColor.TERRACOTTA_YELLOW).noCollision().breakInstantly().sounds(EnderscapeSounds.GROWTH).nonOpaque());
    public static final Block BULB_FLOWER = new BulbFlowerBlock(FabricBlockSettings.of(Material.REPLACEABLE_PLANT, MapColor.DARK_AQUA).noCollision().breakInstantly().sounds(EnderscapeSounds.GROWTH).nonOpaque().luminance(8));
    public static final Block CELESTIAL_FUNGUS = new CelestialFungusBlock(FabricBlockSettings.of(Material.PLANT, MapColor.TERRACOTTA_YELLOW).noCollision().breakInstantly().sounds(EnderscapeSounds.FUNGUS));

    public static final Block MURUSHROOMS = new MurushroomsBlock(FabricBlockSettings.of(Material.REPLACEABLE_PLANT, MapColor.TERRACOTTA_CYAN).noCollision().breakInstantly().sounds(EnderscapeSounds.FUNGUS).nonOpaque());
    public static final Block MURUSHROOM_CAP = new MushroomCapBlock(FabricBlockSettings.of(Material.SOLID_ORGANIC, MapColor.TERRACOTTA_CYAN).strength(1).sounds(EnderscapeSounds.FUNGUS_CAP));

    public static final Block POTTED_BULB_FLOWER = new EnderscapeFlowerPotBlock(BULB_FLOWER, FabricBlockSettings.of(Material.DECORATION).breakInstantly().nonOpaque().luminance(8));
    public static final Block POTTED_CELESTIAL_GROWTH = new EnderscapeFlowerPotBlock(CELESTIAL_GROWTH, FabricBlockSettings.copy(Blocks.POTTED_ACACIA_SAPLING));
    public static final Block POTTED_CELESTIAL_FUNGUS = new EnderscapeFlowerPotBlock(CELESTIAL_FUNGUS, FabricBlockSettings.copy(Blocks.POTTED_ACACIA_SAPLING));

    public static final Block FLANGER_BERRY_VINE = new FlangerBerryVine(FabricBlockSettings.of(Material.PLANT, MapColor.TERRACOTTA_YELLOW).ticksRandomly().noCollision().sounds(EnderscapeSounds.FLANGER_BERRY_VINE).strength(0.2F));
    public static final Block FLANGER_BERRY_BLOCK = new FlangerBerryBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MapColor.BRIGHT_TEAL).ticksRandomly().strength(0.3F).nonOpaque());

    // Nebulite

    public static final Block NEBULITE_ORE = new NebuliteOreBlock(FabricBlockSettings.of(Material.STONE, MapColor.PALE_YELLOW).requiresTool().strength(3, 12).sounds(EnderscapeSounds.NEBULITE_ORE));
    public static final Block NEBULITE_BLOCK = new Block(FabricBlockSettings.of(Material.METAL, MapColor.MAGENTA).requiresTool().strength(5, 12).sounds(EnderscapeSounds.NEBULITE_BLOCK));
    public static final Block NEBULITE_CAULDRON = new NebuliteCauldronBlock(FabricBlockSettings.copy(Blocks.CAULDRON));

    // Shadow Quartz

    public static final Block SHADOW_QUARTZ_ORE = new ShadowQuartzOreBlock(FabricBlockSettings.of(Material.STONE, MapColor.PALE_YELLOW).requiresTool().strength(3).sounds(EnderscapeSounds.SHADOW_QUARTZ_ORE));

    public static final Block SHADOW_QUARTZ_BLOCK = new Block(FabricBlockSettings.of(Material.STONE, MapColor.TERRACOTTA_BLUE).requiresTool().strength(3).sounds(EnderscapeSounds.SHADOW_QUARTZ));
    public static final Block SHADOW_QUARTZ_BLOCK_STAIRS = new StairsBlock(SHADOW_QUARTZ_BLOCK.getDefaultState(), FabricBlockSettings.copy(SHADOW_QUARTZ_BLOCK).requiresTool());
    public static final Block SHADOW_QUARTZ_BLOCK_SLAB = new SlabBlock(FabricBlockSettings.copy(SHADOW_QUARTZ_BLOCK).requiresTool());
    public static final Block SHADOW_QUARTZ_BLOCK_WALL = new WallBlock(FabricBlockSettings.copy(SHADOW_QUARTZ_BLOCK).requiresTool());
    
    public static final Block SMOOTH_SHADOW_QUARTZ = new Block(FabricBlockSettings.copy(SHADOW_QUARTZ_BLOCK));
    public static final Block SMOOTH_SHADOW_QUARTZ_STAIRS = new StairsBlock(SMOOTH_SHADOW_QUARTZ.getDefaultState(), FabricBlockSettings.copy(SMOOTH_SHADOW_QUARTZ).requiresTool());
    public static final Block SMOOTH_SHADOW_QUARTZ_SLAB = new SlabBlock(FabricBlockSettings.copy(SMOOTH_SHADOW_QUARTZ).requiresTool());
    public static final Block SMOOTH_SHADOW_QUARTZ_WALL = new WallBlock(FabricBlockSettings.copy(SMOOTH_SHADOW_QUARTZ).requiresTool());
    
    public static final Block SHADOW_QUARTZ_BRICKS = new Block(FabricBlockSettings.copy(SHADOW_QUARTZ_BLOCK));
    public static final Block SHADOW_QUARTZ_BRICK_STAIRS = new StairsBlock(SHADOW_QUARTZ_BRICKS.getDefaultState(), FabricBlockSettings.copy(SHADOW_QUARTZ_BRICKS).requiresTool());
    public static final Block SHADOW_QUARTZ_BRICK_SLAB = new SlabBlock(FabricBlockSettings.copy(SHADOW_QUARTZ_BRICKS).requiresTool());
    public static final Block SHADOW_QUARTZ_BRICK_WALL = new WallBlock(FabricBlockSettings.copy(SHADOW_QUARTZ_BRICKS).requiresTool());

    public static final Block CHISELED_SHADOW_QUARTZ = new Block(FabricBlockSettings.copy(SHADOW_QUARTZ_BLOCK));
    public static final Block SHADOW_QUARTZ_PILLAR = new PillarBlock(FabricBlockSettings.copy(SHADOW_QUARTZ_BLOCK));

    // Shadow Steel

    public static final Block SHADOW_STEEL_BLOCK = new Block(FabricBlockSettings.copy(Blocks.BEDROCK));
    public static final Block CHISELED_SHADOW_STEEL = new Block(FabricBlockSettings.copy(SHADOW_STEEL_BLOCK));
    public static final Block SHADOW_STEEL_PILLAR = new PillarBlock(FabricBlockSettings.copy(SHADOW_STEEL_BLOCK));
    public static final Block SMALL_SHADOW_STEEL_PILLAR = new SmallPillarBlock(FabricBlockSettings.copy(SHADOW_STEEL_BLOCK).nonOpaque());

    // Celestial Wood

    public static final Block CELESTIAL_STEM = new PillarBlock(FabricBlockSettings.of(Material.WOOD, MapColor.TERRACOTTA_WHITE).strength(1).sounds(EnderscapeSounds.STEM));
    public static final Block STRIPPED_CELESTIAL_STEM = new PillarBlock(FabricBlockSettings.copy(CELESTIAL_STEM));
    public static final Block CELESTIAL_HYPHAE = new PillarBlock(FabricBlockSettings.copy(CELESTIAL_STEM));
    public static final Block STRIPPED_CELESTIAL_HYPHAE = new PillarBlock(FabricBlockSettings.copy(CELESTIAL_STEM));
    public static final Block CELESTIAL_CAP = new MushroomCapBlock(FabricBlockSettings.of(Material.SOLID_ORGANIC, MapColor.TERRACOTTA_YELLOW).strength(1).sounds(EnderscapeSounds.FUNGUS_CAP));
    public static final Block CELESTIAL_PLANKS = new Block(FabricBlockSettings.of(Material.WOOD, MapColor.ORANGE).strength(2, 3).sounds(BlockSoundGroup.WOOD));
    public static final Block CELESTIAL_STAIRS = new StairsBlock(CELESTIAL_PLANKS.getDefaultState(), FabricBlockSettings.copy(CELESTIAL_PLANKS));
    public static final Block CELESTIAL_SLAB = new SlabBlock(FabricBlockSettings.copy(CELESTIAL_PLANKS).requiresTool());
    public static final Block CELESTIAL_PRESSURE_PLATE = new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.of(Material.WOOD, CELESTIAL_PLANKS.getDefaultMapColor()).noCollision().strength(0.5F).sounds(BlockSoundGroup.WOOD));
    public static final Block CELESTIAL_FENCE = new FenceBlock(FabricBlockSettings.copy(CELESTIAL_PLANKS).requiresTool());
    public static final Block CELESTIAL_DOOR = new EnderscapeDoor(FabricBlockSettings.of(Material.WOOD, CELESTIAL_PLANKS.getDefaultMapColor()).strength(3).sounds(BlockSoundGroup.WOOD).nonOpaque());
    public static final Block CELESTIAL_TRAPDOOR = new EnderscapeTrapdoor(FabricBlockSettings.of(Material.WOOD, MapColor.ORANGE).strength(3).sounds(BlockSoundGroup.WOOD).nonOpaque());
    public static final Block CELESTIAL_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copy(CELESTIAL_PLANKS).requiresTool());
    public static final Block CELESTIAL_BUTTON = new WoodenButtonBlock(FabricBlockSettings.of(Material.WOOD).noCollision().strength(0.5F).sounds(BlockSoundGroup.WOOD));
    public static final Block CELESTIAL_SIGN = new EnderscapeSignBlock(CELESTIAL_TYPE, FabricBlockSettings.copy(Blocks.OAK_SIGN));
    public static final Block CELESTIAL_WALL_SIGN = new EnderscapeWallSignBlock(CELESTIAL_TYPE, FabricBlockSettings.copy(Blocks.OAK_WALL_SIGN));

    // Etc

    public static final Block BULB_LANTERN = new BulbLanternBlock(FabricBlockSettings.copy(Blocks.LANTERN));
    public static final Block DRIFT_JELLY_BLOCK = new DriftJellyBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MapColor.BRIGHT_TEAL).breakInstantly().sounds(EnderscapeSounds.DRIFT_JELLY).nonOpaque());

    private static TagKey<Block> register(String name) {
        return TagKey.of(Registry.BLOCK_KEY, Enderscape.id(name));
    }

    private static boolean always(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }
}