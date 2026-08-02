package net.penumbra.enderscape.block;

import com.google.common.collect.Maps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;

import java.util.Map;
import java.util.stream.Stream;

public class EnderscapeBlockFamilies {
    private static final Map<Block, BlockFamily> MAP = Maps.newHashMap();

    public static final BlockFamily VEILED_PLANKS = build(EnderscapeBlocks.VEILED_PLANKS)
            .log(EnderscapeBlocks.VEILED_LOG)
            .strippedLog(EnderscapeBlocks.STRIPPED_VEILED_LOG)
            .button(EnderscapeBlocks.VEILED_BUTTON)
            .fence(EnderscapeBlocks.VEILED_FENCE)
            .fenceGate(EnderscapeBlocks.VEILED_FENCE_GATE)
            .pressurePlate(EnderscapeBlocks.VEILED_PRESSURE_PLATE)
            .sign(EnderscapeBlocks.VEILED_SIGN, EnderscapeBlocks.VEILED_WALL_SIGN)
            .hangingSign(EnderscapeBlocks.VEILED_HANGING_SIGN, EnderscapeBlocks.VEILED_WALL_HANGING_SIGN)
            .slab(EnderscapeBlocks.VEILED_SLAB)
            .stairs(EnderscapeBlocks.VEILED_STAIRS)
            .door(EnderscapeBlocks.VEILED_DOOR)
            .trapdoor(EnderscapeBlocks.VEILED_TRAPDOOR)
            .recipeGroupPrefix("wooden")
            .recipeUnlockedBy("has_planks")
            .getFamily();

    public static final BlockFamily CELESTIAL_PLANKS = build(EnderscapeBlocks.CELESTIAL_PLANKS)
            .log(EnderscapeBlocks.CELESTIAL_STEM)
            .strippedLog(EnderscapeBlocks.STRIPPED_CELESTIAL_STEM)
            .button(EnderscapeBlocks.CELESTIAL_BUTTON)
            .fence(EnderscapeBlocks.CELESTIAL_FENCE)
            .fenceGate(EnderscapeBlocks.CELESTIAL_FENCE_GATE)
            .pressurePlate(EnderscapeBlocks.CELESTIAL_PRESSURE_PLATE)
            .sign(EnderscapeBlocks.CELESTIAL_SIGN, EnderscapeBlocks.CELESTIAL_WALL_SIGN)
            .hangingSign(EnderscapeBlocks.CELESTIAL_HANGING_SIGN, EnderscapeBlocks.CELESTIAL_WALL_HANGING_SIGN)
            .slab(EnderscapeBlocks.CELESTIAL_SLAB)
            .stairs(EnderscapeBlocks.CELESTIAL_STAIRS)
            .door(EnderscapeBlocks.CELESTIAL_DOOR)
            .trapdoor(EnderscapeBlocks.CELESTIAL_TRAPDOOR)
            .recipeGroupPrefix("wooden")
            .recipeUnlockedBy("has_planks")
            .getFamily();

    public static final BlockFamily MURUBLIGHT_PLANKS = build(EnderscapeBlocks.MURUBLIGHT_PLANKS)
            .log(EnderscapeBlocks.MURUBLIGHT_STEM)
            .strippedLog(EnderscapeBlocks.STRIPPED_MURUBLIGHT_STEM)
            .button(EnderscapeBlocks.MURUBLIGHT_BUTTON)
            .fence(EnderscapeBlocks.MURUBLIGHT_FENCE)
            .fenceGate(EnderscapeBlocks.MURUBLIGHT_FENCE_GATE)
            .pressurePlate(EnderscapeBlocks.MURUBLIGHT_PRESSURE_PLATE)
            .sign(EnderscapeBlocks.MURUBLIGHT_SIGN, EnderscapeBlocks.MURUBLIGHT_WALL_SIGN)
            .hangingSign(EnderscapeBlocks.MURUBLIGHT_HANGING_SIGN, EnderscapeBlocks.MURUBLIGHT_WALL_HANGING_SIGN)
            .slab(EnderscapeBlocks.MURUBLIGHT_SLAB)
            .stairs(EnderscapeBlocks.MURUBLIGHT_STAIRS)
            .door(EnderscapeBlocks.MURUBLIGHT_DOOR)
            .trapdoor(EnderscapeBlocks.MURUBLIGHT_TRAPDOOR)
            .recipeGroupPrefix("wooden")
            .recipeUnlockedBy("has_planks")
            .getFamily();

    public static final BlockFamily END_STONE = build(Blocks.END_STONE)
            .wall(EnderscapeBlocks.END_STONE_WALL)
            .stairs(EnderscapeBlocks.END_STONE_STAIRS)
            .slab(EnderscapeBlocks.END_STONE_SLAB)
            .polished(EnderscapeBlocks.POLISHED_END_STONE)
            .getFamily();

    public static final BlockFamily POLISHED_END_STONE = build(EnderscapeBlocks.POLISHED_END_STONE)
            .wall(EnderscapeBlocks.POLISHED_END_STONE_WALL)
            .pressurePlate(EnderscapeBlocks.POLISHED_END_STONE_PRESSURE_PLATE)
            .button(EnderscapeBlocks.POLISHED_END_STONE_BUTTON)
            .stairs(EnderscapeBlocks.POLISHED_END_STONE_STAIRS)
            .slab(EnderscapeBlocks.POLISHED_END_STONE_SLAB)
            .polished(Blocks.END_STONE_BRICKS)
            .chiseled(EnderscapeBlocks.CHISELED_END_STONE)
            .getFamily();

    public static final BlockFamily END_STONE_BRICKS = build(Blocks.END_STONE_BRICKS)
            .cracked(EnderscapeBlocks.CRACKED_END_STONE_BRICKS)
            .getFamily();

    public static final BlockFamily OVERGROWN_END_STONE_BRICKS = build(EnderscapeBlocks.OVERGROWN_END_STONE_BRICKS)
            .wall(EnderscapeBlocks.OVERGROWN_END_STONE_BRICK_WALL)
            .stairs(EnderscapeBlocks.OVERGROWN_END_STONE_BRICK_STAIRS)
            .slab(EnderscapeBlocks.OVERGROWN_END_STONE_BRICK_SLAB)
            .dontGenerateModel()
            .getFamily();

    public static final BlockFamily MIRESTONE = build(EnderscapeBlocks.MIRESTONE)
            .wall(EnderscapeBlocks.MIRESTONE_WALL)
            .stairs(EnderscapeBlocks.MIRESTONE_STAIRS)
            .slab(EnderscapeBlocks.MIRESTONE_SLAB)
            .polished(EnderscapeBlocks.POLISHED_MIRESTONE)
            .getFamily();

    public static final BlockFamily POLISHED_MIRESTONE = build(EnderscapeBlocks.POLISHED_MIRESTONE)
            .wall(EnderscapeBlocks.POLISHED_MIRESTONE_WALL)
            .pressurePlate(EnderscapeBlocks.POLISHED_MIRESTONE_PRESSURE_PLATE)
            .button(EnderscapeBlocks.POLISHED_MIRESTONE_BUTTON)
            .stairs(EnderscapeBlocks.POLISHED_MIRESTONE_STAIRS)
            .slab(EnderscapeBlocks.POLISHED_MIRESTONE_SLAB)
            .polished(EnderscapeBlocks.MIRESTONE_BRICKS)
            .chiseled(EnderscapeBlocks.CHISELED_MIRESTONE)
            .getFamily();

    public static final BlockFamily MIRESTONE_BRICKS = build(EnderscapeBlocks.MIRESTONE_BRICKS)
            .cracked(EnderscapeBlocks.CRACKED_MIRESTONE_BRICKS)
            .wall(EnderscapeBlocks.MIRESTONE_BRICK_WALL)
            .stairs(EnderscapeBlocks.MIRESTONE_BRICK_STAIRS)
            .slab(EnderscapeBlocks.MIRESTONE_BRICK_SLAB)
            .getFamily();

    public static final BlockFamily OVERGROWN_MIRESTONE_BRICKS = build(EnderscapeBlocks.OVERGROWN_MIRESTONE_BRICKS)
            .wall(EnderscapeBlocks.OVERGROWN_MIRESTONE_BRICK_WALL)
            .stairs(EnderscapeBlocks.OVERGROWN_MIRESTONE_BRICK_STAIRS)
            .slab(EnderscapeBlocks.OVERGROWN_MIRESTONE_BRICK_SLAB)
            .dontGenerateModel()
            .getFamily();

    public static final BlockFamily VERADITE = build(EnderscapeBlocks.VERADITE)
            .wall(EnderscapeBlocks.VERADITE_WALL)
            .stairs(EnderscapeBlocks.VERADITE_STAIRS)
            .slab(EnderscapeBlocks.VERADITE_SLAB)
            .polished(EnderscapeBlocks.POLISHED_VERADITE)
            .getFamily();

    public static final BlockFamily POLISHED_VERADITE = build(EnderscapeBlocks.POLISHED_VERADITE)
            .wall(EnderscapeBlocks.POLISHED_VERADITE_WALL)
            .pressurePlate(EnderscapeBlocks.POLISHED_VERADITE_PRESSURE_PLATE)
            .button(EnderscapeBlocks.POLISHED_VERADITE_BUTTON)
            .stairs(EnderscapeBlocks.POLISHED_VERADITE_STAIRS)
            .slab(EnderscapeBlocks.POLISHED_VERADITE_SLAB)
            .polished(EnderscapeBlocks.VERADITE_BRICKS)
            .chiseled(EnderscapeBlocks.CHISELED_VERADITE)
            .getFamily();

    public static final BlockFamily VERADITE_BRICKS = build(EnderscapeBlocks.VERADITE_BRICKS)
            .wall(EnderscapeBlocks.VERADITE_BRICK_WALL)
            .stairs(EnderscapeBlocks.VERADITE_BRICK_STAIRS)
            .slab(EnderscapeBlocks.VERADITE_BRICK_SLAB)
            .getFamily();

    public static final BlockFamily KURODITE = build(EnderscapeBlocks.KURODITE)
            .wall(EnderscapeBlocks.KURODITE_WALL)
            .stairs(EnderscapeBlocks.KURODITE_STAIRS)
            .slab(EnderscapeBlocks.KURODITE_SLAB)
            .polished(EnderscapeBlocks.POLISHED_KURODITE)
            .getFamily();

    public static final BlockFamily POLISHED_KURODITE = build(EnderscapeBlocks.POLISHED_KURODITE)
            .wall(EnderscapeBlocks.POLISHED_KURODITE_WALL)
            .pressurePlate(EnderscapeBlocks.POLISHED_KURODITE_PRESSURE_PLATE)
            .button(EnderscapeBlocks.POLISHED_KURODITE_BUTTON)
            .stairs(EnderscapeBlocks.POLISHED_KURODITE_STAIRS)
            .slab(EnderscapeBlocks.POLISHED_KURODITE_SLAB)
            .polished(EnderscapeBlocks.KURODITE_BRICKS)
            .chiseled(EnderscapeBlocks.CHISELED_KURODITE)
            .getFamily();

    public static final BlockFamily KURODITE_BRICKS = build(EnderscapeBlocks.KURODITE_BRICKS)
            .wall(EnderscapeBlocks.KURODITE_BRICK_WALL)
            .stairs(EnderscapeBlocks.KURODITE_BRICK_STAIRS)
            .slab(EnderscapeBlocks.KURODITE_BRICK_SLAB)
            .getFamily();

    public static final BlockFamily PURPUR_BLOCK = build(Blocks.PURPUR_BLOCK)
            .wall(EnderscapeBlocks.PURPUR_WALL)
            .stairs(Blocks.PURPUR_STAIRS)
            .slab(Blocks.PURPUR_SLAB)
            .chiseled(EnderscapeBlocks.CHISELED_PURPUR)
            .getFamily();

    public static final BlockFamily PURPUR_TILES = build(EnderscapeBlocks.PURPUR_TILES)
            .stairs(EnderscapeBlocks.PURPUR_TILE_STAIRS)
            .slab(EnderscapeBlocks.PURPUR_TILE_SLAB)
            .dontGenerateModel()
            .getFamily();

    public static final BlockFamily DUSK_PURPUR_BLOCK = build(EnderscapeBlocks.DUSK_PURPUR_BLOCK)
            .wall(EnderscapeBlocks.DUSK_PURPUR_WALL)
            .stairs(EnderscapeBlocks.DUSK_PURPUR_STAIRS)
            .slab(EnderscapeBlocks.DUSK_PURPUR_SLAB)
            .chiseled(EnderscapeBlocks.CHISELED_DUSK_PURPUR)
            .getFamily();

    public static final BlockFamily SHADOLINE_BLOCK = build(EnderscapeBlocks.SHADOLINE_BLOCK)
            .wall(EnderscapeBlocks.SHADOLINE_BLOCK_WALL)
            .stairs(EnderscapeBlocks.SHADOLINE_BLOCK_STAIRS)
            .slab(EnderscapeBlocks.SHADOLINE_BLOCK_SLAB)
            .polished(EnderscapeBlocks.CUT_SHADOLINE)
            .getFamily();

    public static final BlockFamily CUT_SHADOLINE = build(EnderscapeBlocks.CUT_SHADOLINE)
            .wall(EnderscapeBlocks.CUT_SHADOLINE_WALL)
            .stairs(EnderscapeBlocks.CUT_SHADOLINE_STAIRS)
            .slab(EnderscapeBlocks.CUT_SHADOLINE_SLAB)
            .chiseled(EnderscapeBlocks.CHISELED_SHADOLINE)
            .getFamily();

    public static final BlockFamily ETCHED_ALLURING_MAGNIA = build(EnderscapeBlocks.ETCHED_ALLURING_MAGNIA)
            .wall(EnderscapeBlocks.ETCHED_ALLURING_MAGNIA_WALL)
            .stairs(EnderscapeBlocks.ETCHED_ALLURING_MAGNIA_STAIRS)
            .slab(EnderscapeBlocks.ETCHED_ALLURING_MAGNIA_SLAB)
            .getFamily();

    public static final BlockFamily ETCHED_REPULSIVE_MAGNIA = build(EnderscapeBlocks.ETCHED_REPULSIVE_MAGNIA)
            .wall(EnderscapeBlocks.ETCHED_REPULSIVE_MAGNIA_WALL)
            .stairs(EnderscapeBlocks.ETCHED_REPULSIVE_MAGNIA_STAIRS)
            .slab(EnderscapeBlocks.ETCHED_REPULSIVE_MAGNIA_SLAB)
            .getFamily();

    public static final BlockFamily CELESTIAL_BRICKS = build(EnderscapeBlocks.CELESTIAL_BRICKS)
            .wall(EnderscapeBlocks.CELESTIAL_BRICK_WALL)
            .stairs(EnderscapeBlocks.CELESTIAL_BRICK_STAIRS)
            .slab(EnderscapeBlocks.CELESTIAL_BRICK_SLAB)
            .getFamily();

    public static final BlockFamily MURUBLIGHT_BRICKS = build(EnderscapeBlocks.MURUBLIGHT_BRICKS)
            .wall(EnderscapeBlocks.MURUBLIGHT_BRICK_WALL)
            .stairs(EnderscapeBlocks.MURUBLIGHT_BRICK_STAIRS)
            .slab(EnderscapeBlocks.MURUBLIGHT_BRICK_SLAB)
            .getFamily();

    private static BlockFamily.Builder build(Block block) {
        BlockFamily.Builder builder = new BlockFamily.Builder(block);
        BlockFamily family = MAP.put(block, builder.getFamily());
        if (family != null) {
            throw new IllegalStateException("Duplicate family definition for " + BuiltInRegistries.BLOCK.getKey(block));
        } else {
            return builder;
        }
    }

    public static Stream<BlockFamily> getAllFamilies() {
        return MAP.values().stream();
    }
}
