package net.penumbra.enderscape.block.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.InsideBlockEffectType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;
import net.penumbra.enderscape.registry.block.EnderscapeFluids;
import net.penumbra.enderscape.registry.item.EnderscapeItems;
import net.penumbra.enderscape.registry.particle.EnderscapeParticles;
import net.penumbra.enderscape.registry.server.EnderscapeGameRules;
import net.penumbra.enderscape.registry.sound.EnderscapeBlockSounds;
import net.penumbra.enderscape.registry.sound.EnderscapeEntitySounds;
import net.penumbra.enderscape.registry.sound.EnderscapeItemSounds;
import net.penumbra.enderscape.registry.tag.EnderscapeBlockTags;
import net.penumbra.enderscape.registry.tag.EnderscapeFluidTags;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static net.minecraft.world.level.block.LiquidBlock.POSSIBLE_FLOW_DIRECTIONS;
import static net.penumbra.enderscape.manager.VoidManager.*;

public abstract class VoidLachrymaFluid extends FlowingFluid {

    @Override
    public Fluid getFlowing() {
        return EnderscapeFluids.FLOWING_VOID_LACHRYMA;
    }

    @Override
    public Fluid getSource() {
        return EnderscapeFluids.VOID_LACHRYMA;
    }

    @Override
    public Item getBucket() {
        return EnderscapeItems.VOID_LACHRYMA_BUCKET;
    }

    @Override
    // TODO: Add unique sound event for this later on
    protected void randomTick(final ServerLevel level, final BlockPos pos, final FluidState state, final RandomSource random) {
        BlockPos below = pos.below();

        if (level.getBlockState(below).is(EnderscapeBlockTags.BASE_STONE_END)) {
            level.setBlock(below, EnderscapeBlocks.VOID_SHALE.defaultBlockState(), 2);
        }
    }

    @Override
    protected boolean isRandomlyTicking() {
        return true;
    }

    @Override
    public void animateTick(final Level level, final BlockPos pos, final FluidState fluid, final RandomSource random) {
        BlockPos above = pos.above();

        if (level.getBlockState(above).isAir() && !level.getBlockState(above).isSolidRender()) {
            if (random.nextInt(4) == 0) {
                double xx = pos.getX() + random.nextDouble();
                double yy = pos.getY() + 0.05F + fluid.getHeight(level, pos) + Mth.nextDouble(random, -0.15, 0.15);
                double zz = pos.getZ() + random.nextDouble();

                level.addParticle(EnderscapeParticles.VOID_STARS, xx, yy, zz, 0.0, 0.015, 0.0);
            }

            if (random.nextInt(300) == 0) {
                level.playLocalSound(
                        pos.getX(),
                        pos.getY(),
                        pos.getZ(),
                        EnderscapeBlockSounds.VOID_LACHRYMA_AMBIENT,
                        SoundSource.AMBIENT,
                        Mth.nextFloat(random, 0.4F, 0.8F),
                        Mth.nextFloat(random, 0.8F, 1.2F),
                        false
                );
            }
        }
    }

    public static void playFreezeWaterEffects(Level level, BlockPos pos) {
        level.playLocalSound(pos, EnderscapeBlockSounds.VOID_LACHRYMA_FREEZE_WATER, SoundSource.BLOCKS, 0.75F, 1.0F, true);

        for (int i = 0; i < 8; i++) {
            level.addParticle(EnderscapeParticles.SNOWFLAKE, pos.getX() + level.getRandom().nextDouble(), pos.getY() + 1.2, pos.getZ() + level.getRandom().nextDouble(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected void entityInside(Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier applier) {
        applyEntityInsideEffects(entity, applier);
    }

    public static void applyEntityInsideEffects(Entity entity, InsideBlockEffectApplier applier) {
        applier.apply(InsideBlockEffectType.EXTINGUISH);

        if (!entity.level().isClientSide()) {
            modifyVoidTicks(entity, value -> value + (MAXIMUM_VOID_TICKS / 20));
            modifyVoidTickDownDelay(entity, value -> Math.max(value, MAXIMUM_VOID_TICKS));
        }
    }

    @Override
    public boolean isSame(Fluid fluid) {
        return fluid == EnderscapeFluids.VOID_LACHRYMA || fluid == EnderscapeFluids.FLOWING_VOID_LACHRYMA;
    }

    @Override
    protected int getSlopeFindDistance(LevelReader level) {
        return 4;
    }

    @Override
    protected int getDropOff(LevelReader level) {
        return 2;
    }

    @Nullable
    @Override
    public ParticleOptions getDripParticle() {
        return EnderscapeParticles.DRIPPING_VOID_LACHRYMA;
    }

    @Override
    protected boolean canConvertToSource(ServerLevel level) {
        return level.getGameRules().get(EnderscapeGameRules.VOID_LACHRYMA_SOURCE_CONVERSION);
    }

    @Override
    protected void beforeDestroyingBlock(final LevelAccessor level, final BlockPos pos, final BlockState state) {
        BlockEntity entity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
        Block.dropResources(state, level, pos, entity);
    }

    public static void shouldSpreadLiquidTail(Level level, BlockPos pos, FlowingFluid fluid) {
        liquidInteraction(level, pos, fluid, FluidTags.LAVA, 1501, (other) -> other.isSource() ? Blocks.OBSIDIAN : Blocks.BASALT);
        liquidInteraction(level, pos, fluid, FluidTags.WATER, -624644, (other) -> Blocks.PACKED_ICE);
    }

    private static boolean liquidInteraction(Level level, BlockPos pos, FlowingFluid fluid, TagKey<Fluid> fluids, int levelEventId, Function<FluidState, Block> function) {
        if (fluid.is(fluids)) {
            for (Direction direction : POSSIBLE_FLOW_DIRECTIONS) {
                BlockPos neighbor = pos.relative(direction.getOpposite());

                if (level.getFluidState(neighbor).is(EnderscapeFluidTags.VOID_LACHRYMA)) {
                    level.setBlockAndUpdate(pos, function.apply(level.getFluidState(pos)).defaultBlockState());
                    level.levelEvent(levelEventId, pos, 0);

                    return false;
                }
            }
        }

        return true;
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !isSame(fluid);
    }

    @Override
    public int getTickDelay(LevelReader level) {
        return 10;
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0F;
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(EnderscapeItemSounds.VOID_LACHRYMA_BUCKET_FILL);
    }

    @Override
    protected BlockState createLegacyBlock(FluidState state) {
        return EnderscapeBlocks.VOID_LACHRYMA.defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
    }

    public static void doSplashEffect(Entity entity) {
        Entity controller = Objects.requireNonNullElse(entity.getControllingPassenger(), entity);

        RandomSource random = entity.getRandom();
        EntityDimensions dimensions = entity.getDimensions(entity.getPose());
        Vec3 movement = controller.getDeltaMovement();

        float volumeModifier = controller == entity ? 0.2F : 0.9F;
        float speed = Math.min(1.0F, (float) Math.sqrt(movement.x * movement.x * 0.2F + movement.y * movement.y + movement.z * movement.z * 0.2F) * volumeModifier);
        float yt = Mth.floor(entity.getY());

        entity.playSound(speed < 0.25F ? EnderscapeEntitySounds.VOID_SPLASH : EnderscapeEntitySounds.VOID_SPLASH_HEAVY, speed, 1.0F + (random.nextFloat() - random.nextFloat()) * 0.4F);
        for (int i = 0; i < 1.0F + dimensions.width() * 20.0F; i++) {
            double xo = (random.nextDouble() * 2.0 - 1.0) * dimensions.width();
            double zo = (random.nextDouble() * 2.0 - 1.0) * dimensions.width();

            entity.level().addParticle(EnderscapeParticles.VOID_SPLASH, entity.getX() + xo, yt + 1.0F, entity.getZ() + zo, movement.x, movement.y, movement.z);
        }

        entity.gameEvent(GameEvent.SPLASH);
    }

    public static class Flowing extends VoidLachrymaFluid {

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        @Override
        public boolean isSource(FluidState state) {
            return false;
        }
    }

    public static class Source extends VoidLachrymaFluid {

        @Override
        public int getAmount(FluidState state) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState state) {
            return true;
        }
    }
}