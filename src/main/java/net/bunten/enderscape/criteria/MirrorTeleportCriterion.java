package net.bunten.enderscape.criteria;

import com.google.gson.JsonObject;

import net.bunten.enderscape.Enderscape;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.DistancePredicate;
import net.minecraft.advancements.critereon.EntityPredicate.Composite;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class MirrorTeleportCriterion extends SimpleCriterionTrigger<MirrorTeleportCriterion.Conditions> {
    private final TeleportType type;

    public MirrorTeleportCriterion(TeleportType type) {
        this.type = type;
    }

    @Override
    public ResourceLocation getId() {
        return Enderscape.id("mirror_teleport_" + type.getSerializedName());
    }

    @Override
    public Conditions createInstance(JsonObject json, Composite player, DeserializationContext context) {
        return new Conditions(player, ItemPredicate.fromJson(json.get("item")), LocationPredicate.fromJson(json.get("location")), DistancePredicate.fromJson(json.get("distance")));
    }

    public void trigger(ServerPlayer player, ItemStack stack, Vec3 vec, boolean sameDimension) {
        trigger(player, (conditions) -> {
            if (conditions.matches(player.getLevel(), stack, vec, player.position())) {
                return switch (type) {
                    case DIFFERENT -> !sameDimension;
                    case SAME -> sameDimension;
                    default -> true;
                };
            } else {
                return false;
            }
        });
    }

    protected class Conditions extends AbstractCriterionTriggerInstance {
        private final ItemPredicate item;
        private final LocationPredicate location;
        private final DistancePredicate distance;

        public Conditions(Composite player, ItemPredicate item, LocationPredicate location, DistancePredicate distance) {
            super(getId(), player);
            this.item = item;
            this.location = location;
            this.distance = distance;
        }

        public boolean matches(ServerLevel world, ItemStack stack, Vec3 start, Vec3 end) {
            return item.matches(stack) && location.matches(world, start.x, start.y, start.z) && distance.matches(start.x, start.y, start.z, end.x, end.y, end.z);
        }

        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject json = super.serializeToJson(context);
            json.add("item", item.serializeToJson());
            json.add("location", location.serializeToJson());
            json.add("distance", distance.serializeToJson());
            return json;
        }
    }

    public static enum TeleportType implements StringRepresentable {
        ANY("any"),
        SAME("same"),
        DIFFERENT("different");

        private final String name;

        private TeleportType(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}