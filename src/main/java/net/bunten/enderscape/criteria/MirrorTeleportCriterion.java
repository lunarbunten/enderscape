package net.bunten.enderscape.criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class MirrorTeleportCriterion extends SimpleCriterionTrigger<MirrorTeleportCriterion.Conditions> {

    public void trigger(ServerPlayer player, ItemStack stack, GlobalPos prior, GlobalPos latter) {
        trigger(player, instance -> instance.matches(player.getServer(), stack, prior, latter));
    }

    @Override
    public Codec<Conditions> codec() {
        return Conditions.CODEC;
    }

    public record Conditions(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item, Optional<LocationPredicate> prior, Optional<LocationPredicate> latter, Optional<DistancePredicate> distance, Optional<Boolean> differentDimensions) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Conditions::player),
                ItemPredicate.CODEC.optionalFieldOf("item").forGetter(Conditions::item),
                LocationPredicate.CODEC.optionalFieldOf("prior").forGetter(Conditions::prior),
                LocationPredicate.CODEC.optionalFieldOf("latter").forGetter(Conditions::latter),
                DistancePredicate.CODEC.optionalFieldOf("distance").forGetter(Conditions::distance),
                Codec.BOOL.optionalFieldOf("different_dimensions").forGetter(Conditions::differentDimensions)
        ).apply(instance, Conditions::new));

        public boolean matches(MinecraftServer server, ItemStack stack, GlobalPos startGlobalPos, GlobalPos endGlobalPos) {
            BlockPos start = startGlobalPos.pos();
            BlockPos end = endGlobalPos.pos();

            if (item.isPresent() && !item.get().test(stack)) return false;

            if (prior.isPresent() && !prior.get().matches(server.getLevel(startGlobalPos.dimension()), start.getX(), start.getY(), start.getZ())) return false;
            if (latter.isPresent() && !latter.get().matches(server.getLevel(endGlobalPos.dimension()), end.getX(), end.getY(), end.getZ())) return false;

            if (differentDimensions().isPresent()) {
                if (differentDimensions().get()) {
                    if (distance.isPresent()) throw new IllegalStateException("Distance predicate rendered invalid by n\"different_dimensionsn\" being n\"truen\"");
                    return startGlobalPos.dimension() != endGlobalPos.dimension();
                } else {
                    if (startGlobalPos.dimension() != endGlobalPos.dimension()) return false;
                    if (distance().isPresent()) {
                        return distance.get().matches(start.getX(), start.getY(), start.getZ(), end.getX(), end.getY(), end.getZ());
                    }
                }
            }

            return true;
        }
    }
}