package net.bunten.enderscape.block;

import net.bunten.enderscape.block.properties.MagniaType;
import net.bunten.enderscape.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class MagniaBlock extends Block {
    private final MagniaType magniaType;

    public MagniaBlock(MagniaType magniaType, Properties properties) {
        super(properties);
        this.magniaType = magniaType;
    }

    public static MagniaType getMagniaType(BlockState state) {
        return state.getBlock() instanceof MagniaBlock sprout ? sprout.magniaType : null;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);

        if (random.nextInt(800) == 0 && !BlockUtil.isBlockObstructed(level, pos)) {
            level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, magniaType.getHumSound(), SoundSource.AMBIENT, 1, 1, false);
        }
    }
}