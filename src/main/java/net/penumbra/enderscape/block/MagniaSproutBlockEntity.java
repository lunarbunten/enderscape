package net.penumbra.enderscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.block.properties.MagniaPolarity;
import net.penumbra.enderscape.entity.magnia.MagniaAffected;
import net.penumbra.enderscape.entity.magnia.MagniaInteractionBehavior;
import net.penumbra.enderscape.entity.magnia.MagniaMovementContext;
import net.penumbra.enderscape.registry.block.EnderscapeBlockEntities;
import net.penumbra.enderscape.util.MagniaUtil;

public class MagniaSproutBlockEntity extends BlockEntity {

    private static final int MAXIMUM_RANGE = 14;

    private static final float VERTICAL_OFFSET_AMOUNT = 1.2F;

    private static final float ALLURING_STEP_MULTIPLIER = 1.2F;
    private static final double ALLURING_DISTANCE_SCALE = 5.0;
    private static final float ALLURING_SLOW_THRESHOLD = 1.5F;
    private static final float ALLURING_SLOW_MIN = 0.75F;
    private static final double ALLURING_STOP_THRESHOLD = 0.1F;

    public MagniaSproutBlockEntity(BlockPos pos, BlockState state) {
        super(EnderscapeBlockEntities.MAGNIA_SPROUT, pos, state);
    }

    public static AABB getRange(Level level, BlockState state, BlockPos originPos) {
        Direction direction = MagniaSproutBlock.getFacing(state);

        if (HasMagniaPolarity.has(state) && HasMagniaPolarity.optional(state).isPresent()) {
            BlockPos current = getEndOfRange(level, state, originPos, direction);

            Vec3 start = Vec3.atCenterOf(originPos.relative(direction)).add(direction.getUnitVec3().scale(-1.0F));
            Vec3 end = Vec3.atCenterOf(current).add(direction.getUnitVec3().scale(-0.5F));
            AABB range = new AABB(start, end);

            return switch (direction.getAxis()) {
                case X -> range.inflate(0.0, 0.75, 0.75);
                case Y -> range.inflate(0.75, 0.0, 0.75);
                case Z -> range.inflate(0.75, 0.75, 0.0);
            };
        }

        return new AABB(originPos);
    }

    public static BlockPos getEndOfRange(Level level, BlockState state, BlockPos origin, Direction direction) {
        if (HasMagniaPolarity.optional(state).isPresent()) {
            BlockPos.MutableBlockPos mutable = origin.mutable();

            for (int i = 1; i <= MAXIMUM_RANGE; i++) {
                mutable.move(direction);
                BlockState other = level.getBlockState(mutable);

                if (HasMagniaPolarity.has(other) && !MagniaUtil.isMatchingPolarity(state, other) && other.isFaceSturdy(level, mutable, direction.getOpposite(), SupportType.CENTER)) break;
            }

            return mutable;
        } else {
            return origin;
        }
    }

    private static <T extends Entity> void doMagniaMovement(T moved, MagniaSproutBlockEntity entity) {
        MagniaInteractionBehavior<T> behavior = MagniaInteractionBehavior.get(moved);

        behavior.startMoving().apply(moved);

        HasMagniaPolarity.optional(entity.getBlockState()).ifPresent(polarity -> {
            MagniaMovementContext<T> context = new MagniaMovementContext<>(moved, entity, polarity);
            polarity.getMovement().accept(context);

            if (entity.getLevel() instanceof ServerLevel level) {
                MagniaAffected.sendEntityEffectParticles(level, moved, polarity.getEffectParticleOptions(), 1.0F);
            }
        });
    }

    public static <T extends Entity> void allureEntity(MagniaMovementContext<T> context) {
        T moved = context.entity();
        MagniaSproutBlockEntity entity = context.blockEntity();
        MagniaInteractionBehavior<T> behavior = context.behavior();
        Direction facing = context.facing();

        Vec3 position = moved.getBoundingBox().getCenter();
        Vec3 moveTarget = Vec3.atCenterOf(entity.getBlockPos()).add(new Vec3(facing.getStepX(), facing.getStepY(), facing.getStepZ()).scale(ALLURING_STEP_MULTIPLIER));

        if (shouldOffset(entity, facing)) {
            moveTarget = moveTarget.subtract(0, moved.getBoundingBox().getYsize() * VERTICAL_OFFSET_AMOUNT, 0);
        }

        double distance = position.distanceTo(moveTarget);

        if (distance > ALLURING_STOP_THRESHOLD) {

            // Pulls the moved towards the Magnia Sprout

            Float baseStrength = behavior.attractStrength().apply(moved);
            float distanceScale = (float) Math.min(distance / ALLURING_DISTANCE_SCALE, 1.0);

            Vec3 movement = moveTarget.subtract(position).normalize().scale(baseStrength * distanceScale);

            if (distance < ALLURING_SLOW_THRESHOLD) {
                movement = movement.scale(Mth.lerp((distance / ALLURING_SLOW_THRESHOLD), ALLURING_SLOW_MIN, 1.0F));
            }

            move(moved, movement);
            playMoveSound(context);

            MagniaAffected.setMoved(moved, true);
        } else {

            // When it's very close, momentum will be canceled to avoid jittering

            move(moved, Vec3.ZERO);
            MagniaAffected.setMoved(moved, true);
        }
    }

    public static <T extends Entity> void repulseEntity(MagniaMovementContext<T> context) {
        T moved = context.entity();
        Direction facing = context.facing();

        // Repels the moved away from the Magnia Sprout in the direction it's facing

        float strength = context.behavior().repelStrength().apply(moved);
        Vec3 movement = new Vec3(facing.getStepX(), facing.getStepY(), facing.getStepZ()).normalize().scale(strength);

        move(moved, movement);
        playMoveSound(context);

        MagniaAffected.setMoved(moved, true);
    }

    private static <T extends Entity> void move(T moved, Vec3 movement) {
        MagniaInteractionBehavior<T> behavior = MagniaInteractionBehavior.get(moved);

        behavior.movementType().apply(moved).applyMovement(moved, movement);

        if (moved instanceof ServerPlayer player) {
            player.connection.send(new ClientboundSetEntityMotionPacket(moved));
        } else {
            moved.needsSync = true;
        }
    }

    private static <T extends Entity> void playMoveSound(MagniaMovementContext<T> context) {
        T entity = context.entity();
        MagniaPolarity polarity = context.polarity();

        if (!MagniaAffected.wasMoved(entity)) {
            entity.playSound(polarity.getMoveSound(), 1, 1);
        }
    }

    private static boolean shouldOffset(MagniaSproutBlockEntity entity, Direction facing) {
        return facing != Direction.UP && !entity.getLevel().getBlockState(entity.getBlockPos().relative(facing).below()).isAir();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, MagniaSproutBlockEntity entity) {
        level.getEntitiesOfClass(Entity.class, getRange(level, state, pos), MagniaInteractionBehavior::shouldApply).forEach(moved -> doMagniaMovement(moved, entity));
    }
}