package net.penumbra.enderscape.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.penumbra.enderscape.Enderscape;

/*
    Credit to FusionSwarly for developing this functionality
    https://github.com/FusionSwarly/structure-music
 */
public record ClientboundStructureChangedPayload(Identifier location) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientboundStructureChangedPayload> TYPE = new CustomPacketPayload.Type<>(Enderscape.id("clientbound_structure_changed"));
    public static final StreamCodec<FriendlyByteBuf, ClientboundStructureChangedPayload> STREAM_CODEC = CustomPacketPayload.codec(ClientboundStructureChangedPayload::write, ClientboundStructureChangedPayload::new);

    private ClientboundStructureChangedPayload(FriendlyByteBuf buf) {
        this(buf.readIdentifier());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeIdentifier(location);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}