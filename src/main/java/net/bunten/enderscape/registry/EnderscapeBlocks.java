package net.bunten.enderscape.registry;

import com.google.common.base.Suppliers;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.block.*;
import net.bunten.enderscape.block.properties.MagniaPolarity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static net.minecraft.world.level.block.Blocks.*;

@EventBusSubscriber(modid = Enderscape.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
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

    public static final WoodType VEILED_WOOD_TYPE = registerWoodType(Enderscape.id("veiled"), VEILED_BLOCK_SET, EnderscapeSoundTypes.VEILED_PLANKS, EnderscapeSoundTypes.VEILED_HANGING_SIGN, EnderscapeBlockSounds.VEILED_FENCE_GATE_CLOSE, EnderscapeBlockSounds.VEILED_FENCE_GATE_OPEN);
    public static final WoodType CELESTIAL_WOOD_TYPE = registerWoodType(Enderscape.id("celestial"), CELESTIAL_BLOCK_SET, EnderscapeSoundTypes.CELESTIAL_PLANKS, EnderscapeSoundTypes.CELESTIAL_HANGING_SIGN, EnderscapeBlockSounds.CELESTIAL_FENCE_GATE_CLOSE, EnderscapeBlockSounds.CELESTIAL_FENCE_GATE_OPEN);
    public static final WoodType MURUBLIGHT_WOOD_TYPE = registerWoodType(Enderscape.id("murublight"), MURUBLIGHT_BLOCK_SET, EnderscapeSoundTypes.MURUBLIGHT_PLANKS, EnderscapeSoundTypes.MURUBLIGHT_HANGING_SIGN, EnderscapeBlockSounds.MURUBLIGHT_FENCE_GATE_CLOSE, EnderscapeBlockSounds.MURUBLIGHT_FENCE_GATE_OPEN);

    public static final Supplier<Block> DRIFT_JELLY_BLOCK = register(true, "drift_jelly_block", DriftJellyBlock::new, Properties.of()
            .mapColor(MapColor.WARPED_WART_BLOCK)
            .instabreak()
            .sound(EnderscapeSoundTypes.DRIFT_JELLY_BLOCK)
            .noOcclusion()
    );

    public static final Supplier<Block> END_TRIAL_SPAWNER = register(true, "end_trial_spawner", TrialSpawnerBlock::new, fullCopyOf(TRIAL_SPAWNER));
    public static final Supplier<Block> END_VAULT = register(true, "end_vault", EndVaultBlock::new, fullCopyOf(VAULT, p -> p.sound(EnderscapeSoundTypes.END_VAULT)));

    public static final Supplier<Block> END_STONE_STAIRS = registerStair("end_stone_stairs", () -> END_STONE);
    public static final Supplier<Block> END_STONE_SLAB = register(true, "end_stone_slab", SlabBlock::new, legacyCopyOf(END_STONE));
    public static final Supplier<Block> END_STONE_WALL = register(true, "end_stone_wall", WallBlock::new, legacyCopyOf(END_STONE));

    public static final Supplier<Block> POLISHED_END_STONE = register(true, "polished_end_stone", Block::new, legacyCopyOf(END_STONE, p -> p.sound(SoundType.STONE)));
    public static final Supplier<Block> POLISHED_END_STONE_STAIRS = registerStair("polished_end_stone_stairs", POLISHED_END_STONE);
    public static final Supplier<Block> POLISHED_END_STONE_SLAB = register(true, "polished_end_stone_slab", SlabBlock::new, legacyCopyOf(POLISHED_END_STONE));
    public static final Supplier<Block> POLISHED_END_STONE_WALL = register(true, "polished_end_stone_wall", WallBlock::new, legacyCopyOf(POLISHED_END_STONE));
    public static final Supplier<Block> POLISHED_END_STONE_BUTTON = register(true, "polished_end_stone_button", properties -> new ButtonBlock(POLISHED_END_STONE_BLOCK_SET, 20, properties), buttonProperties());
    public static final Supplier<Block> POLISHED_END_STONE_PRESSURE_PLATE = register(true, "polished_end_stone_pressure_plate", properties -> new PressurePlateBlock(POLISHED_END_STONE_BLOCK_SET, properties), BlockBehaviour.Properties.of()
            .mapColor(MapColor.SAND)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(SoundType.STONE)
            .requiresCorrectToolForDrops()
            .noCollission()
            .strength(0.5F)
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Supplier<Block> CHISELED_END_STONE = register(true, "chiseled_end_stone", Block::new, legacyCopyOf(POLISHED_END_STONE, p -> p.sound(SoundType.STONE)));

    public static final Supplier<Block> MIRESTONE = register(true, "mirestone", Block::new, Properties.of()
            .mapColor(MapColor.STONE)
            .requiresCorrectToolForDrops()
            .strength(6, 9)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.MIRESTONE)
    );

    public static final Supplier<Block> MIRESTONE_STAIRS = registerStair("mirestone_stairs", MIRESTONE);
    public static final Supplier<Block> MIRESTONE_SLAB = register(true, "mirestone_slab", SlabBlock::new, legacyCopyOf(MIRESTONE));
    public static final Supplier<Block> MIRESTONE_WALL = register(true, "mirestone_wall", WallBlock::new, legacyCopyOf(MIRESTONE));

    public static final Supplier<Block> POLISHED_MIRESTONE = register(true, "polished_mirestone", Block::new, legacyCopyOf(MIRESTONE));
    public static final Supplier<Block> POLISHED_MIRESTONE_STAIRS = registerStair("polished_mirestone_stairs", POLISHED_MIRESTONE);
    public static final Supplier<Block> POLISHED_MIRESTONE_SLAB = register(true, "polished_mirestone_slab", SlabBlock::new, legacyCopyOf(POLISHED_MIRESTONE));
    public static final Supplier<Block> POLISHED_MIRESTONE_WALL = register(true, "polished_mirestone_wall", WallBlock::new, legacyCopyOf(POLISHED_MIRESTONE));
    public static final Supplier<Block> POLISHED_MIRESTONE_BUTTON = register(true, "polished_mirestone_button", properties -> new ButtonBlock(POLISHED_MIRESTONE_BLOCK_SET, 20, properties), buttonProperties());
    public static final Supplier<Block> POLISHED_MIRESTONE_PRESSURE_PLATE = register(true, "polished_mirestone_pressure_plate", properties -> new PressurePlateBlock(POLISHED_MIRESTONE_BLOCK_SET, properties), BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.MIRESTONE)
            .requiresCorrectToolForDrops()
            .noCollission()
            .strength(0.5F)
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Supplier<Block> MIRESTONE_BRICKS = register(true, "mirestone_bricks", Block::new, legacyCopyOf(POLISHED_MIRESTONE, p -> p.sound(EnderscapeSoundTypes.MIRESTONE_BRICKS)));
    public static final Supplier<Block> MIRESTONE_BRICK_STAIRS = registerStair("mirestone_brick_stairs", MIRESTONE_BRICKS);
    public static final Supplier<Block> MIRESTONE_BRICK_SLAB = register(true, "mirestone_brick_slab", SlabBlock::new, legacyCopyOf(MIRESTONE_BRICKS));
    public static final Supplier<Block> MIRESTONE_BRICK_WALL = register(true, "mirestone_brick_wall", WallBlock::new, legacyCopyOf(MIRESTONE_BRICKS));

    public static final Supplier<Block> CHISELED_MIRESTONE = register(true, "chiseled_mirestone", Block::new, legacyCopyOf(POLISHED_MIRESTONE, p -> p.sound(EnderscapeSoundTypes.MIRESTONE_BRICKS)));

    public static final Supplier<Block> VERADITE = register(true, "veradite", Block::new, Properties.of()
            .mapColor(MapColor.GLOW_LICHEN)
            .requiresCorrectToolForDrops()
            .strength(1.5F, 6.0F)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.VERADITE)
    );

    public static final Supplier<Block> VERADITE_STAIRS = registerStair("veradite_stairs", VERADITE);
    public static final Supplier<Block> VERADITE_SLAB = register(true, "veradite_slab", SlabBlock::new, legacyCopyOf(VERADITE));
    public static final Supplier<Block> VERADITE_WALL = register(true, "veradite_wall", WallBlock::new, legacyCopyOf(VERADITE));

    public static final Supplier<Block> POLISHED_VERADITE = register(true, "polished_veradite", Block::new, legacyCopyOf(VERADITE));
    public static final Supplier<Block> POLISHED_VERADITE_STAIRS = registerStair("polished_veradite_stairs", POLISHED_VERADITE);
    public static final Supplier<Block> POLISHED_VERADITE_SLAB = register(true, "polished_veradite_slab", SlabBlock::new, legacyCopyOf(POLISHED_VERADITE));
    public static final Supplier<Block> POLISHED_VERADITE_WALL = register(true, "polished_veradite_wall", WallBlock::new, legacyCopyOf(POLISHED_VERADITE));
    public static final Supplier<Block> POLISHED_VERADITE_BUTTON = register(true, "polished_veradite_button", properties -> new ButtonBlock(POLISHED_VERADITE_BLOCK_SET, 20, properties), buttonProperties());
    public static final Supplier<Block> POLISHED_VERADITE_PRESSURE_PLATE = register(true, "polished_veradite_pressure_plate", properties -> new PressurePlateBlock(POLISHED_VERADITE_BLOCK_SET, properties), BlockBehaviour.Properties.of()
            .mapColor(MapColor.GLOW_LICHEN)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.VERADITE)
            .requiresCorrectToolForDrops()
            .noCollission()
            .strength(0.5F)
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Supplier<Block> VERADITE_BRICKS = register(true, "veradite_bricks", Block::new, legacyCopyOf(POLISHED_VERADITE, p -> p.sound(EnderscapeSoundTypes.VERADITE_BRICKS)));
    public static final Supplier<Block> VERADITE_BRICK_STAIRS = registerStair("veradite_brick_stairs", VERADITE_BRICKS);
    public static final Supplier<Block> VERADITE_BRICK_SLAB = register(true, "veradite_brick_slab", SlabBlock::new, legacyCopyOf(VERADITE_BRICKS));
    public static final Supplier<Block> VERADITE_BRICK_WALL = register(true, "veradite_brick_wall", WallBlock::new, legacyCopyOf(VERADITE_BRICKS));

    public static final Supplier<Block> CHISELED_VERADITE = register(true, "chiseled_veradite", Block::new, legacyCopyOf(POLISHED_VERADITE, p -> p.sound(EnderscapeSoundTypes.VERADITE_BRICKS)));

    public static final Supplier<Block> KURODITE = register(true, "kurodite", Block::new, Properties.of()
            .mapColor(MapColor.GLOW_LICHEN)
            .requiresCorrectToolForDrops()
            .requiresCorrectToolForDrops()
            .strength(1.5F, 6.0F)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.KURODITE)
    );

    public static final Supplier<Block> KURODITE_STAIRS = registerStair("kurodite_stairs", KURODITE);
    public static final Supplier<Block> KURODITE_SLAB = register(true, "kurodite_slab", SlabBlock::new, legacyCopyOf(KURODITE));
    public static final Supplier<Block> KURODITE_WALL = register(true, "kurodite_wall", WallBlock::new, legacyCopyOf(KURODITE));

    public static final Supplier<Block> POLISHED_KURODITE = register(true, "polished_kurodite", Block::new, legacyCopyOf(KURODITE));
    public static final Supplier<Block> POLISHED_KURODITE_STAIRS = registerStair("polished_kurodite_stairs", POLISHED_KURODITE);
    public static final Supplier<Block> POLISHED_KURODITE_SLAB = register(true, "polished_kurodite_slab", SlabBlock::new, legacyCopyOf(POLISHED_KURODITE));
    public static final Supplier<Block> POLISHED_KURODITE_WALL = register(true, "polished_kurodite_wall", WallBlock::new, legacyCopyOf(POLISHED_KURODITE));
    public static final Supplier<Block> POLISHED_KURODITE_BUTTON = register(true, "polished_kurodite_button", properties -> new ButtonBlock(POLISHED_KURODITE_BLOCK_SET, 20, properties), buttonProperties());
    public static final Supplier<Block> POLISHED_KURODITE_PRESSURE_PLATE = register(true, "polished_kurodite_pressure_plate", properties -> new PressurePlateBlock(POLISHED_KURODITE_BLOCK_SET, properties), BlockBehaviour.Properties.of()
            .mapColor(MapColor.GLOW_LICHEN)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.KURODITE)
            .requiresCorrectToolForDrops()
            .noCollission()
            .strength(0.5F)
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Supplier<Block> KURODITE_BRICKS = register(true, "kurodite_bricks", Block::new, legacyCopyOf(POLISHED_KURODITE, p -> p.sound(EnderscapeSoundTypes.KURODITE_BRICKS)));
    public static final Supplier<Block> KURODITE_BRICK_STAIRS = registerStair("kurodite_brick_stairs", KURODITE_BRICKS);
    public static final Supplier<Block> KURODITE_BRICK_SLAB = register(true, "kurodite_brick_slab", SlabBlock::new, legacyCopyOf(KURODITE_BRICKS));
    public static final Supplier<Block> KURODITE_BRICK_WALL = register(true, "kurodite_brick_wall", WallBlock::new, legacyCopyOf(KURODITE_BRICKS));

    public static final Supplier<Block> CHISELED_KURODITE = register(true, "chiseled_kurodite", Block::new, legacyCopyOf(POLISHED_KURODITE, p -> p.sound(EnderscapeSoundTypes.KURODITE_BRICKS)));



    public static final Supplier<Block> VOID_SHALE = register(true, "void_shale", VoidShaleBlock::new, Properties.of()
            .mapColor(MapColor.COLOR_BLACK)
            .strength(0.5F)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.VOID_SHALE)
    );

    public static final Supplier<Block> ALLURING_MAGNIA = register(true, "alluring_magnia", properties -> new MagniaBlock(MagniaPolarity.ALLURING, properties), BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .requiresCorrectToolForDrops()
            .strength(1.5F, 6.0F)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.ALLURING_MAGNIA)
    );

    public static final Supplier<Block> ALLURING_MAGNIA_SPROUT = register(true, "alluring_magnia_sprout", properties -> new MagniaSproutBlock(MagniaPolarity.ALLURING, properties), BlockBehaviour.Properties.of()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .lightLevel(state -> state.getValue(MagniaSproutBlock.POWERED) ? 12 : 0)
            .mapColor(MapColor.METAL)
            .noOcclusion()
            .randomTicks()
            .requiresCorrectToolForDrops()
            .sound(EnderscapeSoundTypes.ALLURING_MAGNIA)
            .strength(1.0F, 6.0F)
    );

    public static final Supplier<Block> ETCHED_ALLURING_MAGNIA = register(true, "etched_alluring_magnia", Block::new, legacyCopyOf(ALLURING_MAGNIA));
    public static final Supplier<Block> ETCHED_ALLURING_MAGNIA_STAIRS = registerStair( "etched_alluring_magnia_stairs", ETCHED_ALLURING_MAGNIA);
    public static final Supplier<Block> ETCHED_ALLURING_MAGNIA_SLAB = register(true, "etched_alluring_magnia_slab", properties -> new EtchedMagniaSlabBlock(MagniaPolarity.ALLURING, properties), legacyCopyOf(ETCHED_ALLURING_MAGNIA));
    public static final Supplier<Block> ETCHED_ALLURING_MAGNIA_WALL = register(true, "etched_alluring_magnia_wall", properties -> new EtchedMagniaWallBlock(MagniaPolarity.ALLURING, properties), legacyCopyOf(ETCHED_ALLURING_MAGNIA));

    public static final Supplier<Block> REPULSIVE_MAGNIA = register(true, "repulsive_magnia", properties -> new MagniaBlock(MagniaPolarity.REPULSIVE, properties), BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_GRAY)
            .requiresCorrectToolForDrops()
            .strength(1.5F, 6.0F)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.REPULSIVE_MAGNIA)
    );

    public static final Supplier<Block> REPULSIVE_MAGNIA_SPROUT = register(true, "repulsive_magnia_sprout", properties -> new MagniaSproutBlock(MagniaPolarity.REPULSIVE, properties), BlockBehaviour.Properties.of()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .lightLevel(state -> state.getValue(MagniaSproutBlock.POWERED) ? 12 : 0)
            .mapColor(MapColor.COLOR_GRAY)
            .noOcclusion()
            .randomTicks()
            .requiresCorrectToolForDrops()
            .sound(EnderscapeSoundTypes.REPULSIVE_MAGNIA)
            .strength(1.0F, 6.0F)
    );

    public static final Supplier<Block> ETCHED_REPULSIVE_MAGNIA = register(true, "etched_repulsive_magnia", Block::new, legacyCopyOf(REPULSIVE_MAGNIA));
    public static final Supplier<Block> ETCHED_REPULSIVE_MAGNIA_STAIRS = registerStair("etched_repulsive_magnia_stairs", ETCHED_REPULSIVE_MAGNIA);
    public static final Supplier<Block> ETCHED_REPULSIVE_MAGNIA_SLAB = register(true, "etched_repulsive_magnia_slab", properties -> new EtchedMagniaSlabBlock(MagniaPolarity.REPULSIVE, properties), legacyCopyOf(ETCHED_REPULSIVE_MAGNIA));
    public static final Supplier<Block> ETCHED_REPULSIVE_MAGNIA_WALL = register(true, "etched_repulsive_magnia_wall", properties -> new EtchedMagniaWallBlock(MagniaPolarity.REPULSIVE, properties), legacyCopyOf(ETCHED_REPULSIVE_MAGNIA));

    public static final Supplier<Block> BLISTERED_MAGNIA = register(true, "blistered_magnia", BlisteredMagniaBlock::new, BlockBehaviour.Properties.of()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .lightLevel(BlisteredMagniaBlock.getLightLevel())
            .mapColor(BlisteredMagniaBlock::getMapColor)
            .requiresCorrectToolForDrops()
            .sound(EnderscapeSoundTypes.BLISTERED_MAGNIA)
            .strength(4.5F, 6.0F)
    );

    public static final Supplier<Block> POLARIZED_MAGNIA = register(true, "polarized_magnia", PolarizedMagniaBlock::new, BlockBehaviour.Properties.of()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .isRedstoneConductor(EnderscapeBlocks::never)
            .lightLevel(PolarizedMagniaBlock.getLightLevel())
            .mapColor(PolarizedMagniaBlock::getMapColor)
            .requiresCorrectToolForDrops()
            .sound(EnderscapeSoundTypes.POLARIZED_MAGNIA)
            .strength(4.5F, 6.0F)
    );

    public static final Supplier<Block> MAGNIA_RADIO = register(true, "magnia_radio", MagniaRadioBlock::new, BlockBehaviour.Properties.of()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .lightLevel(state -> state.getValue(MagniaRadioBlock.ENABLED) ? 4 : 0)
            .mapColor(MapColor.METAL)
            .randomTicks()
            .sound(EnderscapeSoundTypes.MAGNIA_RADIO)
            .strength(1.5F)
    );

    public static final Supplier<Block> SHADOLINE_ORE = register(true, "shadoline_ore", properties -> new DropExperienceBlock(ConstantInt.of(0), properties), fullCopyOf(END_STONE, p -> p.sound(EnderscapeSoundTypes.SHADOLINE_ORE)));
    public static final Supplier<Block> MIRESTONE_SHADOLINE_ORE = register(true, "mirestone_shadoline_ore", properties -> new DropExperienceBlock(ConstantInt.of(0), properties), fullCopyOf(MIRESTONE, p -> p.sound(EnderscapeSoundTypes.MIRESTONE_SHADOLINE_ORE)));

    public static final Supplier<Block> RAW_SHADOLINE_BLOCK = register(true, "raw_shadoline_block", Block::new, fullCopyOf(RAW_IRON_BLOCK, p -> p
            .mapColor(MapColor.COLOR_LIGHT_GRAY)
            .strength(3)
            .sound(EnderscapeSoundTypes.SHADOLINE_ORE)
            .instrument(EnderscapeNoteBlockInstruments.SYNTH_BASS.get())
    ));

    public static final Supplier<Block> SHADOLINE_BLOCK = register(true, "shadoline_block", Block::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_GREEN)
            .requiresCorrectToolForDrops()
            .strength(3, 9)
            .sound(EnderscapeSoundTypes.SHADOLINE)
            .instrument(EnderscapeNoteBlockInstruments.SYNTH_BASS.get())
    );

    public static final Supplier<Block> SHADOLINE_BLOCK_STAIRS = registerStair("shadoline_block_stairs", SHADOLINE_BLOCK);
    public static final Supplier<Block> SHADOLINE_BLOCK_SLAB = register(true, "shadoline_block_slab", SlabBlock::new, legacyCopyOf(SHADOLINE_BLOCK));
    public static final Supplier<Block> SHADOLINE_BLOCK_WALL = register(true, "shadoline_block_wall", WallBlock::new, legacyCopyOf(SHADOLINE_BLOCK));

    public static final Supplier<Block> CUT_SHADOLINE = register(true, "cut_shadoline", Block::new, legacyCopyOf(SHADOLINE_BLOCK, p -> p.sound(EnderscapeSoundTypes.SHADOLINE)));
    public static final Supplier<Block> CUT_SHADOLINE_STAIRS = registerStair("cut_shadoline_stairs", CUT_SHADOLINE);
    public static final Supplier<Block> CUT_SHADOLINE_SLAB = register(true, "cut_shadoline_slab", SlabBlock::new, legacyCopyOf(CUT_SHADOLINE));
    public static final Supplier<Block> CUT_SHADOLINE_WALL = register(true, "cut_shadoline_wall", WallBlock::new, legacyCopyOf(CUT_SHADOLINE));

    public static final Supplier<Block> CHISELED_SHADOLINE = register(true, "chiseled_shadoline", Block::new, legacyCopyOf(CUT_SHADOLINE));
    public static final Supplier<Block> SHADOLINE_PILLAR = register(true, "shadoline_pillar", RotatedPillarBlock::new, legacyCopyOf(SHADOLINE_BLOCK));

    public static final Supplier<Block> SHADOLINE_BARS = register(
            true,
            "shadoline_bars",
            IronBarsBlock::new,
            BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(6.0F, 9.0F).sound(EnderscapeSoundTypes.SHADOLINE).noOcclusion()
    );

    public static final Supplier<Block> SHADOLINE_CHAIN = register(
            true,
            "shadoline_chain",
            ChainBlock::new,
            BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.CHAIN).noOcclusion()
    );

    public static final Supplier<Block> VOID_TORCH = register(
            false,
            "void_torch",
            VoidTorchBlock::new,
            BlockBehaviour.Properties.of().noCollission().instabreak().lightLevel(state -> 12).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY)
    );

    public static final Supplier<Block> VOID_WALL_TORCH = register(
            false,
            "void_wall_torch",
            VoidWallTorchBlock::new, wallVariant(VOID_TORCH, p -> p
            .noCollission().instabreak().lightLevel(state -> 12).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY)
    ));

    public static final Supplier<Block> VOID_LANTERN = register(
            true,
            "void_lantern",
            LanternBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_GREEN)
                    .forceSolidOn()
                    .strength(3.5F)
                    .sound(SoundType.LANTERN)
                    .lightLevel(state -> 12)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY)
    );

    public static final Supplier<Block> VOID_CAMPFIRE = register(true, "void_campfire", VoidCampfireBlock::new, fullCopyOf(CAMPFIRE, p -> p.lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 12 : 0)));

    public static final Supplier<Block> NEBULITE_ORE = register(true, "nebulite_ore", NebuliteOreBlock::new, fullCopyOf(END_STONE, p -> p.sound(EnderscapeSoundTypes.NEBULITE_ORE).randomTicks()));
    public static final Supplier<Block> MIRESTONE_NEBULITE_ORE = register(true, "mirestone_nebulite_ore", NebuliteOreBlock::new, fullCopyOf(MIRESTONE, p -> p.sound(EnderscapeSoundTypes.MIRESTONE_NEBULITE_ORE).randomTicks()));

    public static final Supplier<Block> NEBULITE_BLOCK = register(true, "nebulite_block", NebuliteBlock::new, Properties.of()
            .instrument(EnderscapeNoteBlockInstruments.SYNTH_BELL.get())
            .mapColor(MapColor.COLOR_MAGENTA)
            .requiresCorrectToolForDrops()
            .sound(EnderscapeSoundTypes.NEBULITE_BLOCK)
            .strength(2, 6)
    );

    public static final Supplier<Block> DRY_END_GROWTH = register(true, "dry_end_growth", DryEndGrowthBlock::new, Properties.of()
            .instabreak()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .mapColor(MapColor.SAND)
            .noCollission()
            .noOcclusion()
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .replaceable()
            .sound(EnderscapeSoundTypes.DRY_END_GROWTH)
    );

    public static final Supplier<Block> CHORUS_SPROUTS = register(true, "chorus_sprouts", ChorusSproutsBlock::new, Properties.of()
            .mapColor(MapColor.COLOR_PURPLE)
            .noCollission()
            .instabreak()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(EnderscapeSoundTypes.CHORUS_SPROUTS)
            .noOcclusion()
            .offsetType(BlockBehaviour.OffsetType.XZ)
    );

    public static final Supplier<Block> PURPUR_WALL = register(true, "purpur_wall", WallBlock::new, legacyCopyOf(PURPUR_BLOCK));
    public static final Supplier<Block> CHISELED_PURPUR = register(true, "chiseled_purpur", Block::new, legacyCopyOf(PURPUR_BLOCK));

    public static final Supplier<Block> DUSK_PURPUR_BLOCK = register(true, "dusk_purpur_block", Block::new, fullCopyOf(PURPUR_BLOCK, p -> p.mapColor(MapColor.COLOR_BLACK)));
    public static final Supplier<Block> DUSK_PURPUR_STAIRS = registerStair("dusk_purpur_stairs", DUSK_PURPUR_BLOCK);
    public static final Supplier<Block> DUSK_PURPUR_SLAB = register(true, "dusk_purpur_slab", SlabBlock::new, fullCopyOf(PURPUR_SLAB, p -> p.mapColor(MapColor.COLOR_BLACK)));
    public static final Supplier<Block> DUSK_PURPUR_WALL = register(true, "dusk_purpur_wall", WallBlock::new, fullCopyOf(PURPUR_WALL, p -> p.mapColor(MapColor.COLOR_BLACK)));
    public static final Supplier<Block> CHISELED_DUSK_PURPUR = register(true, "chiseled_dusk_purpur", Block::new, fullCopyOf(CHISELED_PURPUR, p -> p.mapColor(MapColor.COLOR_BLACK)));
    public static final Supplier<Block> DUSK_PURPUR_PILLAR = register(true, "dusk_purpur_pillar", RotatedPillarBlock::new, fullCopyOf(PURPUR_PILLAR, p -> p.mapColor(MapColor.COLOR_BLACK)));

    public static final Supplier<Block> PURPUR_TILES = register(true, "purpur_tiles", Block::new, fullCopyOf(PURPUR_BLOCK));
    public static final Supplier<Block> PURPUR_TILE_STAIRS = registerStair("purpur_tile_stairs", PURPUR_TILES);
    public static final Supplier<Block> PURPUR_TILE_SLAB = register(true, "purpur_tile_slab", SlabBlock::new, fullCopyOf(PURPUR_SLAB));

    public static final Supplier<Block> CHORUS_CAKE_ROLL = register(false, "chorus_cake_roll", ChorusCakeRollBlock::new, BlockBehaviour.Properties.of().forceSolidOn().strength(0.5F).sound(EnderscapeSoundTypes.CHORUS_CAKE_ROLL).pushReaction(PushReaction.DESTROY));

    public static final Supplier<Block> END_LAMP = register(true, "end_lamp", Block::new, Properties.of()
            .instrument(NoteBlockInstrument.PLING)
            .isRedstoneConductor(EnderscapeBlocks::never)
            .lightLevel(state -> 15)
            .mapColor(MapColor.QUARTZ)
            .sound(EnderscapeSoundTypes.END_LAMP)
            .strength(0.6F)
    );

    public static final Supplier<Block> VEILED_END_STONE = register(true, "veiled_end_stone", VeiledOvergrowthBlock::new, Properties.of()
            .mapColor(MapColor.CLAY)
            .requiresCorrectToolForDrops()
            .strength(3, 9)
            .sound(EnderscapeSoundTypes.VEILED_END_STONE)
            .randomTicks()
            .isValidSpawn(Blocks::always)
    );

    public static final Supplier<Block> WISP_SPROUTS = register(true, "wisp_sprouts", WispSproutsBlock::new, Properties.of()
            .instabreak()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .mapColor(MapColor.CLAY)
            .noCollission()
            .noOcclusion()
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .replaceable()
            .sound(EnderscapeSoundTypes.WISP_GROWTH)
    );

    public static final Supplier<Block> WISP_GROWTH = register(true, "wisp_growth", WispGrowthBlock::new, Properties.of()
            .instabreak()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .mapColor(MapColor.CLAY)
            .noCollission()
            .noOcclusion()
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .replaceable()
            .sound(EnderscapeSoundTypes.WISP_GROWTH)
    );

    public static final Supplier<Block> WISP_FLOWER = register(true, "wisp_flower", WispFlowerBlock::new, Properties.of()
            .mapColor(MapColor.CLAY)
            .noCollission()
            .instabreak()
            .sound(EnderscapeSoundTypes.WISP_FLOWER)
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .ignitedByLava()
            .lightLevel(state -> 7)
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Supplier<Block> STRIPPED_VEILED_LOG = register(true, "stripped_veiled_log", RotatedPillarBlock::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.TERRACOTTA_BLUE)
            .strength(2)
            .sound(EnderscapeSoundTypes.VEILED_LOG)
    );

    public static final Supplier<Block> VEILED_LOG = register(true, "veiled_log", RotatedPillarBlock::new, fullCopyOf(STRIPPED_VEILED_LOG, p -> p.mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)));
    public static final Supplier<Block> STRIPPED_VEILED_WOOD = register(true, "stripped_veiled_wood", RotatedPillarBlock::new, fullCopyOf(STRIPPED_VEILED_LOG));
    public static final Supplier<Block> VEILED_WOOD = register(true, "veiled_wood", RotatedPillarBlock::new, fullCopyOf(STRIPPED_VEILED_WOOD, p -> p.mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)));

    public static final Supplier<Block> VEILED_LEAVES = register(true, "veiled_leaves", VeiledLeavesBlock::new, leavesProperties(EnderscapeSoundTypes.VEILED_LEAVES).mapColor(MapColor.CLAY));

    public static final Supplier<Block> VEILED_LEAF_PILE = register(true,
            "veiled_leaf_pile",
            VeiledLeafPileBlock::new,
            BlockBehaviour.Properties.of()
                    .forceSolidOff()
                    .ignitedByLava()
                    .isRedstoneConductor(EnderscapeBlocks::never)
                    .isSuffocating(EnderscapeBlocks::never)
                    .isValidSpawn(Blocks::ocelotOrParrot)
                    .isViewBlocking(EnderscapeBlocks::never)
                    .mapColor(MapColor.CLAY)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY)
                    .randomTicks()
                    .replaceable()
                    .sound(EnderscapeSoundTypes.VEILED_LEAVES)
                    .strength(0.1F)
    );

    public static final Supplier<Block> VEILED_VINES = register(true, "veiled_vines", VeiledVinesBlock::new, Properties.of()
            .mapColor(MapColor.CLAY)
            .noCollission()
            .noOcclusion()
            .pushReaction(PushReaction.DESTROY)
            .sound(EnderscapeSoundTypes.VEILED_LEAVES)
            .strength(0.2F)
    );

    public static final Supplier<Block> VEILED_SAPLING = register(true, "veiled_sapling", properties -> new VeiledSaplingBlock(EnderscapeConfiguredFeatures.VEILED_TREE_FROM_SAPLING, properties), fullCopyOf(OAK_SAPLING, p -> p
            .mapColor(MapColor.CLAY)
            .sound(EnderscapeSoundTypes.CHORUS_SPROUTS)
    ));

    public static final Supplier<Block> VEILED_PLANKS = register(true, "veiled_planks", Block::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_BLUE)
            .strength(2, 3)
            .sound(EnderscapeSoundTypes.VEILED_PLANKS)
    );

    public static final Supplier<Block> VEILED_STAIRS = registerStair("veiled_stairs", VEILED_PLANKS);
    public static final Supplier<Block> VEILED_SLAB = register(true, "veiled_slab", SlabBlock::new, legacyCopyOf(VEILED_PLANKS));
    public static final Supplier<Block> VEILED_PRESSURE_PLATE = register(true, "veiled_pressure_plate", properties -> new PressurePlateBlock(VEILED_BLOCK_SET, properties), () -> BlockBehaviour.Properties.of()
            .mapColor(VEILED_PLANKS.get().defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(0.5F)
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Supplier<Block> VEILED_FENCE = register(true, "veiled_fence", FenceBlock::new, legacyCopyOf(VEILED_PLANKS));
    public static final Supplier<Block> VEILED_DOOR = register(true, "veiled_door", properties -> new DoorBlock(VEILED_BLOCK_SET, properties), () -> BlockBehaviour.Properties.of()
            .mapColor(VEILED_PLANKS.get().defaultMapColor())
            .instrument(NoteBlockInstrument.BASS)
            .strength(3)
            .noOcclusion()
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Supplier<Block> VEILED_TRAPDOOR = register(true, "veiled_trapdoor", properties -> new TrapDoorBlock(VEILED_BLOCK_SET, properties), () -> BlockBehaviour.Properties.of()
            .mapColor(VEILED_PLANKS.get().defaultMapColor())
            .instrument(NoteBlockInstrument.BASS)
            .strength(3)
            .noOcclusion()
            .isValidSpawn(Blocks::never)
            .ignitedByLava()
    );

    public static final Supplier<Block> VEILED_FENCE_GATE = register(true, "veiled_fence_gate", properties -> new FenceGateBlock(VEILED_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.of()
            .mapColor(VEILED_PLANKS.get().defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .strength(2, 3)
            .ignitedByLava()
    );

    public static final Supplier<Block> VEILED_BUTTON = register(true, "veiled_button", properties -> new ButtonBlock(VEILED_BLOCK_SET, 30, properties), buttonProperties());
    public static final Supplier<Block> VEILED_SIGN = register(false, "veiled_sign", properties -> new StandingSignBlock(VEILED_WOOD_TYPE, properties), BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1).ignitedByLava());
    public static final Supplier<Block> VEILED_WALL_SIGN = register(false, "veiled_wall_sign", properties -> new WallSignBlock(VEILED_WOOD_TYPE, properties), wallVariant(VEILED_SIGN, p -> p.mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1).ignitedByLava()));

    public static final Supplier<Block> VEILED_HANGING_SIGN = register(false, "veiled_hanging_sign", properties -> new CeilingHangingSignBlock(VEILED_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.of()
            .mapColor(VEILED_LOG.get().defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(1)
            .ignitedByLava()
    );

    public static final Supplier<Block> VEILED_WALL_HANGING_SIGN = register(false, "veiled_wall_hanging_sign", properties -> new WallHangingSignBlock(VEILED_WOOD_TYPE, properties), wallVariant(VEILED_HANGING_SIGN, p -> p
            .mapColor(VEILED_LOG.get().defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(1)
            .ignitedByLava()
    ));

    public static final Supplier<Block> CELESTIAL_PATH = register(true, "celestial_path", CelestialPathBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_WHITE)
            .requiresCorrectToolForDrops()
            .strength(3, 9)
            .sound(EnderscapeSoundTypes.CELESTIAL_OVERGROWTH)
            .isViewBlocking(EnderscapeBlocks::always)
            .isSuffocating(EnderscapeBlocks::always)
    );

    public static final Supplier<Block> CELESTIAL_OVERGROWTH = register(true, "celestial_overgrowth", CelestialOvergrowthBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_YELLOW)
            .requiresCorrectToolForDrops()
            .strength(3, 9)
            .sound(EnderscapeSoundTypes.CELESTIAL_OVERGROWTH)
            .randomTicks()
            .isValidSpawn(Blocks::always)
    );

    public static final Supplier<Block> CELESTIAL_GROWTH = register(true, "celestial_growth", CelestialGrowthBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_YELLOW)
            .noCollission()
            .instabreak()
            .sound(EnderscapeSoundTypes.CELESTIAL_GROWTH)
            .noOcclusion()
    );

    public static final Supplier<Block> BULB_FLOWER = register(true, "bulb_flower", BulbFlowerBlock::new, Properties.of()
            .mapColor(MapColor.COLOR_YELLOW)
            .noCollission()
            .instabreak()
            .sound(EnderscapeSoundTypes.BULB_FLOWER)
            .noOcclusion()
            .lightLevel(state -> 7)
    );

    public static final Supplier<Block> BULB_LANTERN = register(true, "bulb_lantern", BulbLanternBlock::new, legacyCopyOf(LANTERN, p -> p.sound(EnderscapeSoundTypes.BULB_LANTERN)));

    public static final Supplier<Block> FLANGER_BERRY_VINE = register(false, "flanger_berry_vine", FlangerBerryVine::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_YELLOW)
            .randomTicks()
            .noCollission()
            .sound(EnderscapeSoundTypes.FLANGER_BERRY_VINE)
            .strength(0.2F)
    );

    public static final Supplier<Block> FLANGER_BERRY_FLOWER = register(true, "flanger_berry_flower", FlangerBerryFlowerBlock::new, Properties.of()
            .mapColor(MapColor.WARPED_WART_BLOCK)
            .randomTicks()
            .strength(0.3F)
            .pushReaction(PushReaction.DESTROY)
            .noCollission()
            .sound(EnderscapeSoundTypes.FLANGER_FLOWER)
            .noOcclusion()
    );

    public static final Supplier<Block> UNRIPE_FLANGER_BERRY_BLOCK = register(true, "unripe_flanger_berry_block", UnripeFlangerBerryBlock::new, Properties.of()
            .mapColor(MapColor.WARPED_WART_BLOCK)
            .randomTicks()
            .strength(0.3F)
            .pushReaction(PushReaction.DESTROY)
            .sound(EnderscapeSoundTypes.FLANGER_BERRY_BLOCK)
            .noOcclusion()
    );

    public static final Supplier<Block> RIPE_FLANGER_BERRY_BLOCK = register(true, "ripe_flanger_berry_block", RipeFlangerBerryBlock::new, Properties.of()
            .mapColor(MapColor.WARPED_WART_BLOCK)
            .strength(0.3F)
            .pushReaction(PushReaction.DESTROY)
            .sound(EnderscapeSoundTypes.FLANGER_BERRY_BLOCK)
    );

    public static final Supplier<Block> CELESTIAL_CHANTERELLE = register(true, "celestial_chanterelle", properties -> new CelestialChanterelleBlock(EnderscapeConfiguredFeatures.LARGE_CELESTIAL_CHANTERELLE, properties), Properties.of()
            .mapColor(MapColor.TERRACOTTA_YELLOW)
            .noCollission()
            .instabreak()
            .randomTicks()
            .sound(EnderscapeSoundTypes.CELESTIAL_CHANTERELLE)
    );

    public static final Supplier<Block> STRIPPED_CELESTIAL_STEM = register(true, "stripped_celestial_stem", RotatedPillarBlock::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_ORANGE)
            .strength(2)
            .sound(EnderscapeSoundTypes.CELESTIAL_STEM)
    );

    public static final Supplier<Block> CELESTIAL_STEM = register(true, "celestial_stem", RotatedPillarBlock::new, fullCopyOf(STRIPPED_CELESTIAL_STEM, p -> p.mapColor(MapColor.TERRACOTTA_WHITE)));

    public static final Supplier<Block> STRIPPED_CELESTIAL_HYPHAE = register(true, "stripped_celestial_hyphae", RotatedPillarBlock::new, fullCopyOf(STRIPPED_CELESTIAL_STEM));

    public static final Supplier<Block> CELESTIAL_HYPHAE = register(true, "celestial_hyphae", RotatedPillarBlock::new, fullCopyOf(STRIPPED_CELESTIAL_HYPHAE, p -> p.mapColor(MapColor.TERRACOTTA_WHITE)));

    public static final Supplier<Block> CELESTIAL_CAP = register(true, "celestial_cap", ChanterelleCapBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_YELLOW)
            .strength(1)
            .sound(EnderscapeSoundTypes.CELESTIAL_CAP)
            .isValidSpawn(Blocks::never)
    );

    public static final Supplier<Block> CELESTIAL_BRICKS = register(true, "celestial_bricks", Block::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_YELLOW)
            .strength(1.5F, 6)
            .sound(EnderscapeSoundTypes.CELESTIAL_BRICKS)
    );

    public static final Supplier<Block> CELESTIAL_BRICK_STAIRS = registerStair("celestial_brick_stairs", CELESTIAL_BRICKS);
    public static final Supplier<Block> CELESTIAL_BRICK_SLAB = register(true, "celestial_brick_slab", SlabBlock::new, legacyCopyOf(CELESTIAL_BRICKS));
    public static final Supplier<Block> CELESTIAL_BRICK_WALL = register(true, "celestial_brick_wall", WallBlock::new, legacyCopyOf(CELESTIAL_BRICKS));

    public static final Supplier<Block> CELESTIAL_PLANKS = register(true, "celestial_planks", Block::new, Properties.of()
            .mapColor(MapColor.COLOR_ORANGE)
            .strength(2, 3)
            .sound(EnderscapeSoundTypes.CELESTIAL_PLANKS)
    );

    public static final Supplier<Block> CELESTIAL_STAIRS = registerStair("celestial_stairs", CELESTIAL_PLANKS);
    public static final Supplier<Block> CELESTIAL_SLAB = register(true, "celestial_slab", SlabBlock::new, legacyCopyOf(CELESTIAL_PLANKS));
    public static final Supplier<Block> CELESTIAL_PRESSURE_PLATE = register(true, "celestial_pressure_plate", properties -> new PressurePlateBlock(CELESTIAL_BLOCK_SET, properties), () -> BlockBehaviour.Properties.of()
            .mapColor(CELESTIAL_PLANKS.get().defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(0.5F)
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Supplier<Block> CELESTIAL_FENCE = register(true, "celestial_fence", FenceBlock::new, legacyCopyOf(CELESTIAL_PLANKS));
    public static final Supplier<Block> CELESTIAL_DOOR = register(true, "celestial_door", properties -> new DoorBlock(CELESTIAL_BLOCK_SET, properties), () -> BlockBehaviour.Properties.of()
            .mapColor(CELESTIAL_PLANKS.get().defaultMapColor())
            .instrument(NoteBlockInstrument.BASS)
            .strength(3)
            .noOcclusion()
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Supplier<Block> CELESTIAL_TRAPDOOR = register(true, "celestial_trapdoor", properties -> new TrapDoorBlock(CELESTIAL_BLOCK_SET, properties), () -> BlockBehaviour.Properties.of()
            .mapColor(CELESTIAL_PLANKS.get().defaultMapColor())
            .instrument(NoteBlockInstrument.BASS)
            .strength(3)
            .noOcclusion()
            .isValidSpawn(Blocks::never)
            .ignitedByLava()
    );

    public static final Supplier<Block> CELESTIAL_FENCE_GATE = register(true, "celestial_fence_gate", properties -> new FenceGateBlock(CELESTIAL_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.of()
            .mapColor(CELESTIAL_PLANKS.get().defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .strength(2, 3)
            .ignitedByLava()
    );

    public static final Supplier<Block> CELESTIAL_BUTTON = register(true, "celestial_button", properties -> new ButtonBlock(CELESTIAL_BLOCK_SET, 30, properties), buttonProperties());
    public static final Supplier<Block> CELESTIAL_SIGN = register(false, "celestial_sign", properties -> new StandingSignBlock(CELESTIAL_WOOD_TYPE, properties), BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1).ignitedByLava());
    public static final Supplier<Block> CELESTIAL_WALL_SIGN = register(false, "celestial_wall_sign", properties -> new WallSignBlock(CELESTIAL_WOOD_TYPE, properties), wallVariant(CELESTIAL_SIGN, p -> p.mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1).ignitedByLava()));

    public static final Supplier<Block> CELESTIAL_HANGING_SIGN = register(false, "celestial_hanging_sign", properties -> new CeilingHangingSignBlock(CELESTIAL_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.of()
            .mapColor(CELESTIAL_STEM.get().defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(1)
            .ignitedByLava()
    );

    public static final Supplier<Block> CELESTIAL_WALL_HANGING_SIGN = register(false, "celestial_wall_hanging_sign", properties -> new WallHangingSignBlock(CELESTIAL_WOOD_TYPE, properties), wallVariant(CELESTIAL_HANGING_SIGN, p -> p
            .mapColor(CELESTIAL_STEM.get().defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(1)
            .ignitedByLava()
    ));

    public static final Supplier<Block> CORRUPT_PATH = register(true, "corrupt_path", CorruptPathBlock::new, Properties.of()
            .mapColor(MapColor.COLOR_PURPLE)
            .requiresCorrectToolForDrops()
            .strength(3, 9)
            .sound(EnderscapeSoundTypes.CORRUPT_OVERGROWTH)
            .isSuffocating(EnderscapeBlocks::always)
    );

    public static final Supplier<Block> CORRUPT_OVERGROWTH = register(true, "corrupt_overgrowth", CorruptOvergrowthBlock::new, Properties.of()
            .mapColor(MapColor.COLOR_PURPLE)
            .requiresCorrectToolForDrops()
            .strength(6, 9)
            .sound(EnderscapeSoundTypes.CORRUPT_OVERGROWTH)
            .isValidSpawn(Blocks::always)
    );

    public static final Supplier<Block> CORRUPT_GROWTH = register(true, "corrupt_growth", CorruptGrowthBlock::new, Properties.of()
            .mapColor(MapColor.COLOR_PURPLE)
            .noCollission()
            .instabreak()
            .sound(EnderscapeSoundTypes.CORRUPT_GROWTH)
            .noOcclusion()
    );

    public static final Supplier<Block> BLINKLIGHT_VINES_BODY = register(false, "blinklight_vines_body", BlinklightVinesBodyBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)
            .noCollission()
            .sound(EnderscapeSoundTypes.BLINKLIGHT_VINES)
            .strength(0.8F)
            .randomTicks()
            .lightLevel(BlinklightVines::getLuminance)
    );

    public static final Supplier<Block> BLINKLIGHT_VINES_HEAD = register(false, "blinklight_vines_head", BlinklightVinesHeadBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)
            .noCollission()
            .sound(EnderscapeSoundTypes.BLINKLIGHT_VINES)
            .strength(0.8F)
            .randomTicks()
            .lightLevel(BlinklightVines::getLuminance)
    );

    public static final Supplier<Block> BLINKLAMP = register(true, "blinklamp", BlinklampBlock::new, Properties.of()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .isRedstoneConductor(EnderscapeBlocks::never)
            .mapColor(BlinklampBlock::getColor)
            .sound(EnderscapeSoundTypes.BLINKLAMP)
            .strength(1.5F, 6.0F)
    );

    public static final Supplier<Block> MURUBLIGHT_BRACKET = register(false, "murublight_bracket", MurublightShelfBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)
            .noCollission()
            .instabreak()
            .sound(EnderscapeSoundTypes.MURUBLIGHT_BRACKET)
            .noOcclusion()
    );

    public static final Supplier<Block> MURUBLIGHT_CHANTERELLE = register(true, "murublight_chanterelle", MurublightChanterelleBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)
            .noCollission()
            .instabreak()
            .randomTicks()
            .sound(EnderscapeSoundTypes.CELESTIAL_CHANTERELLE)
    );

    public static final Supplier<Block> STRIPPED_MURUBLIGHT_STEM = register(true, "stripped_murublight_stem", RotatedPillarBlock::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)
            .strength(2)
            .sound(EnderscapeSoundTypes.MURUBLIGHT_STEM)
    );

    public static final Supplier<Block> MURUBLIGHT_STEM = register(true, "murublight_stem", RotatedPillarBlock::new, fullCopyOf(STRIPPED_MURUBLIGHT_STEM, p -> p.mapColor(MapColor.COLOR_BLACK)));
    public static final Supplier<Block> STRIPPED_MURUBLIGHT_HYPHAE = register(true, "stripped_murublight_hyphae", RotatedPillarBlock::new, fullCopyOf(STRIPPED_MURUBLIGHT_STEM));
    public static final Supplier<Block> MURUBLIGHT_HYPHAE = register(true, "murublight_hyphae", RotatedPillarBlock::new, fullCopyOf(STRIPPED_MURUBLIGHT_HYPHAE, p -> p.mapColor(MapColor.COLOR_BLACK)));

    public static final Supplier<Block> MURUBLIGHT_CAP = register(true, "murublight_cap", ChanterelleCapBlock::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_BLUE)
            .strength(1)
            .sound(EnderscapeSoundTypes.MURUBLIGHT_CAP)
            .isValidSpawn(Blocks::never)
    );

    public static final Supplier<Block> MURUBLIGHT_BRICKS = register(true, "murublight_bricks", Block::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_BLUE)
            .strength(1.5F, 6)
            .sound(EnderscapeSoundTypes.MURUBLIGHT_BRICKS)
    );

    public static final Supplier<Block> MURUBLIGHT_BRICK_STAIRS = registerStair("murublight_brick_stairs", MURUBLIGHT_BRICKS);
    public static final Supplier<Block> MURUBLIGHT_BRICK_SLAB = register(true, "murublight_brick_slab", SlabBlock::new, legacyCopyOf(MURUBLIGHT_BRICKS));
    public static final Supplier<Block> MURUBLIGHT_BRICK_WALL = register(true, "murublight_brick_wall", WallBlock::new, legacyCopyOf(MURUBLIGHT_BRICKS));

    public static final Supplier<Block> MURUBLIGHT_PLANKS = register(true, "murublight_planks", Block::new, Properties.of()
            .mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)
            .strength(2, 3)
            .sound(EnderscapeSoundTypes.MURUBLIGHT_PLANKS)
    );

    public static final Supplier<Block> MURUBLIGHT_STAIRS = registerStair("murublight_stairs", MURUBLIGHT_PLANKS);

    public static final Supplier<Block> MURUBLIGHT_SLAB = register(true, "murublight_slab", SlabBlock::new, legacyCopyOf(MURUBLIGHT_PLANKS));

    public static final Supplier<Block> MURUBLIGHT_PRESSURE_PLATE = register(true, "murublight_pressure_plate", properties -> new PressurePlateBlock(MURUBLIGHT_BLOCK_SET, properties), () -> BlockBehaviour.Properties.of()
            .mapColor(MURUBLIGHT_PLANKS.get().defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(0.5F)
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Supplier<Block> MURUBLIGHT_FENCE = register(true, "murublight_fence", FenceBlock::new, legacyCopyOf(MURUBLIGHT_PLANKS));

    public static final Supplier<Block> MURUBLIGHT_DOOR = register(true, "murublight_door", properties -> new DoorBlock(MURUBLIGHT_BLOCK_SET, properties), () -> BlockBehaviour.Properties.of()
            .mapColor(MURUBLIGHT_PLANKS.get().defaultMapColor())
            .instrument(NoteBlockInstrument.BASS)
            .strength(3)
            .noOcclusion()
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Supplier<Block> MURUBLIGHT_TRAPDOOR = register(true, "murublight_trapdoor", properties -> new TrapDoorBlock(MURUBLIGHT_BLOCK_SET, properties), () -> BlockBehaviour.Properties.of()
            .mapColor(MURUBLIGHT_PLANKS.get().defaultMapColor())
            .instrument(NoteBlockInstrument.BASS)
            .strength(3)
            .noOcclusion()
            .isValidSpawn(Blocks::never)
            .ignitedByLava()
    );

    public static final Supplier<Block> MURUBLIGHT_FENCE_GATE = register(true, "murublight_fence_gate", properties -> new FenceGateBlock(MURUBLIGHT_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.of()
            .mapColor(MURUBLIGHT_PLANKS.get().defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .strength(2, 3)
            .ignitedByLava()
    );

    public static final Supplier<Block> MURUBLIGHT_BUTTON = register(true, "murublight_button", properties -> new ButtonBlock(MURUBLIGHT_BLOCK_SET, 30, properties), buttonProperties());
    public static final Supplier<Block> MURUBLIGHT_SIGN = register(false, "murublight_sign", properties -> new StandingSignBlock(MURUBLIGHT_WOOD_TYPE, properties), BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1).ignitedByLava());
    public static final Supplier<Block> MURUBLIGHT_WALL_SIGN = register(false, "murublight_wall_sign", properties -> new WallSignBlock(MURUBLIGHT_WOOD_TYPE, properties), wallVariant(MURUBLIGHT_SIGN, p -> p.mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1).ignitedByLava()));

    public static final Supplier<Block> MURUBLIGHT_HANGING_SIGN = register(false, "murublight_hanging_sign", properties -> new CeilingHangingSignBlock(MURUBLIGHT_WOOD_TYPE, properties), () -> BlockBehaviour.Properties.of()
            .mapColor(MURUBLIGHT_STEM.get().defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(1)
            .ignitedByLava()
    );

    public static final Supplier<Block> MURUBLIGHT_WALL_HANGING_SIGN = register(false, "murublight_wall_hanging_sign", properties -> new WallHangingSignBlock(MURUBLIGHT_WOOD_TYPE, properties), wallVariant(MURUBLIGHT_HANGING_SIGN, p -> p
            .mapColor(MURUBLIGHT_STEM.get().defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(1)
            .ignitedByLava()
    ));

    public static final Supplier<Block> POTTED_ALLURING_MAGNIA_SPROUT = register(false, "potted_alluring_magnia_sprout", properties -> createFlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ALLURING_MAGNIA_SPROUT, properties), flowerPotProperties());
    public static final Supplier<Block> POTTED_BLINKLIGHT = register(false, "potted_blinklight", properties -> createFlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, BLINKLIGHT_VINES_HEAD, properties), flowerPotProperties().lightLevel(state -> 12));
    public static final Supplier<Block> POTTED_BULB_FLOWER = register(false, "potted_bulb_flower", properties -> createFlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, BULB_FLOWER, properties), flowerPotProperties().lightLevel(state -> 7));
    public static final Supplier<Block> POTTED_CELESTIAL_CHANTERELLE = register(false, "potted_celestial_chanterelle", properties -> createFlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, CELESTIAL_CHANTERELLE, properties), flowerPotProperties());
    public static final Supplier<Block> POTTED_CELESTIAL_GROWTH = register(false, "potted_celestial_growth", properties -> createFlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, CELESTIAL_GROWTH, properties), flowerPotProperties());
    public static final Supplier<Block> POTTED_CHORUS_SPROUTS = register(false, "potted_chorus_sprouts", properties -> createFlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, CHORUS_SPROUTS, properties), flowerPotProperties());
    public static final Supplier<Block> POTTED_CORRUPT_GROWTH = register(false, "potted_corrupt_growth", properties -> createFlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, CORRUPT_GROWTH, properties), flowerPotProperties());
    public static final Supplier<Block> POTTED_DRY_END_GROWTH = register(false, "potted_dry_end_growth", properties -> createFlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, DRY_END_GROWTH, properties), flowerPotProperties());
    public static final Supplier<Block> POTTED_MURUBLIGHT_CHANTERELLE = register(false, "potted_murublight_chanterelle", properties -> createFlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, MURUBLIGHT_CHANTERELLE, properties), flowerPotProperties());
    public static final Supplier<Block> POTTED_REPULSIVE_MAGNIA_SPROUT = register(false, "potted_repulsive_magnia_sprout", properties -> createFlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, REPULSIVE_MAGNIA_SPROUT, properties), flowerPotProperties());
    public static final Supplier<Block> POTTED_VEILED_SAPLING = register(false, "potted_veiled_sapling", properties -> createFlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, VEILED_SAPLING, properties), flowerPotProperties());
    public static final Supplier<Block> POTTED_WISP_GROWTH = register(false, "potted_wisp_growth", properties -> createFlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, WISP_GROWTH, properties), flowerPotProperties());


    private static final List<FlowerPotBlock> FLOWER_POT_BLOCKS = new ArrayList<>();

    private static FlowerPotBlock createFlowerPotBlock(Supplier<FlowerPotBlock> flowerPot, Supplier<? extends Block> content, BlockBehaviour.Properties properties) {
        var block = new FlowerPotBlock(flowerPot, content, properties);
        FLOWER_POT_BLOCKS.add(block);
        return block;
    }

    @SubscribeEvent
    public static void onFmlCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            var empty = (FlowerPotBlock) (Blocks.FLOWER_POT);
            for (var block : FLOWER_POT_BLOCKS) {
                empty.addPlant(BuiltInRegistries.BLOCK.getKey(block.getPotted()), () -> block);
            }
        });
    }

    private static Supplier<Block> registerStair(String string, Supplier<Block> block) {
        return register(true, string, properties -> new StairBlock(block.get().defaultBlockState(), properties), fullCopyOf(block));
    }

    public static BlockBehaviour.Properties flowerPotProperties() {
        return BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY);
    }

    public static BlockBehaviour.Properties buttonProperties() {
        return BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY);
    }

    public static BlockBehaviour.Properties leavesProperties(SoundType soundType) {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .strength(0.2F)
                .randomTicks()
                .sound(soundType)
                .noOcclusion()
                .isValidSpawn(Blocks::ocelotOrParrot)
                .isSuffocating(EnderscapeBlocks::never)
                .isViewBlocking(EnderscapeBlocks::never)
                .ignitedByLava()
                .pushReaction(PushReaction.DESTROY)
                .isRedstoneConductor(EnderscapeBlocks::never);
    }

    public static Supplier<BlockBehaviour.Properties> legacyCopyOf(Block block) {
        return legacyCopyOf(() -> block);
    }

    public static Supplier<BlockBehaviour.Properties> legacyCopyOf(Block block, UnaryOperator<BlockBehaviour.Properties> operator) {
        return legacyCopyOf(() -> block, operator);
    }

    public static Supplier<BlockBehaviour.Properties> legacyCopyOf(Supplier<Block> block) {
        return legacyCopyOf(block, p -> p);
    }

    public static Supplier<BlockBehaviour.Properties> legacyCopyOf(Supplier<Block> block, UnaryOperator<BlockBehaviour.Properties> operator) {
        return () -> operator.apply(Properties.ofLegacyCopy(block.get()));
    }

    public static Supplier<BlockBehaviour.Properties> fullCopyOf(Block block) {
        return fullCopyOf(() -> block);
    }

    public static Supplier<BlockBehaviour.Properties> fullCopyOf(Block block, UnaryOperator<BlockBehaviour.Properties> operator) {
        return fullCopyOf(() -> block, operator);
    }

    public static Supplier<BlockBehaviour.Properties> fullCopyOf(Supplier<Block> block) {
        return fullCopyOf(block, p -> p);
    }

    public static Supplier<BlockBehaviour.Properties> fullCopyOf(Supplier<Block> block, UnaryOperator<BlockBehaviour.Properties> operator) {
        return () -> operator.apply(BlockBehaviour.Properties.ofFullCopy(block.get()));
    }

    private static Supplier<BlockBehaviour.Properties> wallVariant(Supplier<Block> block, UnaryOperator<BlockBehaviour.Properties> operator) {
        return () -> operator.apply(BlockBehaviour.Properties.of().dropsLike(block.get()));
    }

    private static Supplier<Block> register(boolean hasItem, String string, Function<BlockBehaviour.Properties, Block> function, BlockBehaviour.Properties properties) {
        return register(hasItem, string, function, () -> properties);
    }

    private static Supplier<Block> register(boolean hasItem, String string, Function<BlockBehaviour.Properties, Block> function, Supplier<BlockBehaviour.Properties> properties) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, Enderscape.id(string));
        var block = RegistryHelper.register(BuiltInRegistries.BLOCK, key, () -> function.apply(properties.get()));
        if (hasItem) EnderscapeItems.registerBlock(block, key);
        return block;
    }

    private static WoodType registerWoodType(ResourceLocation location, BlockSetType setType, SoundType soundType, SoundType hangingSignSoundType, SoundEvent fenceGateClose, SoundEvent fenceGateOpen) {
        var woodType = new WoodType(location.toString(), setType, soundType, hangingSignSoundType, fenceGateClose, fenceGateOpen);
        WoodType.register(woodType);
        return woodType;
    }

    private static boolean always(BlockState state, BlockGetter world, BlockPos pos) {
        return true;
    }

    private static Boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return false;
    }

    private static Function<BlockState, MapColor> axis(MapColor side, MapColor top) {
        return state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? top : side;
    }

    public static final Supplier<BlockStateProvider> VEILED_OVERGROWTH_BONEMEAL_PROVIDER = Suppliers.memoize(() -> new WeightedStateProvider(new SimpleWeightedRandomList.Builder<BlockState>().add(EnderscapeBlocks.WISP_SPROUTS.get().defaultBlockState(), 3).add(EnderscapeBlocks.WISP_GROWTH.get().defaultBlockState(), 1)));
    public static final Supplier<BlockStateProvider> CELESTIAL_OVERGROWTH_BONEMEAL_PROVIDER = Suppliers.memoize(() -> new WeightedStateProvider(new SimpleWeightedRandomList.Builder<BlockState>().add(EnderscapeBlocks.CELESTIAL_CHANTERELLE.get().defaultBlockState(), 3).add(EnderscapeBlocks.BULB_FLOWER.get().defaultBlockState(), 1)));
    public static final Supplier<BlockStateProvider> CORRUPT_OVERGROWTH_BONEMEAL_PROVIDER = Suppliers.memoize(() -> new WeightedStateProvider(new SimpleWeightedRandomList.Builder<BlockState>().add(EnderscapeBlocks.MURUBLIGHT_CHANTERELLE.get().defaultBlockState(), 3)));
}