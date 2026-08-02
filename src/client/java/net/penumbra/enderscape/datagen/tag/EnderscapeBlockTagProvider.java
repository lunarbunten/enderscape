package net.penumbra.enderscape.datagen.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockItemTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

import static net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags.*;
import static net.minecraft.references.BlockItemIds.*;
import static net.penumbra.enderscape.references.EnderscapeBlockItemIds.*;
import static net.penumbra.enderscape.references.EnderscapeBlockIds.*;
import static net.penumbra.enderscape.registry.tag.EnderscapeBlockTags.*;

public class EnderscapeBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {

    public EnderscapeBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        builder(ALL_ETCHED_MAGNIA_BLOCKS).forceAddTag(ETCHED_ALLURING_MAGNIA_BLOCKS).forceAddTag(ETCHED_REPULSIVE_MAGNIA_BLOCKS);
        builder(BASE_STONE_END).add(END_STONE, MIRESTONE, VERADITE, KURODITE);

        builder(POWERS_BULB_FLOWER).add(CELESTIAL_OVERGROWTH);
        builder(CELESTIAL_BRICK_BLOCKS).add(CELESTIAL_BRICKS, CELESTIAL_BRICK_SLAB, CELESTIAL_BRICK_STAIRS, CELESTIAL_BRICK_WALL);
        builder(CELESTIAL_CHANTERELLE_GROWS_ON).add(CELESTIAL_OVERGROWTH);
        builder(CELESTIAL_CORRUPTS_ON).add(CORRUPT_OVERGROWTH);
        builder(CELESTIAL_STEMS).add(CELESTIAL_STEM, STRIPPED_CELESTIAL_STEM, CELESTIAL_HYPHAE, STRIPPED_CELESTIAL_HYPHAE);

        builder(CELESTIAL_WOOD_BLOCKS).add(
                CELESTIAL_BUTTON,
                CELESTIAL_DOOR,
                CELESTIAL_FENCE,
                CELESTIAL_FENCE_GATE,
                CELESTIAL_HANGING_SIGN,
                CELESTIAL_PLANKS,
                CELESTIAL_PRESSURE_PLATE,
                CELESTIAL_SHELF,
                CELESTIAL_SIGN,
                CELESTIAL_SLAB,
                CELESTIAL_STAIRS,
                CELESTIAL_TRAPDOOR
        ).add(
                CELESTIAL_WALL_HANGING_SIGN,
                CELESTIAL_WALL_SIGN
        ).forceAddTag(CELESTIAL_STEMS);

        builder(CHANTERELLE_BRICK_BLOCKS).forceAddTag(CELESTIAL_BRICK_BLOCKS).forceAddTag(MURUBLIGHT_BRICK_BLOCKS);
        builder(CHANTERELLE_CAP_BLOCKS).add(CELESTIAL_CAP, MURUBLIGHT_CAP);
        builder(CORRUPTION_PURIFIES_ON);
        builder(DUSK_PURPUR_BLOCKS).add(DUSK_PURPUR_BLOCK, DUSK_PURPUR_PILLAR, DUSK_PURPUR_STAIRS, DUSK_PURPUR_SLAB, DUSK_PURPUR_WALL, CHISELED_DUSK_PURPUR);
        builder(ENDERMITE_SAFE_WHEN_NEARBY).add(CORRUPT_OVERGROWTH, CORRUPT_PATH, CORRUPT_GROWTH).add(POTTED_CORRUPT_GROWTH);
        builder(END_ORE_BLOCKS).add(NEBULITE_ORE, SHADOLINE_ORE, MIRESTONE_NEBULITE_ORE, MIRESTONE_SHADOLINE_ORE);
        builder(ETCHED_ALLURING_MAGNIA_BLOCKS).add(ETCHED_ALLURING_MAGNIA, ETCHED_ALLURING_MAGNIA_STAIRS, ETCHED_ALLURING_MAGNIA_SLAB, ETCHED_ALLURING_MAGNIA_WALL);
        builder(ETCHED_REPULSIVE_MAGNIA_BLOCKS).add(ETCHED_REPULSIVE_MAGNIA, ETCHED_REPULSIVE_MAGNIA_STAIRS, ETCHED_REPULSIVE_MAGNIA_SLAB, ETCHED_REPULSIVE_MAGNIA_WALL);
        builder(PURUBERRY_VINE_SUPPORTS).add(PURUBERRY_FLOWER, UNRIPE_PURUBERRY_BLOCK, RIPE_PURUBERRY_BLOCK);

        builder(SUPPORTS_BULB_FLOWER).forceAddTag(SUPPORTS_CELESTIAL_VEGETATION);
        builder(SUPPORTS_CELESTIAL_CHANTERELLE).forceAddTag(SUPPORTS_CELESTIAL_VEGETATION);
        builder(SUPPORTS_CELESTIAL_GROWTH).forceAddTag(SUPPORTS_CELESTIAL_VEGETATION);
        builder(SUPPORTS_CELESTIAL_VEGETATION).add(END_STONE, MIRESTONE, VEILED_END_STONE).forceAddTag(OVERGROWTH_BLOCKS).forceAddTag(BlockTags.SUPPORTS_VEGETATION);
        builder(SUPPORTS_CHORUS_SPROUTS).forceAddTag(BlockTags.SUPPORTS_CHORUS_PLANT);
        builder(SUPPORTS_CORRUPT_GROWTH).forceAddTag(SUPPORTS_CORRUPT_VEGETATION);
        builder(SUPPORTS_CORRUPT_VEGETATION).add(END_STONE, MIRESTONE, VEILED_END_STONE).forceAddTag(OVERGROWTH_BLOCKS).forceAddTag(BlockTags.SUPPORTS_VEGETATION);
        builder(SUPPORTS_DRY_END_GROWTH).forceAddTag(BlockTags.SUPPORTS_CHORUS_PLANT);
        builder(SUPPORTS_PURUBERRY_VINE).forceAddTag(CHANTERELLE_CAP_BLOCKS);
        builder(SUPPORTS_MURUBLIGHT_CHANTERELLE).forceAddTag(SUPPORTS_CORRUPT_VEGETATION);
        builder(SUPPORTS_VEILED_VEGETATION).add(END_STONE, VEILED_END_STONE, CELESTIAL_OVERGROWTH);
        builder(SUPPORTS_VEILED_SAPLING).forceAddTag(SUPPORTS_VEILED_VEGETATION);
        builder(SUPPORTS_WISP_FLOWER).forceAddTag(SUPPORTS_VEILED_VEGETATION);
        builder(SUPPORTS_WISP_GROWTH).forceAddTag(SUPPORTS_VEILED_VEGETATION);
        builder(SUPPORTS_WISP_SPROUTS).forceAddTag(SUPPORTS_VEILED_VEGETATION);
        builder(VOID_FIRE_BASE_BLOCKS).add(VOID_SHALE, NEBULITE_BLOCK, CORRUPT_OVERGROWTH).forceAddTag(KURODITE_BLOCKS);
        builder(VOID_LACHRYMA_TURNS_TO_VOID_SHALE).forceAddTag(BASE_STONE_END);

        builder(KURODITE_BLOCKS).add(
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

        builder(MAGNIA_ARCH_REPLACEABLE).add(ALLURING_MAGNIA).forceAddTag(BASE_STONE_END);
        builder(MAGNIA_BLOCKS).add(ALLURING_MAGNIA, REPULSIVE_MAGNIA);
        builder(MAGNIA_SPROUTS).add(ALLURING_MAGNIA_SPROUT, REPULSIVE_MAGNIA_SPROUT);
        builder(MAGNIA_TOWER_REPLACEABLE).forceAddTag(BASE_STONE_END).forceAddTag(MAGNIA_BLOCKS);

        builder(MIRESTONE_BLOCKS).add(
                MIRESTONE,
                MIRESTONE_SLAB,
                MIRESTONE_STAIRS,
                MIRESTONE_WALL,
                MIRESTONE_BRICKS,
                CRACKED_MIRESTONE_BRICKS,
                MIRESTONE_BRICK_SLAB,
                MIRESTONE_BRICK_STAIRS,
                MIRESTONE_BRICK_WALL,
                OVERGROWN_MIRESTONE_BRICKS,
                OVERGROWN_MIRESTONE_BRICK_SLAB,
                OVERGROWN_MIRESTONE_BRICK_STAIRS,
                OVERGROWN_MIRESTONE_BRICK_WALL,
                POLISHED_MIRESTONE,
                POLISHED_MIRESTONE_BUTTON,
                POLISHED_MIRESTONE_PRESSURE_PLATE,
                POLISHED_MIRESTONE_SLAB,
                POLISHED_MIRESTONE_STAIRS,
                POLISHED_MIRESTONE_WALL,
                CHISELED_MIRESTONE
        );

        builder(MURUBLIGHT_BRICK_BLOCKS).add(MURUBLIGHT_BRICKS, MURUBLIGHT_BRICK_SLAB, MURUBLIGHT_BRICK_STAIRS, MURUBLIGHT_BRICK_WALL);
        builder(MURUBLIGHT_CHANTERELLE_GROWS_ON).add(CORRUPT_OVERGROWTH);
        builder(MURUBLIGHT_STEMS).add(MURUBLIGHT_STEM, STRIPPED_MURUBLIGHT_STEM, MURUBLIGHT_HYPHAE, STRIPPED_MURUBLIGHT_HYPHAE);

        builder(MURUBLIGHT_WOOD_BLOCKS).add(
                MURUBLIGHT_BUTTON,
                MURUBLIGHT_DOOR,
                MURUBLIGHT_FENCE,
                MURUBLIGHT_FENCE_GATE,
                MURUBLIGHT_HANGING_SIGN,
                MURUBLIGHT_PLANKS,
                MURUBLIGHT_PRESSURE_PLATE,
                MURUBLIGHT_SHELF,
                MURUBLIGHT_SIGN,
                MURUBLIGHT_SLAB,
                MURUBLIGHT_STAIRS,
                MURUBLIGHT_TRAPDOOR
        ).add(
                MURUBLIGHT_WALL_HANGING_SIGN,
                MURUBLIGHT_WALL_SIGN
        ).forceAddTag(MURUBLIGHT_STEMS);

        builder(NEW_END_STONE_BLOCKS).add(
                CHISELED_END_STONE,
                END_STONE_SLAB,
                END_STONE_STAIRS,
                END_STONE_WALL,
                CRACKED_END_STONE_BRICKS,
                OVERGROWN_END_STONE_BRICKS,
                OVERGROWN_END_STONE_BRICK_SLAB,
                OVERGROWN_END_STONE_BRICK_STAIRS,
                OVERGROWN_END_STONE_BRICK_WALL,
                POLISHED_END_STONE,
                POLISHED_END_STONE_BUTTON,
                POLISHED_END_STONE_PRESSURE_PLATE,
                POLISHED_END_STONE_SLAB,
                POLISHED_END_STONE_STAIRS,
                POLISHED_END_STONE_WALL
        );

        builder(NEW_PURPUR_BLOCKS).add(CHISELED_PURPUR, PURPUR_WALL);
        builder(ORE_REPLACEABLE).forceAddTag(BASE_STONE_END);
        builder(OVERGROWTH_BLOCKS).add(CELESTIAL_OVERGROWTH, CORRUPT_OVERGROWTH);
        builder(OVERHEATS_MAGNIA_SPROUTS).add(MAGMA_BLOCK);
        builder(PURPUR_TILE_BLOCKS).add(PURPUR_TILES, PURPUR_TILE_STAIRS, PURPUR_TILE_SLAB);
        builder(RUBBLEMITES_SPAWNABLE_ON).add(VEILED_END_STONE).forceAddTag(BASE_STONE_END).forceAddTag(OVERGROWTH_BLOCKS).forceAddTag(MAGNIA_BLOCKS);
        builder(EDIBLE_FOR_RUSTLE).add(MURUBLIGHT_CHANTERELLE);
        builder(RUSTLE_PREFER_WALK_ON).add(VEILED_END_STONE);
        builder(RUSTLES_SPAWNABLE_ON).add(VEILED_END_STONE, END_STONE);

        builder(SHADOLINE_BLOCKS).add(
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

        builder(VEILED_LOGS).add(VEILED_LOG, STRIPPED_VEILED_LOG, VEILED_WOOD, STRIPPED_VEILED_WOOD);
        builder(VEILED_LOG_REPLACEABLE).forceAddTag(BlockTags.SCULK_REPLACEABLE).forceAddTag(BlockTags.REPLACEABLE);
        builder(VEILED_SAPLING_GROWS_ON).add(VEILED_END_STONE);

        builder(VEILED_WOOD_BLOCKS).add(
                VEILED_BUTTON,
                VEILED_DOOR,
                VEILED_FENCE,
                VEILED_FENCE_GATE,
                VEILED_HANGING_SIGN,
                VEILED_PLANKS,
                VEILED_PRESSURE_PLATE,
                VEILED_SHELF,
                VEILED_SIGN,
                VEILED_SLAB,
                VEILED_STAIRS,
                VEILED_TRAPDOOR
        ).add(
                VEILED_WALL_HANGING_SIGN,
                VEILED_WALL_SIGN
        ).forceAddTag(VEILED_LOGS);

        builder(VERADITE_BLOCKS).add(
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

        builder(RUBBLEMITE_MIRESTONE_VARIANTS_SPAWN_ON).add(CORRUPT_OVERGROWTH).forceAddTag(MIRESTONE_BLOCKS).forceAddTag(DUSK_PURPUR_BLOCKS);
        builder(RUBBLEMITE_VERADITE_VARIANTS_SPAWN_ON).add(VERADITE);
        builder(RUBBLEMITE_KURODITE_VARIANTS_SPAWN_ON).add(KURODITE);

        // Vanilla

        builder(BlockTags.BARS).add(SHADOLINE_BARS);
        builder(BlockTags.BEACON_BASE_BLOCKS).add(NEBULITE_BLOCK);
        builder(BlockTags.CAMPFIRES).add(VOID_CAMPFIRE);
        builder(BlockTags.CAULDRONS).add(VOID_LACHRYMA_CAULDRON);
        builder(BlockTags.CEILING_HANGING_SIGNS).add(VEILED_HANGING_SIGN, CELESTIAL_HANGING_SIGN, MURUBLIGHT_HANGING_SIGN);
        builder(BlockTags.CHAINS).add(SHADOLINE_CHAIN);
        builder(BlockTags.CLIMBABLE).add(BLINKLIGHT_VINES_BODY, BLINKLIGHT_VINES_HEAD, PURUBERRY_VINE, MURUBLIGHT_BRACKET).add(VEILED_VINES);
        builder(BlockTags.COMBINATION_STEP_SOUND_BLOCKS).add(VEILED_LEAF_PILE);
        builder(BlockTags.ENDERMAN_HOLDABLE).add(CHORUS_SPROUTS, WISP_GROWTH, CELESTIAL_CHANTERELLE, MURUBLIGHT_CHANTERELLE);
        builder(BlockTags.FALL_DAMAGE_RESETTING).add(VEILED_LEAF_PILE);
        builder(BlockTags.FEATURES_CANNOT_REPLACE).add(END_TRIAL_SPAWNER, END_VAULT);
        builder(BlockTags.FENCE_GATES).add(VEILED_FENCE_GATE, CELESTIAL_FENCE_GATE, MURUBLIGHT_FENCE_GATE);
        builder(BlockTags.FIRE).add(VOID_FIRE);
        builder(BlockTags.FLOWERS).add(WISP_FLOWER, PURUBERRY_FLOWER);
        builder(BlockTags.HUGE_BROWN_MUSHROOM_CAN_PLACE_ON).forceAddTag(OVERGROWTH_BLOCKS);
        builder(BlockTags.HUGE_RED_MUSHROOM_CAN_PLACE_ON).forceAddTag(OVERGROWTH_BLOCKS);
        builder(BlockTags.LANTERNS).add(BULB_LANTERN, VOID_LANTERN);
        builder(BlockTags.LEAVES).add(VEILED_LEAVES);
        builder(BlockItemTags.LOGS_THAT_BURN.block()).forceAddTag(CELESTIAL_STEMS).forceAddTag(MURUBLIGHT_STEMS).forceAddTag(VEILED_LOGS);
        builder(BlockTags.NEEDS_DIAMOND_TOOL).add(NEBULITE_ORE, MIRESTONE_NEBULITE_ORE, NEBULITE_BLOCK);
        builder(BlockTags.NEEDS_STONE_TOOL).add(SHADOLINE_ORE, MIRESTONE_SHADOLINE_ORE).forceAddTag(SHADOLINE_BLOCKS);
        builder(BlockTags.OVERRIDES_MUSHROOM_LIGHT_REQUIREMENT).forceAddTag(OVERGROWTH_BLOCKS);
        builder(BlockTags.PLANKS).add(VEILED_PLANKS, CELESTIAL_PLANKS, MURUBLIGHT_PLANKS);
        builder(BlockTags.REPLACEABLE).add(DRY_END_GROWTH, CHORUS_SPROUTS, WISP_SPROUTS, WISP_GROWTH);
        builder(BlockItemTags.SAPLINGS.block()).add(VEILED_SAPLING);
        builder(BlockTags.SCULK_REPLACEABLE).add(VEILED_END_STONE, BLISTERED_MAGNIA, VOID_SHALE).forceAddTag(BASE_STONE_END).forceAddTag(OVERGROWTH_BLOCKS).forceAddTag(MAGNIA_BLOCKS);
        builder(BlockTags.SHEARS_MINOR_BREAKING_SPEED).add(BLINKLIGHT_VINES_BODY, BLINKLIGHT_VINES_HEAD, PURUBERRY_VINE).add(VEILED_VINES);
        builder(BlockTags.SMALL_FLOWERS).add(BULB_FLOWER);
        builder(BlockTags.STANDING_SIGNS).add(VEILED_SIGN, CELESTIAL_SIGN, MURUBLIGHT_SIGN);
        builder(BlockTags.STONE_BUTTONS).add(POLISHED_END_STONE_BUTTON, POLISHED_MIRESTONE_BUTTON, POLISHED_VERADITE_BUTTON, POLISHED_KURODITE_BUTTON);
        builder(BlockTags.STONE_PRESSURE_PLATES).add(POLISHED_END_STONE_PRESSURE_PLATE, POLISHED_MIRESTONE_PRESSURE_PLATE, POLISHED_VERADITE_PRESSURE_PLATE, POLISHED_KURODITE_PRESSURE_PLATE);
        builder(BlockTags.SUPPORTS_CHORUS_FLOWER).add(VEILED_END_STONE, CELESTIAL_OVERGROWTH);
        builder(BlockTags.SUPPORTS_CHORUS_PLANT).add(VEILED_END_STONE, CELESTIAL_OVERGROWTH);
        builder(BlockTags.SUPPORTS_NETHER_SPROUTS).forceAddTag(OVERGROWTH_BLOCKS);
        builder(BlockTags.SUPPORTS_WARPED_FUNGUS).forceAddTag(OVERGROWTH_BLOCKS);
        builder(BlockTags.SUPPORTS_WARPED_ROOTS).forceAddTag(OVERGROWTH_BLOCKS);
        builder(BlockTags.WALL_HANGING_SIGNS).add(VEILED_WALL_HANGING_SIGN, CELESTIAL_WALL_HANGING_SIGN, MURUBLIGHT_WALL_HANGING_SIGN);
        builder(BlockTags.WALL_POST_OVERRIDE).add(VOID_TORCH);
        builder(BlockTags.WALL_SIGNS).add(VEILED_WALL_SIGN, CELESTIAL_WALL_SIGN, MURUBLIGHT_WALL_SIGN);
        builder(BlockTags.WOODEN_BUTTONS).add(VEILED_BUTTON, CELESTIAL_BUTTON, MURUBLIGHT_BUTTON);
        builder(BlockTags.WOODEN_DOORS).add(VEILED_DOOR, CELESTIAL_DOOR, MURUBLIGHT_DOOR);
        builder(BlockTags.WOODEN_FENCES).add(VEILED_FENCE, CELESTIAL_FENCE, MURUBLIGHT_FENCE);
        builder(BlockTags.WOODEN_PRESSURE_PLATES).add(VEILED_PRESSURE_PLATE, CELESTIAL_PRESSURE_PLATE, MURUBLIGHT_PRESSURE_PLATE);
        builder(BlockTags.WOODEN_SHELVES).add(VEILED_SHELF, CELESTIAL_SHELF, MURUBLIGHT_SHELF);
        builder(BlockTags.WOODEN_SLABS).add(VEILED_SLAB, CELESTIAL_SLAB, MURUBLIGHT_SLAB);
        builder(BlockTags.WOODEN_STAIRS).add(VEILED_STAIRS, CELESTIAL_STAIRS, MURUBLIGHT_STAIRS);
        builder(BlockTags.WOODEN_TRAPDOORS).add(VEILED_TRAPDOOR, CELESTIAL_TRAPDOOR, MURUBLIGHT_TRAPDOOR);

        builder(BlockTags.MINEABLE_WITH_AXE)
                .add(VEILED_VINES, VOID_CAMPFIRE).add(PURUBERRY_VINE, BLINKLIGHT_VINES_BODY, BLINKLIGHT_VINES_HEAD)
                .forceAddTag(CELESTIAL_WOOD_BLOCKS).forceAddTag(MURUBLIGHT_WOOD_BLOCKS).forceAddTag(VEILED_WOOD_BLOCKS);

        builder(BlockTags.MINEABLE_WITH_HOE)
                .add(VEILED_LEAF_PILE, RIPE_PURUBERRY_BLOCK, UNRIPE_PURUBERRY_BLOCK, PURUBERRY_FLOWER, VEILED_LEAVES)
                .forceAddTag(CHANTERELLE_CAP_BLOCKS)
        ;

        builder(BlockTags.MINEABLE_WITH_PICKAXE)
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
                )
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
                .forceAddTag(VERADITE_BLOCKS);

        builder(BlockTags.DRAGON_IMMUNE)
                .add(
                        END_STONE_BRICKS,
                        END_STONE_BRICK_STAIRS,
                        END_STONE_BRICK_SLAB,
                        END_STONE_BRICK_WALL,
                        END_TRIAL_SPAWNER,
                        END_VAULT,
                        END_HAVEN_CORE
                )
                .forceAddTag(END_ORE_BLOCKS)
                .forceAddTag(KURODITE_BLOCKS)
                .forceAddTag(MIRESTONE_BLOCKS)
                .forceAddTag(NEW_END_STONE_BLOCKS)
                .forceAddTag(SHADOLINE_BLOCKS)
                .forceAddTag(VERADITE_BLOCKS);

        builder(BlockTags.FLOWER_POTS).add(
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

        builder(BlockTags.SLABS).add(
                CELESTIAL_BRICK_SLAB,
                CELESTIAL_SLAB,
                DUSK_PURPUR_SLAB,
                END_STONE_SLAB,
                OVERGROWN_END_STONE_BRICK_SLAB,
                KURODITE_BRICK_SLAB,
                KURODITE_SLAB,
                MIRESTONE_BRICK_SLAB,
                OVERGROWN_MIRESTONE_BRICK_SLAB,
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

        builder(BlockTags.STAIRS).add(
                CELESTIAL_BRICK_STAIRS,
                CELESTIAL_STAIRS,
                DUSK_PURPUR_STAIRS,
                END_STONE_STAIRS,
                OVERGROWN_END_STONE_BRICK_STAIRS,
                KURODITE_BRICK_STAIRS,
                KURODITE_STAIRS,
                MIRESTONE_BRICK_STAIRS,
                MIRESTONE_STAIRS,
                MURUBLIGHT_BRICK_STAIRS,
                OVERGROWN_MIRESTONE_BRICK_STAIRS,
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

        builder(BlockTags.WALLS).add(
                CELESTIAL_BRICK_WALL,
                DUSK_PURPUR_WALL,
                END_STONE_WALL,
                OVERGROWN_END_STONE_BRICK_WALL,
                KURODITE_BRICK_WALL,
                KURODITE_WALL,
                MIRESTONE_BRICK_WALL,
                OVERGROWN_MIRESTONE_BRICK_WALL,
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

        // Conventional

        builder(CHAINS).add(SHADOLINE_CHAIN);
        builder(STRIPPED_LOGS).add(STRIPPED_VEILED_LOG, STRIPPED_CELESTIAL_STEM, STRIPPED_MURUBLIGHT_STEM);
        builder(STRIPPED_WOODS).add(STRIPPED_VEILED_WOOD, STRIPPED_CELESTIAL_HYPHAE, STRIPPED_MURUBLIGHT_HYPHAE);
        builder(ORES).forceAddTag(END_ORE_BLOCKS);
        builder(STORAGE_BLOCKS).forceAddTag(NEBULITE_STORAGE_BLOCKS).forceAddTag(RAW_SHADOLINE_STORAGE_BLOCKS).forceAddTag(SHADOLINE_STORAGE_BLOCKS);

        builder(NEBULITE_ORES).add(NEBULITE_ORE, MIRESTONE_NEBULITE_ORE);
        builder(NEBULITE_STORAGE_BLOCKS).add(NEBULITE_BLOCK);
        builder(RAW_SHADOLINE_STORAGE_BLOCKS).add(RAW_SHADOLINE_BLOCK);
        builder(SHADOLINE_ORES).add(SHADOLINE_ORE, MIRESTONE_SHADOLINE_ORE);
        builder(SHADOLINE_STORAGE_BLOCKS).add(SHADOLINE_BLOCK);

        // Misc

        builder(externalKey("antixray", "hidden_only_ores")).forceAddTag(NEBULITE_ORES);

        builder(externalKey("create", "tree_attachments")).add(MURUBLIGHT_BRACKET);
        builder(externalKey("create", "tree_roots")).add(VEILED_LOG, VEILED_WOOD);
        builder(externalKey("create", "wrench_pickup")).add(
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