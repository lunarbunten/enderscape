package net.penumbra.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.LevelData.RespawnData;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.block.state.EndHavenCoreState;
import net.penumbra.enderscape.block.state.StateProperties;
import net.penumbra.enderscape.manager.EndHavenManager;
import net.penumbra.enderscape.registry.entity.EnderscapeAttachments;
import net.penumbra.enderscape.registry.particle.EnderscapeParticles;
import net.penumbra.enderscape.registry.sound.EnderscapeBlockSounds;
import net.penumbra.enderscape.registry.tag.EnderscapeItemTags;
import net.penumbra.enderscape.util.BlockUtil;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public class EndHavenCoreBlock extends BaseEntityBlock {

    public static final MapCodec<EndHavenCoreBlock> CODEC = simpleCodec(EndHavenCoreBlock::new);
    public static final Property<EndHavenCoreState> STATE = StateProperties.END_HAVEN_CORE_STATE;

    public EndHavenCoreBlock(final BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(STATE, EndHavenCoreState.INACTIVE));
    }

    public static boolean isInactive(BlockState state) {
        return state.getValue(STATE).equals(EndHavenCoreState.INACTIVE);
    }

    public static boolean isActive(BlockState state) {
        return state.getValue(STATE).equals(EndHavenCoreState.ACTIVE);
    }

    public static int lightLevel(BlockState state) {
        return isActive(state) ? 8 : 0;
    }

    @Override
    public MapCodec<EndHavenCoreBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STATE);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (isInactive(state) && stack.is(EnderscapeItemTags.END_HAVEN_CORE_FUELS)) {
            stack.consume(1, player);
            level.setBlock(pos, state.setValue(STATE, EndHavenCoreState.ACTIVE), 2);
            level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, EnderscapeBlockSounds.END_HAVEN_CORE_ACTIVATE, SoundSource.BLOCKS, 1.0F, 1.0F);

            return InteractionResult.SUCCESS_SERVER;
        } else {
            return super.useItemOn(stack, state, level, pos, player, hand, result);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(final BlockState state, final Level level, final BlockPos pos, final Player player, final BlockHitResult result) {
        if (isActive(state)) {
            if (EndHavenManager.endHavenCoreWorks(level)) {
                if (player instanceof ServerPlayer server) {
                    Optional<ServerPlayer.RespawnConfig> optional = EndHavenManager.respawnConfig(server);
                    ServerPlayer.RespawnConfig newConfig = new ServerPlayer.RespawnConfig(RespawnData.of(level.dimension(), pos, 0.0F, 0.0F), false);

                    if (optional.isEmpty() || !optional.get().isSamePosition(newConfig)) {
                        RespawnData data = newConfig.respawnData();

                        server.sendSystemMessage(Component.translatable("block.enderscape.end_haven_core.set_respawn_point"));
                        server.setAttached(EnderscapeAttachments.END_HAVEN_RESPAWN_DATA, data);
                        level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, EnderscapeBlockSounds.END_HAVEN_CORE_SET_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);

                        return InteractionResult.SUCCESS_SERVER;
                    }
                }
            } else if (level instanceof ServerLevel server) {
                explode(server, pos);
                return InteractionResult.SUCCESS_SERVER;
            }
        }

        return super.useWithoutItem(state, level, pos, player, result);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (isActive(state)) {
            if (random.nextInt(100) == 0) {
                level.playLocalSound(pos, EnderscapeBlockSounds.END_HAVEN_CORE_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }

            int amount = Mth.nextInt(random, 1, 4);
            int range = 1;

            for (int i = 0; i < amount; i++) {
                BlockPos pos2 = BlockUtil.random(pos, random, range, 0, range);

                if (level.getBlockState(pos2).isCollisionShapeFullBlock(level, pos2)) {
                    return;
                }

                Vec3 center = Vec3.atCenterOf(pos);
                Vec3 other = Vec3.atCenterOf(pos2);

                double factor = 0.02F;

                double xs = (center.x() - other.x()) * factor;
                double ys = (center.y() - other.y()) * factor;
                double zs = (center.z() - other.z()) * factor;

                level.addParticle(EnderscapeParticles.END_PORTAL_STARS, other.x(), other.y() + Mth.nextDouble(random, -0.3, 0.3), other.z(), xs, ys, zs);
            }
        }
    }

    private static boolean isWaterThatWouldFlow(final BlockPos pos, final Level level) {
        FluidState fluid = level.getFluidState(pos);
        if (!fluid.is(FluidTags.WATER)) {
            return false;
        } else if (fluid.isSource()) {
            return true;
        } else {
            float amount = fluid.getAmount();
            if (amount < 2.0F) {
                return false;
            } else {
                FluidState fluidBelow = level.getFluidState(pos.below());
                return !fluidBelow.is(FluidTags.WATER);
            }
        }
    }

    private void explode(final ServerLevel level, final BlockPos pos) {
        level.removeBlock(pos, false);

        boolean anyWaterNeighbors = Direction.Plane.HORIZONTAL.stream().map(pos::relative).anyMatch(neighborPos -> isWaterThatWouldFlow(neighborPos, level));
        final boolean inWater = anyWaterNeighbors || level.getFluidState(pos.above()).is(FluidTags.WATER);

        Vec3 center = pos.getCenter();
        level.explode(null, level.damageSources().badRespawnPointExplosion(center), explosionDamageCalculator(pos, inWater), center, 5.0F, true, Level.ExplosionInteraction.BLOCK);
    }

    private static @NonNull ExplosionDamageCalculator explosionDamageCalculator(BlockPos pos, boolean inWater) {
        return new ExplosionDamageCalculator() {

            @Override
            public Optional<Float> getBlockExplosionResistance(final Explosion explosion, final BlockGetter levelx, final BlockPos testPos, final BlockState block, final FluidState fluid) {
                return testPos.equals(pos) && inWater ? Optional.of(Blocks.WATER.getExplosionResistance()) : super.getBlockExplosionResistance(explosion, levelx, testPos, block, fluid);
            }
        };
    }

    @Override
    protected boolean isPathfindable(final BlockState state, final PathComputationType type) {
        return false;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EndHavenCoreBlockEntity(pos, state);
    }
}
