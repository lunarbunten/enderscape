package net.enderscape.registry;

import net.enderscape.Enderscape;
import net.enderscape.blocks.*;
import net.enderscape.util.FungusType;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.SignItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.Tag;
import net.minecraft.util.SignType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

public class EndBlocks {

    public static final Tag<Block> CELESTIAL_STEMS = register("celestial_stems");
    public static final Tag<Block> CHORUS_BLOCKS = register("chorus_blocks");
    public static final Tag<Block> END_MYCELIUM_BLOCKS = register("end_mycelium_blocks");
    public static final Tag<Block> END_ORES = register("end_ores");
    public static final Tag<Block> END_STONE_BLOCKS = register("end_stone_blocks");
    public static final Tag<Block> END_STONE_BRICK_BLOCKS = register("end_stone_brick_blocks");
    public static final Tag<Block> GROWTH_PLANTABLE_BLOCKS = register("growth_plantable_blocks");
    public static final Tag<Block> PURPUR_BLOCKS = register("purpur_blocks");
    public static final Tag<Block> SHADOW_QUARTZ_BLOCKS = register("shadow_quartz_blocks");

    public static final Block CELESTIAL_MYCELIUM_BLOCK = new EndMyceliumBlock(FabricBlockSettings.of(Material.STONE, MapColor.TERRACOTTA_YELLOW).requiresTool().strength(3, 9).sounds(EndSounds.EYLIUM).ticksRandomly());

    public static final Block CELESTIAL_GROWTH = new EndGrowthBlock(FabricBlockSettings.of(Material.REPLACEABLE_PLANT, MapColor.TERRACOTTA_YELLOW).noCollision().breakInstantly().sounds(EndSounds.GROWTH).nonOpaque());

    public static final Block BULB_FLOWER = new BulbFlowerBlock(FabricBlockSettings.of(Material.REPLACEABLE_PLANT, MapColor.TEAL).noCollision().breakInstantly().sounds(EndSounds.GROWTH).nonOpaque().luminance(6));

    public static final Block MURUSHROOMS = new MurushroomsBlock(FabricBlockSettings.of(Material.REPLACEABLE_PLANT, MapColor.TERRACOTTA_CYAN).noCollision().breakInstantly().sounds(BlockSoundGroup.FUNGUS).nonOpaque().ticksRandomly());

    public static final Block FLANGER_BERRY_VINE = new FlangerBerryVine(FabricBlockSettings.of(Material.PLANT, MapColor.TERRACOTTA_YELLOW).ticksRandomly().noCollision().sounds(EndSounds.FLANGER_BERRY_VINE));
    public static final Block FLANGER_BERRY_BLOCK = new FlangerBerryBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MapColor.BRIGHT_TEAL).ticksRandomly().sounds(BlockSoundGroup.NETHER_SPROUTS).strength(0.3F).nonOpaque());

    public static final Block SHADOW_QUARTZ_BLOCK = new Block(FabricBlockSettings.of(Material.STONE, MapColor.TERRACOTTA_BLUE).requiresTool().strength(3).sounds(EndSounds.SHADOW_QUARTZ));
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

    public static final Block SHADOWSTEEL = new Block(FabricBlockSettings.copy(Blocks.BEDROCK));
    public static final Block CHISELED_SHADOWSTEEL = new Block(FabricBlockSettings.copy(SHADOWSTEEL));
    public static final Block SHADOWSTEEL_PILLAR = new PillarBlock(FabricBlockSettings.copy(SHADOWSTEEL));
    public static final Block SMALL_SHADOWSTEEL_PILLAR = new PillarBlock(FabricBlockSettings.copy(SHADOWSTEEL).nonOpaque());

    public static final Block CELESTIAL_FUNGUS = new EndFungusBlock(FungusType.CELESTIAL, FabricBlockSettings.of(Material.PLANT, MapColor.TERRACOTTA_YELLOW).noCollision().breakInstantly().sounds(BlockSoundGroup.FUNGUS));

    private static final SignType CELESTIAL_TYPE = SignType.register(new EndSignType("celestial"));

    public static final Block CELESTIAL_STEM = new PillarBlock(FabricBlockSettings.of(Material.WOOD, MapColor.WHITE).strength(1).sounds(EndSounds.STEM));
    public static final Block STRIPPED_CELESTIAL_STEM = new PillarBlock(FabricBlockSettings.copy(CELESTIAL_STEM));
    public static final Block CELESTIAL_HYPHAE = new PillarBlock(FabricBlockSettings.copy(CELESTIAL_STEM));
    public static final Block STRIPPED_CELESTIAL_HYPHAE = new PillarBlock(FabricBlockSettings.copy(CELESTIAL_STEM));
    public static final Block CELESTIAL_CAP = new MushroomCapBlock(FabricBlockSettings.of(Material.SOLID_ORGANIC, MapColor.GOLD).strength(1).sounds(EndSounds.DEEP_FUNGUS));
    public static final Block CELESTIAL_PLANKS = new Block(FabricBlockSettings.of(Material.WOOD, MapColor.MAGENTA).strength(2, 3).sounds(BlockSoundGroup.WOOD));
    public static final Block CELESTIAL_STAIRS = new StairsBlock(CELESTIAL_PLANKS.getDefaultState(), FabricBlockSettings.copy(CELESTIAL_PLANKS));
    public static final Block CELESTIAL_SLAB = new SlabBlock(FabricBlockSettings.copy(CELESTIAL_PLANKS).requiresTool());
    public static final Block CELESTIAL_PRESSURE_PLATE = new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.of(Material.WOOD, CELESTIAL_PLANKS.getDefaultMapColor()).noCollision().strength(0.5F).sounds(BlockSoundGroup.WOOD));
    public static final Block CELESTIAL_FENCE = new FenceBlock(FabricBlockSettings.copy(CELESTIAL_PLANKS).requiresTool());
    public static final Block CELESTIAL_DOOR = new EndDoor(FabricBlockSettings.of(Material.WOOD, CELESTIAL_PLANKS.getDefaultMapColor()).strength(3).sounds(BlockSoundGroup.WOOD).nonOpaque());
    public static final Block CELESTIAL_TRAPDOOR = new EndTrapdoor(FabricBlockSettings.of(Material.WOOD, MapColor.MAGENTA).strength(3).sounds(BlockSoundGroup.WOOD).nonOpaque());
    public static final Block CELESTIAL_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copy(CELESTIAL_PLANKS).requiresTool());
    public static final Block CELESTIAL_BUTTON = new WoodenButtonBlock(FabricBlockSettings.of(Material.WOOD).noCollision().strength(0.5F).sounds(BlockSoundGroup.WOOD));
    public static final Block CELESTIAL_SIGN = new EndSignBlock(CELESTIAL_TYPE, FabricBlockSettings.copy(Blocks.OAK_SIGN));
    public static final Block CELESTIAL_WALL_SIGN = new EndWallSignBlock(CELESTIAL_TYPE, FabricBlockSettings.copy(Blocks.OAK_WALL_SIGN));

    public static final Block SHADOW_QUARTZ_ORE = new ShadowQuartzOreBlock(FabricBlockSettings.of(Material.STONE, MapColor.PALE_YELLOW).requiresTool().strength(3).sounds(EndSounds.END_STONE));
    public static final Block NEBULITE_ORE = new NebuliteOreBlock(FabricBlockSettings.of(Material.STONE, MapColor.PALE_YELLOW).requiresTool().strength(12, 1200).sounds(EndSounds.NEBULITE_ORE));
    public static final Block NEBULITE_BLOCK = new Block(FabricBlockSettings.of(Material.METAL, MapColor.MAGENTA).requiresTool().strength(4, 1200).sounds(EndSounds.NEBULITE_BLOCK));
    public static final Block NEBULITE_CAULDRON = new NebuliteCauldronBlock(FabricBlockSettings.copy(Blocks.CAULDRON));

    public static final Block DRIFT_JELLY_BLOCK = new DriftJellyBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MapColor.MAGENTA).breakInstantly().sounds(BlockSoundGroup.HONEY).nonOpaque());

    @SuppressWarnings("unused")
    private static boolean always(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    private static Tag<Block> register(String name) {
        return TagFactory.BLOCK.create(Enderscape.id(name));
    }

    private static void register(String name, Block block, boolean hasItem) {
        Registry.register(Registry.BLOCK, Enderscape.id(name), block);
        if (hasItem) {
            BlockItem item = new BlockItem(block, new Item.Settings().group(Enderscape.GROUP));
            register(name, item);
        }
    }

    private static <T extends Item> T register(String id, T item) {
        return Registry.register(Registry.ITEM, Enderscape.id(id), item);
    }

    private static void registerPlantlife() {

        register("celestial_mycelium_block", CELESTIAL_MYCELIUM_BLOCK, true);

        register("celestial_growth", CELESTIAL_GROWTH, true);

        register("bulb_flower", BULB_FLOWER, true);

        register("celestial_fungus", CELESTIAL_FUNGUS, true);

        register("murushrooms", MURUSHROOMS, false);

        register("flanger_berry_vine", FLANGER_BERRY_VINE, false);
        register("flanger_berry_block", FLANGER_BERRY_BLOCK, true);
    }

    private static void registerStones() {

        register("nebulite_ore", NEBULITE_ORE, true);
        register("nebulite_block", NEBULITE_BLOCK, true);
        register("nebulite_cauldron", NEBULITE_CAULDRON, false);

        register("shadow_quartz_ore", SHADOW_QUARTZ_ORE, true);

        register("shadow_quartz_block", SHADOW_QUARTZ_BLOCK, true);
        register("shadow_quartz_block_stairs", SHADOW_QUARTZ_BLOCK_STAIRS, true);
        register("shadow_quartz_block_slab", SHADOW_QUARTZ_BLOCK_SLAB, true);
        register("shadow_quartz_block_wall", SHADOW_QUARTZ_BLOCK_WALL, true);

        register("smooth_shadow_quartz", SMOOTH_SHADOW_QUARTZ, true);
        register("smooth_shadow_quartz_stairs", SMOOTH_SHADOW_QUARTZ_STAIRS, true);
        register("smooth_shadow_quartz_slab", SMOOTH_SHADOW_QUARTZ_SLAB, true);
        register("smooth_shadow_quartz_wall", SMOOTH_SHADOW_QUARTZ_WALL, true);

        register("shadow_quartz_bricks", SHADOW_QUARTZ_BRICKS, true);
        register("shadow_quartz_brick_stairs", SHADOW_QUARTZ_BRICK_STAIRS, true);
        register("shadow_quartz_brick_slab", SHADOW_QUARTZ_BRICK_SLAB, true);
        register("shadow_quartz_brick_wall", SHADOW_QUARTZ_BRICK_WALL, true);

        register("chiseled_shadow_quartz", CHISELED_SHADOW_QUARTZ, true);
        register("shadow_quartz_pillar", SHADOW_QUARTZ_PILLAR, true);
    }

    private static void registerWoods() {

        register("celestial_stem", CELESTIAL_STEM, true);
        register("stripped_celestial_stem", STRIPPED_CELESTIAL_STEM, true);
        register("celestial_hyphae", CELESTIAL_HYPHAE, true);
        register("stripped_celestial_hyphae", STRIPPED_CELESTIAL_HYPHAE, true);
        register("celestial_cap", CELESTIAL_CAP, true);
        register("celestial_planks", CELESTIAL_PLANKS, true);
        register("celestial_stairs", CELESTIAL_STAIRS, true);
        register("celestial_slab", CELESTIAL_SLAB, true);
        register("celestial_pressure_plate", CELESTIAL_PRESSURE_PLATE, true);
        register("celestial_fence", CELESTIAL_FENCE, true);
        register("celestial_door", CELESTIAL_DOOR, true);
        register("celestial_trapdoor", CELESTIAL_TRAPDOOR, true);
        register("celestial_fence_gate", CELESTIAL_FENCE_GATE, true);
        register("celestial_button", CELESTIAL_BUTTON, true);

        register("celestial_sign", CELESTIAL_SIGN, false);
        register("celestial_wall_sign", CELESTIAL_WALL_SIGN, false);

        register("celestial_sign", new SignItem(new FabricItemSettings().group(Enderscape.GROUP), CELESTIAL_SIGN, CELESTIAL_WALL_SIGN));
    }
    private static void registerUnbreakables() {
        register("shadowsteel_block", SHADOWSTEEL, false);
        register("chiseled_shadowsteel", CHISELED_SHADOWSTEEL, false);
        register("shadowsteel_pillar", SHADOWSTEEL_PILLAR, false);
        register("small_shadowsteel_pillar", SMALL_SHADOWSTEEL_PILLAR, false);
    }

    public static void init() {
        registerPlantlife();
        registerWoods();
        registerStones();
        registerUnbreakables();

        register("drift_jelly_block", DRIFT_JELLY_BLOCK, true);
    }
}