package net.bunten.enderscape.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.bunten.enderscape.block.properties.DirectionSet;
import net.bunten.enderscape.block.state.StateProperties;
import net.bunten.enderscape.registry.EnderscapeBlockSounds;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.bunten.enderscape.util.BlockUtil;
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
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Optional;

public class CelestialChanterelleBlock extends DirectionalPlantBlock implements BonemealableBlock {

    public static final IntegerProperty STAGE = StateProperties.STAGE;

    private final ResourceKey<ConfiguredFeature<?, ?>> feature;

    public CelestialChanterelleBlock(ResourceKey<ConfiguredFeature<?, ?>> feature, BlockBehaviour.Properties properties) {
        super(DirectionSet.create().up(), properties);
        this.feature = feature;

        registerDefaultState(defaultBlockState().setValue(STAGE, 0));
    }

    public static final MapCodec<CelestialChanterelleBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    ResourceKey.codec(Registries.CONFIGURED_FEATURE).fieldOf("feature").forGetter(block -> block.feature),
                    propertiesCodec()
            ).apply(instance, CelestialChanterelleBlock::new)
    );

    @Override
    public MapCodec<CelestialChanterelleBlock> codec() {
        return CODEC;
    }

    private Optional<? extends Holder<ConfiguredFeature<?, ?>>> getFeature(LevelReader level) {
        return level.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).get(feature);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, STAGE);
    }

    @Override
    public boolean canPlantOn(BlockState state, BlockState floor, BlockGetter level, BlockPos pos, Direction facing) {
        return floor.is(EnderscapeBlockTags.CELESTIAL_GROVE_VEGETATION_PLANTABLE_ON);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return BlockUtil.createRotatedShape(3, 0, 3, 13, 11, 13, getFacing(state));
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockState relative = level.getBlockState(pos.relative(getFacing(state).getOpposite()));

        if (relative.is(EnderscapeBlockTags.CELESTIAL_CORRUPTS_ON) && random.nextInt(8) == 0) {
            corrupt(level, pos);

            Vec3 center = pos.getCenter();
            level.sendParticles(EnderscapeParticles.VOID_POOF.get(), center.x(), center.y(), center.z(), 6, 0.5F, 0.5F, 0.5F, 0);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        BlockState relative = level.getBlockState(pos.relative(getFacing(state).getOpposite()));
        if (relative.getBlock() instanceof DirectionalBlock && getFacing(relative) != getFacing(state)) return false;
        return relative.is(EnderscapeBlockTags.CELESTIAL_CHANTERELLE_MATURES_ON) || relative.is(EnderscapeBlockTags.CELESTIAL_CORRUPTS_ON);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        BlockState relative = level.getBlockState(pos.relative(getFacing(state).getOpposite()));
        return relative.is(EnderscapeBlockTags.CELESTIAL_CORRUPTS_ON) || random.nextFloat() < 0.2F;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockState relative = level.getBlockState(pos.relative(getFacing(state).getOpposite()));
        if (relative.is(EnderscapeBlockTags.CELESTIAL_CORRUPTS_ON)) {
            corrupt(level, pos);
        } else {
            if (state.getValue(STAGE) == 0) {
                level.setBlock(pos, state.cycle(STAGE), 4);
            } else {
                getFeature(level).ifPresent(holder -> {
                    if (holder.value().place(level, level.getChunkSource().getGenerator(), random, pos) && level.getBlockState(pos).is(this)) level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                });
            }
        }
    }

    private static void corrupt(ServerLevel level, BlockPos pos) {
        level.setBlockAndUpdate(pos, EnderscapeBlocks.MURUBLIGHT_CHANTERELLE.get().defaultBlockState());
        level.playSound(null, pos, EnderscapeBlockSounds.CELESTIAL_CHANTERELLE_CORRUPT, SoundSource.BLOCKS, 1, 1);
        level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
    }
}