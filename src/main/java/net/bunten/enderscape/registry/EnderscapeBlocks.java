package net.bunten.enderscape.registry;

import org.betterx.bclib.blocks.BaseRotatedPillarBlock;
import org.betterx.bclib.blocks.BaseStripableLogBlock;
import org.betterx.bclib.blocks.StonePressurePlateBlock;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.blocks.BlinklampBlock;
import net.bunten.enderscape.blocks.BlinklightVines;
import net.bunten.enderscape.blocks.BlinklightVinesBodyBlock;
import net.bunten.enderscape.blocks.BlinklightVinesHeadBlock;
import net.bunten.enderscape.blocks.BulbFlowerBlock;
import net.bunten.enderscape.blocks.BulbLanternBlock;
import net.bunten.enderscape.blocks.CelestialFungusBlock;
import net.bunten.enderscape.blocks.DriftJellyBlock;
import net.bunten.enderscape.blocks.EndMyceliumBlock;
import net.bunten.enderscape.blocks.EndMyceliumPathBlock;
import net.bunten.enderscape.blocks.EnderscapeDoor;
import net.bunten.enderscape.blocks.EnderscapeFlowerPotBlock;
import net.bunten.enderscape.blocks.EnderscapeSignType;
import net.bunten.enderscape.blocks.EnderscapeTrapdoor;
import net.bunten.enderscape.blocks.FlangerBerryBlock;
import net.bunten.enderscape.blocks.FlangerBerryVine;
import net.bunten.enderscape.blocks.GrowthBlock;
import net.bunten.enderscape.blocks.MurushroomsBlock;
import net.bunten.enderscape.blocks.MushroomCapBlock;
import net.bunten.enderscape.blocks.NebuliteBlock;
import net.bunten.enderscape.blocks.NebuliteCauldronBlock;
import net.bunten.enderscape.blocks.NebuliteOreBlock;
import net.bunten.enderscape.blocks.ShadowQuartzOreBlock;
import net.bunten.enderscape.blocks.properties.DirectionProperties;
import net.bunten.enderscape.blocks.properties.StateProperties;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.StoneButtonBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.WoodButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class EnderscapeBlocks {
    
    public static final TagKey<Block> CELESTIAL_STEMS = register("celestial_stems");
    public static final TagKey<Block> DRIFTER_ATTRACTING_BLOCKS = register("drifter_attracting_blocks");
    public static final TagKey<Block> END_MYCELIUM_BLOCKS = register("end_mycelium_blocks");
    public static final TagKey<Block> END_ORE_BLOCKS = register("end_ore_blocks");
    public static final TagKey<Block> LARGE_CELESTIAL_FUNGUS_GENERATABLE = register("large_celestial_fungus_generatable");
    public static final TagKey<Block> LARGE_CELESTIAL_FUNGUS_GROWABLE_ON = register("large_celestial_fungus_growable_on");
    public static final TagKey<Block> MIRROR_LINKABLE_BLOCKS = register("mirror_linkable_blocks");
    public static final TagKey<Block> MURUSHROOM_STEMS = register("murushroom_stems");
    public static final TagKey<Block> MUSHROOM_CAP_BLOCKS = register("mushroom_cap_blocks");
    public static final TagKey<Block> ORE_REPLACEABLES = register("ore_replaceables");
    public static final TagKey<Block> SHADOW_QUARTZ_BLOCKS = register("shadow_quartz_blocks");
    public static final TagKey<Block> SUPPORTS_END_CRYSTAL = register("supports_end_crystal");

    public static final TagKey<Block> PLANTABLE_BLINKLIGHT_VINES = register("plantable/blinklight_vines");
    public static final TagKey<Block> PLANTABLE_BULB_FLOWER = register("plantable/bulb_flower");
    public static final TagKey<Block> PLANTABLE_FLANGER_BERRY_VINE = register("plantable/flanger_berry_vine");
    public static final TagKey<Block> PLANTABLE_FUNGUS = register("plantable/fungus");
    public static final TagKey<Block> PLANTABLE_GROWTH = register("plantable/growth");

    private static final WoodType CELESTIAL_TYPE = WoodType.register(new EnderscapeSignType("celestial"));
    private static final WoodType MURUSHROOM_TYPE = WoodType.register(new EnderscapeSignType("murushroom"));

    public static final Block BLINKLAMP = new BlinklampBlock(FabricBlockSettings.of(Material.STONE, MaterialColor.COLOR_PINK).strength(3).sound(EnderscapeSounds.BLINKLAMP));

    public static final Block VERADITE = new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.DIAMOND).requiresCorrectToolForDrops().strength(1.5F, 6).sound(EnderscapeSounds.VERADITE));
    public static final Block VERADITE_STAIRS = new StairBlock(VERADITE.defaultBlockState(), FabricBlockSettings.copy(VERADITE).requiresCorrectToolForDrops());
    public static final Block VERADITE_SLAB = new SlabBlock(FabricBlockSettings.copy(VERADITE).requiresCorrectToolForDrops());
    public static final Block VERADITE_WALL = new WallBlock(FabricBlockSettings.copy(VERADITE).requiresCorrectToolForDrops());
    
    public static final Block POLISHED_VERADITE = new Block(FabricBlockSettings.copy(VERADITE));
    public static final Block POLISHED_VERADITE_STAIRS = new StairBlock(POLISHED_VERADITE.defaultBlockState(), FabricBlockSettings.copy(POLISHED_VERADITE).requiresCorrectToolForDrops());
    public static final Block POLISHED_VERADITE_SLAB = new SlabBlock(FabricBlockSettings.copy(POLISHED_VERADITE).requiresCorrectToolForDrops());
    public static final Block POLISHED_VERADITE_WALL = new WallBlock(FabricBlockSettings.copy(POLISHED_VERADITE).requiresCorrectToolForDrops());

    public static final Block POLISHED_VERADITE_BUTTON = new StoneButtonBlock(FabricBlockSettings.of(Material.DECORATION).noCollission().strength(0.5F));
    public static final Block POLISHED_VERADITE_PRESSURE_PLATE = new StonePressurePlateBlock(POLISHED_VERADITE);

    public static final Block VERADITE_BRICKS = new Block(FabricBlockSettings.copy(POLISHED_VERADITE).sound(EnderscapeSounds.VERADITE_BRICKS));
    public static final Block VERADITE_BRICK_STAIRS = new StairBlock(VERADITE_BRICKS.defaultBlockState(), FabricBlockSettings.copy(VERADITE_BRICKS).requiresCorrectToolForDrops());
    public static final Block VERADITE_BRICK_SLAB = new SlabBlock(FabricBlockSettings.copy(VERADITE_BRICKS).requiresCorrectToolForDrops());
    public static final Block VERADITE_BRICK_WALL = new WallBlock(FabricBlockSettings.copy(VERADITE_BRICKS).requiresCorrectToolForDrops());
    
    public static final Block CHISELED_VERADITE = new Block(FabricBlockSettings.copy(POLISHED_VERADITE));
    public static final Block VERADITE_PILLAR = new RotatedPillarBlock(FabricBlockSettings.copy(VERADITE_BRICKS));

    public static final Block CELESTIAL_PATH_BLOCK = new EndMyceliumPathBlock(DirectionProperties.create().up(), FabricBlockSettings.of(Material.STONE, MaterialColor.TERRACOTTA_YELLOW).requiresCorrectToolForDrops().strength(3, 9).sound(EnderscapeSounds.EYLIUM).isViewBlocking(EnderscapeBlocks::always).isSuffocating(EnderscapeBlocks::always));
    public static final Block CELESTIAL_MYCELIUM_BLOCK = new EndMyceliumBlock(true, CELESTIAL_PATH_BLOCK, DirectionProperties.create().up(), FabricBlockSettings.of(Material.STONE, MaterialColor.TERRACOTTA_YELLOW).requiresCorrectToolForDrops().strength(3, 9).sound(EnderscapeSounds.EYLIUM).randomTicks());

    public static final Block BULB_FLOWER = new BulbFlowerBlock(FabricBlockSettings.of(Material.REPLACEABLE_PLANT, MaterialColor.WARPED_STEM).noCollission().instabreak().sound(EnderscapeSounds.GROWTH).noOcclusion().lightLevel(state -> 7));
    public static final Block CELESTIAL_GROWTH = new GrowthBlock(DirectionProperties.create().up(), FabricBlockSettings.of(Material.REPLACEABLE_PLANT, MaterialColor.TERRACOTTA_YELLOW).noCollission().instabreak().sound(EnderscapeSounds.GROWTH).noOcclusion());
    public static final Block CELESTIAL_FUNGUS = new CelestialFungusBlock(FabricBlockSettings.of(Material.PLANT, MaterialColor.TERRACOTTA_YELLOW).noCollission().instabreak().sound(EnderscapeSounds.FUNGUS));

    public static final Block FLANGER_BERRY_VINE = new FlangerBerryVine(FabricBlockSettings.of(Material.PLANT, MaterialColor.TERRACOTTA_YELLOW).randomTicks().noCollission().sound(EnderscapeSounds.FLANGER_BERRY_VINE).strength(0.2F));
    public static final Block FLANGER_BERRY_BLOCK = new FlangerBerryBlock(FabricBlockSettings.of(Material.CLAY, MaterialColor.WARPED_WART_BLOCK).randomTicks().strength(0.3F).noOcclusion());

    public static final Block CORRUPT_PATH_BLOCK = new EndMyceliumPathBlock(DirectionProperties.create().all(), FabricBlockSettings.of(Material.STONE, MaterialColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(3, 9).sound(EnderscapeSounds.EYLIUM).isSuffocating(EnderscapeBlocks::always));
    public static final Block CORRUPT_MYCELIUM_BLOCK = new EndMyceliumBlock(false, CORRUPT_PATH_BLOCK, DirectionProperties.create().all(), FabricBlockSettings.of(Material.STONE, MaterialColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(3, 9).sound(EnderscapeSounds.EYLIUM));

    public static final Block BLINKLIGHT_VINES_BODY = new BlinklightVinesBodyBlock(FabricBlockSettings.of(Material.PLANT, MaterialColor.DIAMOND).noCollission().sound(EnderscapeSounds.BLINKLIGHT_VINES).strength(0.8F).randomTicks().lightLevel(state -> BlinklightVines.getLuminance(state)));
    public static final Block BLINKLIGHT_VINES_HEAD = new BlinklightVinesHeadBlock(FabricBlockSettings.of(Material.PLANT, MaterialColor.DIAMOND).noCollission().sound(EnderscapeSounds.BLINKLIGHT_VINES).strength(0.8F).randomTicks().lightLevel(state -> BlinklightVines.getLuminance(state)));
    public static final Block CORRUPT_GROWTH = new GrowthBlock(DirectionProperties.create().all(), FabricBlockSettings.of(Material.REPLACEABLE_PLANT, MaterialColor.COLOR_PURPLE).noCollission().instabreak().sound(EnderscapeSounds.GROWTH).noOcclusion());
    public static final Block MURUSHROOMS = new MurushroomsBlock(FabricBlockSettings.of(Material.REPLACEABLE_PLANT, MaterialColor.TERRACOTTA_CYAN).noCollission().instabreak().sound(EnderscapeSounds.FUNGUS).noOcclusion());
    
    public static final Block POTTED_BULB_FLOWER = new EnderscapeFlowerPotBlock(BULB_FLOWER, FabricBlockSettings.of(Material.DECORATION).instabreak().noOcclusion().lightLevel(state -> 7));
    public static final Block POTTED_CELESTIAL_GROWTH = new EnderscapeFlowerPotBlock(CELESTIAL_GROWTH, FabricBlockSettings.copy(Blocks.POTTED_ACACIA_SAPLING));
    public static final Block POTTED_CELESTIAL_FUNGUS = new EnderscapeFlowerPotBlock(CELESTIAL_FUNGUS, FabricBlockSettings.copy(Blocks.POTTED_ACACIA_SAPLING));
    public static final Block POTTED_CORRUPT_GROWTH = new EnderscapeFlowerPotBlock(CORRUPT_GROWTH, FabricBlockSettings.copy(Blocks.POTTED_ACACIA_SAPLING));
    public static final Block POTTED_BLINKLIGHT = new EnderscapeFlowerPotBlock(BLINKLIGHT_VINES_HEAD, FabricBlockSettings.copy(Blocks.POTTED_ACACIA_SAPLING).lightLevel((state) -> 15));

    public static final Block NEBULITE_ORE = new NebuliteOreBlock(FabricBlockSettings.of(Material.STONE, MaterialColor.SAND).requiresCorrectToolForDrops().strength(3, 12).randomTicks().sound(EnderscapeSounds.NEBULITE_ORE));
    public static final Block NEBULITE_BLOCK = new NebuliteBlock(FabricBlockSettings.of(Material.METAL, MaterialColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(5, 12).lightLevel(state -> state.getValue(StateProperties.POWERED) ? 6 : 0).sound(EnderscapeSounds.NEBULITE_BLOCK));
    public static final Block NEBULITE_CAULDRON = new NebuliteCauldronBlock(FabricBlockSettings.copy(Blocks.CAULDRON).lightLevel((state) -> NebuliteCauldronBlock.getLuminance(state)));

    public static final Block SHADOW_QUARTZ_ORE = new ShadowQuartzOreBlock(FabricBlockSettings.of(Material.STONE, MaterialColor.SAND).requiresCorrectToolForDrops().strength(3).sound(EnderscapeSounds.SHADOW_QUARTZ_ORE));

    public static final Block SHADOW_QUARTZ_BLOCK = new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.TERRACOTTA_BLUE).requiresCorrectToolForDrops().strength(3).sound(EnderscapeSounds.SHADOW_QUARTZ));
    public static final Block SHADOW_QUARTZ_BLOCK_STAIRS = new StairBlock(SHADOW_QUARTZ_BLOCK.defaultBlockState(), FabricBlockSettings.copy(SHADOW_QUARTZ_BLOCK).requiresCorrectToolForDrops());
    public static final Block SHADOW_QUARTZ_BLOCK_SLAB = new SlabBlock(FabricBlockSettings.copy(SHADOW_QUARTZ_BLOCK).requiresCorrectToolForDrops());
    public static final Block SHADOW_QUARTZ_BLOCK_WALL = new WallBlock(FabricBlockSettings.copy(SHADOW_QUARTZ_BLOCK).requiresCorrectToolForDrops());
    
    public static final Block SMOOTH_SHADOW_QUARTZ = new Block(FabricBlockSettings.copy(SHADOW_QUARTZ_BLOCK));
    public static final Block SMOOTH_SHADOW_QUARTZ_STAIRS = new StairBlock(SMOOTH_SHADOW_QUARTZ.defaultBlockState(), FabricBlockSettings.copy(SMOOTH_SHADOW_QUARTZ).requiresCorrectToolForDrops());
    public static final Block SMOOTH_SHADOW_QUARTZ_SLAB = new SlabBlock(FabricBlockSettings.copy(SMOOTH_SHADOW_QUARTZ).requiresCorrectToolForDrops());
    public static final Block SMOOTH_SHADOW_QUARTZ_WALL = new WallBlock(FabricBlockSettings.copy(SMOOTH_SHADOW_QUARTZ).requiresCorrectToolForDrops());
    
    public static final Block SHADOW_QUARTZ_BRICKS = new Block(FabricBlockSettings.copy(SHADOW_QUARTZ_BLOCK));
    public static final Block SHADOW_QUARTZ_BRICK_STAIRS = new StairBlock(SHADOW_QUARTZ_BRICKS.defaultBlockState(), FabricBlockSettings.copy(SHADOW_QUARTZ_BRICKS).requiresCorrectToolForDrops());
    public static final Block SHADOW_QUARTZ_BRICK_SLAB = new SlabBlock(FabricBlockSettings.copy(SHADOW_QUARTZ_BRICKS).requiresCorrectToolForDrops());
    public static final Block SHADOW_QUARTZ_BRICK_WALL = new WallBlock(FabricBlockSettings.copy(SHADOW_QUARTZ_BRICKS).requiresCorrectToolForDrops());

    public static final Block CHISELED_SHADOW_QUARTZ = new Block(FabricBlockSettings.copy(SHADOW_QUARTZ_BLOCK));
    public static final Block SHADOW_QUARTZ_PILLAR = new RotatedPillarBlock(FabricBlockSettings.copy(SHADOW_QUARTZ_BLOCK));

    public static final Block STRIPPED_CELESTIAL_STEM = new BaseRotatedPillarBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.COLOR_ORANGE).strength(1).sound(EnderscapeSounds.STEM));
    public static final Block CELESTIAL_STEM = new BaseStripableLogBlock(MaterialColor.TERRACOTTA_WHITE, STRIPPED_CELESTIAL_STEM);
    public static final Block STRIPPED_CELESTIAL_HYPHAE = new BaseRotatedPillarBlock(FabricBlockSettings.copy(STRIPPED_CELESTIAL_STEM));
    public static final Block CELESTIAL_HYPHAE = new BaseStripableLogBlock(MaterialColor.TERRACOTTA_WHITE, STRIPPED_CELESTIAL_HYPHAE);

    public static final Block CELESTIAL_CAP = new MushroomCapBlock(FabricBlockSettings.of(Material.GRASS, MaterialColor.TERRACOTTA_YELLOW).strength(1).sound(EnderscapeSounds.FUNGUS_CAP));
    public static final Block CELESTIAL_BRICKS = new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.TERRACOTTA_YELLOW).strength(1.5F, 6).sound(EnderscapeSounds.FUNGUS_BRICKS));
    public static final Block CELESTIAL_BRICK_STAIRS = new StairBlock(CELESTIAL_BRICKS.defaultBlockState(), FabricBlockSettings.copy(CELESTIAL_BRICKS));
    public static final Block CELESTIAL_BRICK_SLAB = new SlabBlock(FabricBlockSettings.copy(CELESTIAL_BRICKS));
    public static final Block CELESTIAL_BRICK_WALL = new WallBlock(FabricBlockSettings.copy(CELESTIAL_BRICKS));

    public static final Block CELESTIAL_PLANKS = new Block(FabricBlockSettings.of(Material.WOOD, MaterialColor.COLOR_ORANGE).strength(2, 3).sound(EnderscapeSounds.FUNGUS_WOOD));
    public static final Block CELESTIAL_STAIRS = new StairBlock(CELESTIAL_PLANKS.defaultBlockState(), FabricBlockSettings.copy(CELESTIAL_PLANKS));
    public static final Block CELESTIAL_SLAB = new SlabBlock(FabricBlockSettings.copy(CELESTIAL_PLANKS).requiresCorrectToolForDrops());
    public static final Block CELESTIAL_PRESSURE_PLATE = new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, FabricBlockSettings.of(Material.WOOD, CELESTIAL_PLANKS.defaultMaterialColor()).noCollission().strength(0.5F).sound(EnderscapeSounds.FUNGUS_WOOD));
    public static final Block CELESTIAL_FENCE = new FenceBlock(FabricBlockSettings.copy(CELESTIAL_PLANKS).requiresCorrectToolForDrops());
    public static final Block CELESTIAL_DOOR = new EnderscapeDoor(FabricBlockSettings.of(Material.WOOD, CELESTIAL_PLANKS.defaultMaterialColor()).strength(3).sound(EnderscapeSounds.FUNGUS_WOOD).noOcclusion());
    public static final Block CELESTIAL_TRAPDOOR = new EnderscapeTrapdoor(FabricBlockSettings.of(Material.WOOD, MaterialColor.COLOR_ORANGE).strength(3).sound(EnderscapeSounds.FUNGUS_WOOD).noOcclusion());
    public static final Block CELESTIAL_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copy(CELESTIAL_PLANKS).requiresCorrectToolForDrops());
    public static final Block CELESTIAL_BUTTON = new WoodButtonBlock(FabricBlockSettings.of(Material.WOOD).noCollission().strength(0.5F).sound(EnderscapeSounds.FUNGUS_WOOD));
    public static final Block CELESTIAL_SIGN = new StandingSignBlock(FabricBlockSettings.copy(Blocks.OAK_SIGN).sound(EnderscapeSounds.FUNGUS_WOOD), CELESTIAL_TYPE);
    public static final Block CELESTIAL_WALL_SIGN = new WallSignBlock(FabricBlockSettings.copy(Blocks.OAK_WALL_SIGN).sound(EnderscapeSounds.FUNGUS_WOOD), CELESTIAL_TYPE);

    public static final Block STRIPPED_MURUSHROOM_STEM = new BaseRotatedPillarBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.ICE).strength(2).sound(EnderscapeSounds.STEM));
    public static final Block MURUSHROOM_STEM = new BaseStripableLogBlock(MaterialColor.COLOR_BLACK, STRIPPED_MURUSHROOM_STEM);
    public static final Block STRIPPED_MURUSHROOM_HYPHAE = new BaseRotatedPillarBlock(FabricBlockSettings.copy(STRIPPED_MURUSHROOM_STEM));
    public static final Block MURUSHROOM_HYPHAE = new BaseStripableLogBlock(MaterialColor.COLOR_BLACK, STRIPPED_MURUSHROOM_HYPHAE);

    public static final Block MURUSHROOM_CAP = new MushroomCapBlock(FabricBlockSettings.of(Material.GRASS, MaterialColor.TERRACOTTA_CYAN).strength(1).sound(EnderscapeSounds.FUNGUS_CAP));
    public static final Block MURUSHROOM_BRICKS = new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.TERRACOTTA_CYAN).strength(1.5F, 6).sound(EnderscapeSounds.FUNGUS_BRICKS));
    public static final Block MURUSHROOM_BRICK_STAIRS = new StairBlock(MURUSHROOM_BRICKS.defaultBlockState(), FabricBlockSettings.copy(MURUSHROOM_BRICKS));
    public static final Block MURUSHROOM_BRICK_SLAB = new SlabBlock(FabricBlockSettings.copy(MURUSHROOM_BRICKS));
    public static final Block MURUSHROOM_BRICK_WALL = new WallBlock(FabricBlockSettings.copy(MURUSHROOM_BRICKS));

    public static final Block MURUSHROOM_PLANKS = new Block(FabricBlockSettings.of(Material.WOOD, MaterialColor.ICE).strength(2, 3).sound(EnderscapeSounds.FUNGUS_WOOD));
    public static final Block MURUSHROOM_STAIRS = new StairBlock(MURUSHROOM_PLANKS.defaultBlockState(), FabricBlockSettings.copy(MURUSHROOM_PLANKS));
    public static final Block MURUSHROOM_SLAB = new SlabBlock(FabricBlockSettings.copy(MURUSHROOM_PLANKS).requiresCorrectToolForDrops());
    public static final Block MURUSHROOM_PRESSURE_PLATE = new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, FabricBlockSettings.of(Material.WOOD, MURUSHROOM_PLANKS.defaultMaterialColor()).noCollission().strength(0.5F).sound(EnderscapeSounds.FUNGUS_WOOD));
    public static final Block MURUSHROOM_FENCE = new FenceBlock(FabricBlockSettings.copy(MURUSHROOM_PLANKS).requiresCorrectToolForDrops());
    public static final Block MURUSHROOM_DOOR = new EnderscapeDoor(FabricBlockSettings.of(Material.WOOD, MURUSHROOM_PLANKS.defaultMaterialColor()).strength(3).sound(EnderscapeSounds.FUNGUS_WOOD).noOcclusion());
    public static final Block MURUSHROOM_TRAPDOOR = new EnderscapeTrapdoor(FabricBlockSettings.of(Material.WOOD, MaterialColor.ICE).strength(3).sound(EnderscapeSounds.FUNGUS_WOOD).noOcclusion());
    public static final Block MURUSHROOM_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copy(MURUSHROOM_PLANKS).requiresCorrectToolForDrops());
    public static final Block MURUSHROOM_BUTTON = new WoodButtonBlock(FabricBlockSettings.of(Material.WOOD).noCollission().strength(0.5F).sound(EnderscapeSounds.FUNGUS_WOOD));
    public static final Block MURUSHROOM_SIGN = new StandingSignBlock(FabricBlockSettings.copy(Blocks.OAK_SIGN).sound(EnderscapeSounds.FUNGUS_WOOD), MURUSHROOM_TYPE);
    public static final Block MURUSHROOM_WALL_SIGN = new WallSignBlock(FabricBlockSettings.copy(Blocks.OAK_WALL_SIGN).sound(EnderscapeSounds.FUNGUS_WOOD), MURUSHROOM_TYPE);

    public static final Block BULB_LANTERN = new BulbLanternBlock(FabricBlockSettings.copy(Blocks.LANTERN));
    public static final Block DRIFT_JELLY_BLOCK = new DriftJellyBlock(FabricBlockSettings.of(Material.CLAY, MaterialColor.WARPED_WART_BLOCK).instabreak().sound(EnderscapeSounds.DRIFT_JELLY).noOcclusion());
    
    private static TagKey<Block> register(String name) {
        return TagKey.create(Registry.BLOCK_REGISTRY, Enderscape.id(name));
    }

    private static boolean always(BlockState state, BlockGetter world, BlockPos pos) {
        return true;
    }
}