package net.penumbra.enderscape.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.penumbra.enderscape.Enderscape;

public record ClientboundSkipSongPayload() implements CustomPacketPayload {
    public static final Type<ClientboundSkipSongPayload> TYPE = new Type<>(Enderscape.id("clientbound_skip_song"));
    public static final StreamCodec<FriendlyByteBuf, ClientboundSkipSongPayload> STREAM_CODEC = CustomPacketPayload.codec(ClientboundSkipSongPayload::write, ClientboundSkipSongPayload::new);

    private ClientboundSkipSongPayload(FriendlyByteBuf buf) {
        this();
    }

    public void write(FriendlyByteBuf buf) {
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}