package net.bunten.enderscape.network;

import net.bunten.enderscape.Enderscape;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientboundMirrorTeleportInfoPayload(Boolean differentDimension) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientboundMirrorTeleportInfoPayload> TYPE = new CustomPacketPayload.Type<>(Enderscape.id("clientbound_mirror_teleport_info"));
    public static final StreamCodec<FriendlyByteBuf, ClientboundMirrorTeleportInfoPayload> STREAM_CODEC = CustomPacketPayload.codec(ClientboundMirrorTeleportInfoPayload::write, ClientboundMirrorTeleportInfoPayload::new);

    private ClientboundMirrorTeleportInfoPayload(FriendlyByteBuf buf) {
        this(buf.readBoolean());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(differentDimension);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}