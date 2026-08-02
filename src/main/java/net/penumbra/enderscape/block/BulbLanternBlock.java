package net.penumbra.enderscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.penumbra.enderscape.block.state.PurifyingPhase;
import net.penumbra.enderscape.manager.VoidManager;
import net.penumbra.enderscape.particle.GlowParticleOptions;
import net.penumbra.enderscape.registry.sound.EnderscapeBlockSounds;

import java.util.List;

import static net.penumbra.enderscape.block.state.PurifyingPhase.ACTIVE;
import static net.penumbra.enderscape.block.state.PurifyingPhase.INACTIVE;

public class BulbLanternBlock extends LanternBlock implements RandomlyPurifiesEntities {

    public static final VoxelShape NORMAL_SHAPE = Shapes.join(box(3, 4, 3, 13, 8, 13), box(5, 0, 5, 11, 6, 11), BooleanOp.OR);
    public static final VoxelShape HANGING_SHAPE = Shapes.join(box(5, 2, 5, 11, 8, 11), box(3, 6, 3, 13, 10, 13), BooleanOp.OR);

    public BulbLanternBlock(Properties settings) {
        super(settings);
        registerDefaultState(PurifyingPhase.set(defaultBlockState(), INACTIVE));
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(HANGING) ? HANGING_SHAPE : NORMAL_SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HANGING, WATERLOGGED, PHASE);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return PurifyingPhase.inactive(state);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        purifyRandomTick(state, level, pos, random);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        purifyScheduledTick(state, level, pos, random);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        purifyAnimateTick(state, level, pos, random);
    }

    @Override
    public boolean hasChargingPhase() {
        return false;
    }

    @Override
    public SoundEvent sparkSound() {
        return EnderscapeBlockSounds.BULB_LANTERN_SPARK;
    }

    @Override
    public void setActivatedTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        List<LivingEntity> entities = purifiableEntities(level, pos);
        int duration = activatedDuration().sample(random);

        if (!entities.isEmpty()) {
            setTemporaryState(level, pos, state, ACTIVE, duration);
            entities.stream().filter(VoidManager::hasVoidedHealth).forEach(entity -> addPurification(entity, purificationEffectDuration(duration)));
        } else {
            setCooldownTick(state, level, pos, random);
        }
    }

    @Override
    public void playServersideDeactivateSound(ServerLevel level, BlockPos pos) {
    }

    @Override
    public void playClientsideActivatedParticle(Level level, BlockPos pos, RandomSource random) {
        BlockState state = level.getBlockState(pos);
        Vec3 center = Vec3.atCenterOf(pos);

        double maxOffset = 0.2;
        double xo = Mth.clamp(random.nextGaussian() * maxOffset, -maxOffset, maxOffset);
        double zo = Mth.clamp(random.nextGaussian() * maxOffset, -maxOffset, maxOffset);

        Vec3 position = center.add(xo, state.getValue(HANGING) ? -0.25 : -0.35, zo);

        double maxMovement = 0.05;
        double xd = Mth.clamp(random.nextGaussian() * maxMovement, -maxMovement, maxMovement);
        double zd = Mth.clamp(random.nextGaussian() * maxMovement, -maxMovement, maxMovement);

        level.addParticle(GlowParticleOptions.DEFAULT, position.x(), position.y(), position.z(), xd, 0, zd);
    }

    @Override
    public AABB purificationArea(BlockPos pos) {
        return new AABB(pos).inflate(6, 6, 6);
    }

    @Override
    public IntProvider activatedDuration() {
        return ConstantInt.of(DEFAULT_ACTIVATED_DURATION * 2);
    }

    @Override
    public IntProvider cooldownDuration() {
        return ConstantInt.of(DEFAULT_COOLDOWN_DURATION);
    }
}