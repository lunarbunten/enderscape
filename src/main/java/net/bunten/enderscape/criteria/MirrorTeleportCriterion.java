package net.bunten.enderscape.criteria;

import com.google.gson.JsonObject;

import net.bunten.enderscape.Enderscape;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.*;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class MirrorTeleportCriterion extends AbstractCriterion<MirrorTeleportCriterion.Conditions> {
    private static final Identifier ID = Enderscape.id("mirror_teleport");

    public Identifier getId() {
        return ID;
    }

    public MirrorTeleportCriterion.Conditions conditionsFromJson(JsonObject object, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer deserializer) {
        ItemPredicate item = ItemPredicate.fromJson(object.get("item"));
        return new MirrorTeleportCriterion.Conditions(extended, item);
    }

    public void trigger(ServerPlayerEntity player, ItemStack stack, Vec3d location) {
        trigger(player, (conditions) -> {
            return conditions.matches(player.getWorld(), stack);
        });
    }

    public static class Conditions extends AbstractCriterionConditions {
        private final ItemPredicate item;

        public Conditions(EntityPredicate.Extended player, ItemPredicate item) {
            super(MirrorTeleportCriterion.ID, player);
            this.item = item;
        }

        public static MirrorTeleportCriterion.Conditions distance(DistancePredicate distance) {
            return new MirrorTeleportCriterion.Conditions(EntityPredicate.Extended.EMPTY, ItemPredicate.ANY);
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