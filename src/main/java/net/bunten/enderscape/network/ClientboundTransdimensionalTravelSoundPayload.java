package net.bunten.enderscape.network;

import net.bunten.enderscape.Enderscape;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientboundTransdimensionalTravelSoundPayload() implements CustomPacketPayload {
    public static final Type<ClientboundTransdimensionalTravelSoundPayload> TYPE = new Type<>(Enderscape.id("transdimensional_travel_sound"));
    public static final StreamCodec<FriendlyByteBuf, ClientboundTransdimensionalTravelSoundPayload> STREAM_CODEC = CustomPacketPayload.codec(ClientboundTransdimensionalTravelSoundPayload::write, ClientboundTransdimensionalTravelSoundPayload::new);

    private ClientboundTransdimensionalTravelSoundPayload(FriendlyByteBuf buf) {
        this();
    }

    public void write(FriendlyByteBuf buf) {
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}