package net.bunten.enderscape.criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.bunten.enderscape.entity.drifter.Drifter;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Optional;

public class BounceOnDrifterCriterion extends SimpleCriterionTrigger<BounceOnDrifterCriterion.Conditions> {

    public void trigger(ServerPlayer player, Drifter drifter) {
        trigger(player, instance -> instance.matches(EntityPredicate.createContext(player, drifter)));
    }

    @Override
    public Codec<Conditions> codec() {
        return Conditions.CODEC;
    }

    public record Conditions(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> drifter) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Conditions::player),
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("drifter").forGetter(Conditions::drifter)
        ).apply(instance, Conditions::new));

        public boolean matches(LootContext context) {
            return drifter.isEmpty() || drifter.get().matches(context);
        }
    }
}