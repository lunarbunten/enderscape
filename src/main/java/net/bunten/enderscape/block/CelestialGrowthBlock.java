package net.bunten.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.block.properties.DirectionProperties;
import net.bunten.enderscape.registry.EnderscapeBlockSounds;
import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CelestialGrowthBlock extends AbstractGrowthBlock {

    public static final MapCodec<CelestialGrowthBlock> CODEC = simpleCodec(CelestialGrowthBlock::new);

    public CelestialGrowthBlock(Properties settings) {
        super(DirectionProperties.create().up(), settings);
    }

    @Override
    public boolean canPlantOn(BlockState state, BlockState floor, BlockGetter level, BlockPos pos, Direction facing) {
        return (floor.is(EnderscapeBlockTags.CELESTIAL_GROVE_VEGETATION_PLANTABLE_ON) && floor.isFaceSturdy(level, pos, facing)) || hasGrowthSupport(state, floor);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(600) == 0 && shouldPlayIdleSound(level, pos)) {
            level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, EnderscapeBlockSounds.CELESTIAL_GROWTH_IDLE, SoundSource.AMBIENT, 1, 1, false);
        }
    }

    private boolean shouldPlayIdleSound(Level level, BlockPos pos) {
        int found = 0;

        for (int x = 0; x < 8; x++) {
            for (int z = 0; z < 8; z++) {
                if (level.getBlockState(pos.offset(x, 0, z)).is(this)) {
                    if (++found > 8) return true;
                }
            }
        }

        return false;
    }

    @Override
    protected MapCodec<CelestialGrowthBlock> codec() {
        return CODEC;
    }
}