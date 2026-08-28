package net.penumbra.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.penumbra.enderscape.block.properties.DirectionSet;
import net.penumbra.enderscape.block.state.StateProperties;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;
import net.penumbra.enderscape.registry.level.EnderscapeConfiguredFeatures;
import net.penumbra.enderscape.registry.sound.EnderscapeBlockSounds;
import net.penumbra.enderscape.registry.tag.EnderscapeBlockTags;
import net.penumbra.enderscape.util.BlockUtil;

import java.util.LinkedHashMap;
import java.util.Optional;

public class MurublightChanterelleBlock extends DirectionalVegetationBlock implements BonemealableBlock {

    public static final IntegerProperty STAGE = StateProperties.STAGE;
    private static final LinkedHashMap<Direction, VoxelShape> VOXEL_SHAPES = BlockUtil.createRotatedShapes(3, 0, 3, 13, 11, 13);
    public static final MapCodec<MurublightChanterelleBlock> CODEC = simpleCodec(MurublightChanterelleBlock::new);

    private final ConfiguredFeatureFunction<BlockState> featureFunction = state -> DirectionalVegetationBlock.getFacing(state) == Direction.UP ? EnderscapeConfiguredFeatures.UPWARD_LARGE_MURUBLIGHT_CHANTERELLE : EnderscapeConfiguredFeatures.DOWNWARD_LARGE_MURUBLIGHT_CHANTERELLE;

    public MurublightChanterelleBlock(Properties properties) {
        super(DirectionSet.create().vertical(), properties);
        registerDefaultState(defaultBlockState().setValue(STAGE, 0));
    }

    @Override
    public MapCodec<MurublightChanterelleBlock> codec() {
        return CODEC;
    }

    private Optional<? extends Holder<ConfiguredFeature<?, ?>>> getFeature(LevelReader level, BlockState state) {
        return level.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).get(featureFunction.get(state));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, STAGE);
    }

    @Override
    public boolean canPlantOn(BlockState state, BlockState floor, BlockGetter level, BlockPos pos, Direction facing) {
        return floor.is(EnderscapeBlockTags.SUPPORTS_MURUBLIGHT_CHANTERELLE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return VOXEL_SHAPES.get(getFacing(state));
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockState relative = level.getBlockState(pos.relative(getFacing(state).getOpposite()));
        if (relative.is(EnderscapeBlockTags.CORRUPTION_PURIFIES_ON) && random.nextInt(8) == 0) purify(level, pos);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        BlockState relative = level.getBlockState(pos.relative(getFacing(state).getOpposite()));
        if (relative.hasProperty(FACING) && getFacing(relative) != getFacing(state)) return false;
        return relative.is(EnderscapeBlockTags.MURUBLIGHT_CHANTERELLE_GROWS_ON) || relative.is(EnderscapeBlockTags.CORRUPTION_PURIFIES_ON);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        BlockState relative = level.getBlockState(pos.relative(getFacing(state).getOpposite()));
        return relative.is(EnderscapeBlockTags.CORRUPTION_PURIFIES_ON) || random.nextFloat() < 0.2F;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockState relative = level.getBlockState(pos.relative(getFacing(state).getOpposite()));
        if (relative.is(EnderscapeBlockTags.CORRUPTION_PURIFIES_ON)) {
            purify(level, pos);
        } else {
            if (state.getValue(STAGE) == 0) {
                level.setBlock(pos, state.cycle(STAGE), 4);
            } else {
                getFeature(level, state).ifPresent(holder -> {
                    if (holder.value().place(level, level.getChunkSource().getGenerator(), random, pos) && level.getBlockState(pos).is(this)) level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                });
            }
        }
    }

    private static void purify(ServerLevel level, BlockPos pos) {
        level.setBlockAndUpdate(pos, EnderscapeBlocks.CELESTIAL_CHANTERELLE.defaultBlockState());
        level.playSound(null, pos, EnderscapeBlockSounds.MURUBLIGHT_CHANTERELLE_PURIFY, SoundSource.BLOCKS, 1, 1);
        level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
    }

    @FunctionalInterface
    public interface ConfiguredFeatureFunction<T> {
        ResourceKey<ConfiguredFeature<?, ?>> get(T value);
    }
}