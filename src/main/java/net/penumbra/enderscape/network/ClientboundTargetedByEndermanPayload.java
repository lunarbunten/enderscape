package net.penumbra.enderscape.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.penumbra.enderscape.Enderscape;

public record ClientboundTargetedByEndermanPayload(int endermanId, boolean playStereoStareSound) implements CustomPacketPayload {
    public static final Type<ClientboundTargetedByEndermanPayload> TYPE = new Type<>(Enderscape.id("clientbound_stare_sound"));
    public static final StreamCodec<FriendlyByteBuf, ClientboundTargetedByEndermanPayload> STREAM_CODEC = CustomPacketPayload.codec(ClientboundTargetedByEndermanPayload::write, ClientboundTargetedByEndermanPayload::new);

    private ClientboundTargetedByEndermanPayload(FriendlyByteBuf input) {
        this(input.readInt(), input.readBoolean());
    }

    public void write(FriendlyByteBuf output) {
        output.writeInt(endermanId);
        output.writeBoolean(playStereoStareSound);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}