package net.bunten.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class RipeFlangerBerryBlock extends Block {

    public RipeFlangerBerryBlock(Properties settings) {
        super(settings);
    }

    public static final MapCodec<RipeFlangerBerryBlock> CODEC = simpleCodec(RipeFlangerBerryBlock::new);

    @Override
    public MapCodec<RipeFlangerBerryBlock> codec() {
        return CODEC;
    }

    protected int getFallDelay() {
        return 2;
    }

    protected boolean canFall(Level world, BlockPos pos) {
        boolean bl = FallingBlock.isFree(world.getBlockState(pos.below())) && world.getBlockState(pos.above()).getBlock() != EnderscapeBlocks.FLANGER_BERRY_VINE;
        if (pos.getY() < world.getMinY()) bl = false;
        return bl;
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        world.scheduleTick(pos, this, getFallDelay());
    }

    @Override
    public BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess access, BlockPos pos, Direction direction, BlockPos pos2, BlockState state2, RandomSource random) {
        access.scheduleTick(pos, this, getFallDelay());
        return super.updateShape(state, world, access, pos, direction, pos2, state2, random);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (canFall(world, pos)) FallingBlockEntity.fall(world, pos, state);
    }

    @Override
    public void onProjectileHit(Level world, BlockState state, BlockHitResult hit, Projectile projectile) {
        if (projectile.is(EntityTypeTags.IMPACT_PROJECTILES)) {
            world.destroyBlock(hit.getBlockPos(), true, projectile);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        return 6;
    }
}