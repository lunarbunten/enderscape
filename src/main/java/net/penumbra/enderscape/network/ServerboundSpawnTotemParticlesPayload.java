package net.penumbra.enderscape.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.penumbra.enderscape.Enderscape;

public record ServerboundSpawnTotemParticlesPayload() implements CustomPacketPayload {
    public static final Type<ServerboundSpawnTotemParticlesPayload> TYPE = new Type<>(Enderscape.id("spawn_totem_particles"));
    public static final StreamCodec<FriendlyByteBuf, ServerboundSpawnTotemParticlesPayload> STREAM_CODEC = CustomPacketPayload.codec(ServerboundSpawnTotemParticlesPayload::write, ServerboundSpawnTotemParticlesPayload::new);

    private ServerboundSpawnTotemParticlesPayload(FriendlyByteBuf buf) {
        this();
    }

    public void write(FriendlyByteBuf buf) {
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}