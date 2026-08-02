package net.penumbra.enderscape.registry.block;

import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.WeightedList;
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
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.block.*;
import net.penumbra.enderscape.block.properties.MagniaPolarity;
import net.penumbra.enderscape.block.state.StateProperties;
import net.penumbra.enderscape.references.BlockItemId;
import net.penumbra.enderscape.references.EnderscapeBlockIds;
import net.penumbra.enderscape.references.EnderscapeBlockItemIds;
import net.penumbra.enderscape.registry.item.EnderscapeItems;
import net.penumbra.enderscape.registry.level.EnderscapeConfiguredFeatures;
import net.penumbra.enderscape.registry.sound.EnderscapeBlockSounds;
import net.penumbra.enderscape.registry.sound.EnderscapeSoundTypes;

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

    public static final WoodType VEILED_WOOD_TYPE = new WoodTypeBuilder().soundType(EnderscapeSoundTypes.VEILED_PLANKS).fenceGateCloseSound(EnderscapeBlockSounds.VEILED_FENCE_GATE_CLOSE).fenceGateOpenSound(EnderscapeBlockSounds.VEILED_FENCE_GATE_OPEN).hangingSignSoundType(EnderscapeSoundTypes.VEILED_HANGING_SIGN).register(Enderscape.id("veiled"), VEILED_BLOCK_SET);
    public static final WoodType CELESTIAL_WOOD_TYPE = new WoodTypeBuilder().soundType(EnderscapeSoundTypes.CELESTIAL_PLANKS).fenceGateCloseSound(EnderscapeBlockSounds.CELESTIAL_FENCE_GATE_CLOSE).fenceGateOpenSound(EnderscapeBlockSounds.CELESTIAL_FENCE_GATE_OPEN).hangingSignSoundType(EnderscapeSoundTypes.CELESTIAL_HANGING_SIGN).register(Enderscape.id("celestial"), CELESTIAL_BLOCK_SET);
    public static final WoodType MURUBLIGHT_WOOD_TYPE = new WoodTypeBuilder().soundType(EnderscapeSoundTypes.MURUBLIGHT_PLANKS).fenceGateCloseSound(EnderscapeBlockSounds.MURUBLIGHT_FENCE_GATE_CLOSE).fenceGateOpenSound(EnderscapeBlockSounds.MURUBLIGHT_FENCE_GATE_OPEN).hangingSignSoundType(EnderscapeSoundTypes.MURUBLIGHT_HANGING_SIGN).register(Enderscape.id("murublight"), MURUBLIGHT_BLOCK_SET);

    public static final Block DRIFT_JELLY_BLOCK = registerWithItem(EnderscapeBlockItemIds.DRIFT_JELLY_BLOCK, DriftJellyBlock::new, Properties.of()
            .mapColor(MapColor.WARPED_WART_BLOCK)
            .instabreak()
            .sound(EnderscapeSoundTypes.DRIFT_JELLY_BLOCK)
            .noOcclusion()
    );

    public static final Block END_TRIAL_SPAWNER = registerWithItem(EnderscapeBlockItemIds.END_TRIAL_SPAWNER, TrialSpawnerBlock::new, Properties.ofFullCopy(TRIAL_SPAWNER));
    public static final Block END_VAULT = registerWithItem(EnderscapeBlockItemIds.END_VAULT, EndVaultBlock::new, Properties.ofFullCopy(VAULT).sound(EnderscapeSoundTypes.END_VAULT));

    public static final Block END_HAVEN_CORE = registerWithItem(EnderscapeBlockItemIds.END_HAVEN_CORE, EndHavenCoreBlock::new, Properties.of()
            .mapColor(MapColor.STONE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .noOcclusion()
            .sound(EnderscapeSoundTypes.END_HAVEN_CORE)
            .lightLevel(EndHavenCoreBlock::lightLevel)
            .strength(50.0F)
            .isViewBlocking(Blocks::never)
    );

    public static final Block END_STONE_STAIRS = registerStair(EnderscapeBlockItemIds.END_STONE_STAIRS, END_STONE);
    public static final Block END_STONE_SLAB = registerWithItem(EnderscapeBlockItemIds.END_STONE_SLAB, SlabBlock::new, Properties.ofLegacyCopy(END_STONE));
    public static final Block END_STONE_WALL = registerWithItem(EnderscapeBlockItemIds.END_STONE_WALL, WallBlock::new, Properties.ofLegacyCopy(END_STONE));

    public static final Block POLISHED_END_STONE = registerWithItem(EnderscapeBlockItemIds.POLISHED_END_STONE, Block::new, Properties.ofLegacyCopy(END_STONE).sound(SoundType.STONE));
    public static final Block POLISHED_END_STONE_STAIRS = registerStair(EnderscapeBlockItemIds.POLISHED_END_STONE_STAIRS, POLISHED_END_STONE);
    public static final Block POLISHED_END_STONE_SLAB = registerWithItem(EnderscapeBlockItemIds.POLISHED_END_STONE_SLAB, SlabBlock::new, Properties.ofLegacyCopy(POLISHED_END_STONE));
    public static final Block POLISHED_END_STONE_WALL = registerWithItem(EnderscapeBlockItemIds.POLISHED_END_STONE_WALL, WallBlock::new, Properties.ofLegacyCopy(POLISHED_END_STONE));
    public static final Block POLISHED_END_STONE_BUTTON = registerWithItem(EnderscapeBlockItemIds.POLISHED_END_STONE_BUTTON, properties -> new ButtonBlock(POLISHED_END_STONE_BLOCK_SET, 20, properties), buttonProperties());
    public static final Block POLISHED_END_STONE_PRESSURE_PLATE = registerWithItem(EnderscapeBlockItemIds.POLISHED_END_STONE_PRESSURE_PLATE, properties -> new PressurePlateBlock(POLISHED_END_STONE_BLOCK_SET, properties), Properties.of()
            .mapColor(MapColor.SAND)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(SoundType.STONE)
            .requiresCorrectToolForDrops()
            .noCollision()
            .strength(0.5F)
            .pushReaction(PushReaction.DESTROY)
    );
    public static final Block CRACKED_END_STONE_BRICKS = registerWithItem(EnderscapeBlockItemIds.CRACKED_END_STONE_BRICKS, Block::new, Properties.ofLegacyCopy(END_STONE_BRICKS).sound(SoundType.STONE));
    public static final Block OVERGROWN_END_STONE_BRICKS = registerWithItem(EnderscapeBlockItemIds.OVERGROWN_END_STONE_BRICKS, Block::new, Properties.ofLegacyCopy(END_STONE_BRICKS).sound(SoundType.STONE));
    public static final Block OVERGROWN_END_STONE_BRICK_STAIRS = registerStair(EnderscapeBlockItemIds.OVERGROWN_END_STONE_BRICK_STAIRS, OVERGROWN_END_STONE_BRICKS);
    public static final Block OVERGROWN_END_STONE_BRICK_SLAB = registerWithItem(EnderscapeBlockItemIds.OVERGROWN_END_STONE_BRICK_SLAB, SlabBlock::new, Properties.ofLegacyCopy(OVERGROWN_END_STONE_BRICKS));
    public static final Block OVERGROWN_END_STONE_BRICK_WALL = registerWithItem(EnderscapeBlockItemIds.OVERGROWN_END_STONE_BRICK_WALL, WallBlock::new, Properties.ofLegacyCopy(OVERGROWN_END_STONE_BRICKS));

    public static final Block CHISELED_END_STONE = registerWithItem(EnderscapeBlockItemIds.CHISELED_END_STONE, Block::new, Properties.ofLegacyCopy(POLISHED_END_STONE).sound(SoundType.STONE));

    public static final Block MIRESTONE = registerWithItem(EnderscapeBlockItemIds.MIRESTONE, Block::new, Properties.of()
            .mapColor(MapColor.STONE)
            .requiresCorrectToolForDrops()
            .strength(6, 9)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.MIRESTONE)
    );

    public static final Block MIRESTONE_STAIRS = registerStair(EnderscapeBlockItemIds.MIRESTONE_STAIRS, MIRESTONE);
    public static final Block MIRESTONE_SLAB = registerWithItem(EnderscapeBlockItemIds.MIRESTONE_SLAB, SlabBlock::new, Properties.ofLegacyCopy(MIRESTONE));
    public static final Block MIRESTONE_WALL = registerWithItem(EnderscapeBlockItemIds.MIRESTONE_WALL, WallBlock::new, Properties.ofLegacyCopy(MIRESTONE));

    public static final Block POLISHED_MIRESTONE = registerWithItem(EnderscapeBlockItemIds.POLISHED_MIRESTONE, Block::new, Properties.ofLegacyCopy(MIRESTONE));
    public static final Block POLISHED_MIRESTONE_STAIRS = registerStair(EnderscapeBlockItemIds.POLISHED_MIRESTONE_STAIRS, POLISHED_MIRESTONE);
    public static final Block POLISHED_MIRESTONE_SLAB = registerWithItem(EnderscapeBlockItemIds.POLISHED_MIRESTONE_SLAB, SlabBlock::new, Properties.ofLegacyCopy(POLISHED_MIRESTONE));
    public static final Block POLISHED_MIRESTONE_WALL = registerWithItem(EnderscapeBlockItemIds.POLISHED_MIRESTONE_WALL, WallBlock::new, Properties.ofLegacyCopy(POLISHED_MIRESTONE));
    public static final Block POLISHED_MIRESTONE_BUTTON = registerWithItem(EnderscapeBlockItemIds.POLISHED_MIRESTONE_BUTTON, properties -> new ButtonBlock(POLISHED_MIRESTONE_BLOCK_SET, 20, properties), buttonProperties());
    public static final Block POLISHED_MIRESTONE_PRESSURE_PLATE = registerWithItem(EnderscapeBlockItemIds.POLISHED_MIRESTONE_PRESSURE_PLATE, properties -> new PressurePlateBlock(POLISHED_MIRESTONE_BLOCK_SET, properties), Properties.of()
            .mapColor(MapColor.STONE)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.MIRESTONE)
            .requiresCorrectToolForDrops()
            .noCollision()
            .strength(0.5F)
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Block MIRESTONE_BRICKS = registerWithItem(EnderscapeBlockItemIds.MIRESTONE_BRICKS, Block::new, Properties.ofLegacyCopy(POLISHED_MIRESTONE).sound(EnderscapeSoundTypes.MIRESTONE_BRICKS));
    public static final Block CRACKED_MIRESTONE_BRICKS = registerWithItem(EnderscapeBlockItemIds.CRACKED_MIRESTONE_BRICKS, Block::new, Properties.ofLegacyCopy(MIRESTONE_BRICKS).sound(EnderscapeSoundTypes.MIRESTONE_BRICKS));
    public static final Block MIRESTONE_BRICK_STAIRS = registerStair(EnderscapeBlockItemIds.MIRESTONE_BRICK_STAIRS, MIRESTONE_BRICKS);
    public static final Block MIRESTONE_BRICK_SLAB = registerWithItem(EnderscapeBlockItemIds.MIRESTONE_BRICK_SLAB, SlabBlock::new, Properties.ofLegacyCopy(MIRESTONE_BRICKS));
    public static final Block MIRESTONE_BRICK_WALL = registerWithItem(EnderscapeBlockItemIds.MIRESTONE_BRICK_WALL, WallBlock::new, Properties.ofLegacyCopy(MIRESTONE_BRICKS));

    public static final Block OVERGROWN_MIRESTONE_BRICKS = registerWithItem(EnderscapeBlockItemIds.OVERGROWN_MIRESTONE_BRICKS, Block::new, Properties.ofLegacyCopy(MIRESTONE_BRICKS));
    public static final Block OVERGROWN_MIRESTONE_BRICK_STAIRS = registerStair(EnderscapeBlockItemIds.OVERGROWN_MIRESTONE_BRICK_STAIRS, OVERGROWN_MIRESTONE_BRICKS);
    public static final Block OVERGROWN_MIRESTONE_BRICK_SLAB = registerWithItem(EnderscapeBlockItemIds.OVERGROWN_MIRESTONE_BRICK_SLAB, SlabBlock::new, Properties.ofLegacyCopy(OVERGROWN_MIRESTONE_BRICKS));
    public static final Block OVERGROWN_MIRESTONE_BRICK_WALL = registerWithItem(EnderscapeBlockItemIds.OVERGROWN_MIRESTONE_BRICK_WALL, WallBlock::new, Properties.ofLegacyCopy(OVERGROWN_MIRESTONE_BRICKS));

    public static final Block CHISELED_MIRESTONE = registerWithItem(EnderscapeBlockItemIds.CHISELED_MIRESTONE, Block::new, Properties.ofLegacyCopy(POLISHED_MIRESTONE).sound(EnderscapeSoundTypes.MIRESTONE_BRICKS));

    public static final Block VERADITE = registerWithItem(EnderscapeBlockItemIds.VERADITE, Block::new, Properties.of()
            .mapColor(MapColor.GLOW_LICHEN)
            .requiresCorrectToolForDrops()
            .strength(1.5F, 6.0F)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.VERADITE)
    );

    public static final Block VERADITE_STAIRS = registerStair(EnderscapeBlockItemIds.VERADITE_STAIRS, VERADITE);
    public static final Block VERADITE_SLAB = registerWithItem(EnderscapeBlockItemIds.VERADITE_SLAB, SlabBlock::new, Properties.ofLegacyCopy(VERADITE));
    public static final Block VERADITE_WALL = registerWithItem(EnderscapeBlockItemIds.VERADITE_WALL, WallBlock::new, Properties.ofLegacyCopy(VERADITE));

    public static final Block POLISHED_VERADITE = registerWithItem(EnderscapeBlockItemIds.POLISHED_VERADITE, Block::new, Properties.ofLegacyCopy(VERADITE));
    public static final Block POLISHED_VERADITE_STAIRS = registerStair(EnderscapeBlockItemIds.POLISHED_VERADITE_STAIRS, POLISHED_VERADITE);
    public static final Block POLISHED_VERADITE_SLAB = registerWithItem(EnderscapeBlockItemIds.POLISHED_VERADITE_SLAB, SlabBlock::new, Properties.ofLegacyCopy(POLISHED_VERADITE));
    public static final Block POLISHED_VERADITE_WALL = registerWithItem(EnderscapeBlockItemIds.POLISHED_VERADITE_WALL, WallBlock::new, Properties.ofLegacyCopy(POLISHED_VERADITE));
    public static final Block POLISHED_VERADITE_BUTTON = registerWithItem(EnderscapeBlockItemIds.POLISHED_VERADITE_BUTTON, properties -> new ButtonBlock(POLISHED_VERADITE_BLOCK_SET, 20, properties), buttonProperties());
    public static final Block POLISHED_VERADITE_PRESSURE_PLATE = registerWithItem(EnderscapeBlockItemIds.POLISHED_VERADITE_PRESSURE_PLATE, properties -> new PressurePlateBlock(POLISHED_VERADITE_BLOCK_SET, properties), Properties.of()
            .mapColor(MapColor.GLOW_LICHEN)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.VERADITE)
            .requiresCorrectToolForDrops()
            .noCollision()
            .strength(0.5F)
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Block VERADITE_BRICKS = registerWithItem(EnderscapeBlockItemIds.VERADITE_BRICKS, Block::new, Properties.ofLegacyCopy(POLISHED_VERADITE).sound(EnderscapeSoundTypes.VERADITE_BRICKS));
    public static final Block VERADITE_BRICK_STAIRS = registerStair(EnderscapeBlockItemIds.VERADITE_BRICK_STAIRS, VERADITE_BRICKS);
    public static final Block VERADITE_BRICK_SLAB = registerWithItem(EnderscapeBlockItemIds.VERADITE_BRICK_SLAB, SlabBlock::new, Properties.ofLegacyCopy(VERADITE_BRICKS));
    public static final Block VERADITE_BRICK_WALL = registerWithItem(EnderscapeBlockItemIds.VERADITE_BRICK_WALL, WallBlock::new, Properties.ofLegacyCopy(VERADITE_BRICKS));

    public static final Block CHISELED_VERADITE = registerWithItem(EnderscapeBlockItemIds.CHISELED_VERADITE, Block::new, Properties.ofLegacyCopy(POLISHED_VERADITE).sound(EnderscapeSoundTypes.VERADITE_BRICKS));

    public static final Block KURODITE = registerWithItem(EnderscapeBlockItemIds.KURODITE, Block::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_GRAY)
            .requiresCorrectToolForDrops()
            .strength(1.5F, 6.0F)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.KURODITE)
    );

    public static final Block KURODITE_STAIRS = registerStair(EnderscapeBlockItemIds.KURODITE_STAIRS, KURODITE);
    public static final Block KURODITE_SLAB = registerWithItem(EnderscapeBlockItemIds.KURODITE_SLAB, SlabBlock::new, Properties.ofLegacyCopy(KURODITE));
    public static final Block KURODITE_WALL = registerWithItem(EnderscapeBlockItemIds.KURODITE_WALL, WallBlock::new, Properties.ofLegacyCopy(KURODITE));

    public static final Block POLISHED_KURODITE = registerWithItem(EnderscapeBlockItemIds.POLISHED_KURODITE, Block::new, Properties.ofLegacyCopy(KURODITE));
    public static final Block POLISHED_KURODITE_STAIRS = registerStair(EnderscapeBlockItemIds.POLISHED_KURODITE_STAIRS, POLISHED_KURODITE);
    public static final Block POLISHED_KURODITE_SLAB = registerWithItem(EnderscapeBlockItemIds.POLISHED_KURODITE_SLAB, SlabBlock::new, Properties.ofLegacyCopy(POLISHED_KURODITE));
    public static final Block POLISHED_KURODITE_WALL = registerWithItem(EnderscapeBlockItemIds.POLISHED_KURODITE_WALL, WallBlock::new, Properties.ofLegacyCopy(POLISHED_KURODITE));
    public static final Block POLISHED_KURODITE_BUTTON = registerWithItem(EnderscapeBlockItemIds.POLISHED_KURODITE_BUTTON, properties -> new ButtonBlock(POLISHED_KURODITE_BLOCK_SET, 20, properties), buttonProperties());
    public static final Block POLISHED_KURODITE_PRESSURE_PLATE = registerWithItem(EnderscapeBlockItemIds.POLISHED_KURODITE_PRESSURE_PLATE, properties -> new PressurePlateBlock(POLISHED_KURODITE_BLOCK_SET, properties), Properties.of()
            .mapColor(MapColor.TERRACOTTA_GRAY)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.KURODITE)
            .requiresCorrectToolForDrops()
            .noCollision()
            .strength(0.5F)
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Block KURODITE_BRICKS = registerWithItem(EnderscapeBlockItemIds.KURODITE_BRICKS, Block::new, Properties.ofLegacyCopy(POLISHED_KURODITE).sound(EnderscapeSoundTypes.KURODITE_BRICKS));
    public static final Block KURODITE_BRICK_STAIRS = registerStair(EnderscapeBlockItemIds.KURODITE_BRICK_STAIRS, KURODITE_BRICKS);
    public static final Block KURODITE_BRICK_SLAB = registerWithItem(EnderscapeBlockItemIds.KURODITE_BRICK_SLAB, SlabBlock::new, Properties.ofLegacyCopy(KURODITE_BRICKS));
    public static final Block KURODITE_BRICK_WALL = registerWithItem(EnderscapeBlockItemIds.KURODITE_BRICK_WALL, WallBlock::new, Properties.ofLegacyCopy(KURODITE_BRICKS));

    public static final Block CHISELED_KURODITE = registerWithItem(EnderscapeBlockItemIds.CHISELED_KURODITE, Block::new, Properties.ofLegacyCopy(POLISHED_KURODITE).sound(EnderscapeSoundTypes.KURODITE_BRICKS));

    public static final Block ALLURING_MAGNIA = registerWithItem(EnderscapeBlockItemIds.ALLURING_MAGNIA, properties -> new MagniaBlock(MagniaPolarity.ALLURING, properties), Properties.of()
            .mapColor(MapColor.METAL)
            .requiresCorrectToolForDrops()
            .strength(1.5F, 6.0F)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.ALLURING_MAGNIA)
    );

    public static final Block ALLURING_MAGNIA_SPROUT = registerWithItem(EnderscapeBlockItemIds.ALLURING_MAGNIA_SPROUT, properties -> new MagniaSproutBlock(MagniaPolarity.ALLURING, properties), Properties.of()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .lightLevel(state -> state.getValue(StateProperties.POWERED) ? 12 : 0)
            .mapColor(MapColor.METAL)
            .noOcclusion()
            .randomTicks()
            .requiresCorrectToolForDrops()
            .sound(EnderscapeSoundTypes.ALLURING_MAGNIA)
            .strength(1.0F, 6.0F)
    );

    public static final Block ETCHED_ALLURING_MAGNIA = registerWithItem(EnderscapeBlockItemIds.ETCHED_ALLURING_MAGNIA, properties -> new EtchedMagniaBlock(MagniaPolarity.ALLURING, properties), Properties.ofLegacyCopy(ALLURING_MAGNIA).sound(EnderscapeSoundTypes.ETCHED_MAGNIA));
    public static final Block ETCHED_ALLURING_MAGNIA_STAIRS = registerWithItem(EnderscapeBlockItemIds.ETCHED_ALLURING_MAGNIA_STAIRS, properties -> new EtchedMagniaStairsBlock(MagniaPolarity.ALLURING, ETCHED_ALLURING_MAGNIA.defaultBlockState(), properties), Properties.ofFullCopy(ETCHED_ALLURING_MAGNIA));
    public static final Block ETCHED_ALLURING_MAGNIA_SLAB = registerWithItem(EnderscapeBlockItemIds.ETCHED_ALLURING_MAGNIA_SLAB, properties -> new EtchedMagniaSlabBlock(MagniaPolarity.ALLURING, properties), Properties.ofLegacyCopy(ETCHED_ALLURING_MAGNIA));
    public static final Block ETCHED_ALLURING_MAGNIA_WALL = registerWithItem(EnderscapeBlockItemIds.ETCHED_ALLURING_MAGNIA_WALL, properties -> new EtchedMagniaWallBlock(MagniaPolarity.ALLURING, properties), Properties.ofLegacyCopy(ETCHED_ALLURING_MAGNIA));

    public static final Block REPULSIVE_MAGNIA = registerWithItem(EnderscapeBlockItemIds.REPULSIVE_MAGNIA, properties -> new MagniaBlock(MagniaPolarity.REPULSIVE, properties), Properties.of()
            .mapColor(MapColor.COLOR_GRAY)
            .requiresCorrectToolForDrops()
            .strength(1.5F, 6.0F)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.REPULSIVE_MAGNIA)
    );

    public static final Block REPULSIVE_MAGNIA_SPROUT = registerWithItem(EnderscapeBlockItemIds.REPULSIVE_MAGNIA_SPROUT, properties -> new MagniaSproutBlock(MagniaPolarity.REPULSIVE, properties), Properties.of()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .lightLevel(state -> state.getValue(StateProperties.POWERED) ? 12 : 0)
            .mapColor(MapColor.COLOR_GRAY)
            .noOcclusion()
            .randomTicks()
            .requiresCorrectToolForDrops()
            .sound(EnderscapeSoundTypes.REPULSIVE_MAGNIA)
            .strength(1.0F, 6.0F)
    );

    public static final Block ETCHED_REPULSIVE_MAGNIA = registerWithItem(EnderscapeBlockItemIds.ETCHED_REPULSIVE_MAGNIA, properties -> new EtchedMagniaBlock(MagniaPolarity.REPULSIVE, properties), Properties.ofLegacyCopy(REPULSIVE_MAGNIA).sound(EnderscapeSoundTypes.ETCHED_MAGNIA));
    public static final Block ETCHED_REPULSIVE_MAGNIA_STAIRS = registerWithItem(EnderscapeBlockItemIds.ETCHED_REPULSIVE_MAGNIA_STAIRS, properties -> new EtchedMagniaStairsBlock(MagniaPolarity.REPULSIVE, ETCHED_REPULSIVE_MAGNIA.defaultBlockState(), properties), Properties.ofFullCopy(ETCHED_REPULSIVE_MAGNIA));
    public static final Block ETCHED_REPULSIVE_MAGNIA_SLAB = registerWithItem(EnderscapeBlockItemIds.ETCHED_REPULSIVE_MAGNIA_SLAB, properties -> new EtchedMagniaSlabBlock(MagniaPolarity.REPULSIVE, properties), Properties.ofLegacyCopy(ETCHED_REPULSIVE_MAGNIA));
    public static final Block ETCHED_REPULSIVE_MAGNIA_WALL = registerWithItem(EnderscapeBlockItemIds.ETCHED_REPULSIVE_MAGNIA_WALL, properties -> new EtchedMagniaWallBlock(MagniaPolarity.REPULSIVE, properties), Properties.ofLegacyCopy(ETCHED_REPULSIVE_MAGNIA));

    public static final Block BLISTERED_MAGNIA = registerWithItem(EnderscapeBlockItemIds.BLISTERED_MAGNIA, BlisteredMagniaBlock::new, Properties.of()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .lightLevel(BlisteredMagniaBlock.lightLevel())
            .mapColor(BlisteredMagniaBlock::getMapColor)
            .requiresCorrectToolForDrops()
            .sound(EnderscapeSoundTypes.BLISTERED_MAGNIA)
            .strength(4.5F, 6.0F)
    );

    public static final Block POLARIZED_MAGNIA = registerWithItem(EnderscapeBlockItemIds.POLARIZED_MAGNIA, PolarizedMagniaBlock::new, Properties.of()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .isRedstoneConductor(Blocks::never)
            .lightLevel(PolarizedMagniaBlock.lightLevel())
            .mapColor(PolarizedMagniaBlock::getMapColor)
            .requiresCorrectToolForDrops()
            .sound(EnderscapeSoundTypes.POLARIZED_MAGNIA)
            .strength(4.5F, 6.0F)
    );

    public static final Block MAGNIA_RADIO = registerWithItem(EnderscapeBlockItemIds.MAGNIA_RADIO, MagniaRadioBlock::new, Properties.of()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .lightLevel(state -> state.getValue(StateProperties.ENABLED) ? 4 : 0)
            .mapColor(MapColor.METAL)
            .sound(EnderscapeSoundTypes.MAGNIA_RADIO)
            .strength(1.5F)
    );

    public static final Block SHADOLINE_ORE = registerWithItem(EnderscapeBlockItemIds.SHADOLINE_ORE, properties -> new DropExperienceBlock(ConstantInt.of(0), properties), Properties.ofFullCopy(END_STONE).sound(EnderscapeSoundTypes.SHADOLINE_ORE));
    public static final Block MIRESTONE_SHADOLINE_ORE = registerWithItem(EnderscapeBlockItemIds.MIRESTONE_SHADOLINE_ORE, properties -> new DropExperienceBlock(ConstantInt.of(0), properties), Properties.ofFullCopy(MIRESTONE).sound(EnderscapeSoundTypes.MIRESTONE_SHADOLINE_ORE));

    public static final Block RAW_SHADOLINE_BLOCK = registerWithItem(EnderscapeBlockItemIds.RAW_SHADOLINE_BLOCK, Block::new, Properties.ofFullCopy(RAW_IRON_BLOCK)
            .mapColor(MapColor.COLOR_LIGHT_GRAY)
            .strength(3)
            .sound(EnderscapeSoundTypes.SHADOLINE_ORE)
            .instrument(EnderscapeNoteBlockInstruments.SYNTH_BASS)
    );

    public static final Block SHADOLINE_BLOCK = registerWithItem(EnderscapeBlockItemIds.SHADOLINE_BLOCK, Block::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_GREEN)
            .requiresCorrectToolForDrops()
            .strength(3, 9)
            .sound(EnderscapeSoundTypes.SHADOLINE)
            .instrument(EnderscapeNoteBlockInstruments.SYNTH_BASS)
    );

    public static final Block SHADOLINE_BLOCK_STAIRS = registerStair(EnderscapeBlockItemIds.SHADOLINE_BLOCK_STAIRS, SHADOLINE_BLOCK);
    public static final Block SHADOLINE_BLOCK_SLAB = registerWithItem(EnderscapeBlockItemIds.SHADOLINE_BLOCK_SLAB, SlabBlock::new, Properties.ofLegacyCopy(SHADOLINE_BLOCK));
    public static final Block SHADOLINE_BLOCK_WALL = registerWithItem(EnderscapeBlockItemIds.SHADOLINE_BLOCK_WALL, WallBlock::new, Properties.ofLegacyCopy(SHADOLINE_BLOCK));

    public static final Block CUT_SHADOLINE = registerWithItem(EnderscapeBlockItemIds.CUT_SHADOLINE, Block::new, Properties.ofLegacyCopy(SHADOLINE_BLOCK).sound(EnderscapeSoundTypes.SHADOLINE));
    public static final Block CUT_SHADOLINE_STAIRS = registerStair(EnderscapeBlockItemIds.CUT_SHADOLINE_STAIRS, CUT_SHADOLINE);
    public static final Block CUT_SHADOLINE_SLAB = registerWithItem(EnderscapeBlockItemIds.CUT_SHADOLINE_SLAB, SlabBlock::new, Properties.ofLegacyCopy(CUT_SHADOLINE));
    public static final Block CUT_SHADOLINE_WALL = registerWithItem(EnderscapeBlockItemIds.CUT_SHADOLINE_WALL, WallBlock::new, Properties.ofLegacyCopy(CUT_SHADOLINE));

    public static final Block CHISELED_SHADOLINE = registerWithItem(EnderscapeBlockItemIds.CHISELED_SHADOLINE, Block::new, Properties.ofLegacyCopy(CUT_SHADOLINE));
    public static final Block SHADOLINE_PILLAR = registerWithItem(EnderscapeBlockItemIds.SHADOLINE_PILLAR, RotatedPillarBlock::new, Properties.ofLegacyCopy(SHADOLINE_BLOCK));

    public static final Block SHADOLINE_BARS = registerWithItem(
            EnderscapeBlockItemIds.SHADOLINE_BARS,
            IronBarsBlock::new,
            Properties.of().requiresCorrectToolForDrops().strength(6.0F, 9.0F).sound(EnderscapeSoundTypes.SHADOLINE).noOcclusion()
    );

    public static final Block SHADOLINE_CHAIN = registerWithItem(
            EnderscapeBlockItemIds.SHADOLINE_CHAIN,
            ChainBlock::new,
            Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.CHAIN).noOcclusion()
    );

    public static final Block VOID_LACHRYMA = register(EnderscapeBlockIds.VOID_LACHRYMA, properties -> new LiquidBlock(EnderscapeFluids.VOID_LACHRYMA, properties), Properties.of()
            .mapColor(MapColor.COLOR_BLACK)
            .replaceable()
            .noCollision()
            .lightLevel((state) -> 8)
            .strength(100.0F)
            .pushReaction(PushReaction.DESTROY)
            .noLootTable()
            .liquid()
            .emissiveRendering(EnderscapeBlocks::always)
            .sound(SoundType.EMPTY)
    );

    public static final Block VOID_LACHRYMA_CAULDRON = register(EnderscapeBlockIds.VOID_LACHRYMA_CAULDRON, VoidLachrymaCauldron::new, Properties.ofLegacyCopy(CAULDRON).lightLevel(statex -> 8));

    public static final Block VOID_SHALE = registerWithItem(EnderscapeBlockItemIds.VOID_SHALE, VoidShaleBlock::new, Properties.of()
            .mapColor(MapColor.COLOR_BLACK)
            .strength(0.5F)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.VOID_SHALE)
    );

    public static final Block VOID_FIRE = register(EnderscapeBlockIds.VOID_FIRE, VoidFireBlock::new, Properties.of()
            .mapColor(MapColor.COLOR_PINK)
            .replaceable()
            .noCollision()
            .instabreak()
            .emissiveRendering(EnderscapeBlocks::always)
            .lightLevel(state -> 8)
            .sound(SoundType.WOOL)
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Block VOID_TORCH = register(EnderscapeBlockIds.VOID_TORCH, VoidTorchBlock::new, Properties.of()
            .noCollision()
            .instabreak()
            .lightLevel(state -> 8)
            .sound(SoundType.WOOD)
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Block VOID_WALL_TORCH = register(EnderscapeBlockIds.VOID_WALL_TORCH, VoidWallTorchBlock::new, wallVariant(VOID_TORCH, true)
            .noCollision()
            .instabreak()
            .lightLevel(state -> 8)
            .sound(SoundType.WOOD)
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Block VOID_LANTERN = registerWithItem(EnderscapeBlockItemIds.VOID_LANTERN, LanternBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_GREEN)
            .forceSolidOn()
            .strength(3.5F)
            .sound(SoundType.LANTERN)
            .lightLevel(state -> 12)
            .noOcclusion()
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Block VOID_CAMPFIRE = registerWithItem(EnderscapeBlockItemIds.VOID_CAMPFIRE, VoidCampfireBlock::new, Properties.of()
            .mapColor(MapColor.PODZOL)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F)
            .sound(SoundType.WOOD)
            .lightLevel(litBlockEmission(8))
            .noOcclusion()
            .ignitedByLava()
    );

    public static final Block NEBULITE_ORE = registerWithItem(EnderscapeBlockItemIds.NEBULITE_ORE, NebuliteOreBlock::new, Properties.ofFullCopy(END_STONE).sound(EnderscapeSoundTypes.NEBULITE_ORE).randomTicks());
    public static final Block MIRESTONE_NEBULITE_ORE = registerWithItem(EnderscapeBlockItemIds.MIRESTONE_NEBULITE_ORE, NebuliteOreBlock::new, Properties.ofFullCopy(MIRESTONE).sound(EnderscapeSoundTypes.MIRESTONE_NEBULITE_ORE).randomTicks());

    public static final Block NEBULITE_BLOCK = registerWithItem(EnderscapeBlockItemIds.NEBULITE_BLOCK, NebuliteBlock::new, Properties.of()
            .instrument(EnderscapeNoteBlockInstruments.SYNTH_BELL)
            .mapColor(MapColor.COLOR_MAGENTA)
            .requiresCorrectToolForDrops()
            .sound(EnderscapeSoundTypes.NEBULITE_BLOCK)
            .strength(2, 6)
    );

    public static final Block DRY_END_GROWTH = registerWithItem(EnderscapeBlockItemIds.DRY_END_GROWTH, DryEndGrowthBlock::new, Properties.of()
            .instabreak()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .mapColor(MapColor.SAND)
            .noCollision()
            .noOcclusion()
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .replaceable()
            .sound(EnderscapeSoundTypes.DRY_END_GROWTH)
    );

    public static final Block CHORUS_SPROUTS = registerWithItem(EnderscapeBlockItemIds.CHORUS_SPROUTS, ChorusSproutsBlock::new, Properties.of()
            .mapColor(MapColor.COLOR_PURPLE)
            .noCollision()
            .instabreak()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.CHORUS_SPROUTS)
            .noOcclusion()
            .offsetType(BlockBehaviour.OffsetType.XZ)
    );

    public static final Block PURPUR_WALL = registerWithItem(EnderscapeBlockItemIds.PURPUR_WALL, WallBlock::new, Properties.ofLegacyCopy(PURPUR_BLOCK));
    public static final Block CHISELED_PURPUR = registerWithItem(EnderscapeBlockItemIds.CHISELED_PURPUR, Block::new, Properties.ofLegacyCopy(PURPUR_BLOCK));

    public static final Block DUSK_PURPUR_BLOCK = registerWithItem(EnderscapeBlockItemIds.DUSK_PURPUR_BLOCK, Block::new, Properties.ofLegacyCopy(PURPUR_BLOCK).mapColor(MapColor.COLOR_BLACK).sound(EnderscapeSoundTypes.DUSK_PURPUR));
    public static final Block DUSK_PURPUR_STAIRS = registerStair(EnderscapeBlockItemIds.DUSK_PURPUR_STAIRS, DUSK_PURPUR_BLOCK);
    public static final Block DUSK_PURPUR_SLAB = registerWithItem(EnderscapeBlockItemIds.DUSK_PURPUR_SLAB, SlabBlock::new, Properties.ofFullCopy(DUSK_PURPUR_BLOCK).mapColor(MapColor.COLOR_BLACK));
    public static final Block DUSK_PURPUR_WALL = registerWithItem(EnderscapeBlockItemIds.DUSK_PURPUR_WALL, WallBlock::new, Properties.ofFullCopy(DUSK_PURPUR_BLOCK).mapColor(MapColor.COLOR_BLACK));
    public static final Block CHISELED_DUSK_PURPUR = registerWithItem(EnderscapeBlockItemIds.CHISELED_DUSK_PURPUR, Block::new, Properties.ofFullCopy(DUSK_PURPUR_BLOCK).mapColor(MapColor.COLOR_BLACK));
    public static final Block DUSK_PURPUR_PILLAR = registerWithItem(EnderscapeBlockItemIds.DUSK_PURPUR_PILLAR, RotatedPillarBlock::new, Properties.ofFullCopy(DUSK_PURPUR_BLOCK).mapColor(MapColor.COLOR_BLACK));

    public static final Block PURPUR_TILES = registerWithItem(EnderscapeBlockItemIds.PURPUR_TILES, Block::new, Properties.ofFullCopy(PURPUR_BLOCK));
    public static final Block PURPUR_TILE_STAIRS = registerStair(EnderscapeBlockItemIds.PURPUR_TILE_STAIRS, PURPUR_TILES);
    public static final Block PURPUR_TILE_SLAB = registerWithItem(EnderscapeBlockItemIds.PURPUR_TILE_SLAB, SlabBlock::new, Properties.ofFullCopy(PURPUR_SLAB));

    public static final Block CHORUS_CAKE_ROLL = register(EnderscapeBlockIds.CHORUS_CAKE_ROLL, ChorusCakeRollBlock::new, Properties.of().forceSolidOn().strength(0.5F).sound(EnderscapeSoundTypes.CHORUS_CAKE_ROLL).pushReaction(PushReaction.DESTROY));

    public static final Block END_LAMP = registerWithItem(EnderscapeBlockItemIds.END_LAMP, Block::new, Properties.of()
            .instrument(NoteBlockInstrument.PLING)
            .isRedstoneConductor(Blocks::never)
            .lightLevel(state -> 15)
            .mapColor(MapColor.QUARTZ)
            .sound(EnderscapeSoundTypes.END_LAMP)
            .strength(0.6F)
    );

    public static final Block VEILED_END_STONE = registerWithItem(EnderscapeBlockItemIds.VEILED_END_STONE, VeiledEndStoneBlock::new, Properties.of()
            .mapColor(MapColor.CLAY)
            .requiresCorrectToolForDrops()
            .strength(3, 9)
            .sound(EnderscapeSoundTypes.VEILED_END_STONE)
            .randomTicks()
            .isValidSpawn(Blocks::always)
    );

    public static final Block WISP_SPROUTS = registerWithItem(EnderscapeBlockItemIds.WISP_SPROUTS, WispSproutsBlock::new, Properties.of()
            .instabreak()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .mapColor(MapColor.CLAY)
            .noCollision()
            .noOcclusion()
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .replaceable()
            .sound(EnderscapeSoundTypes.WISP_GROWTH)
    );

    public static final Block WISP_GROWTH = registerWithItem(EnderscapeBlockItemIds.WISP_GROWTH, WispGrowthBlock::new, Properties.of()
            .instabreak()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .mapColor(MapColor.CLAY)
            .noCollision()
            .noOcclusion()
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .replaceable()
            .sound(EnderscapeSoundTypes.WISP_GROWTH)
    );

    public static final Block WISP_FLOWER = registerWithItem(EnderscapeBlockItemIds.WISP_FLOWER, WispFlowerBlock::new, Properties.of()
            .mapColor(MapColor.CLAY)
            .noCollision()
            .instabreak()
            .sound(EnderscapeSoundTypes.WISP_FLOWER)
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .ignitedByLava()
            .lightLevel(state -> 7)
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Block STRIPPED_VEILED_LOG = registerWithItem(EnderscapeBlockItemIds.STRIPPED_VEILED_LOG, RotatedPillarBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_BLUE)
            .strength(2)
            .sound(EnderscapeSoundTypes.VEILED_LOG)
    );

    public static final Block VEILED_LOG = registerWithItem(EnderscapeBlockItemIds.VEILED_LOG, RotatedPillarBlock::new, Properties.ofFullCopy(STRIPPED_VEILED_LOG).mapColor(EnderscapeBlocks.axis(MapColor.TERRACOTTA_LIGHT_BLUE, MapColor.TERRACOTTA_BLUE)));
    public static final Block STRIPPED_VEILED_WOOD = registerWithItem(EnderscapeBlockItemIds.STRIPPED_VEILED_WOOD, RotatedPillarBlock::new, Properties.ofFullCopy(STRIPPED_VEILED_LOG));
    public static final Block VEILED_WOOD = registerWithItem(EnderscapeBlockItemIds.VEILED_WOOD, RotatedPillarBlock::new, Properties.ofFullCopy(STRIPPED_VEILED_WOOD).mapColor(MapColor.TERRACOTTA_LIGHT_BLUE));

    public static final Block VEILED_LEAVES = registerWithItem(EnderscapeBlockItemIds.VEILED_LEAVES, VeiledLeavesBlock::new, Blocks.leavesProperties(EnderscapeSoundTypes.VEILED_LEAVES).mapColor(MapColor.CLAY));

    public static final Block VEILED_LEAF_PILE = registerWithItem(EnderscapeBlockItemIds.VEILED_LEAF_PILE,
            VeiledLeafPileBlock::new,
            Properties.of()
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

    public static final Block VEILED_VINES = registerWithItem(EnderscapeBlockItemIds.VEILED_VINES, VeiledVinesBlock::new, Properties.of()
            .mapColor(MapColor.CLAY)
            .noCollision()
            .noOcclusion()
            .pushReaction(PushReaction.DESTROY)
            .sound(EnderscapeSoundTypes.VEILED_LEAVES)
            .strength(0.2F)
    );

    public static final Block VEILED_SAPLING = registerWithItem(EnderscapeBlockItemIds.VEILED_SAPLING, properties -> new VeiledSaplingBlock(EnderscapeConfiguredFeatures.VEILED_TREE_FROM_SAPLING, properties), Properties.ofFullCopy(OAK_SAPLING)
            .mapColor(MapColor.CLAY)
            .sound(EnderscapeSoundTypes.CHORUS_SPROUTS)
    );

    public static final Block VEILED_PLANKS = registerWithItem(EnderscapeBlockItemIds.VEILED_PLANKS, Block::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_BLUE)
            .strength(2, 3)
            .sound(EnderscapeSoundTypes.VEILED_PLANKS)
    );

    public static final Block VEILED_STAIRS = registerStair(EnderscapeBlockItemIds.VEILED_STAIRS, VEILED_PLANKS);
    public static final Block VEILED_SLAB = registerWithItem(EnderscapeBlockItemIds.VEILED_SLAB, SlabBlock::new, Properties.ofLegacyCopy(VEILED_PLANKS));
    public static final Block VEILED_PRESSURE_PLATE = registerWithItem(EnderscapeBlockItemIds.VEILED_PRESSURE_PLATE, properties -> new PressurePlateBlock(VEILED_BLOCK_SET, properties), Properties.of()
            .mapColor(VEILED_PLANKS.defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollision()
            .strength(0.5F)
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Block VEILED_FENCE = registerWithItem(EnderscapeBlockItemIds.VEILED_FENCE, FenceBlock::new, Properties.ofLegacyCopy(VEILED_PLANKS));
    public static final Block VEILED_DOOR = registerWithItem(EnderscapeBlockItemIds.VEILED_DOOR, properties -> new DoorBlock(VEILED_BLOCK_SET, properties), Properties.of()
            .mapColor(VEILED_PLANKS.defaultMapColor())
            .instrument(NoteBlockInstrument.BASS)
            .strength(3)
            .noOcclusion()
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Block VEILED_TRAPDOOR = registerWithItem(EnderscapeBlockItemIds.VEILED_TRAPDOOR, properties -> new TrapDoorBlock(VEILED_BLOCK_SET, properties), Properties.of()
            .mapColor(VEILED_PLANKS.defaultMapColor())
            .instrument(NoteBlockInstrument.BASS)
            .strength(3)
            .noOcclusion()
            .isValidSpawn(Blocks::never)
            .ignitedByLava()
    );

    public static final Block VEILED_FENCE_GATE = registerWithItem(EnderscapeBlockItemIds.VEILED_FENCE_GATE, properties -> new FenceGateBlock(VEILED_WOOD_TYPE, properties), Properties.of()
            .mapColor(VEILED_PLANKS.defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .strength(2, 3)
            .ignitedByLava()
    );

    public static final Block VEILED_BUTTON = registerWithItem(EnderscapeBlockItemIds.VEILED_BUTTON, properties -> new ButtonBlock(VEILED_BLOCK_SET, 30, properties), buttonProperties());

    public static final Block VEILED_SHELF = register(EnderscapeBlockItemIds.VEILED_SHELF.block(), ShelfBlock::new, Properties.of().mapColor(VEILED_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).sound(SoundType.SHELF).ignitedByLava().strength(2.0F, 3.0F));
    public static final Block VEILED_SIGN = register(EnderscapeBlockItemIds.VEILED_SIGN.block(), properties -> new StandingSignBlock(VEILED_WOOD_TYPE, properties), Properties.of().mapColor(VEILED_PLANKS.defaultMapColor()).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollision().strength(1).ignitedByLava());
    public static final Block VEILED_WALL_SIGN = register(EnderscapeBlockIds.VEILED_WALL_SIGN, properties -> new WallSignBlock(VEILED_WOOD_TYPE, properties), wallVariant(VEILED_SIGN, true).mapColor(VEILED_PLANKS.defaultMapColor()).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollision().strength(1).ignitedByLava());

    public static final Block VEILED_HANGING_SIGN = register(EnderscapeBlockItemIds.VEILED_HANGING_SIGN.block(), properties -> new CeilingHangingSignBlock(VEILED_WOOD_TYPE, properties), Properties.of()
            .mapColor(VEILED_LOG.defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollision()
            .strength(1)
            .ignitedByLava()
    );

    public static final Block VEILED_WALL_HANGING_SIGN = register(EnderscapeBlockIds.VEILED_WALL_HANGING_SIGN, properties -> new WallHangingSignBlock(VEILED_WOOD_TYPE, properties), wallVariant(VEILED_HANGING_SIGN, true)
            .mapColor(VEILED_LOG.defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollision()
            .strength(1)
            .ignitedByLava()
    );

    public static final Block CELESTIAL_PATH = registerWithItem(EnderscapeBlockItemIds.CELESTIAL_PATH, CelestialPathBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_WHITE)
            .requiresCorrectToolForDrops()
            .strength(3, 9)
            .sound(EnderscapeSoundTypes.CELESTIAL_OVERGROWTH)
            .isViewBlocking(EnderscapeBlocks::always)
            .isSuffocating(EnderscapeBlocks::always)
    );

    public static final Block CELESTIAL_OVERGROWTH = registerWithItem(EnderscapeBlockItemIds.CELESTIAL_OVERGROWTH, CelestialOvergrowthBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_YELLOW)
            .requiresCorrectToolForDrops()
            .strength(3, 9)
            .sound(EnderscapeSoundTypes.CELESTIAL_OVERGROWTH)
            .randomTicks()
            .isValidSpawn(Blocks::always)
    );

    public static final Block CELESTIAL_GROWTH = registerWithItem(EnderscapeBlockItemIds.CELESTIAL_GROWTH, CelestialGrowthBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_YELLOW)
            .noCollision()
            .instabreak()
            .sound(EnderscapeSoundTypes.CELESTIAL_GROWTH)
            .noOcclusion()
    );

    public static final Block BULB_FLOWER = registerWithItem(EnderscapeBlockItemIds.BULB_FLOWER, BulbFlowerBlock::new, Properties.of()
            .mapColor(MapColor.COLOR_YELLOW)
            .noCollision()
            .instabreak()
            .sound(EnderscapeSoundTypes.BULB_FLOWER)
            .noOcclusion()
            .lightLevel(BulbFlowerBlock::lightLevel)
            .randomTicks()
    );

    public static final Block BULB_LANTERN = registerWithItem(EnderscapeBlockItemIds.BULB_LANTERN, BulbLanternBlock::new, Properties.ofLegacyCopy(LANTERN)
            .sound(EnderscapeSoundTypes.BULB_LANTERN)
            .randomTicks()
    );

    public static final Block PURUBERRY_VINE = register(EnderscapeBlockIds.PURUBERRY_VINE, PuruberryVine::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_YELLOW)
            .randomTicks()
            .noCollision()
            .sound(EnderscapeSoundTypes.PURUBERRY_VINE)
            .strength(0.2F)
    );

    public static final Block PURUBERRY_FLOWER = registerWithItem(EnderscapeBlockItemIds.PURUBERRY_FLOWER, PuruberryFlowerBlock::new, Properties.of()
            .mapColor(MapColor.WARPED_WART_BLOCK)
            .randomTicks()
            .strength(0.3F)
            .pushReaction(PushReaction.DESTROY)
            .noCollision()
            .sound(EnderscapeSoundTypes.PURUBERRY_FLOWER)
            .noOcclusion()
    );

    public static final Block UNRIPE_PURUBERRY_BLOCK = registerWithItem(EnderscapeBlockItemIds.UNRIPE_PURUBERRY_BLOCK, UnripePuruberryBlock::new, Properties.of()
            .mapColor(MapColor.WARPED_WART_BLOCK)
            .randomTicks()
            .strength(0.3F)
            .pushReaction(PushReaction.DESTROY)
            .sound(EnderscapeSoundTypes.PURUBERRY_BLOCK)
            .noOcclusion()
    );

    public static final Block RIPE_PURUBERRY_BLOCK = registerWithItem(EnderscapeBlockItemIds.RIPE_PURUBERRY_BLOCK, RipePuruberryBlock::new, Properties.of()
            .mapColor(MapColor.WARPED_WART_BLOCK)
            .strength(0.3F)
            .pushReaction(PushReaction.DESTROY)
            .sound(EnderscapeSoundTypes.PURUBERRY_BLOCK)
    );

    public static final Block CELESTIAL_CHANTERELLE = registerWithItem(EnderscapeBlockItemIds.CELESTIAL_CHANTERELLE, properties -> new CelestialChanterelleBlock(EnderscapeConfiguredFeatures.LARGE_CELESTIAL_CHANTERELLE, properties), Properties.of()
            .mapColor(MapColor.TERRACOTTA_YELLOW)
            .noCollision()
            .instabreak()
            .randomTicks()
            .sound(EnderscapeSoundTypes.CELESTIAL_CHANTERELLE)
    );

    public static final Block STRIPPED_CELESTIAL_STEM = registerWithItem(EnderscapeBlockItemIds.STRIPPED_CELESTIAL_STEM, RotatedPillarBlock::new, Properties.of()
            .mapColor(MapColor.COLOR_ORANGE)
            .strength(2)
            .sound(EnderscapeSoundTypes.CELESTIAL_STEM)
    );

    public static final Block CELESTIAL_STEM = registerWithItem(EnderscapeBlockItemIds.CELESTIAL_STEM, RotatedPillarBlock::new, Properties.ofFullCopy(STRIPPED_CELESTIAL_STEM).mapColor(EnderscapeBlocks.axis(MapColor.TERRACOTTA_WHITE, MapColor.COLOR_ORANGE)));

    public static final Block STRIPPED_CELESTIAL_HYPHAE = registerWithItem(EnderscapeBlockItemIds.STRIPPED_CELESTIAL_HYPHAE, RotatedPillarBlock::new, Properties.ofFullCopy(STRIPPED_CELESTIAL_STEM));

    public static final Block CELESTIAL_HYPHAE = registerWithItem(EnderscapeBlockItemIds.CELESTIAL_HYPHAE, RotatedPillarBlock::new, Properties.ofFullCopy(STRIPPED_CELESTIAL_HYPHAE).mapColor(MapColor.TERRACOTTA_WHITE));

    public static final Block CELESTIAL_CAP = registerWithItem(EnderscapeBlockItemIds.CELESTIAL_CAP, ChanterelleCapBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_YELLOW)
            .strength(1)
            .sound(EnderscapeSoundTypes.CELESTIAL_CAP)
            .isValidSpawn(Blocks::never)
    );

    public static final Block CELESTIAL_BRICKS = registerWithItem(EnderscapeBlockItemIds.CELESTIAL_BRICKS, Block::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_YELLOW)
            .strength(1.5F, 6)
            .sound(EnderscapeSoundTypes.CELESTIAL_BRICKS)
    );

    public static final Block CELESTIAL_BRICK_STAIRS = registerStair(EnderscapeBlockItemIds.CELESTIAL_BRICK_STAIRS, CELESTIAL_BRICKS);
    public static final Block CELESTIAL_BRICK_SLAB = registerWithItem(EnderscapeBlockItemIds.CELESTIAL_BRICK_SLAB, SlabBlock::new, Properties.ofLegacyCopy(CELESTIAL_BRICKS));
    public static final Block CELESTIAL_BRICK_WALL = registerWithItem(EnderscapeBlockItemIds.CELESTIAL_BRICK_WALL, WallBlock::new, Properties.ofLegacyCopy(CELESTIAL_BRICKS));

    public static final Block CELESTIAL_PLANKS = registerWithItem(EnderscapeBlockItemIds.CELESTIAL_PLANKS, Block::new, Properties.of()
            .mapColor(MapColor.COLOR_ORANGE)
            .strength(2, 3)
            .sound(EnderscapeSoundTypes.CELESTIAL_PLANKS)
    );

    public static final Block CELESTIAL_STAIRS = registerStair(EnderscapeBlockItemIds.CELESTIAL_STAIRS, CELESTIAL_PLANKS);
    public static final Block CELESTIAL_SLAB = registerWithItem(EnderscapeBlockItemIds.CELESTIAL_SLAB, SlabBlock::new, Properties.ofLegacyCopy(CELESTIAL_PLANKS));
    public static final Block CELESTIAL_PRESSURE_PLATE = registerWithItem(EnderscapeBlockItemIds.CELESTIAL_PRESSURE_PLATE, properties -> new PressurePlateBlock(CELESTIAL_BLOCK_SET, properties), Properties.of()
            .mapColor(CELESTIAL_PLANKS.defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollision()
            .strength(0.5F)
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Block CELESTIAL_FENCE = registerWithItem(EnderscapeBlockItemIds.CELESTIAL_FENCE, FenceBlock::new, Properties.ofLegacyCopy(CELESTIAL_PLANKS));
    public static final Block CELESTIAL_DOOR = registerWithItem(EnderscapeBlockItemIds.CELESTIAL_DOOR, properties -> new DoorBlock(CELESTIAL_BLOCK_SET, properties), Properties.of()
            .mapColor(CELESTIAL_PLANKS.defaultMapColor())
            .instrument(NoteBlockInstrument.BASS)
            .strength(3)
            .noOcclusion()
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Block CELESTIAL_TRAPDOOR = registerWithItem(EnderscapeBlockItemIds.CELESTIAL_TRAPDOOR, properties -> new TrapDoorBlock(CELESTIAL_BLOCK_SET, properties), Properties.of()
            .mapColor(CELESTIAL_PLANKS.defaultMapColor())
            .instrument(NoteBlockInstrument.BASS)
            .strength(3)
            .noOcclusion()
            .isValidSpawn(Blocks::never)
            .ignitedByLava()
    );

    public static final Block CELESTIAL_FENCE_GATE = registerWithItem(EnderscapeBlockItemIds.CELESTIAL_FENCE_GATE, properties -> new FenceGateBlock(CELESTIAL_WOOD_TYPE, properties), Properties.of()
            .mapColor(CELESTIAL_PLANKS.defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .strength(2, 3)
            .ignitedByLava()
    );

    public static final Block CELESTIAL_BUTTON = registerWithItem(EnderscapeBlockItemIds.CELESTIAL_BUTTON, properties -> new ButtonBlock(CELESTIAL_BLOCK_SET, 30, properties), buttonProperties());
    public static final Block CELESTIAL_SHELF = register(EnderscapeBlockItemIds.CELESTIAL_SHELF.block(), ShelfBlock::new, Properties.of().mapColor(CELESTIAL_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).sound(SoundType.SHELF).ignitedByLava().strength(2.0F, 3.0F));
    public static final Block CELESTIAL_SIGN = register(EnderscapeBlockItemIds.CELESTIAL_SIGN.block(), properties -> new StandingSignBlock(CELESTIAL_WOOD_TYPE, properties), Properties.of().mapColor(CELESTIAL_PLANKS.defaultMapColor()).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollision().strength(1).ignitedByLava());
    public static final Block CELESTIAL_WALL_SIGN = register(EnderscapeBlockIds.CELESTIAL_WALL_SIGN, properties -> new WallSignBlock(CELESTIAL_WOOD_TYPE, properties), wallVariant(CELESTIAL_SIGN, true).mapColor(CELESTIAL_PLANKS.defaultMapColor()).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollision().strength(1).ignitedByLava());

    public static final Block CELESTIAL_HANGING_SIGN = register(EnderscapeBlockItemIds.CELESTIAL_HANGING_SIGN.block(), properties -> new CeilingHangingSignBlock(CELESTIAL_WOOD_TYPE, properties), Properties.of()
            .mapColor(CELESTIAL_STEM.defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollision()
            .strength(1)
            .ignitedByLava()
    );

    public static final Block CELESTIAL_WALL_HANGING_SIGN = register(EnderscapeBlockIds.CELESTIAL_WALL_HANGING_SIGN, properties -> new WallHangingSignBlock(CELESTIAL_WOOD_TYPE, properties), wallVariant(CELESTIAL_HANGING_SIGN, true)
            .mapColor(CELESTIAL_STEM.defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollision()
            .strength(1)
            .ignitedByLava()
    );

    public static final Block CORRUPT_PATH = registerWithItem(EnderscapeBlockItemIds.CORRUPT_PATH, CorruptPathBlock::new, Properties.of()
            .mapColor(MapColor.COLOR_PURPLE)
            .requiresCorrectToolForDrops()
            .strength(3, 9)
            .sound(EnderscapeSoundTypes.CORRUPT_OVERGROWTH)
            .isSuffocating(EnderscapeBlocks::always)
    );

    public static final Block CORRUPT_OVERGROWTH = registerWithItem(EnderscapeBlockItemIds.CORRUPT_OVERGROWTH, CorruptOvergrowthBlock::new, Properties.of()
            .mapColor(MapColor.COLOR_PURPLE)
            .requiresCorrectToolForDrops()
            .strength(6, 9)
            .sound(EnderscapeSoundTypes.CORRUPT_OVERGROWTH)
            .isValidSpawn(Blocks::always)
    );

    public static final Block CORRUPT_GROWTH = registerWithItem(EnderscapeBlockItemIds.CORRUPT_GROWTH, CorruptGrowthBlock::new, Properties.of()
            .mapColor(MapColor.COLOR_PURPLE)
            .noCollision()
            .instabreak()
            .sound(EnderscapeSoundTypes.CORRUPT_GROWTH)
            .noOcclusion()
    );

    public static final Block BLINKLIGHT_VINES_BODY = register(EnderscapeBlockIds.BLINKLIGHT_VINES_BODY, BlinklightVinesBodyBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)
            .noCollision()
            .sound(EnderscapeSoundTypes.BLINKLIGHT_VINES)
            .strength(0.8F)
            .randomTicks()
            .lightLevel(BlinklightVines::lightLevel)
    );

    public static final Block BLINKLIGHT_VINES_HEAD = register(EnderscapeBlockIds.BLINKLIGHT_VINES_HEAD, BlinklightVinesHeadBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)
            .noCollision()
            .sound(EnderscapeSoundTypes.BLINKLIGHT_VINES)
            .strength(0.8F)
            .randomTicks()
            .lightLevel(BlinklightVines::lightLevel)
    );

    public static final Block BLINKLAMP = registerWithItem(EnderscapeBlockItemIds.BLINKLAMP, BlinklampBlock::new, Properties.of()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .isRedstoneConductor(Blocks::never)
            .mapColor(BlinklampBlock::getColor)
            .sound(EnderscapeSoundTypes.BLINKLAMP)
            .strength(1.5F, 6.0F)
    );

    public static final Block MURUBLIGHT_BRACKET = register(EnderscapeBlockIds.MURUBLIGHT_BRACKET, MurublightBracketBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)
            .noCollision()
            .instabreak()
            .sound(EnderscapeSoundTypes.MURUBLIGHT_BRACKET)
            .noOcclusion()
    );

    public static final Block MURUBLIGHT_CHANTERELLE = registerWithItem(EnderscapeBlockItemIds.MURUBLIGHT_CHANTERELLE, MurublightChanterelleBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)
            .noCollision()
            .instabreak()
            .randomTicks()
            .sound(EnderscapeSoundTypes.CELESTIAL_CHANTERELLE)
    );

    public static final Block STRIPPED_MURUBLIGHT_STEM = registerWithItem(EnderscapeBlockItemIds.STRIPPED_MURUBLIGHT_STEM, RotatedPillarBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)
            .strength(2)
            .sound(EnderscapeSoundTypes.MURUBLIGHT_STEM)
    );

    public static final Block MURUBLIGHT_STEM = registerWithItem(EnderscapeBlockItemIds.MURUBLIGHT_STEM, RotatedPillarBlock::new, Properties.ofFullCopy(STRIPPED_MURUBLIGHT_STEM).mapColor(EnderscapeBlocks.axis(MapColor.COLOR_BLACK, MapColor.TERRACOTTA_LIGHT_BLUE)));
    public static final Block STRIPPED_MURUBLIGHT_HYPHAE = registerWithItem(EnderscapeBlockItemIds.STRIPPED_MURUBLIGHT_HYPHAE, RotatedPillarBlock::new, Properties.ofFullCopy(STRIPPED_MURUBLIGHT_STEM));
    public static final Block MURUBLIGHT_HYPHAE = registerWithItem(EnderscapeBlockItemIds.MURUBLIGHT_HYPHAE, RotatedPillarBlock::new, Properties.ofFullCopy(STRIPPED_MURUBLIGHT_HYPHAE).mapColor(MapColor.COLOR_BLACK));

    public static final Block MURUBLIGHT_CAP = registerWithItem(EnderscapeBlockItemIds.MURUBLIGHT_CAP, ChanterelleCapBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_BLUE)
            .strength(1)
            .sound(EnderscapeSoundTypes.MURUBLIGHT_CAP)
            .isValidSpawn(Blocks::never)
    );

    public static final Block MURUBLIGHT_BRICKS = registerWithItem(EnderscapeBlockItemIds.MURUBLIGHT_BRICKS, Block::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_BLUE)
            .strength(1.5F, 6)
            .sound(EnderscapeSoundTypes.MURUBLIGHT_BRICKS)
    );

    public static final Block MURUBLIGHT_BRICK_STAIRS = registerStair(EnderscapeBlockItemIds.MURUBLIGHT_BRICK_STAIRS, MURUBLIGHT_BRICKS);
    public static final Block MURUBLIGHT_BRICK_SLAB = registerWithItem(EnderscapeBlockItemIds.MURUBLIGHT_BRICK_SLAB, SlabBlock::new, Properties.ofLegacyCopy(MURUBLIGHT_BRICKS));
    public static final Block MURUBLIGHT_BRICK_WALL = registerWithItem(EnderscapeBlockItemIds.MURUBLIGHT_BRICK_WALL, WallBlock::new, Properties.ofLegacyCopy(MURUBLIGHT_BRICKS));

    public static final Block MURUBLIGHT_PLANKS = registerWithItem(EnderscapeBlockItemIds.MURUBLIGHT_PLANKS, Block::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)
            .strength(2, 3)
            .sound(EnderscapeSoundTypes.MURUBLIGHT_PLANKS)
    );

    public static final Block MURUBLIGHT_STAIRS = registerStair(EnderscapeBlockItemIds.MURUBLIGHT_STAIRS, MURUBLIGHT_PLANKS);

    public static final Block MURUBLIGHT_SLAB = registerWithItem(EnderscapeBlockItemIds.MURUBLIGHT_SLAB, SlabBlock::new, Properties.ofLegacyCopy(MURUBLIGHT_PLANKS));

    public static final Block MURUBLIGHT_PRESSURE_PLATE = registerWithItem(EnderscapeBlockItemIds.MURUBLIGHT_PRESSURE_PLATE, properties -> new PressurePlateBlock(MURUBLIGHT_BLOCK_SET, properties), Properties.of()
            .mapColor(MURUBLIGHT_PLANKS.defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollision()
            .strength(0.5F)
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Block MURUBLIGHT_FENCE = registerWithItem(EnderscapeBlockItemIds.MURUBLIGHT_FENCE, FenceBlock::new, Properties.ofLegacyCopy(MURUBLIGHT_PLANKS));

    public static final Block MURUBLIGHT_DOOR = registerWithItem(EnderscapeBlockItemIds.MURUBLIGHT_DOOR, properties -> new DoorBlock(MURUBLIGHT_BLOCK_SET, properties), Properties.of()
            .mapColor(MURUBLIGHT_PLANKS.defaultMapColor())
            .instrument(NoteBlockInstrument.BASS)
            .strength(3)
            .noOcclusion()
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Block MURUBLIGHT_TRAPDOOR = registerWithItem(EnderscapeBlockItemIds.MURUBLIGHT_TRAPDOOR, properties -> new TrapDoorBlock(MURUBLIGHT_BLOCK_SET, properties), Properties.of()
            .mapColor(MURUBLIGHT_PLANKS.defaultMapColor())
            .instrument(NoteBlockInstrument.BASS)
            .strength(3)
            .noOcclusion()
            .isValidSpawn(Blocks::never)
            .ignitedByLava()
    );

    public static final Block MURUBLIGHT_FENCE_GATE = registerWithItem(EnderscapeBlockItemIds.MURUBLIGHT_FENCE_GATE, properties -> new FenceGateBlock(MURUBLIGHT_WOOD_TYPE, properties), Properties.of()
            .mapColor(MURUBLIGHT_PLANKS.defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .strength(2, 3)
            .ignitedByLava()
    );

    public static final Block MURUBLIGHT_BUTTON = registerWithItem(EnderscapeBlockItemIds.MURUBLIGHT_BUTTON, properties -> new ButtonBlock(MURUBLIGHT_BLOCK_SET, 30, properties), buttonProperties());
    public static final Block MURUBLIGHT_SHELF = register(EnderscapeBlockItemIds.MURUBLIGHT_SHELF.block(), ShelfBlock::new, Properties.of().mapColor(MURUBLIGHT_PLANKS.defaultMapColor()).instrument(NoteBlockInstrument.BASS).sound(SoundType.SHELF).ignitedByLava().strength(2.0F, 3.0F));
    public static final Block MURUBLIGHT_SIGN = register(EnderscapeBlockItemIds.MURUBLIGHT_SIGN.block(), properties -> new StandingSignBlock(MURUBLIGHT_WOOD_TYPE, properties), Properties.of().mapColor(MURUBLIGHT_PLANKS.defaultMapColor()).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollision().strength(1).ignitedByLava());
    public static final Block MURUBLIGHT_WALL_SIGN = register(EnderscapeBlockIds.MURUBLIGHT_WALL_SIGN, properties -> new WallSignBlock(MURUBLIGHT_WOOD_TYPE, properties), wallVariant(MURUBLIGHT_SIGN, true).mapColor(MURUBLIGHT_PLANKS.defaultMapColor()).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollision().strength(1).ignitedByLava());

    public static final Block MURUBLIGHT_HANGING_SIGN = register(EnderscapeBlockItemIds.MURUBLIGHT_HANGING_SIGN.block(), properties -> new CeilingHangingSignBlock(MURUBLIGHT_WOOD_TYPE, properties), Properties.of()
            .mapColor(MURUBLIGHT_STEM.defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollision()
            .strength(1)
            .ignitedByLava()
    );

    public static final Block MURUBLIGHT_WALL_HANGING_SIGN = register(EnderscapeBlockIds.MURUBLIGHT_WALL_HANGING_SIGN, properties -> new WallHangingSignBlock(MURUBLIGHT_WOOD_TYPE, properties), wallVariant(MURUBLIGHT_HANGING_SIGN, true)
            .mapColor(MURUBLIGHT_STEM.defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollision()
            .strength(1)
            .ignitedByLava()
    );

    public static final Block POTTED_ALLURING_MAGNIA_SPROUT = register(EnderscapeBlockIds.POTTED_ALLURING_MAGNIA_SPROUT, properties -> new FlowerPotBlock(ALLURING_MAGNIA_SPROUT, properties), flowerPotProperties());
    public static final Block POTTED_BLINKLIGHT = register(EnderscapeBlockIds.POTTED_BLINKLIGHT, properties -> new FlowerPotBlock(BLINKLIGHT_VINES_HEAD, properties), flowerPotProperties().lightLevel(state -> 12));
    public static final Block POTTED_BULB_FLOWER = register(EnderscapeBlockIds.POTTED_BULB_FLOWER, properties -> new FlowerPotBlock(BULB_FLOWER, properties), flowerPotProperties().lightLevel(state -> 7));
    public static final Block POTTED_CELESTIAL_CHANTERELLE = register(EnderscapeBlockIds.POTTED_CELESTIAL_CHANTERELLE, properties -> new FlowerPotBlock(CELESTIAL_CHANTERELLE, properties), flowerPotProperties());
    public static final Block POTTED_CELESTIAL_GROWTH = register(EnderscapeBlockIds.POTTED_CELESTIAL_GROWTH, properties -> new FlowerPotBlock(CELESTIAL_GROWTH, properties), flowerPotProperties());
    public static final Block POTTED_CHORUS_SPROUTS = register(EnderscapeBlockIds.POTTED_CHORUS_SPROUTS, properties -> new FlowerPotBlock(CHORUS_SPROUTS, properties), flowerPotProperties());
    public static final Block POTTED_CORRUPT_GROWTH = register(EnderscapeBlockIds.POTTED_CORRUPT_GROWTH, properties -> new FlowerPotBlock(CORRUPT_GROWTH, properties), flowerPotProperties());
    public static final Block POTTED_DRY_END_GROWTH = register(EnderscapeBlockIds.POTTED_DRY_END_GROWTH, properties -> new FlowerPotBlock(DRY_END_GROWTH, properties), flowerPotProperties());
    public static final Block POTTED_MURUBLIGHT_CHANTERELLE = register(EnderscapeBlockIds.POTTED_MURUBLIGHT_CHANTERELLE, properties -> new FlowerPotBlock(MURUBLIGHT_CHANTERELLE, properties), flowerPotProperties());
    public static final Block POTTED_REPULSIVE_MAGNIA_SPROUT = register(EnderscapeBlockIds.POTTED_REPULSIVE_MAGNIA_SPROUT, properties -> new FlowerPotBlock(REPULSIVE_MAGNIA_SPROUT, properties), flowerPotProperties());
    public static final Block POTTED_VEILED_SAPLING = register(EnderscapeBlockIds.POTTED_VEILED_SAPLING, properties -> new FlowerPotBlock(VEILED_SAPLING, properties), flowerPotProperties());
    public static final Block POTTED_WISP_GROWTH = register(EnderscapeBlockIds.POTTED_WISP_GROWTH, properties -> new FlowerPotBlock(WISP_GROWTH, properties), flowerPotProperties());

    private static Block registerStair(BlockItemId blockItemId, Block block) {
        return registerWithItem(blockItemId, properties -> new StairBlock(block.defaultBlockState(), properties), Properties.ofFullCopy(block));
    }

    private static Properties wallVariant(Block block, boolean bl) {
        Properties properties = Properties.of().overrideLootTable(block.getLootTable());
        if (bl) properties = properties.overrideDescription(block.getDescriptionId());

        return properties;
    }

    private static Block registerWithItem(BlockItemId id, Function<Properties, Block> function, Properties properties) {
        ResourceKey<Block> key = id.block();
        Block block = function.apply(properties.setId(key));

        Registry.register(BuiltInRegistries.BLOCK, key, block);

        EnderscapeItems.registerBlock(id.item(), block);

        return block;
    }

    private static Block register(ResourceKey<Block> key, Function<Properties, Block> function, Properties properties) {
        Block block = function.apply(properties.setId(key));

        Registry.register(BuiltInRegistries.BLOCK, key, block);
        return block;
    }

    private static boolean always(BlockState state, BlockGetter world, BlockPos pos) {
        return true;
    }

    private static Function<BlockState, MapColor> axis(MapColor side, MapColor top) {
        return state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? top : side;
    }

    public static final BlockStateProvider VEILED_OVERGROWTH_BONEMEAL_PROVIDER = new WeightedStateProvider(new WeightedList.Builder<BlockState>().add(EnderscapeBlocks.WISP_SPROUTS.defaultBlockState(), 3).add(EnderscapeBlocks.WISP_GROWTH.defaultBlockState(), 1));
    public static final BlockStateProvider CELESTIAL_OVERGROWTH_BONEMEAL_PROVIDER = new WeightedStateProvider(new WeightedList.Builder<BlockState>().add(EnderscapeBlocks.CELESTIAL_CHANTERELLE.defaultBlockState(), 3).add(EnderscapeBlocks.BULB_FLOWER.defaultBlockState(), 1));
    public static final BlockStateProvider CORRUPT_OVERGROWTH_BONEMEAL_PROVIDER = new WeightedStateProvider(new WeightedList.Builder<BlockState>().add(EnderscapeBlocks.MURUBLIGHT_CHANTERELLE.defaultBlockState(), 3));

}