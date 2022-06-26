package net.bunten.enderscape.world.features.vegetation;

import com.mojang.serialization.Codec;

import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.util.PlantUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class LargeCelestialFungusFeature extends Feature<LargeCelestialFungusFeatureConfig> {
    public LargeCelestialFungusFeature(Codec<LargeCelestialFungusFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<LargeCelestialFungusFeatureConfig> context) {
        var config = context.getConfig();

        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos pos = context.getOrigin();

        var height = config.getHeight().get(random);
        var capRadiusDivision = config.getCapRadiusDivision();
        var stemCapDivision = config.getStemCapDivision();
        var percentageForCapDrooping = config.getPercentageforCapDrooping();
        var excessVineDiscardChance = config.getExcessVineDiscardChance();
        var vineGenerationTries = config.getVineGenerationTries();
        var tries = config.getTries();

        if (!world.isAir(pos)) {
            return false;
        } else {
            if (world.getBlockState(pos.down()).isIn(EnderscapeBlocks.LARGE_CELESTIAL_FUNGUS_GENERATABLE)) {
                return PlantUtil.generateLargeCelestialFungus(world, random, pos, height, capRadiusDivision, stemCapDivision, percentageForCapDrooping, excessVineDiscardChance, vineGenerationTries, tries);
            } else {
                return false;
            }
        }
    }
}