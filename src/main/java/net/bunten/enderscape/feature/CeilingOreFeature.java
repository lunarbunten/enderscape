package net.bunten.enderscape.feature;

import com.mojang.serialization.Codec;
import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.bunten.enderscape.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

import java.util.Optional;

public class CeilingOreFeature extends Feature<CeilingOreConfig> {
    public CeilingOreFeature(Codec<CeilingOreConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<CeilingOreConfig> context) {
        CeilingOreConfig config = context.config();

        WorldGenLevel level = context.level();
        RandomSource random = context.random();

        boolean generated = false;

        Optional<BlockPos> closest = BlockPos.findClosestMatch(context.origin(), 4, 32, (pos) -> level.getBlockState(pos).is(EnderscapeBlockTags.ORE_REPLACEABLE) && level.isEmptyBlock(pos.below()) && BlockUtil.hasTerrainDepth(level, pos, config.terrainDepth().sample(random), Direction.UP));

        if (closest.isPresent()) {
            for (OreConfiguration.TargetBlockState target : config.targets()) {
                if (target.target.test(level.getBlockState(closest.get()), random)) {
                    level.setBlock(closest.get(), target.state, 2);
                    generated = true;
                }
            }
        }

        return generated;
    }
}