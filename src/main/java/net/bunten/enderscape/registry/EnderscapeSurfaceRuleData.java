package net.bunten.enderscape.registry;

import net.bunten.enderscape.block.properties.StateProperties;
import net.minecraft.core.Direction;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.placement.CaveSurface;

import static net.bunten.enderscape.registry.EnderscapeBiomes.*;
import static net.minecraft.world.level.levelgen.SurfaceRules.*;

public class EnderscapeSurfaceRuleData {

    private static final RuleSource ALLURING_MAGNIA = state(EnderscapeBlocks.ALLURING_MAGNIA.defaultBlockState());
    private static final RuleSource REPULSIVE_MAGNIA = state(EnderscapeBlocks.REPULSIVE_MAGNIA.defaultBlockState());
    private static final RuleSource CELESTIAL_OVERGROWTH = state(EnderscapeBlocks.CELESTIAL_OVERGROWTH.defaultBlockState());
    private static final RuleSource CORRUPT_OVERGROWTH_FACING_DOWN = state(EnderscapeBlocks.CORRUPT_OVERGROWTH.defaultBlockState().setValue(StateProperties.FACING, Direction.DOWN));
    private static final RuleSource CORRUPT_OVERGROWTH_FACING_UP = state(EnderscapeBlocks.CORRUPT_OVERGROWTH.defaultBlockState().setValue(StateProperties.FACING, Direction.UP));
    private static final RuleSource MIRESTONE = state(EnderscapeBlocks.MIRESTONE.defaultBlockState());
    private static final RuleSource VEILED_END_STONE = state(EnderscapeBlocks.VEILED_END_STONE.defaultBlockState());

    public static final double WIDE_MAGNIA_SURFACE_NOISE_MAXIMUM = 0.075;
    public static final double THIN_MAGNIA_SURFACE_NOISE_MAXIMUM = 0.02;

    public static RuleSource makeRules() {
        return sequence(
                ifTrue(
                        stoneDepthCheck(0, true, 4, CaveSurface.CEILING),
                        sequence(
                                sequence(
                                        ifTrue(
                                                isBiome(CORRUPT_BARRENS),
                                                ifTrue(
                                                        ON_CEILING,
                                                        ifTrue(
                                                                noiseCondition(EnderscapeNoiseParameters.CORRUPTION_CEILING, -1.0D, 0.0D),
                                                                CORRUPT_OVERGROWTH_FACING_DOWN
                                                        )
                                                )
                                        ),
                                        ifTrue(
                                                isBiome(MAGNIA_CRAGS),
                                                sequence(
                                                        ifTrue(
                                                                noiseCondition(Noises.SURFACE, -WIDE_MAGNIA_SURFACE_NOISE_MAXIMUM, WIDE_MAGNIA_SURFACE_NOISE_MAXIMUM),
                                                                REPULSIVE_MAGNIA
                                                        ),
                                                        ifTrue(
                                                                noiseCondition(Noises.CALCITE, -THIN_MAGNIA_SURFACE_NOISE_MAXIMUM, THIN_MAGNIA_SURFACE_NOISE_MAXIMUM),
                                                                REPULSIVE_MAGNIA
                                                        )
                                                )
                                        )
                                ),
                                MIRESTONE
                        )
                ),
                ifTrue(
                        ON_FLOOR,
                        sequence(
                                ifTrue(
                                        isBiome(VEILED_WOODLANDS),
                                        sequence(
                                                ifTrue(
                                                        noiseCondition(EnderscapeNoiseParameters.VEILED_SURFACE, -0.5, 0.5),
                                                        VEILED_END_STONE
                                                )
                                        )
                                ),
                                ifTrue(
                                        isBiome(CELESTIAL_GROVE),
                                        sequence(
                                                ifTrue(
                                                        noiseCondition(EnderscapeNoiseParameters.CELESTIAL_SURFACE, -0.7, 0),
                                                        CELESTIAL_OVERGROWTH
                                                )
                                        )
                                ),
                                ifTrue(
                                        isBiome(CORRUPT_BARRENS),
                                        ifTrue(
                                                stoneDepthCheck(0, true, 4, CaveSurface.FLOOR),
                                                sequence(
                                                        ifTrue(
                                                                ON_FLOOR,
                                                                ifTrue(
                                                                        noiseCondition(EnderscapeNoiseParameters.CORRUPTION_CEILING, -0.6D, 0.0D),
                                                                        CORRUPT_OVERGROWTH_FACING_UP
                                                                )
                                                        ),
                                                        MIRESTONE
                                                )
                                        )
                                ),
                                ifTrue(
                                        isBiome(VOID_DEPTHS),
                                        ifTrue(
                                                noiseCondition(EnderscapeNoiseParameters.CORRUPTION_CEILING, -0.6D, 0.0D),
                                                MIRESTONE
                                        )
                                )
                        )
                ),
                ifTrue(
                        isBiome(MAGNIA_CRAGS),
                        sequence(
                                ifTrue(
                                        noiseCondition(Noises.SURFACE, -WIDE_MAGNIA_SURFACE_NOISE_MAXIMUM, WIDE_MAGNIA_SURFACE_NOISE_MAXIMUM),
                                        ALLURING_MAGNIA
                                ),
                                ifTrue(
                                        noiseCondition(Noises.CALCITE, -THIN_MAGNIA_SURFACE_NOISE_MAXIMUM, THIN_MAGNIA_SURFACE_NOISE_MAXIMUM),
                                        ALLURING_MAGNIA
                                )
                        )
                )
        );
    }
}