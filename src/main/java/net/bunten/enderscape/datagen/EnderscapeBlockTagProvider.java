package net.bunten.enderscape.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.EnderscapeBlocks.*;
import static net.bunten.enderscape.registry.tag.EnderscapeBlockTags.*;
import static net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags.*;
import static net.minecraft.world.level.block.Blocks.*;

public class EnderscapeBlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public EnderscapeBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        valueLookupBuilder(ALL_ETCHED_MAGNIA_BLOCKS).forceAddTag(ETCHED_ALLURING_MAGNIA_BLOCKS).forceAddTag(ETCHED_REPULSIVE_MAGNIA_BLOCKS);
        valueLookupBuilder(CELESTIAL_BRICK_BLOCKS).add(CELESTIAL_BRICKS, CELESTIAL_BRICK_SLAB, CELESTIAL_BRICK_STAIRS, CELESTIAL_BRICK_WALL);
        valueLookupBuilder(CELESTIAL_CHANTERELLE_MATURES_ON).add(CELESTIAL_OVERGROWTH);
        valueLookupBuilder(CELESTIAL_CORRUPTS_ON).add(CORRUPT_OVERGROWTH);
        valueLookupBuilder(CELESTIAL_GROVE_VEGETATION_PLANTABLE_ON).forceAddTag(OVERGROWTH_BLOCKS).forceAddTag(BlockTags.DIRT).add(END_STONE, MIRESTONE, VEILED_END_STONE);
        valueLookupBuilder(CELESTIAL_STEMS).add(CELESTIAL_STEM, STRIPPED_CELESTIAL_STEM, CELESTIAL_HYPHAE, STRIPPED_CELESTIAL_HYPHAE);

        valueLookupBuilder(CELESTIAL_WOOD_BLOCKS).forceAddTag(CELESTIAL_STEMS).add(
                CELESTIAL_BUTTON,
                CELESTIAL_DOOR,
                CELESTIAL_FENCE,
                CELESTIAL_FENCE_GATE,
                CELESTIAL_HANGING_SIGN,
                CELESTIAL_PLANKS,
                CELESTIAL_SIGN,
                CELESTIAL_SLAB,
                CELESTIAL_STAIRS,
                CELESTIAL_TRAPDOOR,
                CELESTIAL_WALL_HANGING_SIGN,
                CELESTIAL_WALL_SIGN
        );

        valueLookupBuilder(CHANTERELLE_BRICK_BLOCKS).forceAddTag(CELESTIAL_BRICK_BLOCKS).forceAddTag(MURUBLIGHT_BRICK_BLOCKS);
        valueLookupBuilder(CHANTERELLE_CAP_BLOCKS).add(CELESTIAL_CAP, MURUBLIGHT_CAP);
        valueLookupBuilder(CHORUS_VEGETATION_PLANTABLE_ON).add(END_STONE, VEILED_END_STONE, CELESTIAL_OVERGROWTH);
        valueLookupBuilder(CORRUPTION_PURIFIES_ON);
        valueLookupBuilder(CORRUPT_BARRENS_VEGETATION_PLANTABLE_ON).forceAddTag(OVERGROWTH_BLOCKS).forceAddTag(BlockTags.DIRT).add(END_STONE, MIRESTONE, VEILED_END_STONE);
        valueLookupBuilder(DRY_END_GROWTH_PLANTABLE_ON).add(END_STONE, VEILED_END_STONE, CELESTIAL_OVERGROWTH);
        valueLookupBuilder(DUSK_PURPUR_BLOCKS).add(DUSK_PURPUR_BLOCK, DUSK_PURPUR_PILLAR, DUSK_PURPUR_STAIRS, DUSK_PURPUR_SLAB, DUSK_PURPUR_WALL, CHISELED_DUSK_PURPUR);
        valueLookupBuilder(ENDERMITE_SAFE_WHEN_NEARBY).add(CORRUPT_OVERGROWTH, CORRUPT_PATH, CORRUPT_GROWTH, POTTED_CORRUPT_GROWTH);
        valueLookupBuilder(END_ORE_BLOCKS).add(NEBULITE_ORE, SHADOLINE_ORE, MIRESTONE_NEBULITE_ORE, MIRESTONE_SHADOLINE_ORE);
        valueLookupBuilder(ETCHED_ALLURING_MAGNIA_BLOCKS).add(ETCHED_ALLURING_MAGNIA, ETCHED_ALLURING_MAGNIA_STAIRS, ETCHED_ALLURING_MAGNIA_SLAB, ETCHED_ALLURING_MAGNIA_WALL);
        valueLookupBuilder(ETCHED_REPULSIVE_MAGNIA_BLOCKS).add(ETCHED_REPULSIVE_MAGNIA, ETCHED_REPULSIVE_MAGNIA_STAIRS, ETCHED_REPULSIVE_MAGNIA_SLAB, ETCHED_REPULSIVE_MAGNIA_WALL);
        valueLookupBuilder(FLANGER_BERRY_VINE_PLANTABLE_ON).forceAddTag(CHANTERELLE_CAP_BLOCKS);
        valueLookupBuilder(FLANGER_BERRY_VINE_SUPPORTS).add(FLANGER_BERRY_FLOWER, UNRIPE_FLANGER_BERRY_BLOCK, RIPE_FLANGER_BERRY_BLOCK);

        valueLookupBuilder(KURODITE_BLOCKS).add(
                CHISELED_KURODITE,
                KURODITE,
                KURODITE_BRICKS,
                KURODITE_BRICK_SLAB,
                KURODITE_BRICK_STAIRS,
                KURODITE_BRICK_WALL,
                KURODITE_SLAB,
                KURODITE_STAIRS,
                KURODITE_WALL,
                POLISHED_KURODITE,
                POLISHED_KURODITE_BUTTON,
                POLISHED_KURODITE_PRESSURE_PLATE,
                POLISHED_KURODITE_SLAB,
                POLISHED_KURODITE_STAIRS,
                POLISHED_KURODITE_WALL
        );

        valueLookupBuilder(MAGNIA_ARCH_REPLACEABLE).add(END_STONE, MIRESTONE, ALLURING_MAGNIA);
        valueLookupBuilder(MAGNIA_BLOCKS).add(ALLURING_MAGNIA, REPULSIVE_MAGNIA);
        valueLookupBuilder(MAGNIA_SPROUTS).add(ALLURING_MAGNIA_SPROUT, REPULSIVE_MAGNIA_SPROUT);
        valueLookupBuilder(MAGNIA_TOWER_REPLACEABLE).add(END_STONE, MIRESTONE, ALLURING_MAGNIA, REPULSIVE_MAGNIA);

        valueLookupBuilder(MIRESTONE_BLOCKS).add(
                CHISELED_MIRESTONE,
                MIRESTONE,
                MIRESTONE_BRICKS,
                MIRESTONE_BRICK_SLAB,
                MIRESTONE_BRICK_STAIRS,
                MIRESTONE_BRICK_WALL,
                MIRESTONE_SLAB,
                MIRESTONE_STAIRS,
                MIRESTONE_WALL,
                POLISHED_MIRESTONE,
                POLISHED_MIRESTONE_BUTTON,
                POLISHED_MIRESTONE_PRESSURE_PLATE,
                POLISHED_MIRESTONE_SLAB,
                POLISHED_MIRESTONE_STAIRS,
                POLISHED_MIRESTONE_WALL
        );

        valueLookupBuilder(MURUBLIGHT_BRICK_BLOCKS).add(MURUBLIGHT_BRICKS, MURUBLIGHT_BRICK_SLAB, MURUBLIGHT_BRICK_STAIRS, MURUBLIGHT_BRICK_WALL);
        valueLookupBuilder(MURUBLIGHT_CHANTERELLE_MATURES_ON).add(CORRUPT_OVERGROWTH);
        valueLookupBuilder(MURUBLIGHT_STEMS).add(MURUBLIGHT_STEM, STRIPPED_MURUBLIGHT_STEM, MURUBLIGHT_HYPHAE, STRIPPED_MURUBLIGHT_HYPHAE);

        valueLookupBuilder(MURUBLIGHT_WOOD_BLOCKS).forceAddTag(MURUBLIGHT_STEMS).add(
                MURUBLIGHT_BUTTON,
                MURUBLIGHT_DOOR,
                MURUBLIGHT_FENCE,
                MURUBLIGHT_FENCE_GATE,
                MURUBLIGHT_HANGING_SIGN,
                MURUBLIGHT_PLANKS,
                MURUBLIGHT_SIGN,
                MURUBLIGHT_SLAB,
                MURUBLIGHT_STAIRS,
                MURUBLIGHT_TRAPDOOR,
                MURUBLIGHT_WALL_HANGING_SIGN,
                MURUBLIGHT_WALL_SIGN
        );

        valueLookupBuilder(NEW_END_STONE_BLOCKS).add(
                CHISELED_END_STONE,
                END_STONE_SLAB,
                END_STONE_STAIRS,
                END_STONE_WALL,
                POLISHED_END_STONE,
                POLISHED_END_STONE_BUTTON,
                POLISHED_END_STONE_PRESSURE_PLATE,
                POLISHED_END_STONE_SLAB,
                POLISHED_END_STONE_STAIRS,
                POLISHED_END_STONE_WALL
        );

        valueLookupBuilder(NEW_PURPUR_BLOCKS).add(CHISELED_PURPUR, PURPUR_WALL);
        valueLookupBuilder(ORE_REPLACEABLE).add(END_STONE, MIRESTONE);
        valueLookupBuilder(OVERGROWTH_BLOCKS).add(CELESTIAL_OVERGROWTH, CORRUPT_OVERGROWTH);
        valueLookupBuilder(OVERHEATS_MAGNIA_SPROUTS).add(MAGMA_BLOCK);
        valueLookupBuilder(PURPUR_TILE_BLOCKS).add(PURPUR_TILES, PURPUR_TILE_STAIRS, PURPUR_TILE_SLAB);
        valueLookupBuilder(RUBBLEMITE_SPAWNABLE_ON).add(END_STONE, MIRESTONE, VERADITE, KURODITE, VEILED_END_STONE, CELESTIAL_OVERGROWTH, CORRUPT_OVERGROWTH, ALLURING_MAGNIA, REPULSIVE_MAGNIA);
        valueLookupBuilder(RUSTLE_FOOD).add(MURUBLIGHT_CHANTERELLE);
        valueLookupBuilder(RUSTLE_PREFERRED).add(VEILED_END_STONE);
        valueLookupBuilder(RUSTLE_SPAWNABLE_ON).add(VEILED_END_STONE, END_STONE);

        valueLookupBuilder(SHADOLINE_BLOCKS).add(
                CHISELED_SHADOLINE,
                CUT_SHADOLINE,
                CUT_SHADOLINE_SLAB,
                CUT_SHADOLINE_STAIRS,
                CUT_SHADOLINE_WALL,
                SHADOLINE_BLOCK,
                SHADOLINE_BLOCK_SLAB,
                SHADOLINE_BLOCK_STAIRS,
                SHADOLINE_BLOCK_WALL,
                SHADOLINE_PILLAR,
                SHADOLINE_BARS,
                SHADOLINE_CHAIN
        );

        valueLookupBuilder(VEILED_LOGS).add(VEILED_LOG, STRIPPED_VEILED_LOG, VEILED_WOOD, STRIPPED_VEILED_WOOD);
        valueLookupBuilder(VEILED_SAPLING_MATURES_ON).add(VEILED_END_STONE);
        valueLookupBuilder(VEILED_WOODLANDS_VEGETATION_PLANTABLE_ON).add(END_STONE, VEILED_END_STONE, CELESTIAL_OVERGROWTH);

        valueLookupBuilder(VEILED_WOOD_BLOCKS).forceAddTag(VEILED_LOGS).add(
                VEILED_BUTTON,
                VEILED_DOOR,
                VEILED_FENCE,
                VEILED_FENCE_GATE,
                VEILED_HANGING_SIGN,
                VEILED_PLANKS,
                VEILED_SIGN,
                VEILED_SLAB,
                VEILED_STAIRS,
                VEILED_TRAPDOOR,
                VEILED_WALL_HANGING_SIGN,
                VEILED_WALL_SIGN
        );

        valueLookupBuilder(VERADITE_BLOCKS).add(
                CHISELED_VERADITE,
                POLISHED_VERADITE,
                POLISHED_VERADITE_BUTTON,
                POLISHED_VERADITE_PRESSURE_PLATE,
                POLISHED_VERADITE_SLAB,
                POLISHED_VERADITE_STAIRS,
                POLISHED_VERADITE_WALL,
                VERADITE,
                VERADITE_BRICKS,
                VERADITE_BRICK_SLAB,
                VERADITE_BRICK_STAIRS,
                VERADITE_BRICK_WALL,
                VERADITE_SLAB,
                VERADITE_STAIRS,
                VERADITE_WALL
        );

        valueLookupBuilder(RUBBLEMITE_MIRESTONE_VARIANTS_SPAWN_ON).forceAddTag(MIRESTONE_BLOCKS).forceAddTag(DUSK_PURPUR_BLOCKS).add(CORRUPT_OVERGROWTH);
        valueLookupBuilder(RUBBLEMITE_VERADITE_VARIANTS_SPAWN_ON).add(VERADITE);
        valueLookupBuilder(RUBBLEMITE_KURODITE_VARIANTS_SPAWN_ON).add(KURODITE);

        valueLookupBuilder(BlockTags.MINEABLE_WITH_AXE)
                .forceAddTag(CELESTIAL_WOOD_BLOCKS).forceAddTag(MURUBLIGHT_WOOD_BLOCKS).forceAddTag(VEILED_WOOD_BLOCKS)
                .add(VEILED_VINES, FLANGER_BERRY_VINE, BLINKLIGHT_VINES_BODY, BLINKLIGHT_VINES_HEAD, VOID_CAMPFIRE);

        valueLookupBuilder(BlockTags.MINEABLE_WITH_HOE)
                .forceAddTag(CHANTERELLE_CAP_BLOCKS)
                .add(VEILED_LEAF_PILE, RIPE_FLANGER_BERRY_BLOCK, UNRIPE_FLANGER_BERRY_BLOCK, FLANGER_BERRY_FLOWER, VEILED_LEAVES);

        valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                .forceAddTag(ALL_ETCHED_MAGNIA_BLOCKS)
                .forceAddTag(CHANTERELLE_BRICK_BLOCKS)
                .forceAddTag(DUSK_PURPUR_BLOCKS)
                .forceAddTag(END_ORE_BLOCKS)
                .forceAddTag(KURODITE_BLOCKS)
                .forceAddTag(MAGNIA_BLOCKS)
                .forceAddTag(MAGNIA_SPROUTS)
                .forceAddTag(MIRESTONE_BLOCKS)
                .forceAddTag(NEW_END_STONE_BLOCKS)
                .forceAddTag(NEW_PURPUR_BLOCKS)
                .forceAddTag(OVERGROWTH_BLOCKS)
                .forceAddTag(PURPUR_TILE_BLOCKS)
                .forceAddTag(SHADOLINE_BLOCKS)
                .forceAddTag(VERADITE_BLOCKS)
                .add(
                        BLINKLAMP,
                        BLISTERED_MAGNIA,
                        POLARIZED_MAGNIA,
                        BULB_LANTERN,
                        CELESTIAL_PATH,
                        CORRUPT_PATH,
                        END_LAMP,
                        MAGNIA_RADIO,
                        NEBULITE_BLOCK,
                        RAW_SHADOLINE_BLOCK,
                        VEILED_END_STONE,
                        VOID_SHALE,
                        VOID_LANTERN
                );

        valueLookupBuilder(BlockTags.DRAGON_IMMUNE)
                .add(
                        END_STONE_BRICKS,
                        END_STONE_BRICK_STAIRS,
                        END_STONE_BRICK_SLAB,
                        END_STONE_BRICK_WALL
                )
                .forceAddTag(END_ORE_BLOCKS)
                .forceAddTag(KURODITE_BLOCKS)
                .forceAddTag(MIRESTONE_BLOCKS)
                .forceAddTag(NEW_END_STONE_BLOCKS)
                .forceAddTag(SHADOLINE_BLOCKS)
                .forceAddTag(VERADITE_BLOCKS);

        valueLookupBuilder(BlockTags.BARS).add(SHADOLINE_BARS);
        valueLookupBuilder(BlockTags.BEACON_BASE_BLOCKS).add(NEBULITE_BLOCK);
        valueLookupBuilder(BlockTags.CAMPFIRES).add(VOID_CAMPFIRE);
        valueLookupBuilder(BlockTags.CEILING_HANGING_SIGNS).add(VEILED_HANGING_SIGN, CELESTIAL_HANGING_SIGN, MURUBLIGHT_HANGING_SIGN);
        valueLookupBuilder(BlockTags.CHAINS).add(SHADOLINE_CHAIN);
        valueLookupBuilder(BlockTags.CLIMBABLE).add(BLINKLIGHT_VINES_BODY, BLINKLIGHT_VINES_HEAD, FLANGER_BERRY_VINE, MURUBLIGHT_BRACKET, VEILED_VINES);
        valueLookupBuilder(BlockTags.COMBINATION_STEP_SOUND_BLOCKS).add(VEILED_LEAF_PILE);
        valueLookupBuilder(BlockTags.FALL_DAMAGE_RESETTING).add(VEILED_LEAF_PILE);
        valueLookupBuilder(BlockTags.FEATURES_CANNOT_REPLACE).add(END_TRIAL_SPAWNER, END_VAULT);
        valueLookupBuilder(BlockTags.FENCE_GATES).add(VEILED_FENCE_GATE, CELESTIAL_FENCE_GATE, MURUBLIGHT_FENCE_GATE);
        valueLookupBuilder(BlockTags.FLOWERS).add(WISP_FLOWER, FLANGER_BERRY_FLOWER);
        valueLookupBuilder(BlockTags.LANTERNS).add(BULB_LANTERN, VOID_LANTERN);

        valueLookupBuilder(BlockTags.FLOWER_POTS).add(
                POTTED_ALLURING_MAGNIA_SPROUT,
                POTTED_BLINKLIGHT,
                POTTED_BULB_FLOWER,
                POTTED_CELESTIAL_CHANTERELLE,
                POTTED_CELESTIAL_GROWTH,
                POTTED_CHORUS_SPROUTS,
                POTTED_CORRUPT_GROWTH,
                POTTED_MURUBLIGHT_CHANTERELLE,
                POTTED_REPULSIVE_MAGNIA_SPROUT,
                POTTED_DRY_END_GROWTH,
                POTTED_VEILED_SAPLING,
                POTTED_WISP_GROWTH
        );

        valueLookupBuilder(BlockTags.LEAVES).add(VEILED_LEAVES);
        valueLookupBuilder(BlockTags.LOGS_THAT_BURN).forceAddTag(CELESTIAL_STEMS).forceAddTag(MURUBLIGHT_STEMS).forceAddTag(VEILED_LOGS);
        valueLookupBuilder(BlockTags.MUSHROOM_GROW_BLOCK).forceAddTag(OVERGROWTH_BLOCKS);
        valueLookupBuilder(BlockTags.NEEDS_DIAMOND_TOOL).add(NEBULITE_ORE, MIRESTONE_NEBULITE_ORE, NEBULITE_BLOCK);
        valueLookupBuilder(BlockTags.NEEDS_STONE_TOOL).forceAddTag(SHADOLINE_BLOCKS).add(SHADOLINE_ORE, MIRESTONE_SHADOLINE_ORE);
        valueLookupBuilder(BlockTags.PLANKS).add(VEILED_PLANKS, CELESTIAL_PLANKS, MURUBLIGHT_PLANKS);
        valueLookupBuilder(BlockTags.REPLACEABLE).add(DRY_END_GROWTH, WISP_SPROUTS, WISP_GROWTH);
        valueLookupBuilder(BlockTags.SAPLINGS).add(VEILED_SAPLING);
        valueLookupBuilder(BlockTags.SMALL_FLOWERS).add(BULB_FLOWER);
        valueLookupBuilder(BlockTags.WALL_POST_OVERRIDE).add(VOID_TORCH);
        valueLookupBuilder(BlockTags.WOODEN_SHELVES).add(VEILED_SHELF, CELESTIAL_SHELF, MURUBLIGHT_SHELF);

        valueLookupBuilder(BlockTags.SLABS).add(
                CELESTIAL_BRICK_SLAB,
                CELESTIAL_SLAB,
                DUSK_PURPUR_SLAB,
                END_STONE_SLAB,
                KURODITE_BRICK_SLAB,
                KURODITE_SLAB,
                MIRESTONE_BRICK_SLAB,
                MIRESTONE_SLAB,
                MURUBLIGHT_BRICK_SLAB,
                MURUBLIGHT_SLAB,
                POLISHED_END_STONE_SLAB,
                POLISHED_KURODITE_SLAB,
                POLISHED_MIRESTONE_SLAB,
                POLISHED_VERADITE_SLAB,
                PURPUR_TILE_SLAB,
                SHADOLINE_BLOCK_SLAB,
                CUT_SHADOLINE_SLAB,
                VEILED_SLAB,
                VERADITE_BRICK_SLAB,
                VERADITE_SLAB,
                ETCHED_ALLURING_MAGNIA_SLAB,
                ETCHED_REPULSIVE_MAGNIA_SLAB
        );

        valueLookupBuilder(BlockTags.STAIRS).add(
                CELESTIAL_BRICK_STAIRS,
                CELESTIAL_STAIRS,
                DUSK_PURPUR_STAIRS,
                END_STONE_STAIRS,
                KURODITE_BRICK_STAIRS,
                KURODITE_STAIRS,
                MIRESTONE_BRICK_STAIRS,
                MIRESTONE_STAIRS,
                MURUBLIGHT_BRICK_STAIRS,
                MURUBLIGHT_STAIRS,
                POLISHED_END_STONE_STAIRS,
                POLISHED_KURODITE_STAIRS,
                POLISHED_MIRESTONE_STAIRS,
                POLISHED_VERADITE_STAIRS,
                PURPUR_TILE_STAIRS,
                SHADOLINE_BLOCK_STAIRS,
                CUT_SHADOLINE_STAIRS,
                VEILED_STAIRS,
                VERADITE_BRICK_STAIRS,
                VERADITE_STAIRS,
                ETCHED_ALLURING_MAGNIA_STAIRS,
                ETCHED_REPULSIVE_MAGNIA_STAIRS
        );

        valueLookupBuilder(BlockTags.WALLS).add(
                CELESTIAL_BRICK_WALL,
                DUSK_PURPUR_WALL,
                END_STONE_WALL,
                KURODITE_BRICK_WALL,
                KURODITE_WALL,
                MIRESTONE_BRICK_WALL,
                MIRESTONE_WALL,
                MURUBLIGHT_BRICK_WALL,
                POLISHED_END_STONE_WALL,
                POLISHED_KURODITE_WALL,
                POLISHED_MIRESTONE_WALL,
                POLISHED_VERADITE_WALL,
                PURPUR_WALL,
                SHADOLINE_BLOCK_WALL,
                CUT_SHADOLINE_WALL,
                VERADITE_BRICK_WALL,
                VERADITE_WALL,
                ETCHED_ALLURING_MAGNIA_WALL,
                ETCHED_REPULSIVE_MAGNIA_WALL
        );

        valueLookupBuilder(BlockTags.STANDING_SIGNS).add(VEILED_SIGN, CELESTIAL_SIGN, MURUBLIGHT_SIGN);
        valueLookupBuilder(BlockTags.STONE_BUTTONS).add(POLISHED_END_STONE_BUTTON, POLISHED_MIRESTONE_BUTTON, POLISHED_VERADITE_BUTTON, POLISHED_KURODITE_BUTTON);
        valueLookupBuilder(BlockTags.STONE_PRESSURE_PLATES).add(POLISHED_END_STONE_PRESSURE_PLATE, POLISHED_MIRESTONE_PRESSURE_PLATE, POLISHED_VERADITE_PRESSURE_PLATE, POLISHED_KURODITE_PRESSURE_PLATE);
        valueLookupBuilder(BlockTags.WALL_HANGING_SIGNS).add(VEILED_WALL_HANGING_SIGN, CELESTIAL_WALL_HANGING_SIGN, MURUBLIGHT_WALL_HANGING_SIGN);
        valueLookupBuilder(BlockTags.WALL_SIGNS).add(VEILED_WALL_SIGN, CELESTIAL_WALL_SIGN, MURUBLIGHT_WALL_SIGN);
        valueLookupBuilder(BlockTags.WOODEN_BUTTONS).add(VEILED_BUTTON, CELESTIAL_BUTTON, MURUBLIGHT_BUTTON);
        valueLookupBuilder(BlockTags.WOODEN_DOORS).add(VEILED_DOOR, CELESTIAL_DOOR, MURUBLIGHT_DOOR);
        valueLookupBuilder(BlockTags.WOODEN_FENCES).add(VEILED_FENCE, CELESTIAL_FENCE, MURUBLIGHT_FENCE);
        valueLookupBuilder(BlockTags.WOODEN_PRESSURE_PLATES).add(VEILED_PRESSURE_PLATE, CELESTIAL_PRESSURE_PLATE, MURUBLIGHT_PRESSURE_PLATE);
        valueLookupBuilder(BlockTags.WOODEN_SLABS).add(VEILED_SLAB, CELESTIAL_SLAB, MURUBLIGHT_SLAB);
        valueLookupBuilder(BlockTags.WOODEN_STAIRS).add(VEILED_STAIRS, CELESTIAL_STAIRS, MURUBLIGHT_STAIRS);
        valueLookupBuilder(BlockTags.WOODEN_TRAPDOORS).add(VEILED_TRAPDOOR, CELESTIAL_TRAPDOOR, MURUBLIGHT_TRAPDOOR);

        valueLookupBuilder(CHAINS).add(SHADOLINE_CHAIN);
        valueLookupBuilder(STRIPPED_LOGS).add(STRIPPED_VEILED_LOG, STRIPPED_CELESTIAL_STEM, STRIPPED_MURUBLIGHT_STEM);
        valueLookupBuilder(STRIPPED_WOODS).add(STRIPPED_VEILED_WOOD, STRIPPED_CELESTIAL_HYPHAE, STRIPPED_MURUBLIGHT_HYPHAE);
        valueLookupBuilder(ORES).add(NEBULITE_ORE, SHADOLINE_ORE, MIRESTONE_NEBULITE_ORE, MIRESTONE_SHADOLINE_ORE);
        valueLookupBuilder(STORAGE_BLOCKS).add(SHADOLINE_BLOCK, NEBULITE_BLOCK);

        valueLookupBuilder(NEBULITE_ORES).add(NEBULITE_ORE, MIRESTONE_NEBULITE_ORE);
        valueLookupBuilder(NEBULITE_STORAGE_BLOCKS).add(NEBULITE_BLOCK);
        valueLookupBuilder(RAW_SHADOLINE_STORAGE_BLOCKS).add(RAW_SHADOLINE_BLOCK);
        valueLookupBuilder(SHADOLINE_ORES).add(SHADOLINE_ORE, MIRESTONE_SHADOLINE_ORE);
        valueLookupBuilder(SHADOLINE_STORAGE_BLOCKS).add(SHADOLINE_BLOCK);

        valueLookupBuilder(externalKey("antixray", "hidden_only_ores")).add(NEBULITE_ORE, MIRESTONE_NEBULITE_ORE);

        valueLookupBuilder(externalKey("create", "tree_attachments")).add(MURUBLIGHT_BRACKET);
        valueLookupBuilder(externalKey("create", "tree_roots")).add(VEILED_LOG, VEILED_WOOD);
        valueLookupBuilder(externalKey("create", "wrench_pickup")).add(
                ALLURING_MAGNIA_SPROUT,
                BLISTERED_MAGNIA,
                BLINKLAMP,
                MAGNIA_RADIO,
                POLARIZED_MAGNIA,
                REPULSIVE_MAGNIA_SPROUT
        );
    }

    private TagKey<Block> externalKey(String namespace, String path) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(namespace, path));
    }
}