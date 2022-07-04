package net.bunten.enderscape.world.features.vegetation;

import com.mojang.serialization.Codec;

import net.bunten.enderscape.util.PlantUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
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

        var age = config.age;
        var horizontalRange = config.horizontalRange;
        var verticalRange = config.verticalRange;
        var tries = config.tries;

        if (!world.isAir(pos)) {
            return false;
        } else {
            return PlantUtil.generateMurushrooms(world, pos, random, age, horizontalRange, verticalRange, tries);
        }
    }
}