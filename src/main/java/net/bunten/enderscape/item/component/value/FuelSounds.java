package net.bunten.enderscape.item.component.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.bunten.enderscape.registry.EnderscapeItemSounds;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public record FuelSounds(
        Holder<SoundEvent> addFuel,
        Holder<SoundEvent> fuelFull,
        Holder<SoundEvent> useFuel
) {

    public static final Holder<SoundEvent> DEFAULT_ADD_FUEL_SOUND = EnderscapeItemSounds.NEBULITE_TOOL_ADD_FUEL;
    public static final Holder<SoundEvent> DEFAULT_FUEL_FULL_SOUND = EnderscapeItemSounds.NEBULITE_TOOL_FUEL_FULL;
    public static final Holder<SoundEvent> DEFAULT_USE_FUEL_SOUND = Holder.direct(SoundEvents.EMPTY);

    public static final Codec<FuelSounds> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    SoundEvent.CODEC.optionalFieldOf("add_fuel", DEFAULT_ADD_FUEL_SOUND).forGetter(FuelSounds::addFuel),
                    SoundEvent.CODEC.optionalFieldOf("fuel_full", DEFAULT_FUEL_FULL_SOUND).forGetter(FuelSounds::fuelFull),
                    SoundEvent.CODEC.optionalFieldOf("use_fuel", DEFAULT_USE_FUEL_SOUND).forGetter(FuelSounds::useFuel)
            ).apply(instance, FuelSounds::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FuelSounds> STREAM_CODEC = StreamCodec.composite(
            SoundEvent.STREAM_CODEC,
            FuelSounds::addFuel,
            SoundEvent.STREAM_CODEC,
            FuelSounds::fuelFull,
            SoundEvent.STREAM_CODEC,
            FuelSounds::useFuel,
            FuelSounds::new
    );

    public static final FuelSounds DEFAULT = FuelSounds.Builder.create().build();

    public static class Builder {
        private Holder<SoundEvent> addFuel = DEFAULT_ADD_FUEL_SOUND;
        private Holder<SoundEvent> fuelFull = DEFAULT_FUEL_FULL_SOUND;
        private Holder<SoundEvent> useFuel = DEFAULT_USE_FUEL_SOUND;

        public static FuelSounds.Builder create() {
            return new FuelSounds.Builder();
        }

        public Builder addFuel(Holder<SoundEvent> addFuel) {
            this.addFuel = addFuel;
            return this;
        }

        public Builder fuelFull(Holder<SoundEvent> fuelFull) {
            this.fuelFull = fuelFull;
            return this;
        }

        public Builder useFuel(Holder<SoundEvent> useFuel) {
            this.useFuel = useFuel;
            return this;
        }

        public FuelSounds build() {
            return new FuelSounds(
                    addFuel,
                    fuelFull,
                    useFuel
            );
        }
    }
}