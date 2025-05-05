package net.bunten.enderscape.feature;

import com.mojang.serialization.Codec;
import net.bunten.enderscape.block.MurublightShelfBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Optional;

public class MurublightShelfFeature extends Feature<NoneFeatureConfiguration> {
    public MurublightShelfFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        Optional<BlockPos> optional = BlockPos.findClosestMatch(context.origin(), 8, 8, (pos) -> MurublightShelfBlock.canGenerate(context.level(), pos, Direction.getRandom(context.random())));
        return optional.isPresent() && MurublightShelfBlock.generate(context.level(), optional.get());
    }
}