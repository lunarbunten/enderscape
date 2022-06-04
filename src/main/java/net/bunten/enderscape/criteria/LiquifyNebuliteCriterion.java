package net.bunten.enderscape.criteria;

import com.google.gson.JsonObject;

import net.bunten.enderscape.Enderscape;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.DistancePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

public class LiquifyNebuliteCriterion extends AbstractCriterion<LiquifyNebuliteCriterion.Conditions> {
    private static final Identifier ID = Enderscape.id("liquify_nebulite");

    public Identifier getId() {
        return ID;
    }

    public LiquifyNebuliteCriterion.Conditions conditionsFromJson(JsonObject object, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer deserializer) {
        ItemPredicate item = ItemPredicate.fromJson(object.get("item"));
        return new LiquifyNebuliteCriterion.Conditions(extended, item);
    }

    public void trigger(ServerPlayerEntity player, ItemStack stack) {
        trigger(player, (conditions) -> {
            return conditions.matches(player.getWorld(), stack);
        });
    }

    public static class Conditions extends AbstractCriterionConditions {
        private final ItemPredicate item;

        public Conditions(EntityPredicate.Extended player, ItemPredicate item) {
            super(LiquifyNebuliteCriterion.ID, player);
            this.item = item;
        }

        public static LiquifyNebuliteCriterion.Conditions distance(DistancePredicate distance) {
            return new LiquifyNebuliteCriterion.Conditions(EntityPredicate.Extended.EMPTY, ItemPredicate.ANY);
        }

        public boolean matches(ServerWorld world, ItemStack stack) {
            return item.test(stack);
        }

        public JsonObject toJson(AdvancementEntityPredicateSerializer serializer) {
            JsonObject object = super.toJson(serializer);
            object.add("item", item.toJson());
            return object;
        }
    }
}