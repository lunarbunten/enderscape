package net.bunten.enderscape.network;

import net.bunten.enderscape.Enderscape;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ClientboundTransdimensionalTravelSoundPayload(ResourceLocation soundEvent) implements CustomPacketPayload {
    public static final Type<ClientboundTransdimensionalTravelSoundPayload> TYPE = new Type<>(Enderscape.id("transdimensional_travel_sound"));
    public static final StreamCodec<FriendlyByteBuf, ClientboundTransdimensionalTravelSoundPayload> STREAM_CODEC = CustomPacketPayload.codec(ClientboundTransdimensionalTravelSoundPayload::write, ClientboundTransdimensionalTravelSoundPayload::new);

    private ClientboundTransdimensionalTravelSoundPayload(FriendlyByteBuf buf) {
        this(buf.readResourceLocation());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(soundEvent);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}