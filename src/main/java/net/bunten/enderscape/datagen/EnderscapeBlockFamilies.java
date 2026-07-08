package net.bunten.enderscape.datagen;

import com.google.common.collect.Maps;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Map;
import java.util.stream.Stream;

public class EnderscapeBlockFamilies {
    private static final Map<Block, BlockFamily> MAP = Maps.newHashMap();

    public static final BlockFamily VEILED_PLANKS = build(EnderscapeBlocks.VEILED_PLANKS.get())
            .button(EnderscapeBlocks.VEILED_BUTTON.get())
            .fence(EnderscapeBlocks.VEILED_FENCE.get())
            .fenceGate(EnderscapeBlocks.VEILED_FENCE_GATE.get())
            .pressurePlate(EnderscapeBlocks.VEILED_PRESSURE_PLATE.get())
            .sign(EnderscapeBlocks.VEILED_SIGN.get(), EnderscapeBlocks.VEILED_WALL_SIGN.get())
            .slab(EnderscapeBlocks.VEILED_SLAB.get())
            .stairs(EnderscapeBlocks.VEILED_STAIRS.get())
            .door(EnderscapeBlocks.VEILED_DOOR.get())
            .trapdoor(EnderscapeBlocks.VEILED_TRAPDOOR.get())
            .recipeGroupPrefix("wooden")
            .recipeUnlockedBy("has_planks")
            .getFamily();

    public static final BlockFamily CELESTIAL_PLANKS = build(EnderscapeBlocks.CELESTIAL_PLANKS.get())
            .button(EnderscapeBlocks.CELESTIAL_BUTTON.get())
            .fence(EnderscapeBlocks.CELESTIAL_FENCE.get())
            .fenceGate(EnderscapeBlocks.CELESTIAL_FENCE_GATE.get())
            .pressurePlate(EnderscapeBlocks.CELESTIAL_PRESSURE_PLATE.get())
            .sign(EnderscapeBlocks.CELESTIAL_SIGN.get(), EnderscapeBlocks.CELESTIAL_WALL_SIGN.get())
            .slab(EnderscapeBlocks.CELESTIAL_SLAB.get())
            .stairs(EnderscapeBlocks.CELESTIAL_STAIRS.get())
            .door(EnderscapeBlocks.CELESTIAL_DOOR.get())
            .trapdoor(EnderscapeBlocks.CELESTIAL_TRAPDOOR.get())
            .recipeGroupPrefix("wooden")
            .recipeUnlockedBy("has_planks")
            .getFamily();

    public static final BlockFamily MURUBLIGHT_PLANKS = build(EnderscapeBlocks.MURUBLIGHT_PLANKS.get())
            .button(EnderscapeBlocks.MURUBLIGHT_BUTTON.get())
            .fence(EnderscapeBlocks.MURUBLIGHT_FENCE.get())
            .fenceGate(EnderscapeBlocks.MURUBLIGHT_FENCE_GATE.get())
            .pressurePlate(EnderscapeBlocks.MURUBLIGHT_PRESSURE_PLATE.get())
            .sign(EnderscapeBlocks.MURUBLIGHT_SIGN.get(), EnderscapeBlocks.MURUBLIGHT_WALL_SIGN.get())
            .slab(EnderscapeBlocks.MURUBLIGHT_SLAB.get())
            .stairs(EnderscapeBlocks.MURUBLIGHT_STAIRS.get())
            .door(EnderscapeBlocks.MURUBLIGHT_DOOR.get())
            .trapdoor(EnderscapeBlocks.MURUBLIGHT_TRAPDOOR.get())
            .recipeGroupPrefix("wooden")
            .recipeUnlockedBy("has_planks")
            .getFamily();

    public static final BlockFamily VERADITE = build(EnderscapeBlocks.VERADITE.get())
            .wall(EnderscapeBlocks.VERADITE_WALL.get())
            .stairs(EnderscapeBlocks.VERADITE_STAIRS.get())
            .slab(EnderscapeBlocks.VERADITE_SLAB.get())
            .polished(EnderscapeBlocks.POLISHED_VERADITE.get())
            .getFamily();

    public static final BlockFamily POLISHED_VERADITE = build(EnderscapeBlocks.POLISHED_VERADITE.get())
            .wall(EnderscapeBlocks.POLISHED_VERADITE_WALL.get())
            .pressurePlate(EnderscapeBlocks.POLISHED_VERADITE_PRESSURE_PLATE.get())
            .button(EnderscapeBlocks.POLISHED_VERADITE_BUTTON.get())
            .stairs(EnderscapeBlocks.POLISHED_VERADITE_STAIRS.get())
            .slab(EnderscapeBlocks.POLISHED_VERADITE_SLAB.get())
            .polished(EnderscapeBlocks.VERADITE_BRICKS.get())
            .chiseled(EnderscapeBlocks.CHISELED_VERADITE.get())
            .getFamily();

    public static final BlockFamily POLISHED_VERADITE_BRICKS = build(EnderscapeBlocks.VERADITE_BRICKS.get())
            .wall(EnderscapeBlocks.VERADITE_BRICK_WALL.get())
            .stairs(EnderscapeBlocks.VERADITE_BRICK_STAIRS.get())
            .slab(EnderscapeBlocks.VERADITE_BRICK_SLAB.get())
            .getFamily();

    public static final BlockFamily KURODITE = build(EnderscapeBlocks.KURODITE.get())
            .wall(EnderscapeBlocks.KURODITE_WALL.get())
            .stairs(EnderscapeBlocks.KURODITE_STAIRS.get())
            .slab(EnderscapeBlocks.KURODITE_SLAB.get())
            .polished(EnderscapeBlocks.POLISHED_KURODITE.get())
            .getFamily();

    public static final BlockFamily POLISHED_KURODITE = build(EnderscapeBlocks.POLISHED_KURODITE.get())
            .wall(EnderscapeBlocks.POLISHED_KURODITE_WALL.get())
            .pressurePlate(EnderscapeBlocks.POLISHED_KURODITE_PRESSURE_PLATE.get())
            .button(EnderscapeBlocks.POLISHED_KURODITE_BUTTON.get())
            .stairs(EnderscapeBlocks.POLISHED_KURODITE_STAIRS.get())
            .slab(EnderscapeBlocks.POLISHED_KURODITE_SLAB.get())
            .polished(EnderscapeBlocks.KURODITE_BRICKS.get())
            .chiseled(EnderscapeBlocks.CHISELED_KURODITE.get())
            .getFamily();

    public static final BlockFamily POLISHED_KURODITE_BRICKS = build(EnderscapeBlocks.KURODITE_BRICKS.get())
            .wall(EnderscapeBlocks.KURODITE_BRICK_WALL.get())
            .stairs(EnderscapeBlocks.KURODITE_BRICK_STAIRS.get())
            .slab(EnderscapeBlocks.KURODITE_BRICK_SLAB.get())
            .getFamily();

    public static final BlockFamily MIRESTONE = build(EnderscapeBlocks.MIRESTONE.get())
            .wall(EnderscapeBlocks.MIRESTONE_WALL.get())
            .stairs(EnderscapeBlocks.MIRESTONE_STAIRS.get())
            .slab(EnderscapeBlocks.MIRESTONE_SLAB.get())
            .polished(EnderscapeBlocks.POLISHED_MIRESTONE.get())
            .getFamily();

    public static final BlockFamily POLISHED_MIRESTONE = build(EnderscapeBlocks.POLISHED_MIRESTONE.get())
            .wall(EnderscapeBlocks.POLISHED_MIRESTONE_WALL.get())
            .pressurePlate(EnderscapeBlocks.POLISHED_MIRESTONE_PRESSURE_PLATE.get())
            .button(EnderscapeBlocks.POLISHED_MIRESTONE_BUTTON.get())
            .stairs(EnderscapeBlocks.POLISHED_MIRESTONE_STAIRS.get())
            .slab(EnderscapeBlocks.POLISHED_MIRESTONE_SLAB.get())
            .polished(EnderscapeBlocks.MIRESTONE_BRICKS.get())
            .chiseled(EnderscapeBlocks.CHISELED_MIRESTONE.get())
            .getFamily();

    public static final BlockFamily END_STONE = build(Blocks.END_STONE)
            .wall(EnderscapeBlocks.END_STONE_WALL.get())
            .stairs(EnderscapeBlocks.END_STONE_STAIRS.get())
            .slab(EnderscapeBlocks.END_STONE_SLAB.get())
            .polished(EnderscapeBlocks.POLISHED_END_STONE.get())
            .getFamily();

    public static final BlockFamily POLISHED_END_STONE = build(EnderscapeBlocks.POLISHED_END_STONE.get())
            .wall(EnderscapeBlocks.POLISHED_END_STONE_WALL.get())
            .pressurePlate(EnderscapeBlocks.POLISHED_END_STONE_PRESSURE_PLATE.get())
            .button(EnderscapeBlocks.POLISHED_END_STONE_BUTTON.get())
            .stairs(EnderscapeBlocks.POLISHED_END_STONE_STAIRS.get())
            .slab(EnderscapeBlocks.POLISHED_END_STONE_SLAB.get())
            .polished(Blocks.END_STONE_BRICKS)
            .chiseled(EnderscapeBlocks.CHISELED_END_STONE.get())
            .getFamily();

    public static final BlockFamily PURPUR = build(Blocks.PURPUR_BLOCK)
            .wall(EnderscapeBlocks.PURPUR_WALL.get())
            .stairs(Blocks.PURPUR_STAIRS)
            .slab(Blocks.PURPUR_SLAB)
            .chiseled(EnderscapeBlocks.CHISELED_PURPUR.get())
            .getFamily();

    public static final BlockFamily DUSK_PURPUR = build(EnderscapeBlocks.DUSK_PURPUR_BLOCK.get())
            .wall(EnderscapeBlocks.DUSK_PURPUR_WALL.get())
            .stairs(EnderscapeBlocks.DUSK_PURPUR_STAIRS.get())
            .slab(EnderscapeBlocks.DUSK_PURPUR_SLAB.get())
            .chiseled(EnderscapeBlocks.CHISELED_DUSK_PURPUR.get())
            .getFamily();

    public static final BlockFamily PURPUR_TILES = build(EnderscapeBlocks.PURPUR_TILES.get())
            .stairs(EnderscapeBlocks.PURPUR_TILE_STAIRS.get())
            .slab(EnderscapeBlocks.PURPUR_TILE_SLAB.get())
            .getFamily();

    public static final BlockFamily POLISHED_MIRESTONE_BRICKS = build(EnderscapeBlocks.MIRESTONE_BRICKS.get())
            .wall(EnderscapeBlocks.MIRESTONE_BRICK_WALL.get())
            .stairs(EnderscapeBlocks.MIRESTONE_BRICK_STAIRS.get())
            .slab(EnderscapeBlocks.MIRESTONE_BRICK_SLAB.get())
            .getFamily();

    public static final BlockFamily ETCHED_ALLURING_MAGNIA = build(EnderscapeBlocks.ETCHED_ALLURING_MAGNIA.get())
            .wall(EnderscapeBlocks.ETCHED_ALLURING_MAGNIA_WALL.get())
            .stairs(EnderscapeBlocks.ETCHED_ALLURING_MAGNIA_STAIRS.get())
            .slab(EnderscapeBlocks.ETCHED_ALLURING_MAGNIA_SLAB.get())
            .getFamily();

    public static final BlockFamily ETCHED_REPULSIVE_MAGNIA = build(EnderscapeBlocks.ETCHED_REPULSIVE_MAGNIA.get())
            .wall(EnderscapeBlocks.ETCHED_REPULSIVE_MAGNIA_WALL.get())
            .stairs(EnderscapeBlocks.ETCHED_REPULSIVE_MAGNIA_STAIRS.get())
            .slab(EnderscapeBlocks.ETCHED_REPULSIVE_MAGNIA_SLAB.get())
            .getFamily();

    public static final BlockFamily SHADOLINE_BLOCK = build(EnderscapeBlocks.SHADOLINE_BLOCK.get())
            .wall(EnderscapeBlocks.SHADOLINE_BLOCK_WALL.get())
            .stairs(EnderscapeBlocks.SHADOLINE_BLOCK_STAIRS.get())
            .slab(EnderscapeBlocks.SHADOLINE_BLOCK_SLAB.get())
            .polished(EnderscapeBlocks.CUT_SHADOLINE.get())
            .getFamily();

    public static final BlockFamily CUT_SHADOLINE = build(EnderscapeBlocks.CUT_SHADOLINE.get())
            .wall(EnderscapeBlocks.CUT_SHADOLINE_WALL.get())
            .stairs(EnderscapeBlocks.CUT_SHADOLINE_STAIRS.get())
            .slab(EnderscapeBlocks.CUT_SHADOLINE_SLAB.get())
            .chiseled(EnderscapeBlocks.CHISELED_SHADOLINE.get())
            .getFamily();

    public static final BlockFamily CELESTIAL_BRICKS = build(EnderscapeBlocks.CELESTIAL_BRICKS.get())
            .wall(EnderscapeBlocks.CELESTIAL_BRICK_WALL.get())
            .stairs(EnderscapeBlocks.CELESTIAL_BRICK_STAIRS.get())
            .slab(EnderscapeBlocks.CELESTIAL_BRICK_SLAB.get())
            .getFamily();

    public static final BlockFamily MURUBLIGHT_BRICKS = build(EnderscapeBlocks.MURUBLIGHT_BRICKS.get())
            .wall(EnderscapeBlocks.MURUBLIGHT_BRICK_WALL.get())
            .stairs(EnderscapeBlocks.MURUBLIGHT_BRICK_STAIRS.get())
            .slab(EnderscapeBlocks.MURUBLIGHT_BRICK_SLAB.get())
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
