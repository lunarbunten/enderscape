package net.penumbra.enderscape.criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.ContextAwarePredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public class StunAttackCriterion extends SimpleCriterionTrigger<StunAttackCriterion.Conditions> {

    public void trigger(ServerPlayer player, ItemStack stack, List<Entity> affectedEntities) {
        trigger(player, instance -> instance.matches(player, stack, affectedEntities));
    }

    @Override
    public Codec<Conditions> codec() {
        return Conditions.CODEC;
    }

    public record Conditions(
            Optional<ContextAwarePredicate> player,
            Optional<ItemPredicate> item,
            Optional<List<EntityPredicate>> affectedEntities
    ) implements SimpleInstance {

        public static final Codec<StunAttackCriterion.Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(StunAttackCriterion.Conditions::player),
                ItemPredicate.CODEC.optionalFieldOf("item").forGetter(StunAttackCriterion.Conditions::item),
                EntityPredicate.CODEC.listOf().optionalFieldOf("affected_entities").forGetter(StunAttackCriterion.Conditions::affectedEntities)
        ).apply(instance, StunAttackCriterion.Conditions::new));

        public boolean matches(ServerPlayer player, ItemStack stack, List<Entity> affectedEntities) {
            if (item().isPresent() && !item().get().test(stack)) return false;
            if (affectedEntities().isPresent()) {
                for (EntityPredicate predicate : affectedEntities().get()) {
                    boolean matched = affectedEntities.stream().anyMatch(e -> predicate.matches(player, e));
                    if (!matched) return false;
                }
            }
            return true;
        }
    }
}