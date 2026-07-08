package net.bunten.enderscape.registry;

import net.bunten.enderscape.block.state.StateProperties;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import org.jetbrains.annotations.NotNull;

import static net.bunten.enderscape.registry.EnderscapeBiomes.*;
import static net.minecraft.world.level.levelgen.SurfaceRules.*;

public class EnderscapeSurfaceRuleData {

    @NotNull
    private static RuleSource getDefault(Block block) {
        return state(block.defaultBlockState());
    }

    private static final RuleSource ALLURING_MAGNIA = getDefault(EnderscapeBlocks.ALLURING_MAGNIA.get());
    private static final RuleSource REPULSIVE_MAGNIA = getDefault(EnderscapeBlocks.REPULSIVE_MAGNIA.get());
    private static final RuleSource VEILED_END_STONE = getDefault(EnderscapeBlocks.VEILED_END_STONE.get());
    private static final RuleSource CELESTIAL_OVERGROWTH = getDefault(EnderscapeBlocks.CELESTIAL_OVERGROWTH.get());
    private static final RuleSource MIRESTONE = getDefault(EnderscapeBlocks.MIRESTONE.get());

    private static final RuleSource CORRUPT_OVERGROWTH_FACING_DOWN = state(EnderscapeBlocks.CORRUPT_OVERGROWTH.get().defaultBlockState().setValue(StateProperties.FACING, Direction.DOWN));
    private static final RuleSource CORRUPT_OVERGROWTH_FACING_UP = state(EnderscapeBlocks.CORRUPT_OVERGROWTH.get().defaultBlockState().setValue(StateProperties.FACING, Direction.UP));

    public static final double WIDE_MAGNIA_SURFACE_NOISE_MAXIMUM = 0.075;
    public static final double THIN_MAGNIA_SURFACE_NOISE_MAXIMUM = 0.02;

    public static final int MAGNIA_DEPTH = 2;
    public static final int MIRESTONE_DEPTH = 4;

    public static RuleSource makeRules() {
        return sequence(
                sequence(
                        magniaCrags(),
                        corruptBarrens(),
                        mirestoneUndersides()
                ),
                veiledWoodlands(),
                celestialGrove(),
                voidDepths()
        );
    }

    @NotNull
    private static RuleSource mirestoneUndersides() {
        return ifTrue(
                stoneDepthCheck(0, true, MIRESTONE_DEPTH, CaveSurface.CEILING),
                MIRESTONE
        );
    }

    @NotNull
    private static RuleSource veiledWoodlands() {
        return ifTrue(
                isBiome(VEILED_WOODLANDS),
                ifTrue(
                        ON_FLOOR,
                        ifTrue(
                                noiseCondition(EnderscapeNoiseParameters.VEILED_SURFACE, -0.5, 0.5),
                                VEILED_END_STONE
                        )
                )
        );
    }

    @NotNull
    private static RuleSource magniaCrags() {
        return ifTrue(
                isBiome(MAGNIA_FIELDS),
                sequence(
                        ifTrue(
                                stoneDepthCheck(0, true, MAGNIA_DEPTH, CaveSurface.FLOOR),
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
                        ),
                        ifTrue(
                                stoneDepthCheck(0, true, MAGNIA_DEPTH, CaveSurface.CEILING),
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
                )
        );
    }

    @NotNull
    private static RuleSource celestialGrove() {
        return ifTrue(
                isBiome(CELESTIAL_GROVE),
                ifTrue(
                        ON_FLOOR,
                        ifTrue(
                                noiseCondition(EnderscapeNoiseParameters.CELESTIAL_SURFACE, -0.7, 0),
                                CELESTIAL_OVERGROWTH
                        )
                )
        );
    }

    @NotNull
    private static RuleSource corruptBarrens() {
        return ifTrue(
                isBiome(CORRUPT_BARRENS),
                sequence(
                        ifTrue(
                                ON_FLOOR,
                                ifTrue(
                                        noiseCondition(EnderscapeNoiseParameters.CORRUPTION_CEILING, -0.6D, 0.0D),
                                        CORRUPT_OVERGROWTH_FACING_UP
                                )
                        ),
                        ifTrue(
                                ON_CEILING,
                                ifTrue(
                                        noiseCondition(EnderscapeNoiseParameters.CORRUPTION_CEILING, -1.0D, 0.0D),
                                        CORRUPT_OVERGROWTH_FACING_DOWN
                                )
                        ),
                        MIRESTONE
                )
        );
    }



    @NotNull
    private static RuleSource voidDepths() {
        return ifTrue(
                isBiome(VOID_DEPTHS),
                ifTrue(
                        ON_FLOOR,
                        ifTrue(
                                noiseCondition(EnderscapeNoiseParameters.CORRUPTION_CEILING, -0.6D, 0.0D),
                                MIRESTONE
                        )
                )
        );
    }
}