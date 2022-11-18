package net.bunten.enderscape.criteria;

import com.google.gson.JsonObject;

import net.bunten.enderscape.Enderscape;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate.Composite;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class HighsAndLowsCriterion extends SimpleCriterionTrigger<HighsAndLowsCriterion.Conditions> {
    private String name;

    public HighsAndLowsCriterion(String name) {
        this.name = name;
    }

    @Override
    public ResourceLocation getId() {
        return Enderscape.id(name);
    }

    @Override
    public Conditions createInstance(JsonObject json, Composite player, DeserializationContext context) {
        return new Conditions(player);
    }

    public void trigger(ServerPlayer player) {
        trigger(player, triggerInstance -> player.isFallFlying() && player.getDeltaMovement().lengthSqr() > 2);
    }

    protected class Conditions extends AbstractCriterionTriggerInstance {
        public Conditions(Composite player) {
            super(getId(), player);
        }
    }
}