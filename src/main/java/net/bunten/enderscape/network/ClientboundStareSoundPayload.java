package net.bunten.enderscape.network;

import net.bunten.enderscape.Enderscape;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientboundStareSoundPayload(int entityId) implements CustomPacketPayload {
    public static final Type<ClientboundStareSoundPayload> TYPE = new Type<>(Enderscape.id("clientbound_stare_sound"));
    public static final StreamCodec<FriendlyByteBuf, ClientboundStareSoundPayload> STREAM_CODEC = CustomPacketPayload.codec(ClientboundStareSoundPayload::write, ClientboundStareSoundPayload::new);

    private ClientboundStareSoundPayload(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}