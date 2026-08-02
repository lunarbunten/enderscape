package net.penumbra.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.penumbra.enderscape.block.properties.DirectionSet;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;
import net.penumbra.enderscape.util.BlockUtil;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MurublightBracketBlock extends DirectionalVegetationBlock implements BonemealableBlock {

    public static final MapCodec<MurublightBracketBlock> CODEC = simpleCodec(MurublightBracketBlock::new);
    private static final LinkedHashMap<Direction, VoxelShape> VOXEL_SHAPES = BlockUtil.createRotatedShapes(1, 0, 1, 15, 6, 15);

    protected final Vector2i bonemealArea = new Vector2i(1, 1);
    protected final IntProvider bonemealMaxCount = UniformInt.of(1, 2);
    protected final FloatProvider bonemealChance = ConstantFloat.of(1.0F);

    public MurublightBracketBlock(Properties settings) {
        super(DirectionSet.create().horizontal(), settings);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public MapCodec<MurublightBracketBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public boolean canPlantOn(BlockState state, BlockState floor, BlockGetter level, BlockPos pos, Direction facing) {
        return floor.isFaceSturdy(level, pos, facing);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return VOXEL_SHAPES.get(getFacing(state));
    }

    @Override
    public BonemealableBlock.Type getType() {
        return Type.NEIGHBOR_SPREADER;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return !findGenerationPositions(level, pos).isEmpty();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        float chance = bonemealChance.sample(random);
        return chance >= 1.0F || random.nextFloat() < chance;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos origin, BlockState state) {
        List<BlockPos> positions = findGenerationPositions(level, origin);
        Util.shuffle(positions, random);

        int maxCount = bonemealMaxCount.sample(random);
        int i = 0;

        for (BlockPos pos : positions) {
            if (i >= maxCount) break;
            if (generate(level, pos, random)) i++;
        }
    }

    private List<BlockPos> findGenerationPositions(LevelReader level, BlockPos origin) {
        List<BlockPos> positions = new ArrayList<>();

        int radius = bonemealArea.x;
        int height = bonemealArea.y;

        BlockPos.betweenClosed(
                origin.offset(-radius, -height, -radius),
                origin.offset(radius, height, radius)
        ).forEach(pos -> {
            if (canGenerate(level, pos, false)) positions.add(pos.immutable());
        });

        return positions;
    }

    private static BlockState stateForGeneration(Direction direction) {
        return EnderscapeBlocks.MURUBLIGHT_BRACKET.defaultBlockState().setValue(MurublightBracketBlock.FACING, direction);
    }

    public static boolean canGenerate(LevelReader level, BlockPos pos, boolean checkSpace) {
        if (level.isEmptyBlock(pos) && (!checkSpace || hasSpace(level, pos))) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                if (canGenerateFacing(level, pos, direction)) return true;
            }
        }

        return false;
    }

    private static boolean hasSpace(LevelReader level, BlockPos pos) {
        return level.isEmptyBlock(pos.above()) && level.isEmptyBlock(pos.below());
    }

    private static boolean canGenerateFacing(LevelReader level, BlockPos pos, Direction direction) {
        return stateForGeneration(direction).canSurvive(level, pos);
    }

    public static boolean generate(LevelAccessor level, BlockPos pos, RandomSource random) {
        if (canGenerate(level, pos, false)) {
            for (Direction direction : Direction.Plane.HORIZONTAL.shuffledCopy(random)) {
                if (canGenerateFacing(level, pos, direction)) {
                    level.setBlock(pos, stateForGeneration(direction), 2);
                    return true;
                }
            }
        }

        return false;
    }
}