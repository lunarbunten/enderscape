package net.bunten.enderscape.network;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientboundNebuliteOreSoundPayload(GlobalPos globalPos) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientboundNebuliteOreSoundPayload> TYPE = new CustomPacketPayload.Type<>(Enderscape.id("clientbound_nebulite_ore_sound"));
    public static final StreamCodec<FriendlyByteBuf, ClientboundNebuliteOreSoundPayload> STREAM_CODEC = CustomPacketPayload.codec(ClientboundNebuliteOreSoundPayload::write, ClientboundNebuliteOreSoundPayload::new);

    private ClientboundNebuliteOreSoundPayload(FriendlyByteBuf buf) {
        this(buf.readGlobalPos());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeGlobalPos(globalPos);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
