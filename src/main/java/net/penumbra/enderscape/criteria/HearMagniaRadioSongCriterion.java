package net.penumbra.enderscape.criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.penumbra.enderscape.sound.MagniaRadioSong;

import java.util.Optional;

public class HearMagniaRadioSongCriterion extends SimpleCriterionTrigger<HearMagniaRadioSongCriterion.Conditions> {

    public void trigger(ServerPlayer player, GlobalPos pos, ResourceKey<MagniaRadioSong> song) {
        trigger(player, instance -> instance.matches(player.level().getServer(), pos, song));
    }

    @Override
    public Codec<Conditions> codec() {
        return Conditions.CODEC;
    }

    public record Conditions(Optional<ContextAwarePredicate> player, Optional<LocationPredicate> location, Optional<MagniaRadioSongPredicate> song) implements SimpleInstance {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Conditions::player),
                LocationPredicate.CODEC.optionalFieldOf("location").forGetter(Conditions::location),
                MagniaRadioSongPredicate.CODEC.optionalFieldOf("song").forGetter(Conditions::song)
        ).apply(instance, Conditions::new));

        public boolean matches(MinecraftServer server, GlobalPos global, ResourceKey<MagniaRadioSong> song) {
            if (song().isPresent() && !song().get().matches(song)) return false;
            return location.isEmpty() || location.get().matches(server.getLevel(global.dimension()), global.pos().getX(), global.pos().getY(), global.pos().getZ());
        }
    }
}