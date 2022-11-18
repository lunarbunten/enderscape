package net.bunten.enderscape.blocks;

import com.mojang.math.Vector3f;
import net.bunten.enderscape.blocks.properties.StateProperties;
import net.minecraft.core.BlockPos;
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

    public static final Vector3f PARTICLE_COLOR = new Vector3f(Vec3.fromRGB24(15182079));
    
    public static final IntegerProperty BODY_STAGE = StateProperties.BLINKLIGHT_BODY_STAGE;
    public static final IntegerProperty HEAD_STAGE = StateProperties.BLINKLIGHT_HEAD_STAGE;

    public static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 16, 14);

    public static boolean isHead(BlockState state) {
        return state.getBlock() instanceof BlinklightVinesHeadBlock;
    }

    public static int getLuminance(BlockState state) {
        return isHead(state) ? 14 : 6;
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

    default boolean isInbetween(BlockState state) {
        return !isOpen(state) && !isClosed(state);
    }

    default void close(BlockState state, ServerLevel world, BlockPos pos) {
        state = state.cycle(getStageProperty());
        world.setBlockAndUpdate(pos, state);
        playBlinkEffects(state, world, pos, world.getRandom());
        if (!isOpen(state)) world.scheduleTick(pos, state.getBlock(), getStageDuration(state));
    }
}