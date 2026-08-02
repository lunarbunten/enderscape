package net.penumbra.enderscape.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.penumbra.enderscape.block.MurublightBracketBlock;

import java.util.Optional;

public class MurublightBracketFeature extends Feature<NoneFeatureConfiguration> {
    public MurublightBracketFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        Optional<BlockPos> optional = BlockPos.findClosestMatch(context.origin(), 8, 8, (pos) -> MurublightBracketBlock.canGenerate(context.level(), pos, true));
        return optional.isPresent() && MurublightBracketBlock.generate(context.level(), optional.get(), context.random());
    }
}