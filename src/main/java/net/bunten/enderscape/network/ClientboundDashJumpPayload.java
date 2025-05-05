package net.bunten.enderscape.network;

import net.bunten.enderscape.Enderscape;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientboundDashJumpPayload(float horizontalPower, float verticalPower, float glideVelocityFactor) implements CustomPacketPayload {
    public static final Type<ClientboundDashJumpPayload> TYPE = new Type<>(Enderscape.id("clientbound_dash_jump"));
    public static final StreamCodec<FriendlyByteBuf, ClientboundDashJumpPayload> STREAM_CODEC = CustomPacketPayload.codec(ClientboundDashJumpPayload::write, ClientboundDashJumpPayload::new);

    private ClientboundDashJumpPayload(FriendlyByteBuf buf) {
        this(buf.readFloat(), buf.readFloat(), buf.readFloat());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeFloat(horizontalPower);
        buf.writeFloat(verticalPower);
        buf.writeFloat(glideVelocityFactor);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}