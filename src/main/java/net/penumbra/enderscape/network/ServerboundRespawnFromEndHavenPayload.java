package net.penumbra.enderscape.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.penumbra.enderscape.Enderscape;

public record ServerboundRespawnFromEndHavenPayload() implements CustomPacketPayload {
    public static final Type<ServerboundRespawnFromEndHavenPayload> TYPE = new Type<>(Enderscape.id("respawn_from_end_haven"));
    public static final StreamCodec<FriendlyByteBuf, ServerboundRespawnFromEndHavenPayload> STREAM_CODEC = CustomPacketPayload.codec(ServerboundRespawnFromEndHavenPayload::write, ServerboundRespawnFromEndHavenPayload::new);

    private ServerboundRespawnFromEndHavenPayload(FriendlyByteBuf buf) {
        this();
    }

    public void write(FriendlyByteBuf buf) {
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}