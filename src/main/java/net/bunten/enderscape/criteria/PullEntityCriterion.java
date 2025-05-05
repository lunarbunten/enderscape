package net.bunten.enderscape.criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DistancePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

public class PullEntityCriterion extends SimpleCriterionTrigger<PullEntityCriterion.Conditions> {

    public void trigger(ServerPlayer player, Entity entity) {
        trigger(player, instance -> instance.matches(player, entity));
    }

    @Override
    public Codec<Conditions> codec() {
        return Conditions.CODEC;
    }

    public record Conditions(Optional<ContextAwarePredicate> player, Optional<EntityPredicate> pulledEntity, Optional<DistancePredicate> distance) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(PullEntityCriterion.Conditions::player),
                EntityPredicate.CODEC.optionalFieldOf("pulled_entity").forGetter(PullEntityCriterion.Conditions::pulledEntity),
                DistancePredicate.CODEC.optionalFieldOf("distance").forGetter(PullEntityCriterion.Conditions::distance)
        ).apply(instance, Conditions::new));

        public boolean matches(ServerPlayer player, Entity entity) {
            if (player == null || player.isRemoved()) return false;
            
            if (entity == null || entity.isRemoved()) return false;

            if (pulledEntity.isPresent() && !pulledEntity.get().matches(player, entity)) return false;

            if (distance.isPresent()) return distance.get().matches(player.getX(), player.getY(), player.getZ(), entity.getX(), entity.getY(), entity.getZ());

            return true;
        }
    }
}