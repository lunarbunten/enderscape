package net.bunten.enderscape.block;

import net.bunten.enderscape.block.properties.MagniaPolarity;
import net.bunten.enderscape.entity.magnia.MagniaMoveable;
import net.bunten.enderscape.entity.magnia.MagniaProperties;
import net.bunten.enderscape.registry.EnderscapeBlockEntities;
import net.bunten.enderscape.util.MagniaUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import static net.bunten.enderscape.entity.magnia.MagniaMoveable.getMagniaProperties;

public class MagniaSproutBlockEntity extends BlockEntity {

    public static final int MAXIMUM_RANGE = 14;

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
        if (!HasMagniaPolarity.has(state) || HasMagniaPolarity.optional(state).isEmpty()) {
            return origin;
        } else {
            BlockPos.MutableBlockPos mutable = origin.mutable();

            for (int i = 1; i <= MAXIMUM_RANGE; i++) {
                mutable.move(direction);
                BlockState other = level.getBlockState(mutable);
                if (HasMagniaPolarity.has(other) && !MagniaUtil.isMatchingPolarity(state, other) && other.isFaceSturdy(level, mutable, direction.getOpposite(), SupportType.CENTER)) break;
            }

            return mutable;
        }
    }

    private static void move(Entity entity, Vec3 velocity) {
        if (entity instanceof ServerPlayer player) player.connection.send(new ClientboundSetEntityMotionPacket(entity));
        if (getMagniaProperties(entity).shouldAddVelocity().test(entity)) {
            entity.addDeltaMovement(velocity);
        } else {
            entity.setDeltaMovement(velocity);
        }
        entity.needsSync = true;
    }

    static void doMagniaMovement(Entity entity, MagniaSproutBlockEntity blockEntity) {
        MagniaProperties properties = getMagniaProperties(entity);

        if (properties == null) return;

        properties.onMoved().apply(entity);

        Vec3 pos = entity.getBoundingBox().getCenter();

        BlockState state = blockEntity.getBlockState();
        Direction facing = state.getValue(MagniaSproutBlock.FACING);

        if (HasMagniaPolarity.optional(state).isPresent()) {
            MagniaPolarity type = HasMagniaPolarity.optional(state).get();

            if (type == MagniaPolarity.REPULSIVE) {

                // Repels the entity away from the Magnia Sprout in the direction it's facing

                float factor = properties.repelStrength().apply(entity);
                Vec3 velocity = new Vec3(facing.getStepX(), facing.getStepY(), facing.getStepZ()).normalize().multiply(factor, factor, factor);
                move(entity, velocity);

                if (!MagniaMoveable.wasMovedByMagnia(entity)) entity.playSound(type.getMoveSound(), 1, 1);
                MagniaMoveable.setMovedByMagnia(entity, true);

            } else {
                Vec3 moveTarget = Vec3.atCenterOf(blockEntity.getBlockPos()).add(facing.getStepX() * 1.2F, facing.getStepY() * 1.2F, facing.getStepZ() * 1.2F);

                if (shouldOffset(blockEntity, facing)) {
                    moveTarget = moveTarget.subtract(0, entity.getBoundingBox().getYsize() * 1.2F, 0);
                }

                double distance = pos.distanceTo(moveTarget);

                if (distance > 0.1F) {

                    // Pulls the entity towards the Magnia Sprout

                    Vec3 velocity = moveTarget.subtract(pos).normalize().scale(properties.attractStrength().apply(entity) * (float)Math.min(distance / 5.0, 1.0));

                    // As it gets close, the speed will slow down

                    if (distance < 1.5F) velocity = velocity.scale((float) (0.75F + (distance / 2.0F) * 0.25F));

                    move(entity, velocity);

                    if (!MagniaMoveable.wasMovedByMagnia(entity)) entity.playSound(type.getMoveSound(), 1, 1);
                    MagniaMoveable.setMovedByMagnia(entity, true);
                } else {

                    // When it's very close, momentum will be canceled to avoid jittering

                    move(entity, Vec3.ZERO);
                    MagniaMoveable.setMovedByMagnia(entity, true);
                }
            }

            if (blockEntity.getLevel() instanceof ServerLevel level) MagniaMoveable.sendEntityEffectParticles(level, entity, type.getEffectParticleOptions(), 1.0F);
        }
    }

    private static boolean shouldOffset(MagniaSproutBlockEntity entity, Direction facing) {
        return facing != Direction.UP && !entity.getLevel().getBlockState(entity.getBlockPos().relative(facing).below()).isAir();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, MagniaSproutBlockEntity entity) {
        level.getEntitiesOfClass(Entity.class, getRange(level, state, pos), MagniaMoveable::canMagniaAffect).forEach(moved -> doMagniaMovement(moved, entity));
    }
}