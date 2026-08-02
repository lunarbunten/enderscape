package net.penumbra.enderscape.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.penumbra.enderscape.block.state.StateProperties;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;
import net.penumbra.enderscape.registry.particle.EnderscapeParticles;
import net.penumbra.enderscape.registry.sound.EnderscapeBlockSounds;
import net.penumbra.enderscape.registry.tag.EnderscapeBlockTags;

import java.util.Optional;

public class CelestialChanterelleBlock extends VegetationBlock implements BonemealableBlock {

    public static final IntegerProperty STAGE = StateProperties.STAGE;
    private static final VoxelShape SHAPE = box(3, 0, 3, 13, 11, 13);
    
    private final ResourceKey<ConfiguredFeature<?, ?>> feature;

    public CelestialChanterelleBlock(ResourceKey<ConfiguredFeature<?, ?>> feature, BlockBehaviour.Properties properties) {
        super(properties);
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
        builder.add(STAGE);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(EnderscapeBlockTags.SUPPORTS_CELESTIAL_CHANTERELLE) && state.isFaceSturdy(level, pos, Direction.UP);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return SHAPE;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockState relative = level.getBlockState(pos.below());

        if (relative.is(EnderscapeBlockTags.CELESTIAL_CORRUPTS_ON) && random.nextInt(8) == 0) {
            corrupt(level, pos);

            Vec3 center = Vec3.atCenterOf(pos);
            level.sendParticles(EnderscapeParticles.VOID_POOF, center.x(), center.y(), center.z(), 6, 0.5F, 0.5F, 0.5F, 0);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        BlockState floor = level.getBlockState(pos.below());
        if (floor.getBlock() instanceof DirectionalBlock && !floor.getValue(StateProperties.FACING).equals(Direction.UP)) return false;
        return floor.is(EnderscapeBlockTags.CELESTIAL_CHANTERELLE_GROWS_ON) || floor.is(EnderscapeBlockTags.CELESTIAL_CORRUPTS_ON);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        BlockState floor = level.getBlockState(pos.below());
        return floor.is(EnderscapeBlockTags.CELESTIAL_CORRUPTS_ON) || random.nextFloat() < 0.2F;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockState floor = level.getBlockState(pos.below());
        if (floor.is(EnderscapeBlockTags.CELESTIAL_CORRUPTS_ON)) {
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
        level.setBlockAndUpdate(pos, EnderscapeBlocks.MURUBLIGHT_CHANTERELLE.defaultBlockState());
        level.playSound(null, pos, EnderscapeBlockSounds.CELESTIAL_CHANTERELLE_CORRUPT, SoundSource.BLOCKS, 1, 1);
        level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
    }
}