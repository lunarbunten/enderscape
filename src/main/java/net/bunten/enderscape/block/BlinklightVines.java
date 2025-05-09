package net.bunten.enderscape.block;

import net.bunten.enderscape.block.properties.StateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public interface BlinklightVines {

    int getStageDuration(BlockState state);
    void playBlinkEffects(BlockState state, ServerLevel world, BlockPos pos, RandomSource random);
    IntegerProperty getStageProperty();

    DustColorTransitionOptions BLINKLIGHT_TO_REDSTONE = new DustColorTransitionOptions(Vec3.fromRGB24(0xE7A8FF).toVector3f(), Vec3.fromRGB24(16711680).toVector3f(), 1.0F);
    
    IntegerProperty BODY_STAGE = StateProperties.BLINKLIGHT_BODY_STAGE;
    IntegerProperty HEAD_STAGE = StateProperties.BLINKLIGHT_HEAD_STAGE;

    VoxelShape SHAPE = Block.box(2, 0, 2, 14, 16, 14);

    static int getLuminance(BlockState state) {
        if (state.getBlock() instanceof BlinklightVinesHeadBlock head) {
            switch (head.getStage(state)) {
                case 0:
                    return 15;
                case 1, 5:
                    return 13;
                case 2, 4:
                    return 11;
                case 3:
                    return 9;
            }
        }
        if (state.getBlock() instanceof BlinklightVinesBodyBlock body) {
            switch (body.getStage(state)) {
                case 0:
                    return 6;
                case 1, 2:
                    return 4;
            }
        }
        return 0;
    }

    default int getStage(BlockState state) {
        return state.getValue(getStageProperty());
    }

    default boolean isOpen(BlockState state) {
        return getStage(state) == 0;
    }

    default boolean isClosed(BlockState state) {
        return getStage(state) == 3;
    }

    default void close(BlockState state, ServerLevel world, BlockPos pos) {
        state = state.cycle(getStageProperty());
        world.setBlockAndUpdate(pos, state);
        playBlinkEffects(state, world, pos, world.getRandom());
        if (!isOpen(state)) world.scheduleTick(pos, state.getBlock(), getStageDuration(state));
    }
}