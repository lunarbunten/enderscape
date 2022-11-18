package net.bunten.enderscape.world.placed;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.world.EnderscapeBiomes;
import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.NoiseBasedCountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;

public class FeaturesClass {

    public static ResourceLocation id(String category, String path) {
        return Enderscape.id(category + "_" + path);
    }

    protected static final int SKY_BIOME_HEIGHT = EnderscapeBiomes.getSkyBiomeHeight();
    protected static final int SUBSURFACE_BIOME_HEIGHT = EnderscapeBiomes.getSubsurfaceBiomeHeight();

    protected static final GenerationStep.Decoration RAW_GENERATION = GenerationStep.Decoration.RAW_GENERATION;
    protected static final GenerationStep.Decoration LAKES = GenerationStep.Decoration.LAKES;
    protected static final GenerationStep.Decoration LOCAL_MODIFICATIONS = GenerationStep.Decoration.LOCAL_MODIFICATIONS;
    protected static final GenerationStep.Decoration UNDERGROUND_STRUCTURES = GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
    protected static final GenerationStep.Decoration SURFACE_STRUCTURES = GenerationStep.Decoration.SURFACE_STRUCTURES;
    protected static final GenerationStep.Decoration STRONGHOLDS = GenerationStep.Decoration.STRONGHOLDS;
    protected static final GenerationStep.Decoration UNDERGROUND_ORES = GenerationStep.Decoration.UNDERGROUND_ORES;
    protected static final GenerationStep.Decoration UNDERGROUND_DECORATION = GenerationStep.Decoration.UNDERGROUND_DECORATION;
    protected static final GenerationStep.Decoration FLUID_SPRINGS = GenerationStep.Decoration.FLUID_SPRINGS;
    protected static final GenerationStep.Decoration VEGETAL_DECORATION = GenerationStep.Decoration.VEGETAL_DECORATION;
    protected static final GenerationStep.Decoration TOP_LAYER_MODIFICATION = GenerationStep.Decoration.TOP_LAYER_MODIFICATION;

    protected static final PlacementModifier FULL_RANGE = PlacementUtils.FULL_RANGE;
    protected static final PlacementModifier VOID_RANGE = uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(SUBSURFACE_BIOME_HEIGHT));
    
    protected static HeightRangePlacement uniform(VerticalAnchor minOffset, VerticalAnchor maxOffset) {
        return HeightRangePlacement.uniform(minOffset, maxOffset);
    }

    protected static EnvironmentScanPlacement environmentScan(Direction direction, BlockPredicate targetPredicate, BlockPredicate allowedSearchPredicate, int maxSteps) {
        return EnvironmentScanPlacement.scanningFor(direction, targetPredicate, allowedSearchPredicate, maxSteps);
    }

    protected static EnvironmentScanPlacement environmentScan(Direction direction, BlockPredicate targetPredicate, int maxSteps) {
        return EnvironmentScanPlacement.scanningFor(direction, targetPredicate, maxSteps);
    }

    protected static RandomOffsetPlacement randomOffset(IntProvider spreadXz, IntProvider spreadY) {
        return RandomOffsetPlacement.of(spreadXz, spreadY);
    }

    protected static RandomOffsetPlacement horizontalOffset(IntProvider spreadXz) {
        return RandomOffsetPlacement.horizontal(spreadXz);
    }

    protected static RandomOffsetPlacement verticalOffset(IntProvider spreadY) {
        return RandomOffsetPlacement.vertical(spreadY);
    }

    protected static NoiseBasedCountPlacement noiseCount(int noiseToCountRatio, double noiseFactor, double noiseOffset) {
        return NoiseBasedCountPlacement.of(noiseToCountRatio, noiseFactor, noiseOffset);
    }
}