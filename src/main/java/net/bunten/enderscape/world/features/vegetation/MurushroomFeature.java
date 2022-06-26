package net.bunten.enderscape.world.features.vegetation;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.bunten.enderscape.util.PlantUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class MurushroomFeature extends Feature<MurushroomFeatureConfig> {
    public MurushroomFeature(Codec<MurushroomFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<MurushroomFeatureConfig> context) {
        var config = context.getConfig();

        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos pos = context.getOrigin();

        var age = config.getAge();
        var horizontalRange = config.getHorizontalRange();
        var verticalRange = config.getVerticalRange();
        var tries = config.getTries();

        if (!world.isAir(pos)) {
            return false;
        } else {
            return PlantUtil.generateMurushrooms(world, pos, random, age, horizontalRange, verticalRange, tries);
        }
    }
}