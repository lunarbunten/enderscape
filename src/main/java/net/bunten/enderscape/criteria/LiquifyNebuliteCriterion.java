package net.bunten.enderscape.criteria;

import com.google.gson.JsonObject;

import net.bunten.enderscape.Enderscape;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate.Composite;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class LiquifyNebuliteCriterion extends SimpleCriterionTrigger<LiquifyNebuliteCriterion.Conditions> {

    @Override
    public ResourceLocation getId() {
        return Enderscape.id("liquify_nebulite");
    }

    @Override
    public Conditions createInstance(JsonObject json, Composite player, DeserializationContext context) {
        return new Conditions(player, ItemPredicate.fromJson(json.get("item")));
    }

    public void trigger(ServerPlayer player, ItemStack stack) {
        trigger(player, (conditions) -> {
            return conditions.matches(player.getLevel(), stack);
        });
    }

    protected class Conditions extends AbstractCriterionTriggerInstance {
        private final ItemPredicate item;

        public Conditions(Composite player, ItemPredicate item) {
            super(getId(), player);
            this.item = item;
        }

        public boolean matches(ServerLevel world, ItemStack stack) {
            return item.matches(stack);
        }

        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject json = super.serializeToJson(context);
            json.add("item", item.serializeToJson());
            return json;
        }
    }
}