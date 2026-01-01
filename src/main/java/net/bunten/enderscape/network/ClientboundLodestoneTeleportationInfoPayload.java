package net.bunten.enderscape.network;

import net.bunten.enderscape.Enderscape;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ClientboundLodestoneTeleportationInfoPayload(boolean differentDimension, ResourceLocation overlayTexture, ResourceLocation vignetteTexture) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientboundLodestoneTeleportationInfoPayload> TYPE = new CustomPacketPayload.Type<>(Enderscape.id("clientbound_lodestone_teleportation_info"));
    public static final StreamCodec<FriendlyByteBuf, ClientboundLodestoneTeleportationInfoPayload> STREAM_CODEC = CustomPacketPayload.codec(ClientboundLodestoneTeleportationInfoPayload::write, ClientboundLodestoneTeleportationInfoPayload::new);

    private ClientboundLodestoneTeleportationInfoPayload(FriendlyByteBuf buf) {
        this(buf.readBoolean(), buf.readResourceLocation(), buf.readResourceLocation());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(differentDimension);
        buf.writeResourceLocation(overlayTexture);
        buf.writeResourceLocation(vignetteTexture);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}