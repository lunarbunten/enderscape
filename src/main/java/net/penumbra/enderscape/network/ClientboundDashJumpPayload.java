package net.penumbra.enderscape.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec2;
import net.penumbra.enderscape.Enderscape;

public record ClientboundDashJumpPayload(Vec2 power) implements CustomPacketPayload {
    public static final Type<ClientboundDashJumpPayload> TYPE = new Type<>(Enderscape.id("clientbound_dash_jump"));
    public static final StreamCodec<FriendlyByteBuf, ClientboundDashJumpPayload> STREAM_CODEC = CustomPacketPayload.codec(ClientboundDashJumpPayload::write, ClientboundDashJumpPayload::new);

    private ClientboundDashJumpPayload(FriendlyByteBuf buf) {
        this(EnderscapeCodecs.readVec2(buf));
    }

    public void write(FriendlyByteBuf buf) {
        EnderscapeCodecs.writeVec2(power, buf);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}