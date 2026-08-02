package net.penumbra.enderscape.entity.magnia;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.phys.Vec3;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public record MagniaInteractionBehavior<T extends Entity>(
        Predicate<T> shouldApply,
        Function<T, Float> attractStrength,
        Function<T, Float> repelStrength,
        Function<T, MovementType> movementType,
        EntityMoveEffect<T> startMoving,
        EntityMoveEffect<T> stopMoving
) {

    private static final Map<Class<? extends Entity>, MagniaInteractionBehavior<?>> BEHAVIOR_OVERRIDES = new LinkedHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends Entity> MagniaInteractionBehavior<T> get(T entity) {
        Class<?> myClass = entity.getClass();

        while (myClass != null) {
            MagniaInteractionBehavior<?> behavior = BEHAVIOR_OVERRIDES.get(myClass);

            if (behavior != null) {
                return (MagniaInteractionBehavior<T>) behavior;
            } else {
                myClass = myClass.getSuperclass();
            }
        }

        return (MagniaInteractionBehavior<T>) defaultBehavior();
    }

    public static <T extends Entity> boolean shouldApply(T entity) {
        Predicate<T> base = MagniaInteractionBehavior.get(entity).shouldApply();
        return base.and(EntitySelector.NO_SPECTATORS).test(entity);
    }

    public static MagniaInteractionBehavior<Entity> defaultBehavior() {
        return new MagniaInteractionBehavior.Builder<>().build();
    }

    public static void registerOverride(Class<? extends Entity> type, MagniaInteractionBehavior<?> behavior) {
        BEHAVIOR_OVERRIDES.put(type, behavior);
    }

    @FunctionalInterface
    public interface EntityMoveEffect<T extends Entity> {
        void apply(T entity);
    }

    public enum MovementType {
        SET(Entity::setDeltaMovement),
        ADD(Entity::addDeltaMovement);

        private final BiConsumer<Entity, Vec3> effect;

        MovementType(BiConsumer<Entity, Vec3> effect) {
            this.effect = effect;
        }

        public void applyMovement(Entity entity, Vec3 movement) {
            effect.accept(entity, movement);
        }
    }

    public static class Builder<T extends Entity> {

        private Predicate<T> shouldApply = entity -> false;
        private Function<T, Float> attractStrength = entity -> 0.6F;
        private Function<T, Float> repelStrength = entity -> 0.8F;
        private Function<T, MovementType> movementType = entity -> MovementType.SET;
        private EntityMoveEffect<T> startMoving = entity -> entity.setNoGravity(true);
        private EntityMoveEffect<T> stopMoving = entity -> entity.setNoGravity(false);

        public Builder<T> shouldApply(Predicate<T> value) {
            this.shouldApply = value;
            return this;
        }

        public Builder<T> attractStrength(Function<T, Float> value) {
            this.attractStrength = value;
            return this;
        }

        public Builder<T> attractStrength(Float value) {
            this.attractStrength = (_) -> value;
            return this;
        }

        public Builder<T> repelStrength(Function<T, Float> value) {
            this.repelStrength = value;
            return this;
        }

        public Builder<T> repelStrength(Float value) {
            this.repelStrength = (_) -> value;
            return this;
        }

        public Builder<T> movementType(Function<T, MovementType> value) {
            this.movementType = value;
            return this;
        }

        public Builder<T> movementType(MovementType value) {
            this.movementType = (_) -> value;
            return this;
        }

        public Builder<T> startMoving(EntityMoveEffect<T> value) {
            this.startMoving = value;
            return this;
        }

        public Builder<T> stopMoving(EntityMoveEffect<T> value) {
            this.stopMoving = value;
            return this;
        }

        @SuppressWarnings("unchecked")
        public Builder<T> copy(Builder<? super T> builder) {
            this.shouldApply = (Predicate<T>) builder.shouldApply;
            this.attractStrength = (Function<T, Float>) builder.attractStrength;
            this.repelStrength = (Function<T, Float>) builder.repelStrength;
            this.movementType = (Function<T, MovementType>) builder.movementType;
            this.startMoving = (EntityMoveEffect<T>) builder.startMoving;
            this.stopMoving = (EntityMoveEffect<T>) builder.stopMoving;
            return this;
        }

        public MagniaInteractionBehavior<T> build() {
            return new MagniaInteractionBehavior<>(
                    shouldApply,
                    attractStrength,
                    repelStrength,
                    movementType,
                    startMoving,
                    stopMoving
            );
        }
    }
}