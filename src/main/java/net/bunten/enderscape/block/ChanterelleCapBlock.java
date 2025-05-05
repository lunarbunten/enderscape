package net.bunten.enderscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ChanterelleCapBlock extends Block {
    public ChanterelleCapBlock(Properties settings) {
        super(settings);
    }

    @Override
    public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        entity.causeFallDamage(Math.max(0, fallDistance - 6), 0.33F, world.damageSources().fall());
    }
}