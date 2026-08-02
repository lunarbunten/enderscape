package net.penumbra.enderscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.penumbra.enderscape.registry.block.EnderscapeBlockEntities;

public class EndHavenCoreBlockEntity extends BlockEntity {
    public EndHavenCoreBlockEntity(BlockPos pos, BlockState state) {
        super(EnderscapeBlockEntities.END_HAVEN_CORE, pos, state);
    }

    public boolean shouldRenderFace(final Direction direction) {
        return Block.shouldRenderFace(getBlockState(), level.getBlockState(getBlockPos().relative(direction)), direction);
    }
}