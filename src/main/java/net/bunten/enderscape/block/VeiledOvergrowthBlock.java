package net.bunten.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.block.properties.DirectionProperties;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class VeiledOvergrowthBlock extends AbstractOvergrowthBlock {

    public static final MapCodec<VeiledOvergrowthBlock> CODEC = simpleCodec(VeiledOvergrowthBlock::new);

    public VeiledOvergrowthBlock(Properties settings) {
        super(true, Blocks.END_STONE, null, null, false, DirectionProperties.create().up(), settings);

        horizontalBonemealRange = 4;
    }

    @Override
    protected MapCodec<VeiledOvergrowthBlock> codec() {
        return CODEC;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos origin, BlockState state) {
        super.performBonemeal(level, random, origin, state);

        int i = 0;
        int max = Mth.nextInt(random, 3, 8);
        var range = Mth.nextInt(random, 4, 8);

        for (BlockPos pos : BlockPos.randomInCube(random, 30, origin, range)) {
            if (i >= max) break;
            BlockState plant = EnderscapeBlocks.VEILED_OVERGROWTH_BONEMEAL_PROVIDER.getState(random, pos);
            if (level.isEmptyBlock(pos) && level.getBlockState(pos.below()).is(this)) {
                level.setBlock(pos, plant, 2);
                i++;
            }
        }
    }
}