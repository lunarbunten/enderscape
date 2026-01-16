package net.bunten.enderscape.network;

import net.bunten.enderscape.Enderscape;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ClientboundLodestoneTeleportationInfoPayload(boolean differentDimension, Identifier overlayTexture, Identifier vignetteTexture) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientboundLodestoneTeleportationInfoPayload> TYPE = new CustomPacketPayload.Type<>(Enderscape.id("clientbound_lodestone_teleportation_info"));
    public static final StreamCodec<FriendlyByteBuf, ClientboundLodestoneTeleportationInfoPayload> STREAM_CODEC = CustomPacketPayload.codec(ClientboundLodestoneTeleportationInfoPayload::write, ClientboundLodestoneTeleportationInfoPayload::new);

    private ClientboundLodestoneTeleportationInfoPayload(FriendlyByteBuf buf) {
        this(buf.readBoolean(), buf.readIdentifier(), buf.readIdentifier());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(differentDimension);
        buf.writeIdentifier(overlayTexture);
        buf.writeIdentifier(vignetteTexture);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}