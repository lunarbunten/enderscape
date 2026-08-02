package net.penumbra.enderscape.registry.level;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.penumbra.enderscape.block.state.StateProperties;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.level.levelgen.SurfaceRules.*;

public class EnderscapeSurfaceRules {

    @NotNull
    private static RuleSource getDefault(Block block) {
        return state(block.defaultBlockState());
    }

    private static final RuleSource ALLURING_MAGNIA = getDefault(EnderscapeBlocks.ALLURING_MAGNIA);
    private static final RuleSource REPULSIVE_MAGNIA = getDefault(EnderscapeBlocks.REPULSIVE_MAGNIA);
    private static final RuleSource VEILED_END_STONE = getDefault(EnderscapeBlocks.VEILED_END_STONE);
    private static final RuleSource CELESTIAL_OVERGROWTH = getDefault(EnderscapeBlocks.CELESTIAL_OVERGROWTH);
    private static final RuleSource MIRESTONE = getDefault(EnderscapeBlocks.MIRESTONE);

    private static final RuleSource CORRUPT_OVERGROWTH_FACING_DOWN = state(EnderscapeBlocks.CORRUPT_OVERGROWTH.defaultBlockState().setValue(StateProperties.FACING, Direction.DOWN));
    private static final RuleSource CORRUPT_OVERGROWTH_FACING_UP = state(EnderscapeBlocks.CORRUPT_OVERGROWTH.defaultBlockState().setValue(StateProperties.FACING, Direction.UP));

    public static final double WIDE_MAGNIA_SURFACE_NOISE_MAXIMUM = 0.075;
    public static final double THIN_MAGNIA_SURFACE_NOISE_MAXIMUM = 0.02;

    public static final int MAGNIA_DEPTH = 2;
    public static final int MIRESTONE_DEPTH = 4;

    public static RuleSource create(HolderGetter<Biome> biomeGetter) {
        return sequence(
                sequence(
                        magniaCrags(biomeGetter),
                        corruptBarrens(biomeGetter),
                        mirestoneUndersides()
                ),
                veiledWoodlands(biomeGetter),
                celestialGrove(biomeGetter),
                voidDepths(biomeGetter)
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
    private static RuleSource veiledWoodlands(HolderGetter<Biome> biomeGetter) {
        return ifTrue(
                SurfaceRules.isBiome(biomeGetter, EnderscapeBiomes.VEILED_WOODLANDS),
                ifTrue(
                        ON_FLOOR,
                        ifTrue(
                                noiseCondition2d(EnderscapeNoiseParameters.VEILED_SURFACE, -0.5, 0.5),
                                VEILED_END_STONE
                        )
                )
        );
    }

    @NotNull
    private static RuleSource magniaCrags(HolderGetter<Biome> biomeGetter) {
        return ifTrue(
                isBiome(biomeGetter, EnderscapeBiomes.MAGNIA_FIELDS),
                sequence(
                        ifTrue(
                                stoneDepthCheck(0, true, MAGNIA_DEPTH, CaveSurface.FLOOR),
                                sequence(
                                        ifTrue(
                                                noiseCondition2d(Noises.SURFACE, -WIDE_MAGNIA_SURFACE_NOISE_MAXIMUM, WIDE_MAGNIA_SURFACE_NOISE_MAXIMUM),
                                                ALLURING_MAGNIA
                                        ),
                                        ifTrue(
                                                noiseCondition2d(Noises.CALCITE, -THIN_MAGNIA_SURFACE_NOISE_MAXIMUM, THIN_MAGNIA_SURFACE_NOISE_MAXIMUM),
                                                ALLURING_MAGNIA
                                        )
                                )
                        ),
                        ifTrue(
                                stoneDepthCheck(0, true, MAGNIA_DEPTH, CaveSurface.CEILING),
                                sequence(
                                        ifTrue(
                                                noiseCondition2d(Noises.SURFACE, -WIDE_MAGNIA_SURFACE_NOISE_MAXIMUM, WIDE_MAGNIA_SURFACE_NOISE_MAXIMUM),
                                                REPULSIVE_MAGNIA
                                        ),
                                        ifTrue(
                                                noiseCondition2d(Noises.CALCITE, -THIN_MAGNIA_SURFACE_NOISE_MAXIMUM, THIN_MAGNIA_SURFACE_NOISE_MAXIMUM),
                                                REPULSIVE_MAGNIA
                                        )
                                )
                        )
                )
        );
    }

    @NotNull
    private static RuleSource celestialGrove(HolderGetter<Biome> biomeGetter) {
        return ifTrue(
                isBiome(biomeGetter, EnderscapeBiomes.CELESTIAL_GROVE),
                ifTrue(
                        ON_FLOOR,
                        ifTrue(
                                noiseCondition2d(EnderscapeNoiseParameters.CELESTIAL_SURFACE, -0.7, 0),
                                CELESTIAL_OVERGROWTH
                        )
                )
        );
    }

    @NotNull
    private static RuleSource corruptBarrens(HolderGetter<Biome> biomeGetter) {
        return ifTrue(
                isBiome(biomeGetter, EnderscapeBiomes.CORRUPT_BARRENS),
                sequence(
                        ifTrue(
                                ON_FLOOR,
                                ifTrue(
                                        noiseCondition2d(EnderscapeNoiseParameters.CORRUPTION_CEILING, -0.6D, 0.0D),
                                        CORRUPT_OVERGROWTH_FACING_UP
                                )
                        ),
                        ifTrue(
                                ON_CEILING,
                                ifTrue(
                                        noiseCondition2d(EnderscapeNoiseParameters.CORRUPTION_CEILING, -1.0D, 0.0D),
                                        CORRUPT_OVERGROWTH_FACING_DOWN
                                )
                        ),
                        MIRESTONE
                )
        );
    }



    @NotNull
    private static RuleSource voidDepths(HolderGetter<Biome> biomeGetter) {
        return ifTrue(
                isBiome(biomeGetter, EnderscapeBiomes.VOID_DEPTHS),
                ifTrue(
                        ON_FLOOR,
                        ifTrue(
                                noiseCondition2d(EnderscapeNoiseParameters.CORRUPTION_CEILING, -0.6D, 0.0D),
                                MIRESTONE
                        )
                )
        );
    }
}