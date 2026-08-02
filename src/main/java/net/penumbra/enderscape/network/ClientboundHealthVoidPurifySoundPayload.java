package net.penumbra.enderscape.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.penumbra.enderscape.Enderscape;

public record ClientboundHealthVoidPurifySoundPayload() implements CustomPacketPayload {
    public static final Type<ClientboundHealthVoidPurifySoundPayload> TYPE = new Type<>(Enderscape.id("clientbound_health_void_purify_sound"));
    public static final StreamCodec<FriendlyByteBuf, ClientboundHealthVoidPurifySoundPayload> STREAM_CODEC = CustomPacketPayload.codec(ClientboundHealthVoidPurifySoundPayload::write, ClientboundHealthVoidPurifySoundPayload::new);

    private ClientboundHealthVoidPurifySoundPayload(FriendlyByteBuf buf) {
        this();
    }

    public void write(FriendlyByteBuf buf) {
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}