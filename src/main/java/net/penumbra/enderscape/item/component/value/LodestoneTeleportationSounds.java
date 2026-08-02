package net.penumbra.enderscape.item.component.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.penumbra.enderscape.registry.sound.EnderscapeItemSounds;

public record LodestoneTeleportationSounds(
        Holder<SoundEvent> link,
        Holder<SoundEvent> teleportFailure,
        Holder<SoundEvent> teleportSuccess,
        Holder<SoundEvent> transdimensionalTravel
) {

    private static final Holder<SoundEvent> DEFAULT_LINK_SOUND = EnderscapeItemSounds.MIRROR_LINK;
    private static final Holder<SoundEvent> DEFAULT_TELEPORT_FAILURE = EnderscapeItemSounds.MIRROR_FAILURE;
    private static final Holder<SoundEvent> DEFAULT_TELEPORT_SUCCESS = EnderscapeItemSounds.MIRROR_TELEPORT;
    private static final Holder<SoundEvent> DEFAULT_TRANSDIMENSIONAL_TRAVEL = EnderscapeItemSounds.MIRROR_TRANSDIMENSIONAL_TRAVEL;

    public static final Codec<LodestoneTeleportationSounds> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    SoundEvent.CODEC.optionalFieldOf("link", DEFAULT_LINK_SOUND).forGetter(LodestoneTeleportationSounds::link),
                    SoundEvent.CODEC.optionalFieldOf("teleport_failure", DEFAULT_TELEPORT_FAILURE).forGetter(LodestoneTeleportationSounds::teleportFailure),
                    SoundEvent.CODEC.optionalFieldOf("teleport_success", DEFAULT_TELEPORT_SUCCESS).forGetter(LodestoneTeleportationSounds::teleportSuccess),
                    SoundEvent.CODEC.optionalFieldOf("transdimensional_travel", DEFAULT_TRANSDIMENSIONAL_TRAVEL).forGetter(LodestoneTeleportationSounds::transdimensionalTravel)
            ).apply(instance, LodestoneTeleportationSounds::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, LodestoneTeleportationSounds> STREAM_CODEC = StreamCodec.composite(
            SoundEvent.STREAM_CODEC,
            LodestoneTeleportationSounds::link,
            SoundEvent.STREAM_CODEC,
            LodestoneTeleportationSounds::teleportFailure,
            SoundEvent.STREAM_CODEC,
            LodestoneTeleportationSounds::teleportSuccess,
            SoundEvent.STREAM_CODEC,
            LodestoneTeleportationSounds::transdimensionalTravel,
            LodestoneTeleportationSounds::new
    );

    public static final LodestoneTeleportationSounds DEFAULT = LodestoneTeleportationSounds.Builder.create().build();

    public static class Builder {
        private Holder<SoundEvent> link = DEFAULT_LINK_SOUND;
        private Holder<SoundEvent> teleportFailure = DEFAULT_TELEPORT_FAILURE;
        private Holder<SoundEvent> teleportSuccess = DEFAULT_TELEPORT_SUCCESS;
        private Holder<SoundEvent> transdimensionalTravel = DEFAULT_TRANSDIMENSIONAL_TRAVEL;

        public static LodestoneTeleportationSounds.Builder create() {
            return new LodestoneTeleportationSounds.Builder();
        }

        public Builder link(Holder<SoundEvent> link) {
            this.link = link;
            return this;
        }

        public Builder teleportFailure(Holder<SoundEvent> teleportFailure) {
            this.teleportFailure = teleportFailure;
            return this;
        }

        public Builder teleportSuccess(Holder<SoundEvent> teleportSuccess) {
            this.teleportSuccess = teleportSuccess;
            return this;
        }

        public Builder transdimensionalTravel(Holder<SoundEvent> transdimensionalTravel) {
            this.transdimensionalTravel = transdimensionalTravel;
            return this;
        }

        public LodestoneTeleportationSounds build() {
            return new LodestoneTeleportationSounds(
                    link,
                    teleportFailure,
                    teleportSuccess,
                    transdimensionalTravel
            );
        }
    }
}