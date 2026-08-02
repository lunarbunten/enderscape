package net.penumbra.enderscape.references;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.penumbra.enderscape.Enderscape;

public class EnderscapeBlockIds {
    public static final ResourceKey<Block> VOID_LACHRYMA = of("void_lachryma");
    public static final ResourceKey<Block> VOID_LACHRYMA_CAULDRON = of("void_lachryma_cauldron");
    public static final ResourceKey<Block> VOID_FIRE = of("void_fire");
    public static final ResourceKey<Block> VOID_TORCH = of("void_torch");
    public static final ResourceKey<Block> VOID_WALL_TORCH = of("void_wall_torch");

    public static final ResourceKey<Block> CHORUS_CAKE_ROLL = of("chorus_cake_roll");

    public static final ResourceKey<Block> VEILED_WALL_SIGN = of("veiled_wall_sign");
    public static final ResourceKey<Block> VEILED_WALL_HANGING_SIGN = of("veiled_wall_hanging_sign");


    public static final ResourceKey<Block> PURUBERRY_VINE = of("puruberry_vine");

    public static final ResourceKey<Block> CELESTIAL_WALL_SIGN = of("celestial_wall_sign");
    public static final ResourceKey<Block> CELESTIAL_WALL_HANGING_SIGN = of("celestial_wall_hanging_sign");


    public static final ResourceKey<Block> BLINKLIGHT_VINES_BODY = of("blinklight_vines_body");
    public static final ResourceKey<Block> BLINKLIGHT_VINES_HEAD = of("blinklight_vines_head");

    public static final ResourceKey<Block> MURUBLIGHT_BRACKET = of("murublight_bracket");
    public static final ResourceKey<Block> MURUBLIGHT_WALL_SIGN = of("murublight_wall_sign");
    public static final ResourceKey<Block> MURUBLIGHT_WALL_HANGING_SIGN = of("murublight_wall_hanging_sign");

    public static final ResourceKey<Block> POTTED_ALLURING_MAGNIA_SPROUT = of("potted_alluring_magnia_sprout");
    public static final ResourceKey<Block> POTTED_BLINKLIGHT = of("potted_blinklight");
    public static final ResourceKey<Block> POTTED_BULB_FLOWER = of("potted_bulb_flower");
    public static final ResourceKey<Block> POTTED_CELESTIAL_CHANTERELLE = of("potted_celestial_chanterelle");
    public static final ResourceKey<Block> POTTED_CELESTIAL_GROWTH = of("potted_celestial_growth");
    public static final ResourceKey<Block> POTTED_CHORUS_SPROUTS = of("potted_chorus_sprouts");
    public static final ResourceKey<Block> POTTED_CORRUPT_GROWTH = of("potted_corrupt_growth");
    public static final ResourceKey<Block> POTTED_DRY_END_GROWTH = of("potted_dry_end_growth");
    public static final ResourceKey<Block> POTTED_MURUBLIGHT_CHANTERELLE = of("potted_murublight_chanterelle");
    public static final ResourceKey<Block> POTTED_REPULSIVE_MAGNIA_SPROUT = of("potted_repulsive_magnia_sprout");
    public static final ResourceKey<Block> POTTED_VEILED_SAPLING = of("potted_veiled_sapling");
    public static final ResourceKey<Block> POTTED_WISP_GROWTH = of("potted_wisp_growth");

    private static ResourceKey<Block> of(String string) {
        return ResourceKey.create(Registries.BLOCK, Enderscape.id(string));
    }
}