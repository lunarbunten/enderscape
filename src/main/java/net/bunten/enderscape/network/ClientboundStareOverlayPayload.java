package net.bunten.enderscape.network;

import net.bunten.enderscape.Enderscape;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientboundStareOverlayPayload() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientboundStareOverlayPayload> TYPE = new CustomPacketPayload.Type<>(Enderscape.id("clientbound_stare_overlay"));
    public static final StreamCodec<FriendlyByteBuf, ClientboundStareOverlayPayload> STREAM_CODEC = CustomPacketPayload.codec(ClientboundStareOverlayPayload::write, ClientboundStareOverlayPayload::new);

    private ClientboundStareOverlayPayload(FriendlyByteBuf buf) {
        this();
    }

    public void write(FriendlyByteBuf buf) {
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}