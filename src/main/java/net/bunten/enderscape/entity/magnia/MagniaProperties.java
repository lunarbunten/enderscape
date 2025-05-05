package net.bunten.enderscape.entity.magnia;

import net.minecraft.world.entity.Entity;

import java.util.function.Function;
import java.util.function.Predicate;

public record MagniaProperties(Predicate<Entity> shouldAddVelocity, Function<Entity, Float> attractStrength, Function<Entity, Float> repelStrength, Predicate<Entity> isAllowed, EntityMoveEffect onMoved, EntityMoveEffect onStopMoving) {

    @FunctionalInterface
    public interface EntityMoveEffect {
        void apply(Entity entity);
    }
}