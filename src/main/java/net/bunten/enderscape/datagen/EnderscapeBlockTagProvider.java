package net.bunten.enderscape.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockItemTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.ids.EnderscapeBlockIds.*;
import static net.bunten.enderscape.registry.tag.EnderscapeBlockTags.*;
import static net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags.*;
import static net.minecraft.references.BlockItemIds.*;

public class EnderscapeBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {

    public EnderscapeBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(ALL_ETCHED_MAGNIA_BLOCKS).forceAddTag(ETCHED_ALLURING_MAGNIA_BLOCKS).forceAddTag(ETCHED_REPULSIVE_MAGNIA_BLOCKS);
        tag(CELESTIAL_BRICK_BLOCKS).add(CELESTIAL_BRICKS, CELESTIAL_BRICK_SLAB, CELESTIAL_BRICK_STAIRS, CELESTIAL_BRICK_WALL);
        tag(CELESTIAL_CHANTERELLE_MATURES_ON).add(CELESTIAL_OVERGROWTH);
        tag(CELESTIAL_CORRUPTS_ON).add(CORRUPT_OVERGROWTH);
        tag(CELESTIAL_GROVE_VEGETATION_PLANTABLE_ON).forceAddTag(OVERGROWTH_BLOCKS).forceAddTag(BlockTags.DIRT).add(END_STONE.block(), MIRESTONE, VEILED_END_STONE);
        tag(CELESTIAL_STEMS).add(CELESTIAL_STEM, STRIPPED_CELESTIAL_STEM, CELESTIAL_HYPHAE, STRIPPED_CELESTIAL_HYPHAE);

        tag(CELESTIAL_WOOD_BLOCKS).forceAddTag(CELESTIAL_STEMS).add(
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

        tag(CHANTERELLE_BRICK_BLOCKS).forceAddTag(CELESTIAL_BRICK_BLOCKS).forceAddTag(MURUBLIGHT_BRICK_BLOCKS);
        tag(CHANTERELLE_CAP_BLOCKS).add(CELESTIAL_CAP, MURUBLIGHT_CAP);
        tag(CHORUS_VEGETATION_PLANTABLE_ON).add(END_STONE.block(), VEILED_END_STONE, CELESTIAL_OVERGROWTH);
        tag(BlockTags.SUPPORTS_CHORUS_FLOWER).forceAddTag(CHORUS_VEGETATION_PLANTABLE_ON);
        tag(BlockTags.SUPPORTS_CHORUS_PLANT).forceAddTag(CHORUS_VEGETATION_PLANTABLE_ON);
        tag(CORRUPTION_PURIFIES_ON);
        tag(CORRUPT_BARRENS_VEGETATION_PLANTABLE_ON).forceAddTag(OVERGROWTH_BLOCKS).forceAddTag(BlockTags.DIRT).add(END_STONE.block(), MIRESTONE, VEILED_END_STONE);
        tag(DRY_END_GROWTH_PLANTABLE_ON).add(END_STONE.block(), VEILED_END_STONE, CELESTIAL_OVERGROWTH);
        tag(DUSK_PURPUR_BLOCKS).add(DUSK_PURPUR_BLOCK, DUSK_PURPUR_PILLAR, DUSK_PURPUR_STAIRS, DUSK_PURPUR_SLAB, DUSK_PURPUR_WALL, CHISELED_DUSK_PURPUR);
        tag(ENDERMITE_SAFE_WHEN_NEARBY).add(CORRUPT_OVERGROWTH, CORRUPT_PATH, CORRUPT_GROWTH, POTTED_CORRUPT_GROWTH);
        tag(END_ORE_BLOCKS).add(NEBULITE_ORE, SHADOLINE_ORE, MIRESTONE_NEBULITE_ORE, MIRESTONE_SHADOLINE_ORE);
        tag(ETCHED_ALLURING_MAGNIA_BLOCKS).add(ETCHED_ALLURING_MAGNIA, ETCHED_ALLURING_MAGNIA_STAIRS, ETCHED_ALLURING_MAGNIA_SLAB, ETCHED_ALLURING_MAGNIA_WALL);
        tag(ETCHED_REPULSIVE_MAGNIA_BLOCKS).add(ETCHED_REPULSIVE_MAGNIA, ETCHED_REPULSIVE_MAGNIA_STAIRS, ETCHED_REPULSIVE_MAGNIA_SLAB, ETCHED_REPULSIVE_MAGNIA_WALL);
        tag(FLANGER_BERRY_VINE_PLANTABLE_ON).forceAddTag(CHANTERELLE_CAP_BLOCKS);
        tag(FLANGER_BERRY_VINE_SUPPORTS).add(FLANGER_BERRY_FLOWER, UNRIPE_FLANGER_BERRY_BLOCK, RIPE_FLANGER_BERRY_BLOCK);

        tag(KURODITE_BLOCKS).add(
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

        tag(MAGNIA_ARCH_REPLACEABLE).add(END_STONE.block(), MIRESTONE, ALLURING_MAGNIA);
        tag(MAGNIA_BLOCKS).add(ALLURING_MAGNIA, REPULSIVE_MAGNIA);
        tag(MAGNIA_SPROUTS).add(ALLURING_MAGNIA_SPROUT, REPULSIVE_MAGNIA_SPROUT);
        tag(MAGNIA_TOWER_REPLACEABLE).add(END_STONE.block(), MIRESTONE, ALLURING_MAGNIA, REPULSIVE_MAGNIA);

        tag(MIRESTONE_BLOCKS).add(
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

        tag(MURUBLIGHT_BRICK_BLOCKS).add(MURUBLIGHT_BRICKS, MURUBLIGHT_BRICK_SLAB, MURUBLIGHT_BRICK_STAIRS, MURUBLIGHT_BRICK_WALL);
        tag(MURUBLIGHT_CHANTERELLE_MATURES_ON).add(CORRUPT_OVERGROWTH);
        tag(MURUBLIGHT_STEMS).add(MURUBLIGHT_STEM, STRIPPED_MURUBLIGHT_STEM, MURUBLIGHT_HYPHAE, STRIPPED_MURUBLIGHT_HYPHAE);

        tag(MURUBLIGHT_WOOD_BLOCKS).forceAddTag(MURUBLIGHT_STEMS).add(
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

        tag(NEW_END_STONE_BLOCKS).add(
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

        tag(NEW_PURPUR_BLOCKS).add(CHISELED_PURPUR, PURPUR_WALL);
        tag(ORE_REPLACEABLE).add(END_STONE.block(), MIRESTONE);
        tag(OVERGROWTH_BLOCKS).add(CELESTIAL_OVERGROWTH, CORRUPT_OVERGROWTH);
        tag(OVERHEATS_MAGNIA_SPROUTS).add(MAGMA_BLOCK.block());
        tag(PURPUR_TILE_BLOCKS).add(PURPUR_TILES, PURPUR_TILE_STAIRS, PURPUR_TILE_SLAB);
        tag(RUBBLEMITE_SPAWNABLE_ON).add(END_STONE.block(), MIRESTONE, VERADITE, KURODITE, VEILED_END_STONE, CELESTIAL_OVERGROWTH, CORRUPT_OVERGROWTH, ALLURING_MAGNIA, REPULSIVE_MAGNIA);
        tag(RUSTLE_FOOD).add(MURUBLIGHT_CHANTERELLE);
        tag(RUSTLE_PREFERRED).add(VEILED_END_STONE);
        tag(RUSTLE_SPAWNABLE_ON).add(VEILED_END_STONE, END_STONE.block());

        tag(SHADOLINE_BLOCKS).add(
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

        tag(VEILED_LOGS).add(VEILED_LOG, STRIPPED_VEILED_LOG, VEILED_WOOD, STRIPPED_VEILED_WOOD);
        tag(VEILED_SAPLING_MATURES_ON).add(VEILED_END_STONE);
        tag(VEILED_WOODLANDS_VEGETATION_PLANTABLE_ON).add(END_STONE.block(), VEILED_END_STONE, CELESTIAL_OVERGROWTH);

        tag(VEILED_WOOD_BLOCKS).forceAddTag(VEILED_LOGS).add(
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

        tag(VERADITE_BLOCKS).add(
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

        tag(RUBBLEMITE_MIRESTONE_VARIANTS_SPAWN_ON).forceAddTag(MIRESTONE_BLOCKS).forceAddTag(DUSK_PURPUR_BLOCKS).add(CORRUPT_OVERGROWTH);
        tag(RUBBLEMITE_VERADITE_VARIANTS_SPAWN_ON).add(VERADITE);
        tag(RUBBLEMITE_KURODITE_VARIANTS_SPAWN_ON).add(KURODITE);

        tag(BlockTags.MINEABLE_WITH_AXE)
                .forceAddTag(CELESTIAL_WOOD_BLOCKS).forceAddTag(MURUBLIGHT_WOOD_BLOCKS).forceAddTag(VEILED_WOOD_BLOCKS)
                .add(VEILED_VINES, FLANGER_BERRY_VINE, BLINKLIGHT_VINES_BODY, BLINKLIGHT_VINES_HEAD, VOID_CAMPFIRE);

        tag(BlockTags.MINEABLE_WITH_HOE)
                .forceAddTag(CHANTERELLE_CAP_BLOCKS)
                .add(VEILED_LEAF_PILE, RIPE_FLANGER_BERRY_BLOCK, UNRIPE_FLANGER_BERRY_BLOCK, FLANGER_BERRY_FLOWER, VEILED_LEAVES);

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
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

        tag(BlockTags.DRAGON_IMMUNE)
                .add(
                        END_STONE_BRICKS.block(),
                        END_STONE_BRICK_STAIRS.block(),
                        END_STONE_BRICK_SLAB.block(),
                        END_STONE_BRICK_WALL.block()
                )
                .forceAddTag(END_ORE_BLOCKS)
                .forceAddTag(KURODITE_BLOCKS)
                .forceAddTag(MIRESTONE_BLOCKS)
                .forceAddTag(NEW_END_STONE_BLOCKS)
                .forceAddTag(SHADOLINE_BLOCKS)
                .forceAddTag(VERADITE_BLOCKS);

        tag(BlockTags.BARS).add(SHADOLINE_BARS);
        tag(BlockTags.BEACON_BASE_BLOCKS).add(NEBULITE_BLOCK);
        tag(BlockTags.CAMPFIRES).add(VOID_CAMPFIRE);
        tag(BlockTags.CEILING_HANGING_SIGNS).add(VEILED_HANGING_SIGN, CELESTIAL_HANGING_SIGN, MURUBLIGHT_HANGING_SIGN);
        tag(BlockTags.CHAINS).add(SHADOLINE_CHAIN);
        tag(BlockTags.CLIMBABLE).add(BLINKLIGHT_VINES_BODY, BLINKLIGHT_VINES_HEAD, FLANGER_BERRY_VINE, MURUBLIGHT_BRACKET, VEILED_VINES);
        tag(BlockTags.COMBINATION_STEP_SOUND_BLOCKS).add(VEILED_LEAF_PILE);
        tag(BlockTags.FALL_DAMAGE_RESETTING).add(VEILED_LEAF_PILE);
        tag(BlockTags.FEATURES_CANNOT_REPLACE).add(END_TRIAL_SPAWNER, END_VAULT);
        tag(BlockTags.FENCE_GATES).add(VEILED_FENCE_GATE, CELESTIAL_FENCE_GATE, MURUBLIGHT_FENCE_GATE);
        tag(BlockTags.FLOWERS).add(WISP_FLOWER, FLANGER_BERRY_FLOWER);
        tag(BlockTags.LANTERNS).add(BULB_LANTERN, VOID_LANTERN);

        tag(BlockTags.FLOWER_POTS).add(
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

        tag(BlockTags.LEAVES).add(VEILED_LEAVES);
        tag(BlockItemTags.LOGS_THAT_BURN.block()).forceAddTag(CELESTIAL_STEMS).forceAddTag(MURUBLIGHT_STEMS).forceAddTag(VEILED_LOGS);
        tag(BlockTags.OVERRIDES_MUSHROOM_LIGHT_REQUIREMENT).forceAddTag(OVERGROWTH_BLOCKS);
        tag(BlockTags.NEEDS_DIAMOND_TOOL).add(NEBULITE_ORE, MIRESTONE_NEBULITE_ORE, NEBULITE_BLOCK);
        tag(BlockTags.NEEDS_STONE_TOOL).forceAddTag(SHADOLINE_BLOCKS).add(SHADOLINE_ORE, MIRESTONE_SHADOLINE_ORE);
        tag(BlockTags.PLANKS).add(VEILED_PLANKS, CELESTIAL_PLANKS, MURUBLIGHT_PLANKS);
        tag(BlockTags.REPLACEABLE).add(DRY_END_GROWTH, WISP_SPROUTS, WISP_GROWTH);
        tag(BlockItemTags.SAPLINGS.block()).add(VEILED_SAPLING);
        tag(BlockTags.SMALL_FLOWERS).add(BULB_FLOWER);
        tag(BlockTags.WALL_POST_OVERRIDE).add(VOID_TORCH);
        tag(BlockTags.WOODEN_SHELVES).add(VEILED_SHELF, CELESTIAL_SHELF, MURUBLIGHT_SHELF);

        tag(BlockTags.SUPPORTS_CRIMSON_FUNGUS).forceAddTag(OVERGROWTH_BLOCKS);
        tag(BlockTags.SUPPORTS_WARPED_FUNGUS).forceAddTag(OVERGROWTH_BLOCKS);
        tag(BlockTags.SUPPORTS_CRIMSON_ROOTS).forceAddTag(OVERGROWTH_BLOCKS);

        tag(BlockTags.SLABS).add(
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

        tag(BlockTags.STAIRS).add(
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

        tag(BlockTags.WALLS).add(
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

        tag(BlockTags.STANDING_SIGNS).add(VEILED_SIGN, CELESTIAL_SIGN, MURUBLIGHT_SIGN);
        tag(BlockTags.STONE_BUTTONS).add(POLISHED_END_STONE_BUTTON, POLISHED_MIRESTONE_BUTTON, POLISHED_VERADITE_BUTTON, POLISHED_KURODITE_BUTTON);
        tag(BlockTags.STONE_PRESSURE_PLATES).add(POLISHED_END_STONE_PRESSURE_PLATE, POLISHED_MIRESTONE_PRESSURE_PLATE, POLISHED_VERADITE_PRESSURE_PLATE, POLISHED_KURODITE_PRESSURE_PLATE);
        tag(BlockTags.WALL_HANGING_SIGNS).add(VEILED_WALL_HANGING_SIGN, CELESTIAL_WALL_HANGING_SIGN, MURUBLIGHT_WALL_HANGING_SIGN);
        tag(BlockTags.WALL_SIGNS).add(VEILED_WALL_SIGN, CELESTIAL_WALL_SIGN, MURUBLIGHT_WALL_SIGN);
        tag(BlockTags.WOODEN_BUTTONS).add(VEILED_BUTTON, CELESTIAL_BUTTON, MURUBLIGHT_BUTTON);
        tag(BlockTags.WOODEN_DOORS).add(VEILED_DOOR, CELESTIAL_DOOR, MURUBLIGHT_DOOR);
        tag(BlockTags.WOODEN_FENCES).add(VEILED_FENCE, CELESTIAL_FENCE, MURUBLIGHT_FENCE);
        tag(BlockTags.WOODEN_PRESSURE_PLATES).add(VEILED_PRESSURE_PLATE, CELESTIAL_PRESSURE_PLATE, MURUBLIGHT_PRESSURE_PLATE);
        tag(BlockTags.WOODEN_SLABS).add(VEILED_SLAB, CELESTIAL_SLAB, MURUBLIGHT_SLAB);
        tag(BlockTags.WOODEN_STAIRS).add(VEILED_STAIRS, CELESTIAL_STAIRS, MURUBLIGHT_STAIRS);
        tag(BlockTags.WOODEN_TRAPDOORS).add(VEILED_TRAPDOOR, CELESTIAL_TRAPDOOR, MURUBLIGHT_TRAPDOOR);

        tag(CHAINS).add(SHADOLINE_CHAIN);
        tag(STRIPPED_LOGS).add(STRIPPED_VEILED_LOG, STRIPPED_CELESTIAL_STEM, STRIPPED_MURUBLIGHT_STEM);
        tag(STRIPPED_WOODS).add(STRIPPED_VEILED_WOOD, STRIPPED_CELESTIAL_HYPHAE, STRIPPED_MURUBLIGHT_HYPHAE);
        tag(ORES).add(NEBULITE_ORE, SHADOLINE_ORE, MIRESTONE_NEBULITE_ORE, MIRESTONE_SHADOLINE_ORE);
        tag(STORAGE_BLOCKS).add(SHADOLINE_BLOCK, NEBULITE_BLOCK);

        tag(NEBULITE_ORES).add(NEBULITE_ORE, MIRESTONE_NEBULITE_ORE);
        tag(NEBULITE_STORAGE_BLOCKS).add(NEBULITE_BLOCK);
        tag(RAW_SHADOLINE_STORAGE_BLOCKS).add(RAW_SHADOLINE_BLOCK);
        tag(SHADOLINE_ORES).add(SHADOLINE_ORE, MIRESTONE_SHADOLINE_ORE);
        tag(SHADOLINE_STORAGE_BLOCKS).add(SHADOLINE_BLOCK);

        tag(externalKey("antixray", "hidden_only_ores")).add(NEBULITE_ORE, MIRESTONE_NEBULITE_ORE);

        tag(externalKey("create", "tree_attachments")).add(MURUBLIGHT_BRACKET);
        tag(externalKey("create", "tree_roots")).add(VEILED_LOG, VEILED_WOOD);
        tag(externalKey("create", "wrench_pickup")).add(
                ALLURING_MAGNIA_SPROUT,
                BLISTERED_MAGNIA,
                BLINKLAMP,
                MAGNIA_RADIO,
                POLARIZED_MAGNIA,
                REPULSIVE_MAGNIA_SPROUT
        );
    }

    private TagKey<Block> externalKey(String namespace, String path) {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(namespace, path));
    }
}