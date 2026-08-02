package net.penumbra.enderscape.manager;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.block.VoidShaleBlock;
import net.penumbra.enderscape.registry.entity.EnderscapeAttachments;
import net.penumbra.enderscape.registry.entity.EnderscapeMobEffects;

import java.util.function.UnaryOperator;

import static net.penumbra.enderscape.registry.entity.EnderscapeAttachments.IDLE_TICKS;
import static net.penumbra.enderscape.registry.entity.EnderscapeAttachments.LAST_POSITION;

public class MiscEntityManager {

    public static void tickTail(LivingEntity entity) {
        if (!entity.level().isClientSide()) {
            boolean standingStill = entity.position().distanceToSqr(lastPosition(entity)) < 0.000001 && entity.onGround();

            if (standingStill && entity.getBlockStateOn().getBlock() instanceof VoidShaleBlock) {
                if (entity.level() instanceof ServerLevel level) {
                    VoidShaleBlock.onEntityIdle(level, entity, idleTicks(entity));
                }

                modifyIdleTicks(entity, value -> value + 1);
            } else {
                setIdleTicks(entity, 0);
            }

            if (entity.hasEffect(EnderscapeMobEffects.STUNNED)) {
                entity.setAttached(EnderscapeAttachments.STUN_TICKS, entity.getEffect(EnderscapeMobEffects.STUNNED).getDuration());
            }

            setLastPosition(entity, entity.position());
        }
    }

    public static Vec3 lastPosition(LivingEntity entity) {
        return entity.getAttachedOrElse(LAST_POSITION, Vec3.ZERO);
    }

    public static void setLastPosition(LivingEntity entity, Vec3 value) {
        entity.setAttached(LAST_POSITION, value);
    }

    public static int idleTicks(LivingEntity entity) {
        return entity.getAttachedOrElse(IDLE_TICKS, 0);
    }

    public static void setIdleTicks(LivingEntity entity, int value) {
        entity.setAttached(IDLE_TICKS, value);
        if (idleTicks(entity) <= 0) entity.removeAttached(IDLE_TICKS);
    }

    public static void modifyIdleTicks(LivingEntity entity, UnaryOperator<Integer> modifier) {
        if (!entity.hasAttached(IDLE_TICKS)) {
            setIdleTicks(entity, 0);
        }

        setIdleTicks(entity, modifier.apply(idleTicks(entity)));
    }
}