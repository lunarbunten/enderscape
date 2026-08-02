package net.penumbra.enderscape.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.penumbra.enderscape.Enderscape;

public record ClientboundSystemMessagePayload(String translationKey) implements CustomPacketPayload {
    public static final Type<ClientboundSystemMessagePayload> TYPE = new Type<>(Enderscape.id("clientbound_system_message_payload"));
    public static final StreamCodec<FriendlyByteBuf, ClientboundSystemMessagePayload> STREAM_CODEC = CustomPacketPayload.codec(ClientboundSystemMessagePayload::write, ClientboundSystemMessagePayload::new);

    private ClientboundSystemMessagePayload(FriendlyByteBuf input) {
        this(input.readUtf());
    }

    public void write(FriendlyByteBuf output) {
        output.writeUtf(translationKey());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}