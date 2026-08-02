package net.penumbra.enderscape.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.penumbra.enderscape.Enderscape;

public record ClientboundTransdimensionalTravelSoundPayload(Identifier soundEvent) implements CustomPacketPayload {
    public static final Type<ClientboundTransdimensionalTravelSoundPayload> TYPE = new Type<>(Enderscape.id("transdimensional_travel_sound"));
    public static final StreamCodec<FriendlyByteBuf, ClientboundTransdimensionalTravelSoundPayload> STREAM_CODEC = CustomPacketPayload.codec(ClientboundTransdimensionalTravelSoundPayload::write, ClientboundTransdimensionalTravelSoundPayload::new);

    private ClientboundTransdimensionalTravelSoundPayload(FriendlyByteBuf buf) {
        this(buf.readIdentifier());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeIdentifier(soundEvent);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}