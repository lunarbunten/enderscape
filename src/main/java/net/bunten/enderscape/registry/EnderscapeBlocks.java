package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.block.*;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Function;

import static net.minecraft.world.level.block.Blocks.*;

public class EnderscapeBlocks {

    public static final BlockSetType VEILED_BLOCK_SET = new BlockSetType(
            "veiled",
            true,
            true,
            true,
            BlockSetType.PressurePlateSensitivity.EVERYTHING,
            EnderscapeSoundTypes.VEILED_PLANKS,
            EnderscapeBlockSounds.VEILED_DOOR_CLOSE,
            EnderscapeBlockSounds.VEILED_DOOR_OPEN,
            EnderscapeBlockSounds.VEILED_TRAPDOOR_CLOSE,
            EnderscapeBlockSounds.VEILED_TRAPDOOR_OPEN,
            EnderscapeBlockSounds.VEILED_PRESSURE_PLATE_CLICK_OFF,
            EnderscapeBlockSounds.VEILED_PRESSURE_PLATE_CLICK_ON,
            EnderscapeBlockSounds.VEILED_BUTTON_CLICK_OFF,
            EnderscapeBlockSounds.VEILED_BUTTON_CLICK_ON
    );

    public static final BlockSetType CELESTIAL_BLOCK_SET = new BlockSetType(
            "celestial",
            true,
            true,
            true,
            BlockSetType.PressurePlateSensitivity.EVERYTHING,
            EnderscapeSoundTypes.CELESTIAL_PLANKS,
            EnderscapeBlockSounds.CELESTIAL_DOOR_CLOSE,
            EnderscapeBlockSounds.CELESTIAL_DOOR_OPEN,
            EnderscapeBlockSounds.CELESTIAL_TRAPDOOR_CLOSE,
            EnderscapeBlockSounds.CELESTIAL_TRAPDOOR_OPEN,
            EnderscapeBlockSounds.CELESTIAL_PRESSURE_PLATE_CLICK_OFF,
            EnderscapeBlockSounds.CELESTIAL_PRESSURE_PLATE_CLICK_ON,
            EnderscapeBlockSounds.CELESTIAL_BUTTON_CLICK_OFF,
            EnderscapeBlockSounds.CELESTIAL_BUTTON_CLICK_ON
    );

    public static final BlockSetType MURUBLIGHT_BLOCK_SET = new BlockSetType(
            "murublight",
            true,
            true,
            true,
            BlockSetType.PressurePlateSensitivity.EVERYTHING,
            EnderscapeSoundTypes.MURUBLIGHT_PLANKS,
            EnderscapeBlockSounds.MURUBLIGHT_DOOR_CLOSE,
            EnderscapeBlockSounds.MURUBLIGHT_DOOR_OPEN,
            EnderscapeBlockSounds.MURUBLIGHT_TRAPDOOR_CLOSE,
            EnderscapeBlockSounds.MURUBLIGHT_TRAPDOOR_OPEN,
            EnderscapeBlockSounds.MURUBLIGHT_PRESSURE_PLATE_CLICK_OFF,
            EnderscapeBlockSounds.MURUBLIGHT_PRESSURE_PLATE_CLICK_ON,
            EnderscapeBlockSounds.MURUBLIGHT_BUTTON_CLICK_OFF,
            EnderscapeBlockSounds.MURUBLIGHT_BUTTON_CLICK_ON
    );

    public static final BlockSetType POLISHED_END_STONE_BLOCK_SET = new BlockSetType("polished_end_stone", true, true, false, BlockSetType.PressurePlateSensitivity.MOBS, SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);
    public static final BlockSetType POLISHED_MIRESTONE_BLOCK_SET = new BlockSetType("polished_mirestone", true, true, false, BlockSetType.PressurePlateSensitivity.MOBS, EnderscapeSoundTypes.MIRESTONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);
    public static final BlockSetType POLISHED_VERADITE_BLOCK_SET = new BlockSetType("polished_veradite", true, true, false, BlockSetType.PressurePlateSensitivity.MOBS, EnderscapeSoundTypes.VERADITE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);
    public static final BlockSetType POLISHED_KURODITE_BLOCK_SET = new BlockSetType("polished_kurodite", true, true, false, BlockSetType.PressurePlateSensitivity.MOBS, EnderscapeSoundTypes.KURODITE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);

    public static final WoodType VEILED_WOOD_TYPE = new WoodTypeBuilder().soundGroup(EnderscapeSoundTypes.VEILED_PLANKS).fenceGateCloseSound(EnderscapeBlockSounds.VEILED_FENCE_GATE_CLOSE).fenceGateOpenSound(EnderscapeBlockSounds.VEILED_FENCE_GATE_OPEN).hangingSignSoundGroup(EnderscapeSoundTypes.VEILED_HANGING_SIGN).register(Enderscape.id("veiled"), VEILED_BLOCK_SET);
    public static final WoodType CELESTIAL_WOOD_TYPE = new WoodTypeBuilder().soundGroup(EnderscapeSoundTypes.CELESTIAL_PLANKS).fenceGateCloseSound(EnderscapeBlockSounds.CELESTIAL_FENCE_GATE_CLOSE).fenceGateOpenSound(EnderscapeBlockSounds.CELESTIAL_FENCE_GATE_OPEN).hangingSignSoundGroup(EnderscapeSoundTypes.CELESTIAL_HANGING_SIGN).register(Enderscape.id("celestial"), CELESTIAL_BLOCK_SET);
    public static final WoodType MURUBLIGHT_WOOD_TYPE = new WoodTypeBuilder().soundGroup(EnderscapeSoundTypes.MURUBLIGHT_PLANKS).fenceGateCloseSound(EnderscapeBlockSounds.MURUBLIGHT_FENCE_GATE_CLOSE).fenceGateOpenSound(EnderscapeBlockSounds.MURUBLIGHT_FENCE_GATE_OPEN).hangingSignSoundGroup(EnderscapeSoundTypes.MURUBLIGHT_HANGING_SIGN).register(Enderscape.id("murublight"), MURUBLIGHT_BLOCK_SET);

    public static final Block DRIFT_JELLY_BLOCK = register(true, "drift_jelly_block", DriftJellyBlock::new, Properties.of()
            .mapColor(MapColor.WARPED_WART_BLOCK)
            .instabreak()
            .sound(EnderscapeSoundTypes.DRIFT_JELLY_BLOCK)
            .noOcclusion()
    );

    public static final Block END_TRIAL_SPAWNER = register(true, "end_trial_spawner", TrialSpawnerBlock::new, Properties.ofFullCopy(TRIAL_SPAWNER));
    public static final Block END_VAULT = register(true, "end_vault", EndVaultBlock::new, Properties.ofFullCopy(VAULT).sound(EnderscapeSoundTypes.END_VAULT));

    public static final Block END_STONE_STAIRS = registerStair("end_stone_stairs", END_STONE);
    public static final Block END_STONE_SLAB = register(true, "end_stone_slab", SlabBlock::new, Properties.ofLegacyCopy(END_STONE));
    public static final Block END_STONE_WALL = register(true, "end_stone_wall", WallBlock::new, Properties.ofLegacyCopy(END_STONE));

    public static final Block POLISHED_END_STONE = register(true, "polished_end_stone", Block::new, Properties.ofLegacyCopy(END_STONE).sound(SoundType.STONE));
    public static final Block POLISHED_END_STONE_STAIRS = registerStair("polished_end_stone_stairs", POLISHED_END_STONE);
    public static final Block POLISHED_END_STONE_SLAB = register(true, "polished_end_stone_slab", SlabBlock::new, Properties.ofLegacyCopy(POLISHED_END_STONE));
    public static final Block POLISHED_END_STONE_WALL = register(true, "polished_end_stone_wall", WallBlock::new, Properties.ofLegacyCopy(POLISHED_END_STONE));
    public static final Block POLISHED_END_STONE_BUTTON = register(true, "polished_end_stone_button", properties -> new ButtonBlock(POLISHED_END_STONE_BLOCK_SET, 20, properties), buttonProperties());
    public static final Block POLISHED_END_STONE_PRESSURE_PLATE = register(true, "polished_end_stone_pressure_plate", properties -> new PressurePlateBlock(POLISHED_END_STONE_BLOCK_SET, properties), BlockBehaviour.Properties.of()
            .mapColor(MapColor.SAND)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(SoundType.STONE)
            .requiresCorrectToolForDrops()
            .noCollission()
            .strength(0.5F)
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Block CHISELED_END_STONE = register(true, "chiseled_end_stone", Block::new, Properties.ofLegacyCopy(POLISHED_END_STONE).sound(SoundType.STONE));

    public static final Block MIRESTONE = register(true, "mirestone", Block::new, Properties.of()
            .mapColor(MapColor.STONE)
            .requiresCorrectToolForDrops()
            .strength(6, 9)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.MIRESTONE)
    );

    public static final Block MIRESTONE_STAIRS = registerStair("mirestone_stairs", MIRESTONE);
    public static final Block MIRESTONE_SLAB = register(true, "mirestone_slab", SlabBlock::new, Properties.ofLegacyCopy(MIRESTONE));
    public static final Block MIRESTONE_WALL = register(true, "mirestone_wall", WallBlock::new, Properties.ofLegacyCopy(MIRESTONE));

    public static final Block POLISHED_MIRESTONE = register(true, "polished_mirestone", Block::new, Properties.ofLegacyCopy(MIRESTONE));
    public static final Block POLISHED_MIRESTONE_STAIRS = registerStair("polished_mirestone_stairs", POLISHED_MIRESTONE);
    public static final Block POLISHED_MIRESTONE_SLAB = register(true, "polished_mirestone_slab", SlabBlock::new, Properties.ofLegacyCopy(POLISHED_MIRESTONE));
    public static final Block POLISHED_MIRESTONE_WALL = register(true, "polished_mirestone_wall", WallBlock::new, Properties.ofLegacyCopy(POLISHED_MIRESTONE));
    public static final Block POLISHED_MIRESTONE_BUTTON = register(true, "polished_mirestone_button", properties -> new ButtonBlock(POLISHED_MIRESTONE_BLOCK_SET, 20, properties), buttonProperties());
    public static final Block POLISHED_MIRESTONE_PRESSURE_PLATE = register(true, "polished_mirestone_pressure_plate", properties -> new PressurePlateBlock(POLISHED_MIRESTONE_BLOCK_SET, properties), BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.MIRESTONE)
            .requiresCorrectToolForDrops()
            .noCollission()
            .strength(0.5F)
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Block MIRESTONE_BRICKS = register(true, "mirestone_bricks", Block::new, Properties.ofLegacyCopy(POLISHED_MIRESTONE).sound(EnderscapeSoundTypes.MIRESTONE_BRICKS));
    public static final Block MIRESTONE_BRICK_STAIRS = registerStair("mirestone_brick_stairs", MIRESTONE_BRICKS);
    public static final Block MIRESTONE_BRICK_SLAB = register(true, "mirestone_brick_slab", SlabBlock::new, Properties.ofLegacyCopy(MIRESTONE_BRICKS));
    public static final Block MIRESTONE_BRICK_WALL = register(true, "mirestone_brick_wall", WallBlock::new, Properties.ofLegacyCopy(MIRESTONE_BRICKS));

    public static final Block CHISELED_MIRESTONE = register(true, "chiseled_mirestone", Block::new, Properties.ofLegacyCopy(POLISHED_MIRESTONE).sound(EnderscapeSoundTypes.MIRESTONE_BRICKS));

    public static final Block VERADITE = register(true, "veradite", Block::new, Properties.of()
            .mapColor(MapColor.GLOW_LICHEN)
            .requiresCorrectToolForDrops()
            .strength(1.5F, 6.0F)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.VERADITE)
    );

    public static final Block VERADITE_STAIRS = registerStair("veradite_stairs", VERADITE);
    public static final Block VERADITE_SLAB = register(true, "veradite_slab", SlabBlock::new, Properties.ofLegacyCopy(VERADITE));
    public static final Block VERADITE_WALL = register(true, "veradite_wall", WallBlock::new, Properties.ofLegacyCopy(VERADITE));

    public static final Block POLISHED_VERADITE = register(true, "polished_veradite", Block::new, Properties.ofLegacyCopy(VERADITE));
    public static final Block POLISHED_VERADITE_STAIRS = registerStair("polished_veradite_stairs", POLISHED_VERADITE);
    public static final Block POLISHED_VERADITE_SLAB = register(true, "polished_veradite_slab", SlabBlock::new, Properties.ofLegacyCopy(POLISHED_VERADITE));
    public static final Block POLISHED_VERADITE_WALL = register(true, "polished_veradite_wall", WallBlock::new, Properties.ofLegacyCopy(POLISHED_VERADITE));
    public static final Block POLISHED_VERADITE_BUTTON = register(true, "polished_veradite_button", properties -> new ButtonBlock(POLISHED_VERADITE_BLOCK_SET, 20, properties), buttonProperties());
    public static final Block POLISHED_VERADITE_PRESSURE_PLATE = register(true, "polished_veradite_pressure_plate", properties -> new PressurePlateBlock(POLISHED_VERADITE_BLOCK_SET, properties), BlockBehaviour.Properties.of()
            .mapColor(MapColor.GLOW_LICHEN)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.VERADITE)
            .requiresCorrectToolForDrops()
            .noCollission()
            .strength(0.5F)
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Block VERADITE_BRICKS = register(true, "veradite_bricks", Block::new, Properties.ofLegacyCopy(POLISHED_VERADITE).sound(EnderscapeSoundTypes.VERADITE_BRICKS));
    public static final Block VERADITE_BRICK_STAIRS = registerStair("veradite_brick_stairs", VERADITE_BRICKS);
    public static final Block VERADITE_BRICK_SLAB = register(true, "veradite_brick_slab", SlabBlock::new, Properties.ofLegacyCopy(VERADITE_BRICKS));
    public static final Block VERADITE_BRICK_WALL = register(true, "veradite_brick_wall", WallBlock::new, Properties.ofLegacyCopy(VERADITE_BRICKS));

    public static final Block CHISELED_VERADITE = register(true, "chiseled_veradite", Block::new, Properties.ofLegacyCopy(POLISHED_VERADITE).sound(EnderscapeSoundTypes.VERADITE_BRICKS));

    public static final Block KURODITE = register(true, "kurodite", Block::new, Properties.of()
            .mapColor(MapColor.GLOW_LICHEN)
            .requiresCorrectToolForDrops()
            .strength(1.5F, 6.0F)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.KURODITE)
    );

    public static final Block KURODITE_STAIRS = registerStair("kurodite_stairs", KURODITE);
    public static final Block KURODITE_SLAB = register(true, "kurodite_slab", SlabBlock::new, Properties.ofLegacyCopy(KURODITE));
    public static final Block KURODITE_WALL = register(true, "kurodite_wall", WallBlock::new, Properties.ofLegacyCopy(KURODITE));

    public static final Block POLISHED_KURODITE = register(true, "polished_kurodite", Block::new, Properties.ofLegacyCopy(KURODITE));
    public static final Block POLISHED_KURODITE_STAIRS = registerStair("polished_kurodite_stairs", POLISHED_KURODITE);
    public static final Block POLISHED_KURODITE_SLAB = register(true, "polished_kurodite_slab", SlabBlock::new, Properties.ofLegacyCopy(POLISHED_KURODITE));
    public static final Block POLISHED_KURODITE_WALL = register(true, "polished_kurodite_wall", WallBlock::new, Properties.ofLegacyCopy(POLISHED_KURODITE));
    public static final Block POLISHED_KURODITE_BUTTON = register(true, "polished_kurodite_button", properties -> new ButtonBlock(POLISHED_KURODITE_BLOCK_SET, 20, properties), buttonProperties());
    public static final Block POLISHED_KURODITE_PRESSURE_PLATE = register(true, "polished_kurodite_pressure_plate", properties -> new PressurePlateBlock(POLISHED_KURODITE_BLOCK_SET, properties), BlockBehaviour.Properties.of()
            .mapColor(MapColor.GLOW_LICHEN)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.KURODITE)
            .requiresCorrectToolForDrops()
            .noCollission()
            .strength(0.5F)
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Block KURODITE_BRICKS = register(true, "kurodite_bricks", Block::new, Properties.ofLegacyCopy(POLISHED_KURODITE).sound(EnderscapeSoundTypes.KURODITE_BRICKS));
    public static final Block KURODITE_BRICK_STAIRS = registerStair("kurodite_brick_stairs", KURODITE_BRICKS);
    public static final Block KURODITE_BRICK_SLAB = register(true, "kurodite_brick_slab", SlabBlock::new, Properties.ofLegacyCopy(KURODITE_BRICKS));
    public static final Block KURODITE_BRICK_WALL = register(true, "kurodite_brick_wall", WallBlock::new, Properties.ofLegacyCopy(KURODITE_BRICKS));

    public static final Block CHISELED_KURODITE = register(true, "chiseled_kurodite", Block::new, Properties.ofLegacyCopy(POLISHED_KURODITE).sound(EnderscapeSoundTypes.KURODITE_BRICKS));


    public static final Block VOID_SHALE = register(true, "void_shale", VoidShaleBlock::new, Properties.of()
            .mapColor(MapColor.COLOR_BLACK)
            .strength(0.5F)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.VOID_SHALE)
    );

    public static final Block ALLURING_MAGNIA = register(true, "alluring_magnia", AlluringMagniaBlock::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .requiresCorrectToolForDrops()
            .strength(1.5F, 6.0F)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.ALLURING_MAGNIA)
    );

    public static final Block ALLURING_MAGNIA_SPROUT = register(true, "alluring_magnia_sprout", AlluringMagniaSproutBlock::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .lightLevel(state -> state.getValue(MagniaSproutBlock.POWERED) ? 12 : 0)
            .requiresCorrectToolForDrops()
            .noOcclusion()
            .strength(1.0F, 6.0F)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.ALLURING_MAGNIA)
    );

    public static final Block ETCHED_ALLURING_MAGNIA = register(true, "etched_alluring_magnia", Block::new, BlockBehaviour.Properties.ofLegacyCopy(ALLURING_MAGNIA));

    public static final Block REPULSIVE_MAGNIA = register(true, "repulsive_magnia", RepulsiveMagniaBlock::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_GRAY)
            .requiresCorrectToolForDrops()
            .strength(1.5F, 6.0F)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.REPULSIVE_MAGNIA)
    );

    public static final Block REPULSIVE_MAGNIA_SPROUT = register(true, "repulsive_magnia_sprout", RepulsiveMagniaSproutBlock::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_GRAY)
            .lightLevel(state -> state.getValue(MagniaSproutBlock.POWERED) ? 12 : 0)
            .requiresCorrectToolForDrops()
            .noOcclusion()
            .strength(1.0F, 6.0F)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.REPULSIVE_MAGNIA)
    );

    public static final Block ETCHED_REPULSIVE_MAGNIA = register(true, "etched_repulsive_magnia", Block::new, BlockBehaviour.Properties.ofLegacyCopy(REPULSIVE_MAGNIA));

    public static final Block SHADOLINE_ORE = register(true, "shadoline_ore", properties -> new DropExperienceBlock(ConstantInt.of(0), properties), Properties.ofFullCopy(END_STONE).sound(EnderscapeSoundTypes.SHADOLINE_ORE));
    public static final Block MIRESTONE_SHADOLINE_ORE = register(true, "mirestone_shadoline_ore", properties -> new DropExperienceBlock(ConstantInt.of(0), properties), Properties.ofFullCopy(MIRESTONE).sound(EnderscapeSoundTypes.MIRESTONE_SHADOLINE_ORE));

    public static final Block RAW_SHADOLINE_BLOCK = register(true, "raw_shadoline_block", Block::new, Properties.ofFullCopy(RAW_IRON_BLOCK)
            .mapColor(MapColor.COLOR_LIGHT_GRAY)
            .strength(3)
            .sound(EnderscapeSoundTypes.SHADOLINE_ORE)
            .instrument(EnderscapeNoteBlockInstruments.SYNTH_BASS.get())
    );

    public static final Block SHADOLINE_BLOCK = register(true, "shadoline_block", Block::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_GREEN)
            .requiresCorrectToolForDrops()
            .strength(3, 9)
            .sound(EnderscapeSoundTypes.SHADOLINE)
            .instrument(EnderscapeNoteBlockInstruments.SYNTH_BASS.get())
    );

    public static final Block SHADOLINE_BLOCK_STAIRS = registerStair("shadoline_block_stairs", SHADOLINE_BLOCK);
    public static final Block SHADOLINE_BLOCK_SLAB = register(true, "shadoline_block_slab", SlabBlock::new, Properties.ofLegacyCopy(SHADOLINE_BLOCK));
    public static final Block SHADOLINE_BLOCK_WALL = register(true, "shadoline_block_wall", WallBlock::new, Properties.ofLegacyCopy(SHADOLINE_BLOCK));

    public static final Block CUT_SHADOLINE = register(true, "cut_shadoline", Block::new, Properties.ofLegacyCopy(SHADOLINE_BLOCK).sound(EnderscapeSoundTypes.CUT_SHADOLINE));
    public static final Block CUT_SHADOLINE_STAIRS = registerStair("cut_shadoline_stairs", CUT_SHADOLINE);
    public static final Block CUT_SHADOLINE_SLAB = register(true, "cut_shadoline_slab", SlabBlock::new, Properties.ofLegacyCopy(CUT_SHADOLINE));
    public static final Block CUT_SHADOLINE_WALL = register(true, "cut_shadoline_wall", WallBlock::new, Properties.ofLegacyCopy(CUT_SHADOLINE));

    public static final Block CHISELED_SHADOLINE = register(true, "chiseled_shadoline", Block::new, Properties.ofLegacyCopy(CUT_SHADOLINE));
    public static final Block SHADOLINE_PILLAR = register(true, "shadoline_pillar", RotatedPillarBlock::new, Properties.ofLegacyCopy(SHADOLINE_BLOCK));

    public static final Block NEBULITE_ORE = register(true, "nebulite_ore", NebuliteOreBlock::new, Properties.ofFullCopy(END_STONE).sound(EnderscapeSoundTypes.NEBULITE_ORE).randomTicks());
    public static final Block MIRESTONE_NEBULITE_ORE = register(true, "mirestone_nebulite_ore", NebuliteOreBlock::new, Properties.ofFullCopy(MIRESTONE).sound(EnderscapeSoundTypes.MIRESTONE_NEBULITE_ORE).randomTicks());

    public static final Block NEBULITE_BLOCK = register(true, "nebulite_block", Block::new, Properties.of()
            .mapColor(MapColor.COLOR_MAGENTA)
            .requiresCorrectToolForDrops()
            .strength(2, 6)
            .sound(EnderscapeSoundTypes.NEBULITE_BLOCK)
            .instrument(EnderscapeNoteBlockInstruments.SYNTH_BELL.get())
    );

    public static final Block DRY_END_GROWTH = register(true, "dry_end_growth", DryEndGrowthBlock::new, Properties.of()
            .instabreak()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .mapColor(MapColor.SAND)
            .noCollission()
            .noOcclusion()
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .replaceable()
            .sound(EnderscapeSoundTypes.DRY_END_GROWTH)
    );

    public static final Block CHORUS_SPROUTS = register(true, "chorus_sprouts", ChorusSproutsBlock::new, Properties.of()
            .mapColor(MapColor.COLOR_PURPLE)
            .noCollission()
            .instabreak()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.CHORUS_SPROUTS)
            .noOcclusion()
            .offsetType(BlockBehaviour.OffsetType.XZ)
    );

    public static final Block PURPUR_WALL = register(true, "purpur_wall", WallBlock::new, Properties.ofLegacyCopy(PURPUR_BLOCK));
    public static final Block CHISELED_PURPUR = register(true, "chiseled_purpur", Block::new, Properties.ofLegacyCopy(PURPUR_BLOCK));

    public static final Block DUSK_PURPUR_BLOCK = register(true, "dusk_purpur_block", Block::new, Properties.ofFullCopy(PURPUR_BLOCK).mapColor(MapColor.COLOR_BLACK));
    public static final Block DUSK_PURPUR_STAIRS = registerStair("dusk_purpur_stairs", DUSK_PURPUR_BLOCK);
    public static final Block DUSK_PURPUR_SLAB = register(true, "dusk_purpur_slab", SlabBlock::new, Properties.ofFullCopy(PURPUR_SLAB).mapColor(MapColor.COLOR_BLACK));
    public static final Block DUSK_PURPUR_WALL = register(true, "dusk_purpur_wall", WallBlock::new, Properties.ofFullCopy(PURPUR_WALL).mapColor(MapColor.COLOR_BLACK));
    public static final Block CHISELED_DUSK_PURPUR = register(true, "chiseled_dusk_purpur", Block::new, Properties.ofFullCopy(CHISELED_PURPUR).mapColor(MapColor.COLOR_BLACK));
    public static final Block DUSK_PURPUR_PILLAR = register(true, "dusk_purpur_pillar", RotatedPillarBlock::new, Properties.ofFullCopy(PURPUR_PILLAR).mapColor(MapColor.COLOR_BLACK));

    public static final Block PURPUR_TILES = register(true, "purpur_tiles", Block::new, Properties.ofFullCopy(PURPUR_BLOCK));
    public static final Block PURPUR_TILE_STAIRS = registerStair("purpur_tile_stairs", PURPUR_TILES);
    public static final Block PURPUR_TILE_SLAB = register(true, "purpur_tile_slab", SlabBlock::new, Properties.ofFullCopy(PURPUR_SLAB));

    public static final Block CHORUS_CAKE_ROLL = register(false, "chorus_cake_roll", ChorusCakeRollBlock::new, BlockBehaviour.Properties.of().forceSolidOn().strength(0.5F).sound(EnderscapeSoundTypes.CHORUS_CAKE_ROLL).pushReaction(PushReaction.DESTROY));

    public static final Block END_LAMP = register(true, "end_lamp", Block::new, Properties.of()
            .mapColor(MapColor.QUARTZ)
            .instrument(NoteBlockInstrument.PLING)
            .strength(0.6F)
            .sound(EnderscapeSoundTypes.END_LAMP)
            .lightLevel(state -> 15)
            .isRedstoneConductor(Blocks::never)
    );

    public static final Block VEILED_END_STONE = register(true, "veiled_end_stone", VeiledOvergrowthBlock::new, Properties.of()
            .mapColor(MapColor.CLAY)
            .requiresCorrectToolForDrops()
            .strength(3, 9)
            .sound(EnderscapeSoundTypes.VEILED_END_STONE)
            .randomTicks()
            .isValidSpawn(Blocks::always)
    );

    public static final Block WISP_SPROUTS = register(true, "wisp_sprouts", WispSproutsBlock::new, Properties.of()
            .instabreak()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .mapColor(MapColor.CLAY)
            .noCollission()
            .noOcclusion()
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .replaceable()
            .sound(EnderscapeSoundTypes.WISP_GROWTH)
    );

    public static final Block WISP_GROWTH = register(true, "wisp_growth", WispGrowthBlock::new, Properties.of()
            .instabreak()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .mapColor(MapColor.CLAY)
            .noCollission()
            .noOcclusion()
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .replaceable()
            .sound(EnderscapeSoundTypes.WISP_GROWTH)
    );

    public static final Block WISP_FLOWER = register(true, "wisp_flower", WispFlowerBlock::new, Properties.of()
            .mapColor(MapColor.CLAY)
            .noCollission()
            .instabreak()
            .sound(EnderscapeSoundTypes.WISP_FLOWER)
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .ignitedByLava()
            .lightLevel(state -> 7)
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Block STRIPPED_VEILED_LOG = register(true, "stripped_veiled_log", RotatedPillarBlock::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.TERRACOTTA_BLUE)
            .strength(2)
            .sound(EnderscapeSoundTypes.VEILED_LOG)
    );

    public static final Block VEILED_LOG = register(true, "veiled_log", RotatedPillarBlock::new, BlockBehaviour.Properties.ofFullCopy(STRIPPED_VEILED_LOG).mapColor(MapColor.TERRACOTTA_LIGHT_BLUE));
    public static final Block STRIPPED_VEILED_WOOD = register(true, "stripped_veiled_wood", RotatedPillarBlock::new, BlockBehaviour.Properties.ofFullCopy(STRIPPED_VEILED_LOG));
    public static final Block VEILED_WOOD = register(true, "veiled_wood", RotatedPillarBlock::new, BlockBehaviour.Properties.ofFullCopy(STRIPPED_VEILED_WOOD).mapColor(MapColor.TERRACOTTA_LIGHT_BLUE));

    public static final Block VEILED_LEAVES = register(true, "veiled_leaves", VeiledLeavesBlock::new, Blocks.leavesProperties(EnderscapeSoundTypes.VEILED_LEAVES).mapColor(MapColor.CLAY));

    public static final Block VEILED_LEAF_PILE = register(true,
            "veiled_leaf_pile",
            VeiledLeafPileBlock::new,
            BlockBehaviour.Properties.of()
                    .forceSolidOff()
                    .ignitedByLava()
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)
                    .isValidSpawn(Blocks::ocelotOrParrot)
                    .isViewBlocking(Blocks::never)
                    .mapColor(MapColor.CLAY)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY)
                    .randomTicks()
                    .replaceable()
                    .sound(EnderscapeSoundTypes.VEILED_LEAVES)
                    .strength(0.1F)
    );

    public static final Block VEILED_VINES = register(true, "veiled_vines", VeiledVinesBlock::new, Properties.of()
            .mapColor(MapColor.CLAY)
            .noCollission()
            .noOcclusion()
            .pushReaction(PushReaction.DESTROY)
            .sound(EnderscapeSoundTypes.VEILED_LEAVES)
            .strength(0.2F)
    );

    public static final Block VEILED_SAPLING = register(true, "veiled_sapling", properties -> new VeiledSaplingBlock(EnderscapeConfiguredFeatures.VEILED_TREE_FROM_SAPLING, properties), Properties.ofFullCopy(OAK_SAPLING)
            .mapColor(MapColor.CLAY)
            .sound(EnderscapeSoundTypes.CHORUS_SPROUTS)
    );

    public static final Block VEILED_PLANKS = register(true, "veiled_planks", Block::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_BLUE)
            .strength(2, 3)
            .sound(EnderscapeSoundTypes.VEILED_PLANKS)
    );

    public static final Block VEILED_STAIRS = registerStair("veiled_stairs", VEILED_PLANKS);
    public static final Block VEILED_SLAB = register(true, "veiled_slab", SlabBlock::new, Properties.ofLegacyCopy(VEILED_PLANKS));
    public static final Block VEILED_PRESSURE_PLATE = register(true, "veiled_pressure_plate", properties -> new PressurePlateBlock(VEILED_BLOCK_SET, properties), BlockBehaviour.Properties.of()
            .mapColor(VEILED_PLANKS.defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(0.5F)
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Block VEILED_FENCE = register(true, "veiled_fence", FenceBlock::new, Properties.ofLegacyCopy(VEILED_PLANKS));
    public static final Block VEILED_DOOR = register(true, "veiled_door", properties -> new DoorBlock(VEILED_BLOCK_SET, properties), BlockBehaviour.Properties.of()
            .mapColor(VEILED_PLANKS.defaultMapColor())
            .instrument(NoteBlockInstrument.BASS)
            .strength(3)
            .noOcclusion()
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Block VEILED_TRAPDOOR = register(true, "veiled_trapdoor", properties -> new TrapDoorBlock(VEILED_BLOCK_SET, properties), BlockBehaviour.Properties.of()
            .mapColor(VEILED_PLANKS.defaultMapColor())
            .instrument(NoteBlockInstrument.BASS)
            .strength(3)
            .noOcclusion()
            .isValidSpawn(Blocks::never)
            .ignitedByLava()
    );

    public static final Block VEILED_FENCE_GATE = register(true, "veiled_fence_gate", properties -> new FenceGateBlock(VEILED_WOOD_TYPE, properties), BlockBehaviour.Properties.of()
            .mapColor(VEILED_PLANKS.defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .strength(2, 3)
            .ignitedByLava()
    );

    public static final Block VEILED_BUTTON = register(true, "veiled_button", properties -> new ButtonBlock(VEILED_BLOCK_SET, 30, properties), buttonProperties());
    public static final Block VEILED_SIGN = register(false, "veiled_sign", properties -> new StandingSignBlock(VEILED_WOOD_TYPE, properties), BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1).ignitedByLava());
    public static final Block VEILED_WALL_SIGN = register(false, "veiled_wall_sign", properties -> new WallSignBlock(VEILED_WOOD_TYPE, properties), wallVariant(VEILED_SIGN, true).mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1).ignitedByLava());

    public static final Block VEILED_HANGING_SIGN = register(false, "veiled_hanging_sign", properties -> new CeilingHangingSignBlock(VEILED_WOOD_TYPE, properties), BlockBehaviour.Properties.of()
            .mapColor(VEILED_LOG.defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(1)
            .ignitedByLava()
    );

    public static final Block VEILED_WALL_HANGING_SIGN = register(false, "veiled_wall_hanging_sign", properties -> new WallHangingSignBlock(VEILED_WOOD_TYPE, properties), wallVariant(VEILED_HANGING_SIGN, true)
            .mapColor(VEILED_LOG.defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(1)
            .ignitedByLava()
    );

    public static final Block CELESTIAL_PATH_BLOCK = register(true, "celestial_path_block", CelestialPathBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_WHITE)
            .requiresCorrectToolForDrops()
            .strength(3, 9)
            .sound(EnderscapeSoundTypes.CELESTIAL_OVERGROWTH)
            .isViewBlocking(EnderscapeBlocks::always)
            .isSuffocating(EnderscapeBlocks::always)
    );

    public static final Block CELESTIAL_OVERGROWTH = register(true, "celestial_overgrowth", CelestialOvergrowthBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_YELLOW)
            .requiresCorrectToolForDrops()
            .strength(3, 9)
            .sound(EnderscapeSoundTypes.CELESTIAL_OVERGROWTH)
            .randomTicks()
            .isValidSpawn(Blocks::always)
    );

    public static final Block CELESTIAL_GROWTH = register(true, "celestial_growth", CelestialGrowthBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_YELLOW)
            .noCollission()
            .instabreak()
            .sound(EnderscapeSoundTypes.CELESTIAL_GROWTH)
            .noOcclusion()
    );

    public static final Block BULB_FLOWER = register(true, "bulb_flower", BulbFlowerBlock::new, Properties.of()
            .mapColor(MapColor.COLOR_YELLOW)
            .noCollission()
            .instabreak()
            .sound(EnderscapeSoundTypes.BULB_FLOWER)
            .noOcclusion()
            .lightLevel(state -> 7)
    );

    public static final Block BULB_LANTERN = register(true, "bulb_lantern", BulbLanternBlock::new, Properties.ofLegacyCopy(LANTERN).sound(EnderscapeSoundTypes.BULB_LANTERN));

    public static final Block FLANGER_BERRY_VINE = register(false, "flanger_berry_vine", FlangerBerryVine::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_YELLOW)
            .randomTicks()
            .noCollission()
            .sound(EnderscapeSoundTypes.FLANGER_BERRY_VINE)
            .strength(0.2F)
    );

    public static final Block FLANGER_BERRY_FLOWER = register(true, "flanger_berry_flower", FlangerBerryFlowerBlock::new, Properties.of()
            .mapColor(MapColor.WARPED_WART_BLOCK)
            .randomTicks()
            .strength(0.3F)
            .pushReaction(PushReaction.DESTROY)
            .noCollission()
            .sound(EnderscapeSoundTypes.FLANGER_FLOWER)
            .noOcclusion()
    );

    public static final Block UNRIPE_FLANGER_BERRY_BLOCK = register(true, "unripe_flanger_berry_block", UnripeFlangerBerryBlock::new, Properties.of()
            .mapColor(MapColor.WARPED_WART_BLOCK)
            .randomTicks()
            .strength(0.3F)
            .pushReaction(PushReaction.DESTROY)
            .sound(EnderscapeSoundTypes.FLANGER_BERRY_BLOCK)
            .noOcclusion()
    );

    public static final Block RIPE_FLANGER_BERRY_BLOCK = register(true, "ripe_flanger_berry_block", RipeFlangerBerryBlock::new, Properties.of()
            .mapColor(MapColor.WARPED_WART_BLOCK)
            .strength(0.3F)
            .pushReaction(PushReaction.DESTROY)
            .sound(EnderscapeSoundTypes.FLANGER_BERRY_BLOCK)
    );

    public static final Block CELESTIAL_CHANTERELLE = register(true, "celestial_chanterelle", properties -> new CelestialChanterelleBlock(EnderscapeConfiguredFeatures.LARGE_CELESTIAL_CHANTERELLE, properties), Properties.of()
            .mapColor(MapColor.TERRACOTTA_YELLOW)
            .noCollission()
            .instabreak()
            .randomTicks()
            .sound(EnderscapeSoundTypes.CELESTIAL_CHANTERELLE)
    );

    public static final Block STRIPPED_CELESTIAL_STEM = register(true, "stripped_celestial_stem", RotatedPillarBlock::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_ORANGE)
            .strength(2)
            .sound(EnderscapeSoundTypes.CELESTIAL_STEM)
    );

    public static final Block CELESTIAL_STEM = register(true, "celestial_stem", RotatedPillarBlock::new, BlockBehaviour.Properties.ofFullCopy(STRIPPED_CELESTIAL_STEM).mapColor(MapColor.TERRACOTTA_WHITE));

    public static final Block STRIPPED_CELESTIAL_HYPHAE = register(true, "stripped_celestial_hyphae", RotatedPillarBlock::new, BlockBehaviour.Properties.ofFullCopy(STRIPPED_CELESTIAL_STEM));

    public static final Block CELESTIAL_HYPHAE = register(true, "celestial_hyphae", RotatedPillarBlock::new, BlockBehaviour.Properties.ofFullCopy(STRIPPED_CELESTIAL_HYPHAE).mapColor(MapColor.TERRACOTTA_WHITE));

    public static final Block CELESTIAL_CAP = register(true, "celestial_cap", ChanterelleCapBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_YELLOW)
            .strength(1)
            .sound(EnderscapeSoundTypes.CELESTIAL_CAP)
            .isValidSpawn(Blocks::never)
    );

    public static final Block CELESTIAL_BRICKS = register(true, "celestial_bricks", Block::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_YELLOW)
            .strength(1.5F, 6)
            .sound(EnderscapeSoundTypes.CELESTIAL_BRICKS)
    );

    public static final Block CELESTIAL_BRICK_STAIRS = registerStair("celestial_brick_stairs", CELESTIAL_BRICKS);
    public static final Block CELESTIAL_BRICK_SLAB = register(true, "celestial_brick_slab", SlabBlock::new, Properties.ofLegacyCopy(CELESTIAL_BRICKS));
    public static final Block CELESTIAL_BRICK_WALL = register(true, "celestial_brick_wall", WallBlock::new, Properties.ofLegacyCopy(CELESTIAL_BRICKS));

    public static final Block CELESTIAL_PLANKS = register(true, "celestial_planks", Block::new, Properties.of()
            .mapColor(MapColor.COLOR_ORANGE)
            .strength(2, 3)
            .sound(EnderscapeSoundTypes.CELESTIAL_PLANKS)
    );

    public static final Block CELESTIAL_STAIRS = registerStair("celestial_stairs", CELESTIAL_PLANKS);
    public static final Block CELESTIAL_SLAB = register(true, "celestial_slab", SlabBlock::new, Properties.ofLegacyCopy(CELESTIAL_PLANKS));
    public static final Block CELESTIAL_PRESSURE_PLATE = register(true, "celestial_pressure_plate", properties -> new PressurePlateBlock(CELESTIAL_BLOCK_SET, properties), BlockBehaviour.Properties.of()
            .mapColor(CELESTIAL_PLANKS.defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(0.5F)
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Block CELESTIAL_FENCE = register(true, "celestial_fence", FenceBlock::new, Properties.ofLegacyCopy(CELESTIAL_PLANKS));
    public static final Block CELESTIAL_DOOR = register(true, "celestial_door", properties -> new DoorBlock(CELESTIAL_BLOCK_SET, properties), BlockBehaviour.Properties.of()
            .mapColor(CELESTIAL_PLANKS.defaultMapColor())
            .instrument(NoteBlockInstrument.BASS)
            .strength(3)
            .noOcclusion()
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Block CELESTIAL_TRAPDOOR = register(true, "celestial_trapdoor", properties -> new TrapDoorBlock(CELESTIAL_BLOCK_SET, properties), BlockBehaviour.Properties.of()
            .mapColor(CELESTIAL_PLANKS.defaultMapColor())
            .instrument(NoteBlockInstrument.BASS)
            .strength(3)
            .noOcclusion()
            .isValidSpawn(Blocks::never)
            .ignitedByLava()
    );

    public static final Block CELESTIAL_FENCE_GATE = register(true, "celestial_fence_gate", properties -> new FenceGateBlock(CELESTIAL_WOOD_TYPE, properties), BlockBehaviour.Properties.of()
            .mapColor(CELESTIAL_PLANKS.defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .strength(2, 3)
            .ignitedByLava()
    );

    public static final Block CELESTIAL_BUTTON = register(true, "celestial_button", properties -> new ButtonBlock(CELESTIAL_BLOCK_SET, 30, properties), buttonProperties());
    public static final Block CELESTIAL_SIGN = register(false, "celestial_sign", properties -> new StandingSignBlock(CELESTIAL_WOOD_TYPE, properties), BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1).ignitedByLava());
    public static final Block CELESTIAL_WALL_SIGN = register(false, "celestial_wall_sign", properties -> new WallSignBlock(CELESTIAL_WOOD_TYPE, properties), wallVariant(CELESTIAL_SIGN, true).mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1).ignitedByLava());

    public static final Block CELESTIAL_HANGING_SIGN = register(false, "celestial_hanging_sign", properties -> new CeilingHangingSignBlock(CELESTIAL_WOOD_TYPE, properties), BlockBehaviour.Properties.of()
            .mapColor(CELESTIAL_STEM.defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(1)
            .ignitedByLava()
    );

    public static final Block CELESTIAL_WALL_HANGING_SIGN = register(false, "celestial_wall_hanging_sign", properties -> new WallHangingSignBlock(CELESTIAL_WOOD_TYPE, properties), wallVariant(CELESTIAL_HANGING_SIGN, true)
            .mapColor(CELESTIAL_STEM.defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(1)
            .ignitedByLava()
    );

    public static final Block CORRUPT_PATH_BLOCK = register(true, "corrupt_path_block", CorruptPathBlock::new, Properties.of()
            .mapColor(MapColor.COLOR_PURPLE)
            .requiresCorrectToolForDrops()
            .strength(3, 9)
            .sound(EnderscapeSoundTypes.CORRUPT_OVERGROWTH)
            .isSuffocating(EnderscapeBlocks::always)
    );

    public static final Block CORRUPT_OVERGROWTH = register(true, "corrupt_overgrowth", CorruptOvergrowthBlock::new, Properties.of()
            .mapColor(MapColor.COLOR_PURPLE)
            .requiresCorrectToolForDrops()
            .strength(6, 9)
            .sound(EnderscapeSoundTypes.CORRUPT_OVERGROWTH)
            .isValidSpawn(Blocks::always)
    );

    public static final Block CORRUPT_GROWTH = register(true, "corrupt_growth", CorruptGrowthBlock::new, Properties.of()
            .mapColor(MapColor.COLOR_PURPLE)
            .noCollission()
            .instabreak()
            .sound(EnderscapeSoundTypes.CORRUPT_GROWTH)
            .noOcclusion()
    );

    public static final Block BLINKLIGHT_VINES_BODY = register(false, "blinklight_vines_body", BlinklightVinesBodyBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)
            .noCollission()
            .sound(EnderscapeSoundTypes.BLINKLIGHT_VINES)
            .strength(0.8F)
            .randomTicks()
            .lightLevel(BlinklightVines::getLuminance)
    );

    public static final Block BLINKLIGHT_VINES_HEAD = register(false, "blinklight_vines_head", BlinklightVinesHeadBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)
            .noCollission()
            .sound(EnderscapeSoundTypes.BLINKLIGHT_VINES)
            .strength(0.8F)
            .randomTicks()
            .lightLevel(BlinklightVines::getLuminance)
    );

    public static final Block BLINKLAMP = register(true, "blinklamp", BlinklampBlock::new, Properties.of()
            .mapColor(MapColor.COLOR_PINK)
            .strength(3, 9)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.BLINKLAMP)
    );

    public static final Block MURUBLIGHT_SHELF = register(false, "murublight_shelf", MurublightShelfBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)
            .noCollission()
            .instabreak()
            .sound(EnderscapeSoundTypes.MURUBLIGHT_SHELF)
            .noOcclusion()
    );

    public static final Block MURUBLIGHT_CHANTERELLE = register(true, "murublight_chanterelle", MurublightChanterelleBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)
            .noCollission()
            .instabreak()
            .randomTicks()
            .sound(EnderscapeSoundTypes.CELESTIAL_CHANTERELLE)
    );

    public static final Block STRIPPED_MURUBLIGHT_STEM = register(true, "stripped_murublight_stem", RotatedPillarBlock::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)
            .strength(2)
            .sound(EnderscapeSoundTypes.MURUBLIGHT_STEM)
    );

    public static final Block MURUBLIGHT_STEM = register(true, "murublight_stem", RotatedPillarBlock::new, BlockBehaviour.Properties.ofFullCopy(STRIPPED_MURUBLIGHT_STEM).mapColor(MapColor.COLOR_BLACK));
    public static final Block STRIPPED_MURUBLIGHT_HYPHAE = register(true, "stripped_murublight_hyphae", RotatedPillarBlock::new, BlockBehaviour.Properties.ofFullCopy(STRIPPED_MURUBLIGHT_STEM));
    public static final Block MURUBLIGHT_HYPHAE = register(true, "murublight_hyphae", RotatedPillarBlock::new, BlockBehaviour.Properties.ofFullCopy(STRIPPED_MURUBLIGHT_HYPHAE).mapColor(MapColor.COLOR_BLACK));

    public static final Block MURUBLIGHT_CAP = register(true, "murublight_cap", ChanterelleCapBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_BLUE)
            .strength(1)
            .sound(EnderscapeSoundTypes.MURUBLIGHT_CAP)
            .isValidSpawn(Blocks::never)
    );

    public static final Block MURUBLIGHT_BRICKS = register(true, "murublight_bricks", Block::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_BLUE)
            .strength(1.5F, 6)
            .sound(EnderscapeSoundTypes.MURUBLIGHT_BRICKS)
    );

    public static final Block MURUBLIGHT_BRICK_STAIRS = registerStair("murublight_brick_stairs", MURUBLIGHT_BRICKS);
    public static final Block MURUBLIGHT_BRICK_SLAB = register(true, "murublight_brick_slab", SlabBlock::new, Properties.ofLegacyCopy(MURUBLIGHT_BRICKS));
    public static final Block MURUBLIGHT_BRICK_WALL = register(true, "murublight_brick_wall", WallBlock::new, Properties.ofLegacyCopy(MURUBLIGHT_BRICKS));

    public static final Block MURUBLIGHT_PLANKS = register(true, "murublight_planks", Block::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)
            .strength(2, 3)
            .sound(EnderscapeSoundTypes.MURUBLIGHT_PLANKS)
    );

    public static final Block MURUBLIGHT_STAIRS = registerStair("murublight_stairs", MURUBLIGHT_PLANKS);

    public static final Block MURUBLIGHT_SLAB = register(true, "murublight_slab", SlabBlock::new, Properties.ofLegacyCopy(MURUBLIGHT_PLANKS));

    public static final Block MURUBLIGHT_PRESSURE_PLATE = register(true, "murublight_pressure_plate", properties -> new PressurePlateBlock(MURUBLIGHT_BLOCK_SET, properties), BlockBehaviour.Properties.of()
            .mapColor(MURUBLIGHT_PLANKS.defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(0.5F)
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Block MURUBLIGHT_FENCE = register(true, "murublight_fence", FenceBlock::new, Properties.ofLegacyCopy(MURUBLIGHT_PLANKS));

    public static final Block MURUBLIGHT_DOOR = register(true, "murublight_door", properties -> new DoorBlock(MURUBLIGHT_BLOCK_SET, properties), BlockBehaviour.Properties.of()
            .mapColor(MURUBLIGHT_PLANKS.defaultMapColor())
            .instrument(NoteBlockInstrument.BASS)
            .strength(3)
            .noOcclusion()
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Block MURUBLIGHT_TRAPDOOR = register(true, "murublight_trapdoor", properties -> new TrapDoorBlock(MURUBLIGHT_BLOCK_SET, properties), BlockBehaviour.Properties.of()
            .mapColor(MURUBLIGHT_PLANKS.defaultMapColor())
            .instrument(NoteBlockInstrument.BASS)
            .strength(3)
            .noOcclusion()
            .isValidSpawn(Blocks::never)
            .ignitedByLava()
    );

    public static final Block MURUBLIGHT_FENCE_GATE = register(true, "murublight_fence_gate", properties -> new FenceGateBlock(MURUBLIGHT_WOOD_TYPE, properties), BlockBehaviour.Properties.of()
            .mapColor(MURUBLIGHT_PLANKS.defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .strength(2, 3)
            .ignitedByLava()
    );

    public static final Block MURUBLIGHT_BUTTON = register(true, "murublight_button", properties -> new ButtonBlock(MURUBLIGHT_BLOCK_SET, 30, properties), buttonProperties());
    public static final Block MURUBLIGHT_SIGN = register(false, "murublight_sign", properties -> new StandingSignBlock(MURUBLIGHT_WOOD_TYPE, properties), BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1).ignitedByLava());
    public static final Block MURUBLIGHT_WALL_SIGN = register(false, "murublight_wall_sign", properties -> new WallSignBlock(MURUBLIGHT_WOOD_TYPE, properties), wallVariant(MURUBLIGHT_SIGN, true).mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1).ignitedByLava());

    public static final Block MURUBLIGHT_HANGING_SIGN = register(false, "murublight_hanging_sign", properties -> new CeilingHangingSignBlock(MURUBLIGHT_WOOD_TYPE, properties), BlockBehaviour.Properties.of()
            .mapColor(MURUBLIGHT_STEM.defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(1)
            .ignitedByLava()
    );

    public static final Block MURUBLIGHT_WALL_HANGING_SIGN = register(false, "murublight_wall_hanging_sign", properties -> new WallHangingSignBlock(MURUBLIGHT_WOOD_TYPE, properties), wallVariant(MURUBLIGHT_HANGING_SIGN, true)
            .mapColor(MURUBLIGHT_STEM.defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(1)
            .ignitedByLava()
    );

    public static final Block POTTED_ALLURING_MAGNIA_SPROUT = register(false, "potted_alluring_magnia_sprout", properties -> new FlowerPotBlock(ALLURING_MAGNIA_SPROUT, properties), flowerPotProperties());
    public static final Block POTTED_BLINKLIGHT = register(false, "potted_blinklight", properties -> new FlowerPotBlock(BLINKLIGHT_VINES_HEAD, properties), flowerPotProperties().lightLevel(state -> 12));
    public static final Block POTTED_BULB_FLOWER = register(false, "potted_bulb_flower", properties -> new FlowerPotBlock(BULB_FLOWER, properties), flowerPotProperties().lightLevel(state -> 7));
    public static final Block POTTED_CELESTIAL_CHANTERELLE = register(false, "potted_celestial_chanterelle", properties -> new FlowerPotBlock(CELESTIAL_CHANTERELLE, properties), flowerPotProperties());
    public static final Block POTTED_CELESTIAL_GROWTH = register(false, "potted_celestial_growth", properties -> new FlowerPotBlock(CELESTIAL_GROWTH, properties), flowerPotProperties());
    public static final Block POTTED_CHORUS_SPROUTS = register(false, "potted_chorus_sprouts", properties -> new FlowerPotBlock(CHORUS_SPROUTS, properties), flowerPotProperties());
    public static final Block POTTED_CORRUPT_GROWTH = register(false, "potted_corrupt_growth", properties -> new FlowerPotBlock(CORRUPT_GROWTH, properties), flowerPotProperties());
    public static final Block POTTED_DRY_END_GROWTH = register(false, "potted_dry_end_growth", properties -> new FlowerPotBlock(DRY_END_GROWTH, properties), flowerPotProperties());
    public static final Block POTTED_MURUBLIGHT_CHANTERELLE = register(false, "potted_murublight_chanterelle", properties -> new FlowerPotBlock(MURUBLIGHT_CHANTERELLE, properties), flowerPotProperties());
    public static final Block POTTED_REPULSIVE_MAGNIA_SPROUT = register(false, "potted_repulsive_magnia_sprout", properties -> new FlowerPotBlock(REPULSIVE_MAGNIA_SPROUT, properties), flowerPotProperties());
    public static final Block POTTED_VEILED_SAPLING = register(false, "potted_veiled_sapling", properties -> new FlowerPotBlock(VEILED_SAPLING, properties), flowerPotProperties());
    public static final Block POTTED_WISP_GROWTH = register(false, "potted_wisp_growth", properties -> new FlowerPotBlock(WISP_GROWTH, properties), flowerPotProperties());

    private static Block registerStair(String string, Block block) {
        return register(true, string, properties -> new StairBlock(block.defaultBlockState(), properties), BlockBehaviour.Properties.ofFullCopy(block));
    }

    private static BlockBehaviour.Properties wallVariant(Block block, boolean bl) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().overrideLootTable(block.getLootTable());
        if (bl) properties = properties.overrideDescription(block.getDescriptionId());

        return properties;
    }

    private static Block register(boolean hasItem, String string, Function<BlockBehaviour.Properties, Block> function, BlockBehaviour.Properties properties) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, Enderscape.id(string));
        Block block = function.apply(properties.setId(key));

        Registry.register(BuiltInRegistries.BLOCK, key, block);

        if (hasItem) EnderscapeItems.registerBlock(block);

        return block;
    }

    private static Block register(String string, Function<Item.Properties, Item> itemFunction, Item.Properties itemProperties, Function<BlockBehaviour.Properties, Block> function, BlockBehaviour.Properties properties) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, Enderscape.id(string));
        Block block = function.apply(properties.setId(key));

        Registry.register(BuiltInRegistries.BLOCK, key, block);

        EnderscapeItems.registerItem(string, itemFunction, itemProperties);

        return block;
    }

    private static boolean always(BlockState state, BlockGetter world, BlockPos pos) {
        return true;
    }

    public static final BlockStateProvider VEILED_OVERGROWTH_BONEMEAL_PROVIDER = new WeightedStateProvider(new SimpleWeightedRandomList.Builder<BlockState>().add(EnderscapeBlocks.WISP_SPROUTS.defaultBlockState(), 3).add(EnderscapeBlocks.WISP_GROWTH.defaultBlockState(), 1));
    public static final BlockStateProvider CELESTIAL_OVERGROWTH_BONEMEAL_PROVIDER = new WeightedStateProvider(new SimpleWeightedRandomList.Builder<BlockState>().add(EnderscapeBlocks.CELESTIAL_CHANTERELLE.defaultBlockState(), 3).add(EnderscapeBlocks.BULB_FLOWER.defaultBlockState(), 1));
    public static final BlockStateProvider CORRUPT_OVERGROWTH_BONEMEAL_PROVIDER = new WeightedStateProvider(new SimpleWeightedRandomList.Builder<BlockState>().add(EnderscapeBlocks.MURUBLIGHT_CHANTERELLE.defaultBlockState(), 3));

}