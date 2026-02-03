package net.bunten.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.block.properties.DirectionSet;
import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.bunten.enderscape.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class BulbFlowerBlock extends DirectionalPlantBlock implements SuspiciousEffectHolder {
    public BulbFlowerBlock(Properties settings) {
        super(DirectionSet.create().up(), settings);
    }

    public static final MapCodec<BulbFlowerBlock> CODEC = simpleCodec(BulbFlowerBlock::new);

    @Override
    public MapCodec<BulbFlowerBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean canPlantOn(BlockState state, BlockState floor, BlockGetter level, BlockPos pos, Direction facing) {
        return floor.is(EnderscapeBlockTags.CELESTIAL_GROVE_VEGETATION_PLANTABLE_ON) && floor.isFaceSturdy(level, pos, facing);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return BlockUtil.createRotatedShape(2, 0, 2, 14, 15, 14, getFacing(state));
    }

    @Override
    public SuspiciousStewEffects getSuspiciousEffects() {
        return new SuspiciousStewEffects(List.of(new SuspiciousStewEffects.Entry(MobEffects.GLOWING, Mth.floor(7 * 20))));
    }
}