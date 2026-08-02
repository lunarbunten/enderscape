package net.penumbra.enderscape.criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.triggers.*;
import net.minecraft.advancements.predicates.*;
import net.minecraft.advancements.predicates.entity.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class DashJumpCriterion extends SimpleCriterionTrigger<DashJumpCriterion.Conditions> {

    public void trigger(ServerPlayer player, ItemStack stack) {
        trigger(player, instance -> instance.matches(player.level(), player.position(), stack));
    }

    @Override
    public Codec<Conditions> codec() {
        return Conditions.CODEC;
    }

    public record Conditions(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item, Optional<LocationPredicate> location) implements SimpleInstance {
        public static final Codec<DashJumpCriterion.Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(DashJumpCriterion.Conditions::player),
                ItemPredicate.CODEC.optionalFieldOf("item").forGetter(DashJumpCriterion.Conditions::item),
                LocationPredicate.CODEC.optionalFieldOf("location").forGetter(DashJumpCriterion.Conditions::location)
        ).apply(instance, DashJumpCriterion.Conditions::new));

        public boolean matches(ServerLevel level, Vec3 pos, ItemStack stack) {
            if (item.isPresent() && !item.get().test(stack)) return false;
            if (location.isPresent() && !location.get().matches(level, pos.x(), pos.y(), pos.z())) return false;
            return true;
        }
    }
}