package net.bunten.enderscape.datagen;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import static net.bunten.enderscape.registry.EnderscapeBlocks.*;
import static net.bunten.enderscape.registry.tag.EnderscapeBlockTags.*;
import static net.minecraft.world.level.block.Blocks.*;
import static net.neoforged.neoforge.common.Tags.Blocks.*;

public class EnderscapeBlockTagProvider  extends BlockTagsProvider {

    public EnderscapeBlockTagProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), event.getLookupProvider(), Enderscape.MOD_ID, event.getExistingFileHelper());
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(ALL_ETCHED_MAGNIA_BLOCKS).addTag(ETCHED_ALLURING_MAGNIA_BLOCKS).addTag(ETCHED_REPULSIVE_MAGNIA_BLOCKS);
        tag(CELESTIAL_BRICK_BLOCKS).add(CELESTIAL_BRICKS.get(), CELESTIAL_BRICK_SLAB.get(), CELESTIAL_BRICK_STAIRS.get(), CELESTIAL_BRICK_WALL.get());
        tag(CELESTIAL_CHANTERELLE_MATURES_ON).add(CELESTIAL_OVERGROWTH.get());
        tag(CELESTIAL_CORRUPTS_ON).add(CORRUPT_OVERGROWTH.get());
        tag(CELESTIAL_GROVE_VEGETATION_PLANTABLE_ON).addTag(OVERGROWTH_BLOCKS).addTag(BlockTags.DIRT).add(END_STONE, MIRESTONE.get(), VEILED_END_STONE.get());
        tag(CELESTIAL_STEMS).add(CELESTIAL_STEM.get(), STRIPPED_CELESTIAL_STEM.get(), CELESTIAL_HYPHAE.get(), STRIPPED_CELESTIAL_HYPHAE.get());

        tag(CELESTIAL_WOOD_BLOCKS).addTag(CELESTIAL_STEMS).add(
                CELESTIAL_BUTTON.get(),
                CELESTIAL_DOOR.get(),
                CELESTIAL_FENCE.get(),
                CELESTIAL_FENCE_GATE.get(),
                CELESTIAL_HANGING_SIGN.get(),
                CELESTIAL_PLANKS.get(),
                CELESTIAL_SIGN.get(),
                CELESTIAL_SLAB.get(),
                CELESTIAL_STAIRS.get(),
                CELESTIAL_TRAPDOOR.get(),
                CELESTIAL_WALL_HANGING_SIGN.get(),
                CELESTIAL_WALL_SIGN.get()
        );

        tag(CHANTERELLE_BRICK_BLOCKS).addTag(CELESTIAL_BRICK_BLOCKS).addTag(MURUBLIGHT_BRICK_BLOCKS);
        tag(CHANTERELLE_CAP_BLOCKS).add(CELESTIAL_CAP.get(), MURUBLIGHT_CAP.get());
        tag(CHORUS_VEGETATION_PLANTABLE_ON).add(END_STONE, VEILED_END_STONE.get(), CELESTIAL_OVERGROWTH.get());
        tag(CORRUPTION_PURIFIES_ON);
        tag(CORRUPT_BARRENS_VEGETATION_PLANTABLE_ON).addTag(OVERGROWTH_BLOCKS).addTag(BlockTags.DIRT).add(END_STONE, MIRESTONE.get(), VEILED_END_STONE.get());
        tag(DRY_END_GROWTH_PLANTABLE_ON).add(END_STONE, VEILED_END_STONE.get(), CELESTIAL_OVERGROWTH.get());
        tag(DUSK_PURPUR_BLOCKS).add(DUSK_PURPUR_BLOCK.get(), DUSK_PURPUR_PILLAR.get(), DUSK_PURPUR_STAIRS.get(), DUSK_PURPUR_SLAB.get(), DUSK_PURPUR_WALL.get(), CHISELED_DUSK_PURPUR.get());
        tag(ENDERMITE_SAFE_WHEN_NEARBY).add(CORRUPT_OVERGROWTH.get(), CORRUPT_PATH.get(), CORRUPT_GROWTH.get(), POTTED_CORRUPT_GROWTH.get());
        tag(END_ORE_BLOCKS).add(NEBULITE_ORE.get(), SHADOLINE_ORE.get(), MIRESTONE_NEBULITE_ORE.get(), MIRESTONE_SHADOLINE_ORE.get());
        tag(ETCHED_ALLURING_MAGNIA_BLOCKS).add(ETCHED_ALLURING_MAGNIA.get(), ETCHED_ALLURING_MAGNIA_STAIRS.get(), ETCHED_ALLURING_MAGNIA_SLAB.get(), ETCHED_ALLURING_MAGNIA_WALL.get());
        tag(ETCHED_REPULSIVE_MAGNIA_BLOCKS).add(ETCHED_REPULSIVE_MAGNIA.get(), ETCHED_REPULSIVE_MAGNIA_STAIRS.get(), ETCHED_REPULSIVE_MAGNIA_SLAB.get(), ETCHED_REPULSIVE_MAGNIA_WALL.get());
        tag(FLANGER_BERRY_VINE_PLANTABLE_ON).addTag(CHANTERELLE_CAP_BLOCKS);
        tag(FLANGER_BERRY_VINE_SUPPORTS).add(FLANGER_BERRY_FLOWER.get(), UNRIPE_FLANGER_BERRY_BLOCK.get(), RIPE_FLANGER_BERRY_BLOCK.get());

        tag(KURODITE_BLOCKS).add(
                CHISELED_KURODITE.get(),
                KURODITE.get(),
                KURODITE_BRICKS.get(),
                KURODITE_BRICK_SLAB.get(),
                KURODITE_BRICK_STAIRS.get(),
                KURODITE_BRICK_WALL.get(),
                KURODITE_SLAB.get(),
                KURODITE_STAIRS.get(),
                KURODITE_WALL.get(),
                POLISHED_KURODITE.get(),
                POLISHED_KURODITE_BUTTON.get(),
                POLISHED_KURODITE_PRESSURE_PLATE.get(),
                POLISHED_KURODITE_SLAB.get(),
                POLISHED_KURODITE_STAIRS.get(),
                POLISHED_KURODITE_WALL.get()
        );

        tag(MAGNIA_ARCH_REPLACEABLE).add(END_STONE, MIRESTONE.get(), ALLURING_MAGNIA.get());
        tag(MAGNIA_BLOCKS).add(ALLURING_MAGNIA.get(), REPULSIVE_MAGNIA.get());
        tag(MAGNIA_SPROUTS).add(ALLURING_MAGNIA_SPROUT.get(), REPULSIVE_MAGNIA_SPROUT.get());
        tag(MAGNIA_TOWER_REPLACEABLE).add(END_STONE, MIRESTONE.get(), ALLURING_MAGNIA.get(), REPULSIVE_MAGNIA.get());

        tag(MIRESTONE_BLOCKS).add(
                CHISELED_MIRESTONE.get(),
                MIRESTONE.get(),
                MIRESTONE_BRICKS.get(),
                MIRESTONE_BRICK_SLAB.get(),
                MIRESTONE_BRICK_STAIRS.get(),
                MIRESTONE_BRICK_WALL.get(),
                MIRESTONE_SLAB.get(),
                MIRESTONE_STAIRS.get(),
                MIRESTONE_WALL.get(),
                POLISHED_MIRESTONE.get(),
                POLISHED_MIRESTONE_BUTTON.get(),
                POLISHED_MIRESTONE_PRESSURE_PLATE.get(),
                POLISHED_MIRESTONE_SLAB.get(),
                POLISHED_MIRESTONE_STAIRS.get(),
                POLISHED_MIRESTONE_WALL.get()
        );

        tag(MURUBLIGHT_BRICK_BLOCKS).add(MURUBLIGHT_BRICKS.get(), MURUBLIGHT_BRICK_SLAB.get(), MURUBLIGHT_BRICK_STAIRS.get(), MURUBLIGHT_BRICK_WALL.get());
        tag(MURUBLIGHT_CHANTERELLE_MATURES_ON).add(CORRUPT_OVERGROWTH.get());
        tag(MURUBLIGHT_STEMS).add(MURUBLIGHT_STEM.get(), STRIPPED_MURUBLIGHT_STEM.get(), MURUBLIGHT_HYPHAE.get(), STRIPPED_MURUBLIGHT_HYPHAE.get());

        tag(MURUBLIGHT_WOOD_BLOCKS).addTag(MURUBLIGHT_STEMS).add(
                MURUBLIGHT_BUTTON.get(),
                MURUBLIGHT_DOOR.get(),
                MURUBLIGHT_FENCE.get(),
                MURUBLIGHT_FENCE_GATE.get(),
                MURUBLIGHT_HANGING_SIGN.get(),
                MURUBLIGHT_PLANKS.get(),
                MURUBLIGHT_SIGN.get(),
                MURUBLIGHT_SLAB.get(),
                MURUBLIGHT_STAIRS.get(),
                MURUBLIGHT_TRAPDOOR.get(),
                MURUBLIGHT_WALL_HANGING_SIGN.get(),
                MURUBLIGHT_WALL_SIGN.get()
        );

        tag(NEW_END_STONE_BLOCKS).add(
                CHISELED_END_STONE.get(),
                END_STONE_SLAB.get(),
                END_STONE_STAIRS.get(),
                END_STONE_WALL.get(),
                POLISHED_END_STONE.get(),
                POLISHED_END_STONE_BUTTON.get(),
                POLISHED_END_STONE_PRESSURE_PLATE.get(),
                POLISHED_END_STONE_SLAB.get(),
                POLISHED_END_STONE_STAIRS.get(),
                POLISHED_END_STONE_WALL.get()
        );

        tag(NEW_PURPUR_BLOCKS).add(CHISELED_PURPUR.get(), PURPUR_WALL.get());
        tag(ORE_REPLACEABLE).add(END_STONE, MIRESTONE.get());
        tag(OVERGROWTH_BLOCKS).add(CELESTIAL_OVERGROWTH.get(), CORRUPT_OVERGROWTH.get());
        tag(OVERHEATS_MAGNIA_SPROUTS).add(MAGMA_BLOCK);
        tag(PURPUR_TILE_BLOCKS).add(PURPUR_TILES.get(), PURPUR_TILE_STAIRS.get(), PURPUR_TILE_SLAB.get());
        tag(RUBBLEMITE_SPAWNABLE_ON).add(END_STONE, MIRESTONE.get(), VERADITE.get(), KURODITE.get(), VEILED_END_STONE.get(), CELESTIAL_OVERGROWTH.get(), CORRUPT_OVERGROWTH.get(), ALLURING_MAGNIA.get(), REPULSIVE_MAGNIA.get());
        tag(RUSTLE_FOOD).add(MURUBLIGHT_CHANTERELLE.get());
        tag(RUSTLE_PREFERRED).add(VEILED_END_STONE.get());
        tag(RUSTLE_SPAWNABLE_ON).add(VEILED_END_STONE.get(), END_STONE);

        tag(SHADOLINE_BLOCKS).add(
                CHISELED_SHADOLINE.get(),
                CUT_SHADOLINE.get(),
                CUT_SHADOLINE_SLAB.get(),
                CUT_SHADOLINE_STAIRS.get(),
                CUT_SHADOLINE_WALL.get(),
                SHADOLINE_BLOCK.get(),
                SHADOLINE_BLOCK_SLAB.get(),
                SHADOLINE_BLOCK_STAIRS.get(),
                SHADOLINE_BLOCK_WALL.get(),
                SHADOLINE_PILLAR.get(),
                SHADOLINE_BARS.get(),
                SHADOLINE_CHAIN.get()
        );

        tag(VEILED_LOGS).add(VEILED_LOG.get(), STRIPPED_VEILED_LOG.get(), VEILED_WOOD.get(), STRIPPED_VEILED_WOOD.get());
        tag(VEILED_SAPLING_MATURES_ON).add(VEILED_END_STONE.get());
        tag(VEILED_WOODLANDS_VEGETATION_PLANTABLE_ON).add(END_STONE, VEILED_END_STONE.get(), CELESTIAL_OVERGROWTH.get());

        tag(VEILED_WOOD_BLOCKS).addTag(VEILED_LOGS).add(
                VEILED_BUTTON.get(),
                VEILED_DOOR.get(),
                VEILED_FENCE.get(),
                VEILED_FENCE_GATE.get(),
                VEILED_HANGING_SIGN.get(),
                VEILED_PLANKS.get(),
                VEILED_SIGN.get(),
                VEILED_SLAB.get(),
                VEILED_STAIRS.get(),
                VEILED_TRAPDOOR.get(),
                VEILED_WALL_HANGING_SIGN.get(),
                VEILED_WALL_SIGN.get()
        );

        tag(VERADITE_BLOCKS).add(
                CHISELED_VERADITE.get(),
                POLISHED_VERADITE.get(),
                POLISHED_VERADITE_BUTTON.get(),
                POLISHED_VERADITE_PRESSURE_PLATE.get(),
                POLISHED_VERADITE_SLAB.get(),
                POLISHED_VERADITE_STAIRS.get(),
                POLISHED_VERADITE_WALL.get(),
                VERADITE.get(),
                VERADITE_BRICKS.get(),
                VERADITE_BRICK_SLAB.get(),
                VERADITE_BRICK_STAIRS.get(),
                VERADITE_BRICK_WALL.get(),
                VERADITE_SLAB.get(),
                VERADITE_STAIRS.get(),
                VERADITE_WALL.get()
        );

        tag(RUBBLEMITE_MIRESTONE_VARIANTS_SPAWN_ON).addTag(MIRESTONE_BLOCKS).addTag(DUSK_PURPUR_BLOCKS).add(CORRUPT_OVERGROWTH.get());
        tag(RUBBLEMITE_VERADITE_VARIANTS_SPAWN_ON).add(VERADITE.get());
        tag(RUBBLEMITE_KURODITE_VARIANTS_SPAWN_ON).add(KURODITE.get());

        tag(BlockTags.MINEABLE_WITH_AXE)
                .addTag(CELESTIAL_WOOD_BLOCKS).addTag(MURUBLIGHT_WOOD_BLOCKS).addTag(VEILED_WOOD_BLOCKS)
                .add(VEILED_VINES.get(), FLANGER_BERRY_VINE.get(), BLINKLIGHT_VINES_BODY.get(), BLINKLIGHT_VINES_HEAD.get(), VOID_CAMPFIRE.get());

        tag(BlockTags.MINEABLE_WITH_HOE)
                .addTag(CHANTERELLE_CAP_BLOCKS)
                .add(VEILED_LEAF_PILE.get(), RIPE_FLANGER_BERRY_BLOCK.get(), UNRIPE_FLANGER_BERRY_BLOCK.get(), FLANGER_BERRY_FLOWER.get(), VEILED_LEAVES.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addTag(
                        ALL_ETCHED_MAGNIA_BLOCKS).addTag(
                        CHANTERELLE_BRICK_BLOCKS).addTag(
                        DUSK_PURPUR_BLOCKS).addTag(
                        END_ORE_BLOCKS).addTag(
                        KURODITE_BLOCKS).addTag(
                        MAGNIA_BLOCKS).addTag(
                        MAGNIA_SPROUTS).addTag(
                        MIRESTONE_BLOCKS).addTag(
                        NEW_END_STONE_BLOCKS).addTag(
                        NEW_PURPUR_BLOCKS).addTag(
                        OVERGROWTH_BLOCKS).addTag(
                        PURPUR_TILE_BLOCKS).addTag(
                        SHADOLINE_BLOCKS).addTag(
                        VERADITE_BLOCKS
                )
                .add(
                        BLINKLAMP.get(),
                        BLISTERED_MAGNIA.get(),
                        POLARIZED_MAGNIA.get(),
                        BULB_LANTERN.get(),
                        CELESTIAL_PATH.get(),
                        CORRUPT_PATH.get(),
                        END_LAMP.get(),
                        MAGNIA_RADIO.get(),
                        NEBULITE_BLOCK.get(),
                        RAW_SHADOLINE_BLOCK.get(),
                        VEILED_END_STONE.get(),
                        VOID_SHALE.get(),
                        VOID_LANTERN.get()
                );

        tag(BlockTags.DRAGON_IMMUNE)
                .add(
                        END_STONE_BRICKS,
                        END_STONE_BRICK_STAIRS,
                        END_STONE_BRICK_SLAB,
                        END_STONE_BRICK_WALL
                )
                .addTag(END_ORE_BLOCKS)
                .addTag(KURODITE_BLOCKS)
                .addTag(MIRESTONE_BLOCKS)
                .addTag(NEW_END_STONE_BLOCKS)
                .addTag(SHADOLINE_BLOCKS)
                .addTag(VERADITE_BLOCKS);

        tag(BlockTags.BEACON_BASE_BLOCKS).add(NEBULITE_BLOCK.get());
        tag(BlockTags.CAMPFIRES).add(VOID_CAMPFIRE.get());
        tag(BlockTags.CEILING_HANGING_SIGNS).add(VEILED_HANGING_SIGN.get(), CELESTIAL_HANGING_SIGN.get(), MURUBLIGHT_HANGING_SIGN.get());
        tag(BlockTags.CLIMBABLE).add(BLINKLIGHT_VINES_BODY.get(), BLINKLIGHT_VINES_HEAD.get(), FLANGER_BERRY_VINE.get(), MURUBLIGHT_BRACKET.get(), VEILED_VINES.get());
        tag(BlockTags.COMBINATION_STEP_SOUND_BLOCKS).add(VEILED_LEAF_PILE.get());
        tag(BlockTags.FALL_DAMAGE_RESETTING).add(VEILED_LEAF_PILE.get());
        tag(BlockTags.FEATURES_CANNOT_REPLACE).add(END_TRIAL_SPAWNER.get(), END_VAULT.get());
        tag(BlockTags.FENCE_GATES).add(VEILED_FENCE_GATE.get(), CELESTIAL_FENCE_GATE.get(), MURUBLIGHT_FENCE_GATE.get());
        tag(BlockTags.FLOWERS).add(WISP_FLOWER.get(), FLANGER_BERRY_FLOWER.get());

        tag(BlockTags.FLOWER_POTS).add(
                POTTED_ALLURING_MAGNIA_SPROUT.get(),
                POTTED_BLINKLIGHT.get(),
                POTTED_BULB_FLOWER.get(),
                POTTED_CELESTIAL_CHANTERELLE.get(),
                POTTED_CELESTIAL_GROWTH.get(),
                POTTED_CHORUS_SPROUTS.get(),
                POTTED_CORRUPT_GROWTH.get(),
                POTTED_MURUBLIGHT_CHANTERELLE.get(),
                POTTED_REPULSIVE_MAGNIA_SPROUT.get(),
                POTTED_DRY_END_GROWTH.get(),
                POTTED_VEILED_SAPLING.get(),
                POTTED_WISP_GROWTH.get()
        );

        tag(BlockTags.LEAVES).add(VEILED_LEAVES.get());
        tag(BlockTags.LOGS_THAT_BURN).addTag(CELESTIAL_STEMS).addTag(MURUBLIGHT_STEMS).addTag(VEILED_LOGS);
        tag(BlockTags.MUSHROOM_GROW_BLOCK).addTag(OVERGROWTH_BLOCKS);
        tag(BlockTags.NEEDS_DIAMOND_TOOL).add(NEBULITE_ORE.get(), MIRESTONE_NEBULITE_ORE.get(), NEBULITE_BLOCK.get());
        tag(BlockTags.NEEDS_STONE_TOOL).addTag(SHADOLINE_BLOCKS).add(SHADOLINE_ORE.get(), MIRESTONE_SHADOLINE_ORE.get());
        tag(BlockTags.PLANKS).add(VEILED_PLANKS.get(), CELESTIAL_PLANKS.get(), MURUBLIGHT_PLANKS.get());
        tag(BlockTags.REPLACEABLE).add(DRY_END_GROWTH.get(), WISP_SPROUTS.get(), WISP_GROWTH.get());
        tag(BlockTags.SAPLINGS).add(VEILED_SAPLING.get());
        tag(BlockTags.SMALL_FLOWERS).add(BULB_FLOWER.get());
        tag(BlockTags.WALL_POST_OVERRIDE).add(VOID_TORCH.get());

        tag(BlockTags.SLABS).add(
                CELESTIAL_BRICK_SLAB.get(),
                CELESTIAL_SLAB.get(),
                DUSK_PURPUR_SLAB.get(),
                END_STONE_SLAB.get(),
                KURODITE_BRICK_SLAB.get(),
                KURODITE_SLAB.get(),
                MIRESTONE_BRICK_SLAB.get(),
                MIRESTONE_SLAB.get(),
                MURUBLIGHT_BRICK_SLAB.get(),
                MURUBLIGHT_SLAB.get(),
                POLISHED_END_STONE_SLAB.get(),
                POLISHED_KURODITE_SLAB.get(),
                POLISHED_MIRESTONE_SLAB.get(),
                POLISHED_VERADITE_SLAB.get(),
                PURPUR_TILE_SLAB.get(),
                SHADOLINE_BLOCK_SLAB.get(),
                CUT_SHADOLINE_SLAB.get(),
                VEILED_SLAB.get(),
                VERADITE_BRICK_SLAB.get(),
                VERADITE_SLAB.get(),
                ETCHED_ALLURING_MAGNIA_SLAB.get(),
                ETCHED_REPULSIVE_MAGNIA_SLAB.get()
        );

        tag(BlockTags.STAIRS).add(
                CELESTIAL_BRICK_STAIRS.get(),
                CELESTIAL_STAIRS.get(),
                DUSK_PURPUR_STAIRS.get(),
                END_STONE_STAIRS.get(),
                KURODITE_BRICK_STAIRS.get(),
                KURODITE_STAIRS.get(),
                MIRESTONE_BRICK_STAIRS.get(),
                MIRESTONE_STAIRS.get(),
                MURUBLIGHT_BRICK_STAIRS.get(),
                MURUBLIGHT_STAIRS.get(),
                POLISHED_END_STONE_STAIRS.get(),
                POLISHED_KURODITE_STAIRS.get(),
                POLISHED_MIRESTONE_STAIRS.get(),
                POLISHED_VERADITE_STAIRS.get(),
                PURPUR_TILE_STAIRS.get(),
                SHADOLINE_BLOCK_STAIRS.get(),
                CUT_SHADOLINE_STAIRS.get(),
                VEILED_STAIRS.get(),
                VERADITE_BRICK_STAIRS.get(),
                VERADITE_STAIRS.get(),
                ETCHED_ALLURING_MAGNIA_STAIRS.get(),
                ETCHED_REPULSIVE_MAGNIA_STAIRS.get()
        );

        tag(BlockTags.TALL_FLOWERS).add(WISP_FLOWER.get());

        tag(BlockTags.WALLS).add(
                CELESTIAL_BRICK_WALL.get(),
                DUSK_PURPUR_WALL.get(),
                END_STONE_WALL.get(),
                KURODITE_BRICK_WALL.get(),
                KURODITE_WALL.get(),
                MIRESTONE_BRICK_WALL.get(),
                MIRESTONE_WALL.get(),
                MURUBLIGHT_BRICK_WALL.get(),
                POLISHED_END_STONE_WALL.get(),
                POLISHED_KURODITE_WALL.get(),
                POLISHED_MIRESTONE_WALL.get(),
                POLISHED_VERADITE_WALL.get(),
                PURPUR_WALL.get(),
                SHADOLINE_BLOCK_WALL.get(),
                CUT_SHADOLINE_WALL.get(),
                VERADITE_BRICK_WALL.get(),
                VERADITE_WALL.get(),
                ETCHED_ALLURING_MAGNIA_WALL.get(),
                ETCHED_REPULSIVE_MAGNIA_WALL.get()
        );

        tag(BlockTags.STANDING_SIGNS).add(VEILED_SIGN.get(), CELESTIAL_SIGN.get(), MURUBLIGHT_SIGN.get());
        tag(BlockTags.STONE_BUTTONS).add(POLISHED_END_STONE_BUTTON.get(), POLISHED_MIRESTONE_BUTTON.get(), POLISHED_VERADITE_BUTTON.get(), POLISHED_KURODITE_BUTTON.get());
        tag(BlockTags.STONE_PRESSURE_PLATES).add(POLISHED_END_STONE_PRESSURE_PLATE.get(), POLISHED_MIRESTONE_PRESSURE_PLATE.get(), POLISHED_VERADITE_PRESSURE_PLATE.get(), POLISHED_KURODITE_PRESSURE_PLATE.get());
        tag(BlockTags.WALL_HANGING_SIGNS).add(VEILED_WALL_HANGING_SIGN.get(), CELESTIAL_WALL_HANGING_SIGN.get(), MURUBLIGHT_WALL_HANGING_SIGN.get());
        tag(BlockTags.WALL_SIGNS).add(VEILED_WALL_SIGN.get(), CELESTIAL_WALL_SIGN.get(), MURUBLIGHT_WALL_SIGN.get());
        tag(BlockTags.WOODEN_BUTTONS).add(VEILED_BUTTON.get(), CELESTIAL_BUTTON.get(), MURUBLIGHT_BUTTON.get());
        tag(BlockTags.WOODEN_DOORS).add(VEILED_DOOR.get(), CELESTIAL_DOOR.get(), MURUBLIGHT_DOOR.get());
        tag(BlockTags.WOODEN_FENCES).add(VEILED_FENCE.get(), CELESTIAL_FENCE.get(), MURUBLIGHT_FENCE.get());
        tag(BlockTags.WOODEN_PRESSURE_PLATES).add(VEILED_PRESSURE_PLATE.get(), CELESTIAL_PRESSURE_PLATE.get(), MURUBLIGHT_PRESSURE_PLATE.get());
        tag(BlockTags.WOODEN_SLABS).add(VEILED_SLAB.get(), CELESTIAL_SLAB.get(), MURUBLIGHT_SLAB.get());
        tag(BlockTags.WOODEN_STAIRS).add(VEILED_STAIRS.get(), CELESTIAL_STAIRS.get(), MURUBLIGHT_STAIRS.get());
        tag(BlockTags.WOODEN_TRAPDOORS).add(VEILED_TRAPDOOR.get(), CELESTIAL_TRAPDOOR.get(), MURUBLIGHT_TRAPDOOR.get());

        tag(STRIPPED_LOGS).add(STRIPPED_VEILED_LOG.get(), STRIPPED_CELESTIAL_STEM.get(), STRIPPED_MURUBLIGHT_STEM.get());
        tag(STRIPPED_WOODS).add(STRIPPED_VEILED_WOOD.get(), STRIPPED_CELESTIAL_HYPHAE.get(), STRIPPED_MURUBLIGHT_HYPHAE.get());
        tag(ORES).add(NEBULITE_ORE.get(), SHADOLINE_ORE.get(), MIRESTONE_NEBULITE_ORE.get(), MIRESTONE_SHADOLINE_ORE.get());
        tag(STORAGE_BLOCKS).add(SHADOLINE_BLOCK.get(), NEBULITE_BLOCK.get());

        tag(NEBULITE_ORES).add(NEBULITE_ORE.get(), MIRESTONE_NEBULITE_ORE.get());
        tag(NEBULITE_STORAGE_BLOCKS).add(NEBULITE_BLOCK.get());
        tag(RAW_SHADOLINE_STORAGE_BLOCKS).add(RAW_SHADOLINE_BLOCK.get());
        tag(SHADOLINE_ORES).add(SHADOLINE_ORE.get(), MIRESTONE_SHADOLINE_ORE.get());
        tag(SHADOLINE_STORAGE_BLOCKS).add(SHADOLINE_BLOCK.get());

        tag(externalKey("antixray", "hidden_only_ores")).add(NEBULITE_ORE.get(), MIRESTONE_NEBULITE_ORE.get());

        tag(externalKey("create", "tree_attachments")).add(MURUBLIGHT_BRACKET.get());
        tag(externalKey("create", "tree_roots")).add(VEILED_LOG.get(), VEILED_WOOD.get());
        tag(externalKey("create", "wrench_pickup")).add(
                ALLURING_MAGNIA_SPROUT.get(),
                BLISTERED_MAGNIA.get(),
                BLINKLAMP.get(),
                MAGNIA_RADIO.get(),
                POLARIZED_MAGNIA.get(),
                REPULSIVE_MAGNIA_SPROUT.get()
        );
    }

    private TagKey<Block> externalKey(String namespace, String path) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(namespace, path));
    }
}